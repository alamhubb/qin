package com.qin.cli;

import com.qin.core.*;
import com.qin.types.*;
import com.qin.plugins.*;
import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Qin CLI - Java-Vite Build Tool
 * A modern Java build tool with zero XML configuration
 */
public class QinCli {
    private static final String VERSION = "0.1.0";
    private static final EnvironmentChecker envChecker = new EnvironmentChecker();

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            switch (command) {
                case "init" -> initProject();
                case "run" -> runProject(cmdArgs);
                case "build" -> buildProject(cmdArgs);
                case "dev" -> devMode(cmdArgs);
                case "compile" -> compileProject(cmdArgs);
                case "clean" -> cleanProject();
                case "sync" -> syncDependencies();
                case "test" -> runTests(cmdArgs);
                case "help", "-h", "--help" -> printHelp();
                case "version", "-v", "--version" -> System.out.println("qin " + VERSION);
                default -> {
                    System.err.println("Unknown command: " + command);
                    printHelp();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.err.println(red("Error: ") + e.getMessage());
            System.exit(1);
        }
    }

    private static void initProject() throws IOException {
        System.out.println(blue("→ Initializing new Qin project..."));

        Path cwd = Paths.get(System.getProperty("user.dir"));

        // Create directories
        Files.createDirectories(cwd.resolve("src"));

        // Create Main.java
        Path mainJava = cwd.resolve("src/Main.java");
        if (!Files.exists(mainJava)) {
            Files.writeString(mainJava, """
                    public class Main {
                        public static void main(String[] args) {
                            System.out.println("Hello, Qin!");
                        }
                    }
                    """);
        }

        // Create qin.config.json
        Path configFile = cwd.resolve(QinConstants.CONFIG_FILE);
        if (!Files.exists(configFile)) {
            String projectName = cwd.getFileName().toString();
            Files.writeString(configFile, String.format("""
                    {
                      "name": "%s",
                      "version": "1.0.0",
                      "entry": "src/Main.java",
                      "dependencies": {}
                    }
                    """, projectName));
        }

        System.out.println(green("✓ Project initialized!"));
        System.out.println(gray("  Run 'qin run' to start"));
    }

    private static void runProject(String[] args) throws Exception {
        // 检查是否指定了文件
        if (args.length > 0 && !args[0].startsWith("-")) {
            String file = args[0];
            Path filePath = Paths.get(file);
            if (!filePath.isAbsolute()) {
                filePath = Paths.get(System.getProperty("user.dir")).resolve(file);
            }

            // 使用插件系统自动检测文件类型
            RunnerPlugin plugin = PluginRegistry.getInstance().getPlugin(filePath);

            if (plugin != null) {
                // 找到对应插件，使用插件运行
                System.out.println(blue("→ Running with " + plugin.name() + " plugin..."));
                String[] runArgs = Arrays.copyOfRange(args, 1, args.length);
                plugin.run(filePath, runArgs, Paths.get(System.getProperty("user.dir")));
                System.out.println(green("✓ Done!"));
                return;
            }

            // 如果不是已知文件类型，检查是否是 .java
            if (!file.endsWith(".java")) {
                String ext = file.contains(".") ? file.substring(file.lastIndexOf('.')) : "无后缀";
                System.err.println(red("Error: 不支持的文件类型: " + ext));
                System.err.println("  支持的类型: " + PluginRegistry.getInstance().getSupportedExtensions());
                System.exit(1);
            }
        }

        // 原有的 Java 项目运行逻辑
        runJavaProject(args);
    }

    private static void runJavaProject(String[] args) throws Exception {
        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.out.println(envChecker.getInstallGuide("javac"));
            System.exit(1);
        }

        // 检查是否指定了 .java 文件
        String javaFile = null;
        List<String> runArgs = new ArrayList<>();

        if (args.length > 0 && args[0].endsWith(".java")) {
            javaFile = args[0];
            Path javaFilePath = Paths.get(System.getProperty("user.dir"), javaFile);
            if (!Files.exists(javaFilePath)) {
                System.err.println(red("Error: Java file not found: " + javaFile));
                System.exit(1);
            }
            for (int i = 1; i < args.length; i++) {
                runArgs.add(args[i]);
            }
        } else {
            runArgs = Arrays.asList(args);
        }

