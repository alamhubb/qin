package com.qin.core;

import com.qin.types.*;

import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
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

    public JavaRunner(QinConfig config, String classpath) {
        this(config, classpath, System.getProperty("user.dir"));
    }

    public JavaRunner(QinConfig config, String classpath, String cwd) {
        this.config = config;
        this.classpath = classpath;
        this.cwd = cwd;
        this.outputDir = Paths.get(cwd, "build", "classes").toString();
    }

    /**
     * 增量编译 Java 源文件
     * 使用 javax.tools API，只编译修改过的文件
     */
    public CompileResult compile() {
        try {
            Files.createDirectories(Paths.get(outputDir));

            ConfigLoader configLoader = new ConfigLoader(cwd);
            ParsedEntry parsed = configLoader.parseEntry(config.entry());
            Path srcDir = Paths.get(cwd, parsed.srcDir());

            List<String> allJavaFiles = findJavaFiles(srcDir);
            if (allJavaFiles.isEmpty()) {
                return CompileResult.failure("No Java files found in " + srcDir);
            }

            // 复制资源文件
            copyResources(parsed.srcDir());

            // 检查哪些文件需要编译（增量编译）
            List<String> modifiedFiles = filterModifiedFiles(allJavaFiles, srcDir.toString());

            if (modifiedFiles.isEmpty()) {
                System.out.println("  ✓ No changes detected, skip compilation");
                return CompileResult.success(0, outputDir);
            }

            System.out.println("  → Compiling " + modifiedFiles.size() + " of " + allJavaFiles.size() + " files...");

            // 使用 javax.tools API 编译
            return compileWithToolsApi(modifiedFiles);
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    /**
     * 过滤出需要编译的文件（.java 比 .class 新的文件）
     */
    private List<String> filterModifiedFiles(List<String> javaFiles, String srcDir) {
        return javaFiles.stream()
                .filter(javaFile -> isModified(javaFile, srcDir))
                .collect(Collectors.toList());
    }

    /**
     * 检查 Java 文件是否被修改（需要重新编译）
     */
    private boolean isModified(String javaFilePath, String srcDir) {
        try {
            Path javaFile = Paths.get(javaFilePath);

            // 计算对应的 .class 文件路径
            String relativePath = javaFilePath;
            if (javaFilePath.startsWith(srcDir)) {
                relativePath = javaFilePath.substring(srcDir.length());
                if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
                    relativePath = relativePath.substring(1);
                }
            }
            String classRelativePath = relativePath.replace(".java", ".class");
            Path classFile = Paths.get(outputDir, classRelativePath);

            // .class 不存在，需要编译
            if (!Files.exists(classFile)) {
                return true;
            }

            // 比较修改时间
            FileTime javaTime = Files.getLastModifiedTime(javaFile);
            FileTime classTime = Files.getLastModifiedTime(classFile);
            return javaTime.compareTo(classTime) > 0;
        } catch (IOException e) {
            return true; // 出错时默认需要编译
        }
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
            // 准备源文件
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(javaFiles);

            // 编译选项
            List<String> options = new ArrayList<>();
            options.add("-d");
            options.add(outputDir);
            options.add("-encoding");
            options.add("UTF-8");

            String fullCp = buildCompileClasspath();
            if (fullCp != null && !fullCp.isEmpty()) {
                options.add("-cp");
                options.add(fullCp);
            }

            // 收集诊断信息
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            // 执行编译
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
                "src/main/java/",
                "src/test/java/",
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
        args.add("UTF-8");

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
        List<String> cpParts = new ArrayList<>();

        // Add local project dependencies using auto-discovery
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            LocalProjectResolver localResolver = new LocalProjectResolver(cwd);
            LocalProjectResolver.ResolutionResult result = localResolver.resolveDependencies(deps);

            // 添加本地classpath
            if (result.localClasspath != null && !result.localClasspath.isEmpty()) {
                cpParts.add(result.localClasspath);
            }

            // 对于远程依赖,如果提供了classpath参数,说明已经通过DependencyResolver解析过了
            // 这里我们只添加本地的,远程的由上层(compileProject)通过DependencyResolver解析
        }

        // Add resolved remote dependencies (from Maven/Coursier)
        // 这个classpath参数由调用方传入,已经包含了远程依赖
        if (classpath != null && !classpath.isEmpty()) {
            cpParts.add(classpath);
        }

        if (cpParts.isEmpty()) {
            return "";
        }

        String sep = DependencyResolver.getClasspathSeparator();
        return String.join(sep, cpParts);
    }

    private String buildFullClasspath() {
        String sep = DependencyResolver.getClasspathSeparator();
        if (classpath != null && !classpath.isEmpty()) {
            return outputDir + sep + classpath;
        }
        return outputDir;
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

    private void copyResources(String srcDir) throws IOException {
        String[] resourceDirs = {
                Paths.get(cwd, "src", "resources").toString(),
                Paths.get(cwd, "src", "main", "resources").toString(),
                Paths.get(cwd, srcDir, "resources").toString()
        };

        for (String resourceDir : resourceDirs) {
            Path resPath = Paths.get(resourceDir);
            if (Files.exists(resPath)) {
                copyDir(resPath, Paths.get(outputDir));
            }
        }
    }

    private void copyDir(Path src, Path dest) throws IOException {
        try (Stream<Path> walk = Files.walk(src)) {
            walk.forEach(source -> {
                try {
                    Path target = dest.resolve(src.relativize(source));
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
