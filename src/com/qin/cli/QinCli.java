package com.qin.cli;

import com.qin.core.*;
import com.qin.plugins.PluginRegistry;
import com.qin.plugins.RunnerPlugin;
import com.qin.types.BuildResult;
import com.qin.types.CompileResult;
import com.qin.types.EnvironmentStatus;
import com.qin.types.QinConfig;
import com.qin.core.*;
import com.qin.types.*;
import com.qin.plugins.*;
import com.qin.constants.QinConstants;
import com.qin.utils.QinUtils;

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
                case "compile" -> compileProject(cmdArgs);
                case "test" -> runTests(cmdArgs);
                case "jar" -> jarProject(cmdArgs);           // 🆕 新增
                case "fatjar" -> fatjarProject(cmdArgs);     // 🆕 新增
                case "build" -> buildProject(cmdArgs);
                case "clean" -> cleanProject();
                case "sync" -> syncDependencies(cmdArgs);
                case "deps" -> showDependencies(cmdArgs);    // 🆕 新增
                case "dev" -> devMode(cmdArgs);
                case "dist" -> distProject();
                case "bsp" -> startBspServer();              // 🆕 BSP Server
                case "bsp-init" -> initBspConfig();          // 🆕 生成 BSP 配置
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
        System.out.println(blue("-> Initializing new Qin project..."));

        Path cwd = Paths.get(QinConstants.getCwd());

        // Create directories
        Files.createDirectories(cwd.resolve("src"));

        // Create Main.java
        Path mainJava = cwd.resolve(QinConstants.DEFAULT_ENTRY);
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
                      "entry": "%s",
                      "dependencies": {}
                    }
                    """, projectName, QinConstants.DEFAULT_ENTRY));
        }

        System.out.println(green("[OK] Project initialized!"));
        System.out.println(gray("  Run 'qin run' to start"));
    }

    private static void runProject(String[] args) throws Exception {
        // 检查是否指定了文件
        if (args.length > 0 && !args[0].startsWith("-")) {
            String file = args[0];
            Path filePath = Paths.get(file);
            if (!filePath.isAbsolute()) {
                filePath = Paths.get(QinConstants.getCwd()).resolve(file);
            }

            // 使用插件系统自动检测文件类型
            RunnerPlugin plugin = PluginRegistry.getInstance().getPlugin(filePath);

            if (plugin != null) {
                // 找到对应插件，使用插件运行
                System.out.println(blue("-> Running with " + plugin.name() + " plugin..."));
                String[] runArgs = Arrays.copyOfRange(args, 1, args.length);
                plugin.run(filePath, runArgs, Paths.get(QinConstants.getCwd()));
                System.out.println(green("[OK] Done!"));
                return;
            }

            // 如果不是已知文件类型，检查是否是 .java
            if (!file.endsWith(".java")) {
                String ext = file.contains(".") ? file.substring(file.lastIndexOf('.')) : "<none>";
                System.err.println(red("Error: unsupported file type: " + ext));
                System.err.println("  Supported types: " + PluginRegistry.getInstance().getSupportedExtensions());
                System.exit(1);
            }
        }

        // 原有的 Java 项目运行逻辑
        runJavaProject(args);
    }

    private static void runJavaProject(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
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
            Path javaFilePath = Paths.get(QinConstants.getCwd(), javaFile);
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
        System.out.println(blue("-> Compiling and running..."));
        JavaRunner runner = new JavaRunner(config, classpath);

        if (javaFile != null) {
            runner.compileAndRunFile(javaFile, runArgs);
        } else {
            runner.compileAndRun(runArgs);
        }

        System.out.println(green("[OK] Done!"));
    }

    private static void buildProject(String[] args) throws Exception {
        boolean debug = Arrays.asList(args).contains("--debug");
        boolean clean = Arrays.asList(args).contains("--clean");
        boolean skipTests = Arrays.asList(args).contains("--skip-tests") ||
                           Arrays.asList(args).contains("-DskipTests");

        if (clean) {
            cleanProject();
        }

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // 使用 BuildLifecycle 进行完整构建
        System.out.println(blue("-> Building project..."));
        BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
        lifecycle.setSkipTests(skipTests);

        if (skipTests) {
            System.out.println(yellow("  (skipping tests)"));
        }

        BuildResult result = lifecycle.build();

        if (result.isSuccess()) {
            System.out.println(green("[OK] Build completed successfully!"));
            if (result.getOutputPath() != null) {
                System.out.println(green("  Output: " + result.getOutputPath()));
            }
        } else {
            System.err.println(red("Build failed: ") + result.getError());
            System.exit(1);
        }
    }

    private static void devMode(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
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
            System.out.println(blue("-> Resolving dependencies..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    QinConstants.getCwd(), config.localRep());
            classpath = resolver.resolveFromObject(deps);
        }

        System.out.println(blue("-> Starting development mode..."));
        JavaRunner runner = new JavaRunner(config, classpath);

        // Simple dev mode - just compile and run
        // TODO: Add hot reload support
        runner.compileAndRun(new ArrayList<>());

        System.out.println(green("[OK] Development server started"));
        System.out.println(gray("  Press Ctrl+C to stop"));
    }

    private static void compileProject(String[] args) throws Exception {
        String outputDir = QinConstants.BUILD_CLASSES_DIR;
        boolean skipSync = Arrays.asList(args).contains("--no-sync");

        for (int i = 0; i < args.length - 1; i++) {
            if ("-o".equals(args[i]) || "--output".equals(args[i])) {
                outputDir = args[i + 1];
            }
        }

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // 🔑 关键改动：compile 之前自动确保依赖已同步（复用 sync 的缓存）
        String classpath = "";
        if (!skipSync) {
            classpath = ensureDependenciesSynced(config);
        }

        System.out.println(blue("-> Compiling..."));
        JavaRunner runner = new JavaRunner(config, classpath);
        CompileResult result = runner.compile();

        if (result.isSuccess()) {
            System.out.println(green("[OK] Compiled " + result.getCompiledFiles() + " files to " + outputDir));
        } else {
            System.err.println(red("Compilation failed: ") + result.getError());
            System.exit(1);
        }
    }

    private static void cleanProject() throws IOException {
        Path buildDir = Paths.get(QinConstants.getCwd(), "build");

        if (Files.exists(buildDir)) {
            System.out.println(blue("-> Cleaning build directory..."));
            QinUtils.deleteDir(buildDir);
            System.out.println(green("[OK] Cleaned build/"));
        } else {
            System.out.println(gray("[OK] No build directory to clean"));
        }
    }

    private static void syncDependencies(String[] args) throws Exception {
        // 解析参数
        List<String> argList = Arrays.asList(args);
        boolean syncAll = argList.contains(QinConstants.ARG_ALL);
        boolean force = argList.contains(QinConstants.ARG_FORCE);
        boolean withCompile = argList.contains(QinConstants.ARG_COMPILE);

        if (syncAll) {
            // 同步所有子项目
            syncAllProjects(force, withCompile);
        } else {
            // 同步当前项目
            syncCurrentProject(force);

            // 如果指定了 --compile，同步后自动编译
            if (withCompile) {
                System.out.println();
                compileProject(new String[]{QinConstants.ARG_NO_SYNC});
            }
        }
    }

    /**
     * 同步当前项目的依赖
     */
    private static void syncCurrentProject(boolean force) throws Exception {
        String cwd = QinConstants.getCwd();

        // 如果不是强制同步，检查缓存
        if (!force && CacheValidator.isCacheValid(cwd)) {
            System.out.println(blue("-> Using cached dependencies (" + QinPaths.CLASSPATH_CACHE + ")"));
            System.out.println(green("[OK] Dependencies up to date (use --force to re-sync)"));
            return;
        }

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        if (deps.isEmpty()) {
            System.out.println(green("[OK] No dependencies to sync"));
            return;
        }

        syncDependenciesCore(config);
    }

    /**
     * 同步所有子项目（Monorepo 场景）
     * 向上查找根目录，递归扫描所有 qin.config.json 项目
     */
    private static void syncAllProjects(boolean force, boolean withCompile) throws Exception {
        String cwd = QinConstants.getCwd();

        System.out.println(blue("-> Scanning for Qin projects..."));
        List<Path> projects = LocalProjectResolver.scanAllProjects(cwd);

        if (projects.isEmpty()) {
            System.out.println(yellow("[WARN] No Qin projects found"));
            return;
        }

        System.out.println(blue("-> Found " + projects.size() + " Qin project(s)"));

        // 统计
        int synced = 0;
        int skipped = 0;
        int failed = 0;

        // 遍历所有项目，异步执行 sync
        List<Thread> threads = new ArrayList<>();
        List<SyncResult> results = Collections.synchronizedList(new ArrayList<>());

        for (Path projectPath : projects) {
            Thread t = new Thread(() -> {
                try {
                    String projectName = projectPath.getFileName().toString();

                    // 如果不是强制同步，检查缓存
                    if (!force && CacheValidator.isCacheValid(projectPath)) {
                        System.out.println(gray("  [SKIP] " + projectName + " (cached, skipped)"));
                        results.add(new SyncResult(projectName, SyncStatus.SKIPPED));
                        return;
                    }

                    // 执行 qin sync --compile（同步 + 编译）
                    System.out.println(blue("  -> Syncing " + projectName + "..."));

                    List<String> command = new ArrayList<>();
                    command.add(QinConstants.CMD_PREFIX);
                    command.add(QinConstants.CMD_FLAG);
                    command.add(QinConstants.QIN_CMD);
                    command.add("sync");
                    if (force) {
                        command.add(QinConstants.ARG_FORCE);
                    }
                    if (withCompile) {
                        command.add(QinConstants.ARG_COMPILE);
                    }

                    ProcessBuilder pb = new ProcessBuilder(command);
                    pb.directory(projectPath.toFile());
                    pb.redirectErrorStream(true);

                    Process process = pb.start();

                    // 读取输出（静默处理）
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                        while (reader.readLine() != null) {
                            // 静默消费输出
                        }
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        System.out.println(green("  [OK] " + projectName));
                        results.add(new SyncResult(projectName, SyncStatus.SUCCESS));
                    } else {
                        System.out.println(red("  [ERROR] " + projectName + " (exit: " + exitCode + ")"));
                        results.add(new SyncResult(projectName, SyncStatus.FAILED));
                    }
                } catch (Exception e) {
                    String projectName = projectPath.getFileName().toString();
                    System.out.println(red("  [ERROR] " + projectName + " (" + e.getMessage() + ")"));
                    results.add(new SyncResult(projectName, SyncStatus.FAILED));
                }
            });
            threads.add(t);
            t.start();
        }

        // 等待所有线程完成
        for (Thread t : threads) {
            t.join();
        }

        // 统计结果
        for (SyncResult r : results) {
            switch (r.status) {
                case SUCCESS -> synced++;
                case SKIPPED -> skipped++;
                case FAILED -> failed++;
            }
        }

        // 输出汇总
        System.out.println();
        if (failed > 0) {
            System.out.println(yellow("[WARN] Sync completed: " + synced + " synced, " + skipped + " skipped, " + failed + " failed"));
        } else {
            System.out.println(green("[OK] All projects synced: " + synced + " synced, " + skipped + " skipped"));
        }
    }

    /**
     * 同步结果状态
     */
    private enum SyncStatus {
        SUCCESS, SKIPPED, FAILED
    }

    /**
     * 同步结果
     */
    private record SyncResult(String projectName, SyncStatus status) {}

    /**
     * 同步依赖的核心逻辑，返回生成的 classpath
     * 使用增强版解析器，支持自动编译本地项目
     */
    private static String syncDependenciesCore(QinConfig config) throws Exception {
        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        System.out.println(blue("-> Syncing dependencies..."));
        String sep = QinConstants.getClasspathSeparator();
        List<String> classpaths = new ArrayList<>();

        // 1. 使用增强版解析器（支持自动编译本地项目）
        LocalProjectResolverEnhanced localResolver = new LocalProjectResolverEnhanced(QinConstants.getCwd());
        LocalProjectResolverEnhanced.ResolutionResult localResult = localResolver.resolveDependencies(deps);

        int localCount = localResult.localCount;
        if (!localResult.localClasspath.isEmpty()) {
            System.out.println(blue("  -> Found " + localCount + " local dependencies"));
            if (localResult.autoCompiledCount > 0) {
                System.out.println(green("  [OK] Auto-compiled " + localResult.autoCompiledCount + " project(s)"));
            }
            classpaths.add(localResult.localClasspath);
        }

        // 2. 只有远程依赖才调用 Coursier
        int remoteCount = 0;
        if (!localResult.remoteDependencies.isEmpty()) {
            System.out.println(
                    blue("  -> Resolving " + localResult.remoteDependencies.size() + " remote dependencies..."));
            System.out.println(blue("-> Checking environment..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    QinConstants.getCwd(), config.localRep());

            String remoteClasspath = resolver.resolveFromObject(localResult.remoteDependencies);

            if (!remoteClasspath.isEmpty()) {
                String[] jarPaths = remoteClasspath.split(sep);
                remoteCount = jarPaths.length;
                classpaths.add(remoteClasspath);
            }
        }

        // Save classpath cache to .qin/classpath.json
        Path cacheDir = QinPaths.getQinDir(QinConstants.getCwd());
        Files.createDirectories(cacheDir);

        String classpath = String.join(sep, classpaths);

        // 按配置中的依赖顺序排序 classpath
        classpath = sortClasspathByConfigOrder(classpath, deps);

        String json = buildClasspathJson(classpath);
        Files.writeString(QinPaths.getClasspathCache(QinConstants.getCwd()), json);

        // 生成 IDEA 库配置文件（.idea/libraries/*.xml）
        if (!classpath.isEmpty()) {
            try {
                System.out.println(blue("-> Generating IDEA library configs..."));
                IdeaLibraryGenerator ideaGen = new IdeaLibraryGenerator(QinConstants.getCwd());
                ideaGen.cleanLibraryConfigs(); // 清理旧配置
                int libCount = ideaGen.generateLibraryConfigs(classpath);
                System.out.println(green("  [OK] Generated " + libCount + " library configs in .idea/libraries/"));
            } catch (IOException e) {
                System.err.println(yellow("  Warning: Failed to generate IDEA configs: " + e.getMessage()));
            }
        }

        System.out.println(green("[OK] Dependencies synced (" + localCount + " local, " + remoteCount + " remote)"));
        System.out.println(gray("  Cache: " + QinPaths.CLASSPATH_CACHE));

        return classpath;
    }

    /**
     * 确保依赖已同步，如果缓存有效则使用缓存，否则执行同步
     * 
     * @return classpath 字符串
     */
    private static String ensureDependenciesSynced(QinConfig config) throws Exception {
        String cwd = QinConstants.getCwd();

        // 使用通用缓存验证器检查缓存
        if (CacheValidator.isCacheValid(cwd)) {
            String classpath = CacheValidator.getCachedClasspath(cwd);
            if (classpath != null) {
                System.out.println(
                        blue("-> Using cached dependencies (" + QinPaths.CLASSPATH_CACHE + ")"));
                return classpath;
            }
        }

        // 缓存无效，执行同步
        return syncDependenciesCore(config);
    }

    /**
     * 验证 classpath 中的所有文件是否存在
     * @deprecated 使用 CacheValidator.validateClasspathFiles() 代替
     */
    @Deprecated
    private static boolean validateClasspathFiles(String classpath) {
        return CacheValidator.validateClasspathFiles(classpath);
    }

    private static void distProject() throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Get dist directory from config or use default
        String distDir = QinConstants.getDistDir(config.output());
        Path distPath = Paths.get(QinConstants.getCwd(), distDir);

        // Create dist directory if not exists
        if (!Files.exists(distPath)) {
            Files.createDirectories(distPath);
        }

        // Build Fat Jar first
        System.out.println(blue("-> Building Fat Jar for distribution..."));
        FatJarBuilder builder = new FatJarBuilder(config, false);
        BuildResult result = builder.build();

        if (!result.isSuccess()) {
            System.err.println(red("Build failed: ") + result.getError());
            System.exit(1);
        }

        // Copy the jar to dist directory
        Path sourceJar = Paths.get(result.getOutputPath());
        String jarName = QinConstants.getJarName(config.output());
        Path targetJar = distPath.resolve(jarName);

        System.out.println(blue("-> Copying to " + distDir + "/..."));
        Files.copy(sourceJar, targetJar, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        System.out.println(green("[OK] Distribution created: " + distDir + "/" + jarName));
        System.out.println(gray("  Run with: java -jar " + distDir + "/" + jarName));
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

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // Compile main source first
        System.out.println(blue("-> Compiling source code..."));
        String classpath = "";
        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        if (!deps.isEmpty()) {
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                    csCommand, config.repositories(), null,
                    QinConstants.getCwd(), config.localRep());
            classpath = resolver.resolveFromObject(deps);
        }

        JavaRunner runner = new JavaRunner(config, classpath);
        CompileResult compileResult = runner.compile();

        if (!compileResult.isSuccess()) {
            System.err.println(red("Compilation failed: ") + compileResult.getError());
            System.exit(1);
        }

        System.out.println(blue("-> Running tests..."));
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

    /**
     * qin jar - 打包普通 JAR（不含依赖）
     */
    private static void jarProject(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
        JarResult result = lifecycle.jar();

        if (result.isSuccess()) {
            System.out.println(green("[OK] JAR created: " + result.getJarPath()));
            System.out.println(gray("  Size: " + formatSize(result.getJarSize())));
        } else {
            System.err.println(red("[ERROR] Failed: " + result.getError()));
            System.exit(1);
        }
    }

    /**
     * qin fatjar - 打包 Fat JAR（包含所有依赖）
     */
    private static void fatjarProject(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
        JarResult result = lifecycle.fatjar();

        if (result.isSuccess()) {
            System.out.println(green("[OK] Fat JAR created: " + result.getJarPath()));
            System.out.println(gray("  Size: " + formatSize(result.getJarSize())));
        } else {
            System.err.println(red("[ERROR] Failed: " + result.getError()));
            System.exit(1);
        }
    }

    /**
     * qin deps - 显示依赖树
     */
    private static void showDependencies(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        System.out.println(blue("-> Dependencies:"));

        Map<String, String> allDeps = new HashMap<>();
        if (config.dependencies() != null) allDeps.putAll(config.dependencies());
        if (config.devDependencies() != null) allDeps.putAll(config.devDependencies());

        if (allDeps.isEmpty()) {
            System.out.println(gray("  No dependencies"));
            return;
        }

        for (Map.Entry<String, String> dep : allDeps.entrySet()) {
            System.out.println("  - " + dep.getKey() + " : " + dep.getValue());
        }
    }

    // ==================== BSP 相关命令 ====================

    /**
     * 启动 BSP Server
     * IDE 会调用此命令与构建工具通信
     */
    private static void startBspServer() throws Exception {
        com.qin.bsp.QinBspServer server = new com.qin.bsp.QinBspServer(QinConstants.getCwd());
        server.start();
    }

    /**
     * 初始化 BSP 配置
     * 生成 .bsp/qin.json 文件，让 IDE 能发现此构建工具
     */
    private static void initBspConfig() throws Exception {
        com.qin.bsp.BspConnectionGenerator generator =
            new com.qin.bsp.BspConnectionGenerator(QinConstants.getCwd());

        if (generator.exists()) {
            System.out.println(yellow("BSP configuration already exists."));
            System.out.println("  Location: .bsp/qin.json");
            return;
        }

        java.nio.file.Path configPath = generator.generate();
        System.out.println(green("[OK] BSP configuration generated!"));
        System.out.println("  Location: " + configPath);
        System.out.println();
        System.out.println("Your IDE should now detect Qin as the build server.");
        System.out.println("Restart your IDE if it doesn't detect automatically.");
    }

    /**
     * 格式化文件大小（辅助方法）
     */
    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }

    private static void printHelp() {
        System.out.println("""
                Qin - Java-Vite Build Tool
                A modern Java build tool with zero XML configuration

                Usage: qin <command> [options]

                Commands:
                  init        Initialize a new Qin project
                  run         Compile and run the Java program
                  compile     Compile Java source code
                  test        Run JUnit tests
                  jar         Build a JAR (without dependencies)
                  fatjar      Build a Fat JAR (with all dependencies)
                  build       Full build (compile + test + jar)
                  clean       Clean build artifacts
                  sync        Sync dependencies (auto-compiles local projects)
                  deps        Show dependency tree
                  dev         Start development server with hot reload
                  dist        Create distribution package

                IDE Integration (BSP):
                  bsp         Start BSP Server (called by IDE)
                  bsp-init    Generate .bsp/qin.json for IDE discovery

                Other:
                  help        Show this help message
                  version     Show version

                Options:
                  --skip-tests    Skip tests during build
                  -DskipTests     Skip tests (Maven-style)
                  --debug         Keep temporary files for debugging
                  --clean         Clean build directory before building
                  -o, --output <dir>  Output directory (compile)
                  -f, --filter <pattern>  Filter tests (test)
                  -v, --verbose   Show verbose output

                Examples:
                  qin init                    # Initialize new project
                  qin run                     # Compile and run
                  qin compile                 # Compile only
                  qin jar                     # Build JAR (without dependencies)
                  qin fatjar                  # Build Fat JAR (with dependencies)
                  qin build                   # Full build
                  qin build --skip-tests      # Build without running tests
                  qin sync                    # Sync deps (auto-compiles local projects)
                  qin bsp-init                # Generate BSP config for IDE
                """);
    }

    // ANSI color helpers
    private static String blue(String s) {
        return colorize("\u001B[34m", s);
    }

    private static String green(String s) {
        return colorize("\u001B[32m", s);
    }

    private static String red(String s) {
        return colorize("\u001B[31m", s);
    }

    private static String yellow(String s) {
        return colorize("\u001B[33m", s);
    }

    private static String gray(String s) {
        return colorize("\u001B[90m", s);
    }

    private static String colorize(String ansi, String value) {
        return ansi + normalizeConsoleText(value) + "\u001B[0m";
    }

    private static String normalizeConsoleText(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        return value.trim();
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
