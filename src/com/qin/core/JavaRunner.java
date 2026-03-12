package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.CompileResult;
import com.qin.types.ParsedEntry;
import com.qin.types.QinConfig;
import com.qin.types.*;

import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Java Runner for Qin
 * Compiles and runs Java programs
 */
public class JavaRunner {
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
        this.incrementalChecker = new IncrementalCompilationChecker();
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
            Files.createDirectories(Paths.get(outputDir));

            List<String> allJavaFiles = new ArrayList<>();

            // 使用 sourceDir 配置（默认 src/main/java）
            String srcDirStr = getSourceDir();
            if (srcDirStr == null) {
                return CompileResult
                        .failure("No source directory found (checked " + QinConstants.JAVA_SOURCE_DIR + " and " + QinConstants.DEFAULT_SOURCE_DIR + ")");
            }
            Path srcDir = Paths.get(cwd, srcDirStr);
            allJavaFiles.addAll(findJavaFiles(srcDir));

            // 同时查找测试目录（默认 src/test/java）
            String testDirStr = getTestDir();
            Path testDir = Paths.get(cwd, testDirStr);
            if (Files.exists(testDir) && !testDir.equals(srcDir)) {
                allJavaFiles.addAll(findJavaFiles(testDir));
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

            System.out.println("  → Compiling " + allJavaFiles.size() + " files (javac handles incremental)...");

            // 使用 javax.tools API 编译（javac 自动增量编译）
            return compileWithToolsApi(allJavaFiles);
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
                return CompileResult.failure(errorMsg.toString().trim());
            }

            return CompileResult.success(javaFiles.size(), outputDir);
        } catch (IOException e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    /**
     * Run compiled Java program
     */
    public void run(List<String> args) throws Exception {
        ConfigLoader configLoader = new ConfigLoader(cwd);
        ParsedEntry parsed = configLoader.parseEntry(config.entry());

        String fullClasspath = buildFullClasspath();

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");

        // ✨ Java 25 适配：如果检测到是 Spring Boot 项目且版本较高，自动添加忽略类格式限制的参数
        if (config.hasDependency("org.springframework.boot:spring-boot-starter-web") ||
                config.hasDependency("org.springframework.boot:spring-boot-starter")) {
            javaArgs.add("-Dspring.classformat.ignore=true");
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

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    /**
     * Compile and run in one step
     */
    public void compileAndRun(List<String> args) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        run(args);
    }

    /**
     * 编译并运行指定的 Java 文件
     * 
     * @param javaFilePath 要运行的 Java 文件路径（相对于项目目录）
     * @param args         传递给 main 方法的参数
     */
    public void compileAndRunFile(String javaFilePath, List<String> args) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        runFile(javaFilePath, args);
    }

    /**
     * 运行指定的已编译 Java 文件
     * 
     * @param javaFilePath Java 文件路径
     * @param args         传递给 main 方法的参数
     */
    public void runFile(String javaFilePath, List<String> args) throws Exception {
        String className = javaFilePathToClassName(javaFilePath);

        String fullClasspath = buildFullClasspath();

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");

        // ✨ Java 25 适配：如果检测到是 Spring Boot 项目且版本较高，自动添加忽略类格式限制的参数
        if (config.hasDependency("org.springframework.boot:spring-boot-starter-web") ||
                config.hasDependency("org.springframework.boot:spring-boot-starter")) {
            javaArgs.add("-Dspring.classformat.ignore=true");
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

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    /**
     * 将 Java 文件路径转换为完全限定类名
     * 例如: src/main/java/com/slime/parser/test/MinimalTokenTest.java ->
     * com.slime.parser.test.MinimalTokenTest
     */
    private String javaFilePathToClassName(String javaFilePath) {
        // 标准化路径分隔符
        String normalized = javaFilePath.replace('\\', '/');

        // 移除 .java 后缀
        if (normalized.endsWith(".java")) {
            normalized = normalized.substring(0, normalized.length() - 5);
        }

        // 常见的源码目录前缀
        String[] srcPrefixes = {
                QinConstants.JAVA_SOURCE_DIR + "/",
                QinConstants.DEFAULT_TEST_DIR + "/",
                "src/"
        };

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

            System.out.println("  → Compiling " + outdated.size() + " outdated local dependencies...");

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
            System.out.println("    → Compiling dependency: " + projectInfo.fullName);

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
                System.out.println("      ✓ Compiled " + result.getCompiledFiles() + " files");
            } else {
                System.err.println("      ✗ Compilation failed: " + result.getError());
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
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }
}

