package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.CompileResult;
import com.qin.types.ParsedEntry;
import com.qin.types.QinConfig;
import com.qin.types.*;

import javax.tools.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

/**
 * Java Runner for Qin
 * Compiles and runs Java programs
 */
public class JavaRunner {
    private static final Pattern JAVA_TOP_LEVEL_TYPE_PATTERN = Pattern.compile(
            "(?m)^\\s*(?:public\\s+|protected\\s+|private\\s+|abstract\\s+|final\\s+|sealed\\s+|non-sealed\\s+|static\\s+)*"
                    + "(?:class|interface|enum|record)\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\b");
    private static final List<String> DEFAULT_RUN_JVM_ARGS = List.of(
            "-Xms16m",
            "-Xshare:off",
            "-XX:+UseSerialGC",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:-UseJVMCICompiler",
            "-XX:TieredStopAtLevel=1",
            "-Dfile.encoding=UTF-8",
            "-Dstdout.encoding=UTF-8",
            "-Dstderr.encoding=UTF-8");

    private final QinConfig config;
    private final String classpath;
    private final String cwd;
    private final String outputDir;
    private final JavaCompileConfig javaCompileConfig;

    private final ClasspathBuilder classpathBuilder;
    private final DependencyGraphBuilder graphBuilder;
    private final IncrementalCompilationChecker incrementalChecker;

    public JavaRunner(QinConfig config, String classpath) {
        this(config, classpath, QinConstants.getCwd());
    }

    public JavaRunner(QinConfig config, String classpath, String cwd) {
        this.config = config;
        this.classpath = classpath;
        this.cwd = cwd;
        this.javaCompileConfig = JavaCompileConfig.from(config);
        this.outputDir = Paths.get(cwd, javaCompileConfig.outputDir()).toString();

        this.classpathBuilder = new ClasspathBuilder(cwd, outputDir, classpath, config);
        this.graphBuilder = new DependencyGraphBuilder();
        this.incrementalChecker = new IncrementalCompilationChecker(cwd);
    }

