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
                case "jar" -> jarProject(cmdArgs);           // 棣冨晭 閺傛澘顤?
                case "fatjar" -> fatjarProject(cmdArgs);     // 棣冨晭 閺傛澘顤?
                case "build" -> buildProject(cmdArgs);
                case "clean" -> cleanProject();
                case "sync" -> syncDependencies(cmdArgs);
                case "deps" -> showDependencies(cmdArgs);    // 棣冨晭 閺傛澘顤?
                case "dev" -> devMode(cmdArgs);
                case "dist" -> distProject();
                case "bsp" -> startBspServer();              // 棣冨晭 BSP Server
                case "bsp-init" -> initBspConfig();          // 棣冨晭 閻㈢喐鍨?BSP 闁板秶鐤?
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
        // 濡偓閺屻儲妲搁崥锔藉瘹鐎规矮绨￠弬鍥︽
        if (args.length > 0 && !args[0].startsWith("-")) {
            String target = args[0];
            Path targetPath = Paths.get(target);
            if (!targetPath.isAbsolute()) {
                targetPath = Paths.get(QinConstants.getCwd()).resolve(target);
            }

            // 娴ｈ法鏁ら幓鎺嶆缁崵绮洪懛顏勫З濡偓濞村鏋冩禒鍓佽閸?
            RunnerPlugin plugin = PluginRegistry.getInstance().getPlugin(targetPath);

            if (plugin != null) {
                // 閹垫儳鍩岀€电懓绨查幓鎺嶆閿涘奔濞囬悽銊﹀絻娴犳儼绻嶇悰?
                System.out.println(blue("-> Running with " + plugin.name() + " plugin..."));
                String[] runArgs = Arrays.copyOfRange(args, 1, args.length);
                plugin.run(targetPath, runArgs, Paths.get(QinConstants.getCwd()));
                System.out.println(green("[OK] Done!"));
                return;
            }
        }

        // 閸樼喐婀侀惃?Java 妞ゅ湱娲版潻鎰攽闁槒绶?
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

        // 濡偓閺屻儲妲搁崥锔藉瘹鐎规矮绨?.java 閺傚洣娆?
        JavaRunOptions runOptions = parseJavaRunOptions(config, args);
        if (runOptions.javaFile != null) {
            Path javaFilePath = Paths.get(QinConstants.getCwd(), runOptions.javaFile);
            if (!Files.exists(javaFilePath)) {
                System.err.println(red("Error: Java file not found: " + runOptions.javaFile));
                System.exit(1);
            }
        }

        // Resolve dependencies
        String classpath = "";
        Map<String, String> deps = collectAllDependencies(config);
        if (!deps.isEmpty()) {
            classpath = ensureDependenciesSynced(config);
        }

        // Compile and run
        System.out.println(blue("-> Compiling and running..."));
        JavaRunner runner = new JavaRunner(config, classpath);

        if (runOptions.javaFile != null) {
            runner.compileAndRunFile(runOptions.javaFile, runOptions.programArgs, runOptions.jvmArgs);
        } else {
            runner.compileAndRun(runOptions.programArgs, runOptions.jvmArgs);
        }

        System.out.println(green("[OK] Done!"));
    }

    private static JavaRunOptions parseJavaRunOptions(QinConfig config, String[] args) {
        JavaRunOptions options = new JavaRunOptions();
        boolean passthrough = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (passthrough) {
                options.programArgs.add(arg);
                continue;
            }

            if ("--".equals(arg)) {
                passthrough = true;
                continue;
            }

            if (arg.startsWith("--main")) {
                throw new IllegalArgumentException("--main is no longer supported. Use `qin run <target>`.");
            }

            if (!arg.startsWith("-") && options.javaFile == null) {
                options.javaFile = resolveRunTargetToJavaFile(config, arg);
                if (options.javaFile == null) {
                    throw new IllegalArgumentException("Unable to resolve run target: " + arg);
                }
                continue;
            }

            if (arg.startsWith("--jvm-args=")) {
                options.jvmArgs.addAll(tokenizeArguments(arg.substring("--jvm-args=".length())));
                continue;
            }
            if ("--jvm-args".equals(arg)) {
                options.jvmArgs.addAll(tokenizeArguments(nextArg(args, ++i, "--jvm-args")));
                continue;
            }

            if ("--debug".equals(arg)) {
                options.debug = true;
                continue;
            }
            if (arg.startsWith("--debug-port=")) {
                options.debugPort = parsePort(arg.substring("--debug-port=".length()), options.debugPort);
                continue;
            }
            if ("--debug-port".equals(arg)) {
                options.debugPort = parsePort(nextArg(args, ++i, "--debug-port"), options.debugPort);
                continue;
            }

            options.programArgs.add(arg);
        }

        if (options.debug) {
            options.jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:" + options.debugPort);
        }

        return options;
    }
    private static String nextArg(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value for " + flag);
        }
        return args[index];
    }

    private static int parsePort(String value, int defaultPort) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ignored) {
            return defaultPort;
        }
    }

    private static String resolveRunTargetToJavaFile(QinConfig config, String runTarget) {
        if (runTarget == null || runTarget.isBlank()) {
            return null;
        }

        String normalizedTarget = runTarget.trim();
        if (normalizedTarget.endsWith(".java")) {
            return normalizeRelativePath(normalizedTarget);
        }

        if (normalizedTarget.contains("/") || normalizedTarget.contains("\\")) {
            String pathTarget = normalizeRelativePath(normalizedTarget);
            if (!pathTarget.endsWith(".java")) {
                pathTarget = pathTarget + ".java";
            }
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(pathTarget).normalize();
            return Files.exists(absolute) ? pathTarget : null;
        }

        String relativeClassPath = normalizedTarget.replace('.', '/') + ".java";
        LinkedHashSet<Path> candidates = new LinkedHashSet<>();
        String sourceDir = JavaCompileConfig.from(config).sourceDir();
        if (sourceDir != null && !sourceDir.isBlank()) {
            candidates.add(Paths.get(sourceDir, relativeClassPath));
        }
        candidates.add(Paths.get(QinConstants.JAVA_SOURCE_DIR, relativeClassPath));
        candidates.add(Paths.get("src/java", relativeClassPath));
        candidates.add(Paths.get(QinConstants.DEFAULT_SOURCE_DIR, relativeClassPath));
        candidates.add(Paths.get(QinConstants.MAIN_SOURCE_DIR, relativeClassPath));

        for (Path candidate : candidates) {
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(candidate).normalize();
            if (Files.exists(absolute)) {
                return normalizeRelativePath(candidate.toString());
            }
        }

        return null;
    }
    private static List<String> tokenizeArguments(String raw) {
        List<String> tokens = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return tokens;
        }

        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char quoteChar = 0;

        for (int i = 0; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            if (inQuotes) {
                if (ch == quoteChar) {
                    inQuotes = false;
                } else if (ch == '\\' && i + 1 < raw.length()) {
                    current.append(raw.charAt(++i));
                } else {
                    current.append(ch);
                }
                continue;
            }

            if (ch == '"' || ch == '\'') {
                inQuotes = true;
                quoteChar = ch;
                continue;
            }

            if (Character.isWhitespace(ch)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }

            current.append(ch);
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }
        return tokens;
    }

    private static String normalizeRelativePath(String path) {
        return path.replace('\\', '/');
    }

    private static final class JavaRunOptions {
        private String javaFile;
        private final List<String> programArgs = new ArrayList<>();
        private final List<String> jvmArgs = new ArrayList<>();
        private boolean debug;
        private int debugPort = 5005;
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

        // 娴ｈ法鏁?BuildLifecycle 鏉╂稖顢戠€瑰本鏆ｉ弸鍕紦
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

        // 棣冩斀 閸忔娊鏁弨鐟板З閿涙瓭ompile 娑斿澧犻懛顏勫З绾喕绻氭笟婵婄瀹告彃鎮撳銉礄婢跺秶鏁?sync 閻ㄥ嫮绱︾€涙﹫绱?
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
        // 鐟欙絾鐎介崣鍌涙殶
        List<String> argList = Arrays.asList(args);
        boolean syncAll = argList.contains(QinConstants.ARG_ALL);
        boolean force = argList.contains(QinConstants.ARG_FORCE);
        boolean withCompile = argList.contains(QinConstants.ARG_COMPILE);

        if (syncAll) {
            // 閸氬本顒為幍鈧張澶婄摍妞ゅ湱娲?
            syncAllProjects(force, withCompile);
        } else {
            // 閸氬本顒炶ぐ鎾冲妞ゅ湱娲?
            syncCurrentProject(force);

            // 婵″倹鐏夐幐鍥х暰娴?--compile閿涘苯鎮撳銉ユ倵閼奉亜濮╃紓鏍槯
            if (withCompile) {
                System.out.println();
                compileProject(new String[]{QinConstants.ARG_NO_SYNC});
            }
        }
    }

    /**
     * 閸氬本顒炶ぐ鎾冲妞ゅ湱娲伴惃鍕贩鐠?
     */
    private static void syncCurrentProject(boolean force) throws Exception {
        String cwd = QinConstants.getCwd();

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();
        ensureLocalDependenciesReady(config);

        if (!force && CacheValidator.isCacheValid(cwd)) {
            System.out.println(blue("-> Using cached dependencies (" + QinConstants.CLASSPATH_CACHE_PATH + ")"));
            System.out.println(green("[OK] Dependencies up to date (use --force to re-sync)"));
            return;
        }

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
     * 閸氬本顒為幍鈧張澶婄摍妞ゅ湱娲伴敍鍦norepo 閸︾儤娅欓敍?
     * 閸氭垳绗傞弻銉﹀閺嶅湱娲拌ぐ鏇礉闁帒缍婇幍顐ｅ伎閹碘偓閺?qin.config.json 妞ゅ湱娲?
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

        // 缂佺喕顓?
        int synced = 0;
        int skipped = 0;
        int failed = 0;

        // 闁秴宸婚幍鈧張澶愩€嶉惄顕嗙礉瀵倹顒為幍褑顢?sync
        List<Thread> threads = new ArrayList<>();
        List<SyncResult> results = Collections.synchronizedList(new ArrayList<>());

        for (Path projectPath : projects) {
            Thread t = new Thread(() -> {
                try {
                    String projectName = projectPath.getFileName().toString();

                    // 婵″倹鐏夋稉宥嗘Ц瀵搫鍩楅崥灞绢劄閿涘本顥呴弻銉х处鐎?
                    if (!force && CacheValidator.isCacheValid(projectPath)) {
                        System.out.println(gray("  [SKIP] " + projectName + " (cached, skipped)"));
                        results.add(new SyncResult(projectName, SyncStatus.SKIPPED));
                        return;
                    }

                    // 閹笛嗩攽 qin sync --compile閿涘牆鎮撳?+ 缂傛牞鐦ч敍?
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

                    // 鐠囪褰囨潏鎾冲毉閿涘牓娼ゆ妯侯槱閻炲棴绱?
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                        while (reader.readLine() != null) {
                            // 闂堟瑩绮☉鍫ｅ瀭鏉堟挸鍤?
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

        // 缁涘绶熼幍鈧張澶屽殠缁嬪鐣幋?
        for (Thread t : threads) {
            t.join();
        }

        // 缂佺喕顓哥紒鎾寸亯
        for (SyncResult r : results) {
            switch (r.status) {
                case SUCCESS -> synced++;
                case SKIPPED -> skipped++;
                case FAILED -> failed++;
            }
        }

        // 鏉堟挸鍤Ч鍥ㄢ偓?
        System.out.println();
        if (failed > 0) {
            System.out.println(yellow("[WARN] Sync completed: " + synced + " synced, " + skipped + " skipped, " + failed + " failed"));
        } else {
            System.out.println(green("[OK] All projects synced: " + synced + " synced, " + skipped + " skipped"));
        }
    }

    /**
     * 閸氬本顒炵紒鎾寸亯閻樿埖鈧?
     */
    private enum SyncStatus {
        SUCCESS, SKIPPED, FAILED
    }

    /**
     * 閸氬本顒炵紒鎾寸亯
     */
    private record SyncResult(String projectName, SyncStatus status) {}

    /**
     * 閸氬本顒炴笟婵婄閻ㄥ嫭鐗宠箛鍐偓鏄忕帆閿涘矁绻戦崶鐐垫晸閹存劗娈?classpath
     * 娴ｈ法鏁ゆ晶鐐插繁閻楀牐袙閺嬫劕娅掗敍灞炬暜閹镐浇鍤滈崝銊х椽鐠囨垶婀伴崷浼淬€嶉惄?
     */
    private static String syncDependenciesCore(QinConfig config) throws Exception {
        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        System.out.println(blue("-> Syncing dependencies..."));
        String sep = QinConstants.getClasspathSeparator();
        List<String> classpaths = new ArrayList<>();

        // 1. 娴ｈ法鏁ゆ晶鐐插繁閻楀牐袙閺嬫劕娅掗敍鍫熸暜閹镐浇鍤滈崝銊х椽鐠囨垶婀伴崷浼淬€嶉惄顕嗙礆
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

        // 2. 閸欘亝婀佹潻婊呪柤娓氭繆绂嗛幍宥堢殶閻?Coursier
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
        Path cacheDir = QinConstants.getProjectQinDir(QinConstants.getCwd());
        Files.createDirectories(cacheDir);

        String classpath = String.join(sep, classpaths);

        // 閹稿鍘ょ純顔昏厬閻ㄥ嫪绶风挧鏍€庢惔蹇斿笓鎼?classpath
        classpath = sortClasspathByConfigOrder(classpath, deps);

        String json = buildClasspathJson(classpath);
        Files.writeString(QinConstants.getProjectClasspathCache(QinConstants.getCwd()), json);

        // 閻㈢喐鍨?IDEA 鎼存捇鍘ょ純顔芥瀮娴犺绱?idea/libraries/*.xml閿?
        if (!classpath.isEmpty()) {
            try {
                System.out.println(blue("-> Generating IDEA library configs..."));
                IdeaLibraryGenerator ideaGen = new IdeaLibraryGenerator(QinConstants.getCwd());
                ideaGen.cleanLibraryConfigs(); // 濞撳懐鎮婇弮褔鍘ょ純?
                int libCount = ideaGen.generateLibraryConfigs(classpath);
                System.out.println(green("  [OK] Generated " + libCount + " library configs in .idea/libraries/"));
            } catch (IOException e) {
                System.err.println(yellow("  Warning: Failed to generate IDEA configs: " + e.getMessage()));
            }
        }

        System.out.println(green("[OK] Dependencies synced (" + localCount + " local, " + remoteCount + " remote)"));
        System.out.println(gray("  Cache: " + QinConstants.CLASSPATH_CACHE_PATH));

        return classpath;
    }

    /**
     * 绾喕绻氭笟婵婄瀹告彃鎮撳銉礉婵″倹鐏夌紓鎾崇摠閺堝鏅ラ崚娆庡▏閻劎绱︾€涙﹫绱濋崥锕€鍨幍褑顢戦崥灞绢劄
     * 
     * @return classpath 鐎涙顑佹稉?
     */
    private static String ensureDependenciesSynced(QinConfig config) throws Exception {
        String cwd = QinConstants.getCwd();
        ensureLocalDependenciesReady(config);

        if (CacheValidator.isCacheValid(cwd)) {
            String classpath = CacheValidator.getCachedClasspath(cwd);
            if (classpath != null) {
                System.out.println(
                        blue("-> Using cached dependencies (" + QinConstants.CLASSPATH_CACHE_PATH + ")"));
                return classpath;
            }
        }

        return syncDependenciesCore(config);
    }

    private static Map<String, String> collectAllDependencies(QinConfig config) {
        Map<String, String> deps = new LinkedHashMap<>();
        if (config.dependencies() != null) {
            deps.putAll(config.dependencies());
        }
        if (config.devDependencies() != null) {
            deps.putAll(config.devDependencies());
        }
        return deps;
    }

    private static void ensureLocalDependenciesReady(QinConfig config) {
        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            return;
        }

        LocalProjectResolverEnhanced resolver = new LocalProjectResolverEnhanced(QinConstants.getCwd());
        resolver.resolveDependencies(deps);
    }

    /**
     * 妤犲矁鐦?classpath 娑擃厾娈戦幍鈧張澶嬫瀮娴犺埖妲搁崥锕€鐡ㄩ崷?
     * @deprecated 娴ｈ法鏁?CacheValidator.validateClasspathFiles() 娴狅絾娴?
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
     * qin jar - 閹垫挸瀵橀弲顕€鈧?JAR閿涘牅绗夐崥顐＄贩鐠ф牭绱?
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
     * qin fatjar - 閹垫挸瀵?Fat JAR閿涘牆瀵橀崥顐ｅ閺堝绶风挧鏍电礆
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
     * qin deps - 閺勫墽銇氭笟婵婄閺?
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

    // ==================== BSP 閻╃鍙ч崨鎴掓姢 ====================

    /**
     * 閸氼垰濮?BSP Server
     * IDE 娴兼俺鐨熼悽銊︻劃閸涙垝鎶ゆ稉搴㈢€鍝勪紣閸忕兘鈧矮淇?
     */
    private static void startBspServer() throws Exception {
        com.qin.bsp.QinBspServer server = new com.qin.bsp.QinBspServer(QinConstants.getCwd());
        server.start();
    }

    /**
     * 閸掓繂顫愰崠?BSP 闁板秶鐤?
     * 閻㈢喐鍨?.bsp/qin.json 閺傚洣娆㈤敍宀冾唨 IDE 閼宠棄褰傞悳鐗堫劃閺嬪嫬缂撳銉ュ徔
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
     * 閺嶇厧绱￠崠鏍ㄦ瀮娴犺泛銇囩亸蹇ョ礄鏉堝懎濮弬瑙勭《閿?
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
                  run         Compile and run the Java program (or target)
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
                  qin run src/app/Main.java   # Run a specific Java file
                  qin run com.app.Main        # Run by fully-qualified class name
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
     * 娴?.qin/classpath.json 鐟欙絾鐎?classpath
     */
    private static String parseClasspathFromJson(String json) {
        try {
            // 缁犫偓閸楁洝袙閺?JSON 閺佹壆绮?
            int start = json.indexOf("[");
            int end = json.lastIndexOf("]");
            if (start < 0 || end < 0)
                return "";

            String arrayContent = json.substring(start + 1, end);
            List<String> paths = new ArrayList<>();

            // 鐟欙絾鐎藉В蹇庨嚋鐠侯垰绶?
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
     * 閺嬪嫬缂?.qin/classpath.json 閺嶇厧绱?
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
     * 閹稿鍘ょ純顔昏厬閻ㄥ嫪绶风挧鏍€庢惔蹇斿笓鎼?classpath
     * 
     * @param classpath 閸樼喎顫?classpath閿涘牆鍨庨崣?閸愭帒褰块崚鍡涙閿?
     * @param deps      闁板秶鐤嗘稉顓犳畱娓氭繆绂嗛敍鍫滅箽閹镐焦褰冮崗銉┿€庢惔蹇ョ礆
     * @return 閹烘帒绨崥搴ｆ畱 classpath
     */
    private static String sortClasspathByConfigOrder(String classpath, Map<String, String> deps) {
        if (classpath == null || classpath.isEmpty() || deps == null || deps.isEmpty()) {
            return classpath;
        }

        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep.equals(";") ? ";" : ":");

        // 閸掓稑缂?artifactId 閸掍即銆庢惔蹇曟畱閺勭姴鐨?
        Map<String, Integer> orderMap = new LinkedHashMap<>();
        int order = 0;
        for (String depKey : deps.keySet()) {
            // depKey 閺嶇厧绱? groupId@artifactId 閹?groupId:artifactId
            String artifactId = extractArtifactId(depKey);
            orderMap.put(artifactId.toLowerCase(), order++);
        }

        // 閹稿鍘ょ純顕€銆庢惔蹇斿笓鎼?
        List<String> sortedPaths = new ArrayList<>(Arrays.asList(paths));
        sortedPaths.sort((a, b) -> {
            String artifactA = extractArtifactIdFromPath(a).toLowerCase();
            String artifactB = extractArtifactIdFromPath(b).toLowerCase();

            int orderA = orderMap.getOrDefault(artifactA, Integer.MAX_VALUE);
            int orderB = orderMap.getOrDefault(artifactB, Integer.MAX_VALUE);

            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }
            // 婵″倹鐏夐柈鎴掔瑝閸︺劑鍘ょ純顔昏厬閿涘本瀵滅€涙鐦濇い鍝勭碍
            return a.compareToIgnoreCase(b);
        });

        return String.join(sep, sortedPaths);
    }

    /**
     * 娴犲簼绶风挧?key 娑擃厽褰侀崣?artifactId
     */
    private static String extractArtifactId(String depKey) {
        // 閺嶇厧绱? groupId@artifactId 閹?groupId:artifactId
        int sepIndex = depKey.lastIndexOf('@');
        if (sepIndex < 0)
            sepIndex = depKey.lastIndexOf(':');
        return sepIndex >= 0 ? depKey.substring(sepIndex + 1) : depKey;
    }

    /**
     * 娴?jar 鐠侯垰绶炴稉顓熷絹閸?artifactId
     */
    private static String extractArtifactIdFromPath(String path) {
        // 鐠侯垰绶為弽鐓庣础: .../groupId/artifactId/version/artifactId-version.jar
        // 閹? .../build/classes
        if (path.contains("build") || path.contains("classes")) {
            // 閺堫剙婀存い鍦窗閿涘奔濞囬悽銊ф窗瑜版洖鎮?
            Path p = Paths.get(path);
            if (p.getParent() != null && p.getParent().getParent() != null) {
                return p.getParent().getParent().getFileName().toString();
            }
            return p.getFileName().toString();
        }

        // Maven jar 鐠侯垰绶?
        String fileName = Paths.get(path).getFileName().toString();
        // 缁夊娅?.jar 閸滃瞼澧楅張顒€褰?
        if (fileName.endsWith(".jar")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        // 鐏忔繆鐦幓鎰絿 artifactId閿涘牆婀張鈧崥搴濈娑?- 娑斿澧犻敍灞筋洤閺嬫粌鎮楅棃銏℃Ц閻楀牊婀伴崣鍑ょ礆
        int lastDash = fileName.lastIndexOf('-');
        if (lastDash > 0) {
            String suffix = fileName.substring(lastDash + 1);
            // 濡偓閺屻儲妲搁崥锔芥Ц閻楀牊婀伴崣鍑ょ礄娴犮儲鏆熺€涙绱戞径杈剧礆
            if (!suffix.isEmpty() && Character.isDigit(suffix.charAt(0))) {
                return fileName.substring(0, lastDash);
            }
        }
        return fileName;
    }
}