        // Resolve dependencies
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            classpath = ensureDependenciesSynced(config);
        }

        // Compile and run
        System.out.println(blue("→ Compiling and running..."));
        JavaRunner runner = new JavaRunner(config, classpath);

        if (javaFile != null) {
            runner.compileAndRunFile(javaFile, runArgs);
        } else {
            runner.compileAndRun(runArgs);
        }

        System.out.println(green("✓ Done!"));
    }

    private static void buildProject(String[] args) throws Exception {
        boolean debug = Arrays.asList(args).contains("--debug");
        boolean clean = Arrays.asList(args).contains("--clean");

        if (clean) {
            cleanProject();
        }

        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Build Fat Jar
        System.out.println(blue("→ Building Fat Jar..."));
        FatJarBuilder builder = new FatJarBuilder(config, debug);
        BuildResult result = builder.build();

        if (result.isSuccess()) {
            System.out.println(green("✓ Fat Jar built successfully: " + result.getOutputPath()));
        } else {
            System.err.println(red("Build failed: ") + result.getError());
            System.exit(1);
        }
    }

    private static void devMode(String[] args) throws Exception {
        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Resolve dependencies
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            System.out.println(blue("→ Resolving dependencies..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    System.getProperty("user.dir"), config.localRep());
            classpath = resolver.resolveFromObject(deps);
        }

        System.out.println(blue("→ Starting development mode..."));
        JavaRunner runner = new JavaRunner(config, classpath);

        // Simple dev mode - just compile and run
        // TODO: Add hot reload support
        runner.compileAndRun(new ArrayList<>());

        System.out.println(green("✓ Development server started"));
        System.out.println(gray("  Press Ctrl+C to stop"));
    }

    private static void compileProject(String[] args) throws Exception {
        String outputDir = QinConstants.BUILD_CLASSES_DIR;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-o".equals(args[i]) || "--output".equals(args[i])) {
                outputDir = args[i + 1];
            }
        }

        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Resolve dependencies: 本地优先,远程fallback
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            // 1. 先尝试本地解析
            LocalProjectResolver localResolver = new LocalProjectResolver(System.getProperty("user.dir"));
            LocalProjectResolver.ResolutionResult localResult = localResolver.resolveDependencies(deps);

            // 2. 对于未在本地找到的依赖,从Maven下载
            if (localResult.remoteDependencies != null && !localResult.remoteDependencies.isEmpty()) {
                System.out.println(blue("→ Resolving remote dependencies..."));
                String csCommand = ensureCoursier();
                DependencyResolver resolver = new DependencyResolver(
                        csCommand, config.repositories(), null,
                        System.getProperty("user.dir"), config.localRep());
                classpath = resolver.resolveFromObject(localResult.remoteDependencies);
            }
        }

        System.out.println(blue("→ Compiling..."));
        JavaRunner runner = new JavaRunner(config, classpath);
        CompileResult result = runner.compile();

        if (result.isSuccess()) {
            System.out.println(green("✓ Compiled " + result.getCompiledFiles() + " files to " + outputDir));
        } else {
            System.err.println(red("Compilation failed: ") + result.getError());
            System.exit(1);
        }
    }

    private static void cleanProject() throws IOException {
        Path buildDir = Paths.get(System.getProperty("user.dir"), "build");

        if (Files.exists(buildDir)) {
            System.out.println(blue("→ Cleaning build directory..."));
            deleteDir(buildDir);
            System.out.println(green("✓ Cleaned build/"));
        } else {
            System.out.println(gray("✓ No build directory to clean"));
        }
    }

    private static void syncDependencies() throws Exception {
        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        Map<String, String> deps = config.dependencies();
        if (deps == null || deps.isEmpty()) {
            System.out.println(green("✓ No dependencies to sync"));
            return;
        }

        syncDependenciesCore(config);
    }

    /**
     * 同步依赖的核心逻辑，返回生成的 classpath
     */
    private static String syncDependenciesCore(QinConfig config) throws Exception {
        Map<String, String> deps = config.dependencies();

        System.out.println(blue("→ Syncing dependencies..."));
        String sep = QinConstants.getClasspathSeparator();
        List<String> classpaths = new ArrayList<>();

        // 1. 先用 LocalProjectResolver 解析本地依赖
        LocalProjectResolver localResolver = new LocalProjectResolver(System.getProperty("user.dir"));
        LocalProjectResolver.ResolutionResult localResult = localResolver.resolveDependencies(deps);

        int localCount = 0;
        if (!localResult.localClasspath.isEmpty()) {
            localCount = localResult.localClasspath.split(sep).length;
            System.out.println(blue("  → Found " + localCount + " local dependencies"));
            classpaths.add(localResult.localClasspath);
        }

        // 2. 只有远程依赖才调用 Coursier
        int remoteCount = 0;
        if (!localResult.remoteDependencies.isEmpty()) {
            System.out.println(
                    blue("  → Resolving " + localResult.remoteDependencies.size() + " remote dependencies..."));
            System.out.println(blue("→ Checking environment..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    System.getProperty("user.dir"), config.localRep());

            String remoteClasspath = resolver.resolveFromObject(localResult.remoteDependencies);

            if (!remoteClasspath.isEmpty()) {
                String[] jarPaths = remoteClasspath.split(sep);
                remoteCount = jarPaths.length;
                classpaths.add(remoteClasspath);
            }
        }

        // Save classpath cache to .qin/classpath.json
        Path cacheDir = com.qin.core.QinPaths.getQinDir(System.getProperty("user.dir"));
        Files.createDirectories(cacheDir);

        String classpath = String.join(sep, classpaths);

        // 按配置中的依赖顺序排序 classpath
        classpath = sortClasspathByConfigOrder(classpath, deps);

        String json = buildClasspathJson(classpath);
        Files.writeString(com.qin.core.QinPaths.getClasspathCache(System.getProperty("user.dir")), json);

        // 生成 IDEA 库配置文件（.idea/libraries/*.xml）
        if (!classpath.isEmpty()) {
            try {
                System.out.println(blue("→ Generating IDEA library configs..."));
                IdeaLibraryGenerator ideaGen = new IdeaLibraryGenerator(System.getProperty("user.dir"));
                ideaGen.cleanLibraryConfigs(); // 清理旧配置
                int libCount = ideaGen.generateLibraryConfigs(classpath);
                System.out.println(green("  ✓ Generated " + libCount + " library configs in .idea/libraries/"));
            } catch (IOException e) {
                System.err.println(yellow("  Warning: Failed to generate IDEA configs: " + e.getMessage()));
            }
        }

        System.out.println(green("✓ Dependencies synced (" + localCount + " local, " + remoteCount + " remote)"));
        System.out.println(gray("  Cache: " + com.qin.core.QinPaths.CLASSPATH_CACHE));

        return classpath;
    }

    /**
     * 确保依赖已同步，如果缓存有效则使用缓存，否则执行同步
     * 
     * @return classpath 字符串
     */
    private static String ensureDependenciesSynced(QinConfig config) throws Exception {
        String cwd = System.getProperty("user.dir");
        Path cacheFile = com.qin.core.QinPaths.getClasspathCache(cwd);
        Path configFile = Paths.get(cwd, QinConstants.CONFIG_FILE);

        // 检查缓存是否有效
        if (Files.exists(cacheFile) && Files.exists(configFile)) {
            try {
                if (Files.getLastModifiedTime(cacheFile).compareTo(
                        Files.getLastModifiedTime(configFile)) > 0) {
                    String json = Files.readString(cacheFile);
                    String classpath = parseClasspathFromJson(json);
                    if (!classpath.isEmpty()) {
                        // 验证所有 jar 文件是否存在
                        if (validateClasspathFiles(classpath)) {
                            System.out.println(
                                    blue("→ Using cached dependencies (" + com.qin.core.QinPaths.CLASSPATH_CACHE
                                            + ")"));
                            return classpath;
                        } else {
                            System.out.println(
                                    yellow("→ Cache invalid (some jars missing), re-syncing..."));
                        }
                    }
                }
            } catch (IOException e) {
                // 缓存读取失败，重新同步
            }
        }

        // 缓存无效，执行同步
        return syncDependenciesCore(config);
    }

    /**
     * 验证 classpath 中的所有文件是否存在
     */
    private static boolean validateClasspathFiles(String classpath) {
        if (classpath == null || classpath.isEmpty()) {
            return true;
        }
        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep);
        for (String path : paths) {
            if (path.isEmpty())
                continue;
            if (!Files.exists(Paths.get(path))) {
                return false;
            }
        }
        return true;
    }

    private static void runTests(String[] args) throws Exception {
        String filter = null;
        boolean verbose = false;

        for (int i = 0; i < args.length; i++) {
            if (("-f".equals(args[i]) || "--filter".equals(args[i])) && i + 1 < args.length) {
                filter = args[i + 1];
            }
            if ("-v".equals(args[i]) || "--verbose".equals(args[i])) {
                verbose = true;
            }
        }

        System.out.println(blue("→ Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Compile main source first
        System.out.println(blue("→ Compiling source code..."));
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    System.getProperty("user.dir"), config.localRep());
            classpath = resolver.resolveFromObject(deps);
        }

        JavaRunner runner = new JavaRunner(config, classpath);
        CompileResult compileResult = runner.compile();

        if (!compileResult.isSuccess()) {
            System.err.println(red("Compilation failed: ") + compileResult.getError());
            System.exit(1);
        }

        System.out.println(blue("→ Running tests..."));
        // TODO: Implement test runner with JUnit
        System.out.println(yellow("Test runner not yet implemented in Java version"));
    }

    private static String ensureCoursier() throws Exception {
        if (envChecker.checkCoursier()) {
            return envChecker.getCoursierCommand();
        }

        boolean installed = envChecker.installCoursier();
        if (!installed) {
            throw new Exception("Unable to install Coursier. Please install manually.");
        }
        return envChecker.getCoursierCommand();
    }

    private static void printHelp() {
        System.out.println("""
                Qin - Java-Vite Build Tool
                A modern Java build tool with zero XML configuration

                Usage: qin <command> [options]

                Commands:
                  init        Initialize a new Qin project
                  run         Compile and run the Java program
                  build       Build a Fat Jar (Uber Jar)
                  dev         Start development server with hot reload
                  compile     Compile Java source code
                  clean       Clean build artifacts
                  sync        Sync dependencies
                  test        Run JUnit tests
                  help        Show this help message
                  version     Show version

                Options:
                  --debug     Keep temporary files for debugging (build)
                  --clean     Clean build directory before building (build)
                  -o, --output <dir>  Output directory (compile)
                  -f, --filter <pattern>  Filter tests (test)
                  -v, --verbose  Show verbose output

                Examples:
                  qin init              # Initialize new project
                  qin run               # Compile and run
                  qin build             # Build Fat Jar
                  qin dev               # Start dev server
                """);
    }

    private static void deleteDir(Path dir) throws IOException {
        if (!Files.exists(dir))
            return;
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        // Ignore
                    }
                });
    }

    // ANSI color helpers
    private static String blue(String s) {
        return "\u001B[34m" + s + "\u001B[0m";
    }

    private static String green(String s) {
        return "\u001B[32m" + s + "\u001B[0m";
    }

    private static String red(String s) {
        return "\u001B[31m" + s + "\u001B[0m";
    }

    private static String yellow(String s) {
        return "\u001B[33m" + s + "\u001B[0m";
    }

    private static String gray(String s) {
        return "\u001B[90m" + s + "\u001B[0m";
    }

    /**
     * 从 .qin/classpath.json 解析 classpath
     */
    private static String parseClasspathFromJson(String json) {
        try {
            // 简单解析 JSON 数组
            int start = json.indexOf("[");
            int end = json.lastIndexOf("]");
            if (start < 0 || end < 0)
                return "";

            String arrayContent = json.substring(start + 1, end);
            List<String> paths = new ArrayList<>();

            // 解析每个路径
            int pos = 0;
            while (pos < arrayContent.length()) {
                int quote1 = arrayContent.indexOf("\"", pos);
                if (quote1 < 0)
                    break;
                int quote2 = arrayContent.indexOf("\"", quote1 + 1);
                if (quote2 < 0)
                    break;
                paths.add(arrayContent.substring(quote1 + 1, quote2));
                pos = quote2 + 1;
            }

            String sep = QinConstants.getClasspathSeparator();
            return String.join(sep, paths);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 构建 .qin/classpath.json 格式
     */
    private static String buildClasspathJson(String classpath) {
        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep.equals(";") ? ";" : ":");

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"classpath\": [\n");
        for (int i = 0; i < paths.length; i++) {
            sb.append("    \"").append(paths[i].replace("\\", "/")).append("\"");
            if (i < paths.length - 1)
                sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"lastUpdated\": \"").append(java.time.Instant.now()).append("\"\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 按配置中的依赖顺序排序 classpath
     * 
     * @param classpath 原始 classpath（分号/冒号分隔）
     * @param deps      配置中的依赖（保持插入顺序）
     * @return 排序后的 classpath
     */
    private static String sortClasspathByConfigOrder(String classpath, Map<String, String> deps) {
        if (classpath == null || classpath.isEmpty() || deps == null || deps.isEmpty()) {
            return classpath;
        }

        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep.equals(";") ? ";" : ":");

        // 创建 artifactId 到顺序的映射
        Map<String, Integer> orderMap = new LinkedHashMap<>();
        int order = 0;
        for (String depKey : deps.keySet()) {
            // depKey 格式: groupId@artifactId 或 groupId:artifactId
            String artifactId = extractArtifactId(depKey);
            orderMap.put(artifactId.toLowerCase(), order++);
        }

        // 按配置顺序排序
        List<String> sortedPaths = new ArrayList<>(Arrays.asList(paths));
        sortedPaths.sort((a, b) -> {
            String artifactA = extractArtifactIdFromPath(a).toLowerCase();
            String artifactB = extractArtifactIdFromPath(b).toLowerCase();

            int orderA = orderMap.getOrDefault(artifactA, Integer.MAX_VALUE);
            int orderB = orderMap.getOrDefault(artifactB, Integer.MAX_VALUE);

            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }
            // 如果都不在配置中，按字母顺序
            return a.compareToIgnoreCase(b);
        });

        return String.join(sep, sortedPaths);
    }

    /**
     * 从依赖 key 中提取 artifactId
     */
    private static String extractArtifactId(String depKey) {
        // 格式: groupId@artifactId 或 groupId:artifactId
        int sepIndex = depKey.lastIndexOf('@');
        if (sepIndex < 0)
            sepIndex = depKey.lastIndexOf(':');
        return sepIndex >= 0 ? depKey.substring(sepIndex + 1) : depKey;
    }

    /**
     * 从 jar 路径中提取 artifactId
     */
    private static String extractArtifactIdFromPath(String path) {
        // 路径格式: .../groupId/artifactId/version/artifactId-version.jar
        // 或: .../build/classes
        if (path.contains("build") || path.contains("classes")) {
            // 本地项目，使用目录名
            Path p = Paths.get(path);
            if (p.getParent() != null && p.getParent().getParent() != null) {
                return p.getParent().getParent().getFileName().toString();
            }
            return p.getFileName().toString();
        }

        // Maven jar 路径
        String fileName = Paths.get(path).getFileName().toString();
        // 移除 .jar 和版本号
        if (fileName.endsWith(".jar")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        // 尝试提取 artifactId（在最后一个 - 之前，如果后面是版本号）
        int lastDash = fileName.lastIndexOf('-');
        if (lastDash > 0) {
            String suffix = fileName.substring(lastDash + 1);
            // 检查是否是版本号（以数字开头）
            if (!suffix.isEmpty() && Character.isDigit(suffix.charAt(0))) {
                return fileName.substring(0, lastDash);
            }
        }
        return fileName;
    }
}