    /**
     * 编译 Java 源文件
     * 使用 javax.tools API，javac 自动处理增量编译
     * 自动检测并编译过期的本地依赖项目
     */
    public CompileResult compile() {
        try {
            // 1. 先编译所有过期的本地依赖
            compileOutdatedLocalDependencies();

            // 2. 编译当前项目
            Path compileLockPath = ensureCompileLockFile();
            try (RandomAccessFile lockHandle = new RandomAccessFile(compileLockPath.toFile(), "rw");
                    FileChannel lockChannel = lockHandle.getChannel();
                    FileLock compileLock = acquireCompileLock(lockChannel)) {
                incrementalChecker.reloadCache();
                Files.createDirectories(Paths.get(outputDir));

                List<String> allJavaFiles = new ArrayList<>();
                List<String> sourceDirsForCache = new ArrayList<>();

                // 使用 sourceDir 配置（默认 src/main/java）
                String srcDirStr = getSourceDir();
                if (srcDirStr == null) {
                    return CompileResult
                            .failure("No source directory found (checked " + QinConstants.JAVA_SOURCE_DIR + " and " + QinConstants.DEFAULT_SOURCE_DIR + ")");
                }
                Path srcDir = Paths.get(cwd, srcDirStr);
                allJavaFiles.addAll(findJavaFiles(srcDir));
                sourceDirsForCache.add(srcDirStr);

                // 同时查找测试目录（默认 src/test/java）
                String testDirStr = getTestDir();
                Path testDir = Paths.get(cwd, testDirStr);
                if (Files.exists(testDir) && !testDir.equals(srcDir)) {
                    allJavaFiles.addAll(findJavaFiles(testDir));
                    sourceDirsForCache.add(testDirStr);
                }

                if (allJavaFiles.isEmpty()) {
                    return CompileResult
                            .failure("No Java files found in " + srcDir + (Files.exists(testDir) ? " or " + testDir : ""));
                }

                // 复制资源文件
                ResourceCopier resourceCopier = new ResourceCopier(cwd, srcDirStr, outputDir);
                resourceCopier.copyResources();

                // 如果存在测试资源目录，也进行复制
                if (Files.exists(Paths.get(cwd, "src/test/resources"))) {
                    ResourceCopier testResourceCopier = new ResourceCopier(cwd, "src/test/resources", outputDir);
                    testResourceCopier.copyResources();
                }

                boolean hasStaleClasses = pruneStaleClassFiles(allJavaFiles, sourceDirsForCache);
                List<Path> changedPaths = new ArrayList<>();
                boolean hasDeletedFiles = false;
                for (String sourceDir : sourceDirsForCache) {
                    changedPaths.addAll(incrementalChecker.getChangedFiles(sourceDir));
                    if (incrementalChecker.hasDeletedFiles(sourceDir)) {
                        hasDeletedFiles = true;
                    }
                }

                if (hasDeletedFiles || hasStaleClasses) {
                    System.out.println("  -> Java output cleanup detected stale classes, forcing full recompile...");
                }

                boolean hasCompiledClass = hasAnyCompiledClass();
                List<String> filesToCompile;
                if (hasDeletedFiles || hasStaleClasses || !hasCompiledClass) {
                    filesToCompile = allJavaFiles;
                } else {
                    LinkedHashSet<String> dedup = new LinkedHashSet<>();
                    for (Path changedPath : changedPaths) {
                        dedup.add(changedPath.toString());
                    }
                    filesToCompile = new ArrayList<>(dedup);
                }

                if (filesToCompile.isEmpty() && hasCompiledClass) {
                    System.out.println("  [OK] No Java changes detected, skip compile (cache)");
                    return CompileResult.success(0, outputDir);
                }

                if (filesToCompile.isEmpty()) {
                    filesToCompile = allJavaFiles;
                }

                System.out.println("  -> Compiling " + filesToCompile.size() + " file(s)...");

                CompileResult compileResult = compileWithToolsApi(filesToCompile);
                if (!compileResult.isSuccess()) {
                    return compileResult;
                }

                for (String sourceDir : sourceDirsForCache) {
                    incrementalChecker.updateAllHashes(sourceDir);
                }
                incrementalChecker.saveCache();

                return compileResult;
            }
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    /**
     * 获取源码目录
     * 优先使用 java.sourceDir 配置，否则自动检测
     */
    private String getSourceDir() {
        if (config.java() != null && config.java().sourceDir() != null) {
            return config.java().sourceDir();
        }

        if (config.entry() != null && !config.entry().isBlank()) {
            Path entryPath = Paths.get(config.entry().replace("\\", "/"));
            Path parent = entryPath.getParent();
            if (parent != null) {
                Path parentDir = Paths.get(cwd, parent.toString());
                if (Files.isDirectory(parentDir)) {
                    return parent.toString().replace("\\", "/");
                }
            }
        }

        if (Files.isDirectory(Paths.get(cwd, QinConstants.JAVA_SOURCE_DIR))) {
            return QinConstants.JAVA_SOURCE_DIR;
        }
        if (Files.isDirectory(Paths.get(cwd, QinConstants.DEFAULT_SOURCE_DIR))) {
            return QinConstants.DEFAULT_SOURCE_DIR;
        }
        if (Files.isDirectory(Paths.get(cwd, QinConstants.MAIN_SOURCE_DIR))) {
            return QinConstants.MAIN_SOURCE_DIR;
        }
        return null;
    }

    /**
     * 获取测试目录
     */
    private String getTestDir() {
        if (config.java() != null && config.java().testDir() != null) {
            return config.java().testDir();
        }
        return QinConstants.DEFAULT_TEST_DIR;
    }

    /**
     * 使用 javax.tools API 编译
     */
    private CompileResult compileWithToolsApi(List<String> javaFiles) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return CompileResult.failure("No Java compiler available. Make sure you're using JDK, not JRE.");
        }

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(javaFiles);

            List<String> options = new ArrayList<>();
            options.add("-d");
            options.add(outputDir);
            javaCompileConfig.appendJavacOptions(options);

            String fullCp = buildCompileClasspath();
            System.out.println("  [DEBUG] Compile classpath: "
                    + (fullCp != null ? fullCp.substring(0, Math.min(200, fullCp.length())) + "..." : "null"));
            if (fullCp != null && !fullCp.isEmpty()) {
                options.add("-cp");
                options.add(fullCp);
            }

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, options, null, compilationUnits);

            boolean success = task.call();

            if (!success) {
                StringBuilder errorMsg = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                        errorMsg.append(diagnostic.getMessage(null)).append("\n");
                    }
                }
                CompileResult fallback = compileWithExternalJavac(javaFiles, fullCp);
                if (fallback.isSuccess()) {
                    System.out.println("  -> In-process javac failed, external javac fallback succeeded.");
                    return fallback;
                }
                String externalError = fallback.getError();
                if (externalError != null && !externalError.isBlank()) {
                    errorMsg.append("\n[external javac]\n").append(externalError.trim());
                }
                return CompileResult.failure(errorMsg.toString().trim());
            }

            return CompileResult.success(javaFiles.size(), outputDir);
        } catch (IOException e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    private CompileResult compileWithExternalJavac(List<String> javaFiles, String fullCp) {
        try {
            List<String> command = new ArrayList<>();
            command.add(currentJavacCommand());
            command.add("-d");
            command.add(outputDir);
            javaCompileConfig.appendJavacOptions(command);
            if (fullCp != null && !fullCp.isEmpty()) {
                command.add("-cp");
                command.add(fullCp);
            }
            command.addAll(javaFiles);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(cwd));
            pb.redirectErrorStream(true);

            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            int exitCode = proc.waitFor();
            if (exitCode == 0) {
                return CompileResult.success(javaFiles.size(), outputDir);
            }
            return CompileResult.failure(output);
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    private String currentJavacCommand() {
        String executable = QinConstants.isWindows() ? "javac.exe" : "javac";
        return Paths.get(System.getProperty("java.home"), "bin", executable).toString();
    }

    /**
     * Run compiled Java program
     */
    public void run(List<String> args) throws Exception {
        run(args, List.of());
    }

    public void run(List<String> args, List<String> jvmArgs) throws Exception {
        ConfigLoader configLoader = new ConfigLoader(cwd);
        ParsedEntry parsed = configLoader.parseEntry(config.entry());

        String fullClasspath = buildFullClasspath();

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            javaArgs.addAll(jvmArgs);
        }

        // ✨ Java 25 适配：如果检测到是 Spring Boot 项目且版本较高，自动添加忽略类格式限制的参数
        if (config.hasDependency("org.springframework.boot:spring-boot-starter-web") ||
                config.hasDependency("org.springframework.boot:spring-boot-starter")) {
            appendConfiguredSpringPort(javaArgs, jvmArgs);
        }

        javaArgs.add("-cp");
        javaArgs.add(fullClasspath);
        javaArgs.add(parsed.className());
        if (args != null) {
            javaArgs.addAll(args);
        }

        ProcessBuilder pb = new ProcessBuilder(javaArgs);
        pb.directory(new File(cwd));
        pb.inheritIO();
        Process proc = pb.start();

        int exitCode = ChildProcessSupport.waitFor(proc, parsed.className());
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    /**
     * Compile and run in one step
     */
    public void compileAndRun(List<String> args) throws Exception {
        compileAndRun(args, List.of());
    }

    public void compileAndRun(List<String> args, List<String> jvmArgs) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        run(args, jvmArgs);
    }

    public void compileAndRunMainClass(String mainClass, List<String> args, List<String> jvmArgs) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        runMainClass(mainClass, args, jvmArgs);
    }

    /**
     * 编译并运行指定的 Java 文件
     * 
     * @param javaFilePath 要运行的 Java 文件路径（相对于项目目录）
     * @param args         传递给 main 方法的参数
     */
    public void compileAndRunFile(String javaFilePath, List<String> args) throws Exception {
        compileAndRunFile(javaFilePath, args, List.of());
    }

    public void compileAndRunFile(String javaFilePath, List<String> args, List<String> jvmArgs) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        runFile(javaFilePath, args, jvmArgs);
    }

    /**
     * 运行指定的已编译 Java 文件
     * 
     * @param javaFilePath Java 文件路径
     * @param args         传递给 main 方法的参数
     */
    public void runFile(String javaFilePath, List<String> args) throws Exception {
        runFile(javaFilePath, args, List.of());
    }

    public void runFile(String javaFilePath, List<String> args, List<String> jvmArgs) throws Exception {
        String className = javaFilePathToClassName(javaFilePath);
        runMainClass(className, args, jvmArgs);
    }

    public void runMainClass(String className, List<String> args, List<String> jvmArgs) throws Exception {
        String fullClasspath = buildFullClasspath();

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");
        appendDefaultRunJvmArgs(javaArgs, jvmArgs);
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            javaArgs.addAll(jvmArgs);
        }

        // ✨ Java 25 适配：如果检测到是 Spring Boot 项目且版本较高，自动添加忽略类格式限制的参数
        if (config.hasDependency("org.springframework.boot:spring-boot-starter-web") ||
                config.hasDependency("org.springframework.boot:spring-boot-starter")) {
            appendConfiguredSpringPort(javaArgs, jvmArgs);
        }

        javaArgs.add("-cp");
        javaArgs.add(fullClasspath);
        javaArgs.add(className);
        if (args != null) {
            javaArgs.addAll(args);
        }

        ProcessBuilder pb = new ProcessBuilder(javaArgs);
        pb.directory(new File(cwd));
        pb.inheritIO();
        Process proc = pb.start();

        int exitCode = ChildProcessSupport.waitFor(proc, className);
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    private void appendDefaultRunJvmArgs(List<String> javaArgs, List<String> explicitJvmArgs) {
        if (!hasJvmArg("-Xmx", explicitJvmArgs)) {
            javaArgs.add("-Xmx" + configuredRunMaxHeap());
        }
        for (String defaultArg : DEFAULT_RUN_JVM_ARGS) {
            if (!isOverridden(defaultArg, explicitJvmArgs)) {
                javaArgs.add(defaultArg);
            }
        }
    }

    private String configuredRunMaxHeap() {
        String property = System.getProperty("qin.run.maxHeap");
        if (property != null && !property.isBlank()) {
            return property.trim();
        }
        String env = System.getenv("QIN_RUN_MAX_HEAP");
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return "1536m";
    }

    private boolean hasJvmArg(String key, List<String> explicitJvmArgs) {
        if (explicitJvmArgs == null || explicitJvmArgs.isEmpty()) {
            return false;
        }
        for (String arg : explicitJvmArgs) {
            if (arg != null && arg.startsWith(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverridden(String defaultArg, List<String> explicitJvmArgs) {
        if (explicitJvmArgs == null || explicitJvmArgs.isEmpty()) {
            return false;
        }
        String key = defaultArg;
        int equals = defaultArg.indexOf('=');
        if (equals > 0) {
            key = defaultArg.substring(0, equals + 1);
        } else if (defaultArg.startsWith("-XX:+") || defaultArg.startsWith("-XX:-")) {
            key = "-XX:" + defaultArg.substring(5);
        } else if (defaultArg.startsWith("-Xms")) {
            key = "-Xms";
        } else if (defaultArg.startsWith("-Xmx")) {
            key = "-Xmx";
        } else if (defaultArg.startsWith("-Xss")) {
            key = "-Xss";
        }
        for (String arg : explicitJvmArgs) {
            if (arg.equals(defaultArg) || arg.startsWith(key)) {
                return true;
            }
            if (defaultArg.startsWith("-XX:+") && arg.equals("-XX:-" + defaultArg.substring(5))) {
                return true;
            }
            if (defaultArg.startsWith("-XX:-") && arg.equals("-XX:+" + defaultArg.substring(5))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将 Java 文件路径转换为完全限定类名
     * 例如: src/main/java/com/slime/parser/test/MinimalTokenTest.java ->
     * com.slime.parser.test.MinimalTokenTest
     */
    private void appendConfiguredSpringPort(List<String> javaArgs, List<String> jvmArgs) {
        if (containsServerPortOverride(jvmArgs)) {
            return;
        }
        if (config.port() != null && config.port() > 0) {
            javaArgs.add("-Dserver.port=" + config.port());
        }
    }

    private boolean containsServerPortOverride(List<String> jvmArgs) {
        if (jvmArgs == null || jvmArgs.isEmpty()) {
            return false;
        }
        for (String arg : jvmArgs) {
            if (arg != null && arg.startsWith("-Dserver.port=")) {
                return true;
            }
        }
        return false;
    }

    private String javaFilePathToClassName(String javaFilePath) {
        // 标准化路径分隔符
        String normalized = javaFilePath.replace('\\', '/');

        // 移除 .java 后缀
        if (normalized.endsWith(".java")) {
            normalized = normalized.substring(0, normalized.length() - 5);
        }

        // 常见的源码目录前缀
        LinkedHashSet<String> srcPrefixes = new LinkedHashSet<>();
        String configuredSourceDir = getSourceDir();
        String configuredTestDir = getTestDir();
        if (configuredSourceDir != null && !configuredSourceDir.isBlank()) {
            srcPrefixes.add(configuredSourceDir.replace('\\', '/') + "/");
        }
        if (configuredTestDir != null && !configuredTestDir.isBlank()) {
            srcPrefixes.add(configuredTestDir.replace('\\', '/') + "/");
        }
        srcPrefixes.add(QinConstants.JAVA_SOURCE_DIR + "/");
        srcPrefixes.add("src/java/");
        srcPrefixes.add(QinConstants.DEFAULT_TEST_DIR + "/");
        srcPrefixes.add("src/");

        for (String prefix : srcPrefixes) {
            int idx = normalized.indexOf(prefix);
            if (idx >= 0) {
                normalized = normalized.substring(idx + prefix.length());
                break;
            }
        }

        // 将路径分隔符转换为包分隔符
        return normalized.replace('/', '.');
    }

    private List<String> buildCompileArgs(List<String> javaFiles) {
        List<String> args = new ArrayList<>();
        args.add("javac");
        args.add("-d");
        args.add(outputDir);
        args.add("-encoding");
        args.add(QinConstants.CHARSET_UTF8);

        // Build full classpath including localDependencies
        String fullCp = buildCompileClasspath();
        if (fullCp != null && !fullCp.isEmpty()) {
            args.add("-cp");
            args.add(fullCp);
        }

        args.addAll(javaFiles);
        return args;
    }

    /**
     * Build classpath for compilation including local and remote dependencies
     */
    private String buildCompileClasspath() {
        return classpathBuilder.buildCompileClasspath();
    }

    private String buildFullClasspath() {
        return classpathBuilder.buildRuntimeClasspath();
    }

    private List<String> findJavaFiles(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }

        try (Stream<Path> walk = Files.walk(dir)) {
            return walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private boolean hasAnyCompiledClass() throws IOException {
        Path classesDir = Paths.get(outputDir);
        if (!Files.exists(classesDir)) {
            return false;
        }
        try (Stream<Path> walk = Files.walk(classesDir)) {
            return walk.anyMatch(path -> path.toString().endsWith(".class"));
        }
    }

    private boolean pruneStaleClassFiles(List<String> javaFiles, List<String> sourceDirsForCache) throws IOException {
        Path outputPath = Paths.get(outputDir);
        if (!Files.exists(outputPath)) {
            return false;
        }
        Set<String> currentClassPrefixes = new HashSet<>();
        for (String javaFile : javaFiles) {
            Path sourcePath = Paths.get(javaFile).toAbsolutePath().normalize();
            Path sourceRoot = findSourceRootForFile(sourcePath, sourceDirsForCache);
            if (sourceRoot == null) {
                continue;
            }
            Path relativeSource = sourceRoot.relativize(sourcePath);
            String relative = relativeSource.toString().replace(File.separatorChar, '/');
            if (!relative.endsWith(".java")) {
                continue;
            }
            String filePrefix = relative.substring(0, relative.length() - ".java".length());
            currentClassPrefixes.add(filePrefix);
            currentClassPrefixes.addAll(extractTopLevelTypePrefixes(sourcePath, filePrefix));
        }
        if (currentClassPrefixes.isEmpty()) {
            return false;
        }

        boolean deleted = false;
        try (Stream<Path> walk = Files.walk(outputPath)) {
            List<Path> classFiles = walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".class"))
                    .collect(Collectors.toList());
            for (Path classFile : classFiles) {
                String relativeClass = outputPath.relativize(classFile).toString().replace(File.separatorChar, '/');
                String classStem = relativeClass.substring(0, relativeClass.length() - ".class".length());
                String topLevelStem = classStem.contains("$")
                        ? classStem.substring(0, classStem.indexOf('$'))
                        : classStem;
                if (!currentClassPrefixes.contains(topLevelStem)) {
                    Files.deleteIfExists(classFile);
                    deleted = true;
                }
            }
        }
        return deleted;
    }

    private Set<String> extractTopLevelTypePrefixes(Path sourcePath, String filePrefix) {
        try {
            String source = Files.readString(sourcePath);
            int slash = filePrefix.lastIndexOf('/');
            String packagePrefix = slash >= 0 ? filePrefix.substring(0, slash + 1) : "";
            Set<String> prefixes = new HashSet<>();
            Matcher matcher = JAVA_TOP_LEVEL_TYPE_PATTERN.matcher(source);
            while (matcher.find()) {
                prefixes.add(packagePrefix + matcher.group(1));
            }
            return prefixes;
        } catch (IOException e) {
            return Set.of();
        }
    }

    private Path findSourceRootForFile(Path sourcePath, List<String> sourceDirsForCache) {
        Path best = null;
        for (String sourceDir : sourceDirsForCache) {
            Path sourceRoot = Paths.get(cwd, sourceDir).toAbsolutePath().normalize();
            if (!sourcePath.startsWith(sourceRoot)) {
                continue;
            }
            if (best == null || sourceRoot.getNameCount() > best.getNameCount()) {
                best = sourceRoot;
            }
        }
        return best;
    }

    private void clearOutputDirectory() throws IOException {
        Path outputPath = Paths.get(outputDir);
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            return;
        }
        try (Stream<Path> walk = Files.walk(outputPath)) {
            List<Path> paths = walk.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (Path path : paths) {
                Files.deleteIfExists(path);
            }
        }
        Files.createDirectories(outputPath);
    }

    private Path ensureCompileLockFile() throws IOException {
        Path qinDir = QinConstants.getProjectQinDir(cwd);
        Files.createDirectories(qinDir);
        return qinDir.resolve(QinConstants.COMPILE_LOCK_FILE);
    }

    private FileLock acquireCompileLock(FileChannel lockChannel) throws IOException {
        boolean waitingLogged = false;
        while (true) {
            try {
                FileLock lock = lockChannel.tryLock();
                if (lock != null) {
                    if (waitingLogged) {
                        System.out.println("  -> Compile lock acquired.");
                    }
                    return lock;
                }
            } catch (OverlappingFileLockException ignored) {
                // Same JVM overlap; wait and retry.
            }

            if (!waitingLogged) {
                System.out.println("  -> Another compile is in progress, waiting for compile lock...");
                waitingLogged = true;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for compile lock", e);
            }
        }
    }

    /**
     * 编译所有过期的本地依赖项目
     */
    private void compileOutdatedLocalDependencies() {
        try {
            Map<String, String> deps = config.dependencies();
            if (deps == null || deps.isEmpty()) {
                return;
            }

            // 1. 解析本地依赖
            LocalProjectResolver localResolver = new LocalProjectResolver(cwd);
            Map<String, LocalProjectResolver.ProjectInfo> allLocalProjects = discoverLocalProjects(localResolver, deps);

            if (allLocalProjects.isEmpty()) {
                return; // 没有本地依赖
            }

            // 2. 构建依赖图
            DependencyGraphBuilder.DependencyGraph graph = graphBuilder.buildGraph(config.name(), allLocalProjects);

            // 3. 检测需要重新编译的项目
            List<String> outdated = incrementalChecker
                    .getProjectsNeedingRecompilation(graph, allLocalProjects);

            if (outdated.isEmpty()) {
                return; // 所有依赖都是最新的
            }

            // 4. 拓扑排序，按依赖顺序编译
            List<String> compileOrder = graphBuilder.topologicalSort(graph);

            System.out.println("  -> Compiling " + outdated.size() + " outdated local dependencies...");

            for (String projectName : compileOrder) {
                if (outdated.contains(projectName)) {
                    LocalProjectResolver.ProjectInfo projectInfo = allLocalProjects.get(projectName);
                    compileLocalDependencyProject(projectInfo);
                }
            }
        } catch (Exception e) {
            // 依赖编译失败不阻塞当前项目
            System.err.println("Warning: Failed to compile local dependencies: " + e.getMessage());
        }
    }

    /**
     * 发现所有本地项目依赖
     */
    private Map<String, LocalProjectResolver.ProjectInfo> discoverLocalProjects(
            LocalProjectResolver resolver, Map<String, String> deps) {
        Map<String, LocalProjectResolver.ProjectInfo> result = new HashMap<>();

        // 简化实现：只收集 deps 中声明的本地依赖
        LocalProjectResolver.ResolutionResult localResult = resolver.resolveDependencies(deps);

        // 这里需要扩展 LocalProjectResolver 以提供更多信息
        // 暂时返回空，表示功能尚未完全实现
        return result;
    }

    /**
     * 编译单个本地依赖项目
     */
    private void compileLocalDependencyProject(LocalProjectResolver.ProjectInfo projectInfo) {
        try {
            System.out.println("    -> Compiling dependency: " + projectInfo.fullName);

            // 加载依赖项目的配置
            Path configPath = projectInfo.projectDir.resolve(QinConstants.CONFIG_FILE);
            if (!Files.exists(configPath)) {
                System.err.println("      Warning: No " + QinConstants.CONFIG_FILE + " found");
                return;
            }

            String json = Files.readString(configPath);
            QinConfig depConfig = new com.google.gson.Gson().fromJson(json, QinConfig.class);

            // 创建 JavaRunner 编译依赖项目
            JavaRunner depRunner = new JavaRunner(depConfig, "", projectInfo.projectDir.toString());
            CompileResult result = depRunner.compileCurrentOnly(); // 只编译当前，不递归

            if (result.isSuccess()) {
                System.out.println("      [OK] Compiled " + result.getCompiledFiles() + " files");
            } else {
                System.err.println("      [ERROR] Compilation failed: " + result.getError());
            }
        } catch (Exception e) {
            System.err.println("      Error: " + e.getMessage());
        }
    }

    /**
     * 只编译当前项目，不编译依赖（避免递归）
     */
    private CompileResult compileCurrentOnly() {
        try {
            Path compileLockPath = ensureCompileLockFile();
            try (RandomAccessFile lockHandle = new RandomAccessFile(compileLockPath.toFile(), "rw");
                    FileChannel lockChannel = lockHandle.getChannel();
                    FileLock compileLock = acquireCompileLock(lockChannel)) {
                Files.createDirectories(Paths.get(outputDir));

                // 使用 sourceDir 配置（默认 src）
                String srcDirStr = getSourceDir();
                Path srcDir = Paths.get(cwd, srcDirStr);

                List<String> allJavaFiles = findJavaFiles(srcDir);
                if (allJavaFiles.isEmpty()) {
                    return CompileResult.failure("No Java files found in " + srcDir);
                }

                // 复制资源文件
                ResourceCopier resourceCopier = new ResourceCopier(cwd, srcDirStr, outputDir);
                resourceCopier.copyResources();

                // 使用 javax.tools API 编译（javac 自动增量编译）
                return compileWithToolsApi(allJavaFiles);
            }
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }
}

