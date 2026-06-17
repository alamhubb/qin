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
import com.qin.npm.NpmPackageManager;
import com.qin.utils.QinUtils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Qin CLI - Qin native build tool
 * A modern Java build tool with zero XML configuration
 */
public class QinCli {
    private static final String VERSION = "0.1.0";
    private static final EnvironmentChecker envChecker = new EnvironmentChecker();
    private static final List<String> QIN_SCRIPT_EXTENSIONS = List.of(".qin", ".js", ".mjs", ".ts");

    public static void main(String[] args) {
        ParentProcessWatchdog.install();
        if (args.length == 0) {
            printHelp();
            return;
        }

        CliInvocation invocation = parseInvocation(args);
        String command = invocation.command;
        String[] cmdArgs = invocation.args;

        try {
            if (invocation.root != null) {
                System.setProperty("user.dir", invocation.root.toString());
            }
            switch (command) {
                case "init" -> initProject();
                case "run" -> runProject(cmdArgs);
                case "compile" -> compileProject(cmdArgs);
                case "test" -> runTests(cmdArgs);
                case "jar" -> jarProject(cmdArgs);           // 濡絽鍟弲?闂佸搫鍊瑰姗€路?
                case "fatjar" -> fatjarProject(cmdArgs);     // 濡絽鍟弲?闂佸搫鍊瑰姗€路?
                case "build" -> buildProject(cmdArgs);
                case "clean" -> cleanProject();
                case "install" -> QinInstallCommand.execute(cmdArgs);
                case "sync" -> syncDependencies(cmdArgs);
                case "deps" -> showDependencies(cmdArgs);    // 濡絽鍟弲?闂佸搫鍊瑰姗€路?
                case "dev" -> devMode(cmdArgs);
                case "dist" -> distProject();
                case "conformance" -> runConformance(cmdArgs);
                case "bsp" -> startBspServer();              // 濡絽鍟弲?BSP Server
                case "bsp-init" -> initBspConfig();          // 濡絽鍟弲?闂佹眹鍨婚崰鎰板垂?BSP 闂備焦婢樼粔鍫曟偪?
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

        // Create qin.config.js
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

    private static CliInvocation parseInvocation(String[] args) {
        String command = args[0];
        List<String> commandArgs = new ArrayList<>();
        Path root = null;

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if ("--root".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --root");
                }
                root = resolveCliRoot(args[++i]);
                continue;
            }
            commandArgs.add(arg);
        }

        return new CliInvocation(command, commandArgs.toArray(String[]::new), root);
    }

    private static Path resolveCliRoot(String rawRoot) {
        if (rawRoot == null || rawRoot.isBlank()) {
            throw new IllegalArgumentException("--root must not be empty");
        }

        Path root = Paths.get(rawRoot);
        if (!root.isAbsolute()) {
            root = Paths.get(QinConstants.getCwd()).resolve(root);
        }
        root = root.toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("--root does not exist or is not a directory: " + root);
        }
        return root;
    }

    private record CliInvocation(String command, String[] args, Path root) {}

    private static void runProject(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        if (args.length > 0 && !args[0].startsWith("-")) {
            String target = args[0];
            Path targetPath = Paths.get(target);
            if (!targetPath.isAbsolute()) {
                targetPath = Paths.get(QinConstants.getCwd()).resolve(target);
            }

            RunnerPlugin plugin = PluginRegistry.getInstance().getPlugin(targetPath);
            if (plugin != null) {
                System.out.println(blue("-> Running with " + plugin.name() + " plugin..."));
                String[] runArgs = Arrays.copyOfRange(args, 1, args.length);
                plugin.run(targetPath, runArgs, Paths.get(QinConstants.getCwd()));
                System.out.println(green("[OK] Done!"));
                return;
            }

            String qinFile = resolveRunTargetToQinFile(config, target);
            if (qinFile != null) {
                String[] qinArgs = Arrays.copyOfRange(args, 1, args.length);
                runQinRuntime(config, qinFile, false, qinArgs);
                return;
            }
        } else {
            String qinEntry = resolveDefaultQinEntry(config);
            if (qinEntry != null) {
                runQinRuntime(config, qinEntry, false, args);
                return;
            }
        }

        runJavaProject(config, args);
    }

    private static void runConformance(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJava()) {
            System.err.println(red("Error: java is not installed."));
            System.out.println(envChecker.getInstallGuide("java"));
            System.exit(1);
        }

        String conformanceMainClass = "com.qin.conformance.QinConformanceMain";
        String dependencyClasspath = ensureDependenciesSynced(config);
        String compileOutputDir = Paths.get(QinConstants.getCwd(), JavaCompileConfig.from(config).outputDir()).toString();
        String separator = QinConstants.getClasspathSeparator();
        String runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                ? compileOutputDir
                : compileOutputDir + separator + dependencyClasspath;
        runtimeClasspath = appendBundledQinRuntimeClasspath(runtimeClasspath);
        runtimeClasspath = appendBundledQinRuntimeClasspath(runtimeClasspath);

        if (!isClassAvailableOnClasspath(runtimeClasspath, conformanceMainClass)) {
            System.out.println(yellow("-> Conformance class missing in cached classpath, forcing dependency resync..."));
            dependencyClasspath = syncDependenciesCore(config);
            runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                    ? compileOutputDir
                    : compileOutputDir + separator + dependencyClasspath;
        }

        if (!isClassAvailableOnClasspath(runtimeClasspath, conformanceMainClass)) {
            throw new IllegalStateException("""
                    Conformance command requires com.qin:qin-conformance.
                    Add it to qin.config.js dependencies, then run `qin sync`.
                    """.trim());
        }

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(runtimeClasspath);
        command.add(conformanceMainClass);

        if (!hasRootOption(args)) {
            command.add("--root");
            command.add(Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize().toString());
        }
        command.addAll(Arrays.asList(args));

        System.out.println(blue("-> Running Qin conformance (Qin vs Chrome)..."));
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(Paths.get(QinConstants.getCwd()).toFile());
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.exit(exitCode);
        }
        System.out.println(green("[OK] Conformance completed"));
    }

    private static void runJavaProject(String[] args) throws Exception {
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();
        runJavaProject(config, args);
    }

    private static void runJavaProject(QinConfig config, String[] args) throws Exception {
        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.out.println(envChecker.getInstallGuide("javac"));
            System.exit(1);
        }

        // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晜閽樺妯勯柣搴ゎ潐閻喚鑺?.java 闂佸搫鍊稿ú锝呪枎?
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
        } else if (runOptions.mainClass != null) {
            runner.compileAndRunMainClass(runOptions.mainClass, runOptions.programArgs, runOptions.jvmArgs);
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

            if (!arg.startsWith("-") && options.javaFile == null && options.mainClass == null) {
                options.javaFile = resolveRunTargetToJavaFile(config, arg);
                if (options.javaFile == null) {
                    options.mainClass = resolveRunTargetToMainClass(arg);
                }
                if (options.javaFile == null && options.mainClass == null) {
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
            if (arg.startsWith("--port=")) {
                options.portOverride = Integer.parseInt(arg.substring("--port=".length()).trim());
                continue;
            }
            if ("--port".equals(arg)) {
                options.portOverride = Integer.parseInt(nextArg(args, ++i, "--port").trim());
                continue;
            }

            options.programArgs.add(arg);
        }

        if (options.debug) {
            options.jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:" + options.debugPort);
        }

        if (options.portOverride != null) {
            options.jvmArgs.add("-Dserver.port=" + options.portOverride);
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

    private static String resolveRunTargetToQinFile(QinConfig config, String runTarget) {
        if (runTarget == null || runTarget.isBlank()) {
            return null;
        }

        String normalizedTarget = runTarget.trim();
        if (hasQinBackendExtension(normalizedTarget)) {
            String pathTarget = normalizeRelativePath(normalizedTarget);
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(pathTarget).normalize();
            return Files.exists(absolute) ? pathTarget : null;
        }

        if (normalizedTarget.contains("/") || normalizedTarget.contains("\\")) {
            String pathTarget = normalizeRelativePath(normalizedTarget);
            if (hasQinBackendExtension(pathTarget)) {
                Path absolute = Paths.get(QinConstants.getCwd()).resolve(pathTarget).normalize();
                return Files.exists(absolute) ? pathTarget : null;
            }
            for (String ext : QIN_SCRIPT_EXTENSIONS) {
                String candidate = pathTarget + ext;
                Path absolute = Paths.get(QinConstants.getCwd()).resolve(candidate).normalize();
                if (Files.exists(absolute)) {
                    return normalizeRelativePath(candidate);
                }
            }
            return null;
        }

        LinkedHashSet<Path> candidates = new LinkedHashSet<>();
        if (config.entry() != null && hasQinScriptExtension(config.entry())) {
            Path entryPath = Paths.get(config.entry().replace("\\", "/"));
            Path parent = entryPath.getParent();
            if (parent != null) {
                for (String ext : QIN_SCRIPT_EXTENSIONS) {
                    candidates.add(parent.resolve(normalizedTarget + ext));
                }
            }
        }
        addScriptCandidates(candidates, Paths.get(QinConstants.MAIN_SOURCE_DIR), normalizedTarget);
        addScriptCandidates(candidates, Paths.get(QinConstants.APP_DIR), normalizedTarget);
        addScriptCandidates(candidates, Paths.get(QinConstants.SHARED_DIR), normalizedTarget);
        addScriptCandidates(candidates, Paths.get("src"), normalizedTarget);
        addScriptCandidates(candidates, Paths.get(""), normalizedTarget);

        for (Path candidate : candidates) {
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(candidate).normalize();
            if (Files.exists(absolute)) {
                return normalizeRelativePath(candidate.toString());
            }
        }

        return null;
    }

    private static String resolveDefaultQinEntry(QinConfig config) {
        if (config.backend() != null && config.backend().entry() != null && !config.backend().entry().isBlank()) {
            String entry = normalizeRelativePath(config.backend().entry());
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(entry).normalize();
            return Files.exists(absolute) ? entry : null;
        }

        if (config.entry() != null && !config.entry().isBlank()) {
            if (hasQinBackendExtension(config.entry())) {
                String entry = normalizeRelativePath(config.entry());
                Path absolute = Paths.get(QinConstants.getCwd()).resolve(entry).normalize();
                return Files.exists(absolute) ? entry : null;
            }
        }

        for (String candidate : QinConstants.DEFAULT_QIN_ENTRY_CANDIDATES) {
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(candidate).normalize();
            if (Files.exists(absolute)) {
                return normalizeRelativePath(candidate);
            }
        }
        return null;
    }

    private static boolean hasQinScriptExtension(String path) {
        String lower = path.toLowerCase(Locale.ROOT);
        for (String ext : QIN_SCRIPT_EXTENSIONS) {
            if (lower.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasQinBackendExtension(String path) {
        return hasQinScriptExtension(path) || path.toLowerCase(Locale.ROOT).endsWith(".java");
    }

    private static void addScriptCandidates(Set<Path> out, Path baseDir, String baseName) {
        for (String ext : QIN_SCRIPT_EXTENSIONS) {
            if (baseDir.toString().isBlank()) {
                out.add(Paths.get(baseName + ext));
            } else {
                out.add(baseDir.resolve(baseName + ext));
            }
        }
    }

    private static void runQinProject(QinConfig config, String qinFile) throws Exception {
        runQinRuntime(config, qinFile, false, new String[0]);
    }

    private static void runQinDevProject(QinConfig config, String qinFile) throws Exception {
        runQinRuntime(config, qinFile, true, new String[0]);
    }

    private static void runQinRuntime(QinConfig config, String qinFile, boolean devMode, String[] args) throws Exception {
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJava()) {
            System.err.println(red("Error: java is not installed."));
            System.out.println(envChecker.getInstallGuide("java"));
            System.exit(1);
        }

        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            throw new IllegalStateException(
                    "Qin runtime dependencies are missing. Add `com.qin:qin-runtime-core` to qin.config.js dependencies.");
        }

        String dependencyClasspath = ensureDependenciesSynced(config);
        String compileOutputDir = Paths.get(QinConstants.getCwd(), JavaCompileConfig.from(config).outputDir()).toString();
        String separator = QinConstants.getClasspathSeparator();
        String runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                ? compileOutputDir
                : compileOutputDir + separator + dependencyClasspath;
        runtimeClasspath = appendBundledQinRuntimeClasspath(runtimeClasspath);

        int port = resolveQinRuntimePort(config, args);
        Path root = Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize();
        Path backendSource = firstNonNullPath(
                resolvePathArg(args, "--backend-file", root),
                resolveQinBackendEntry(config, root, qinFile));
        Path frontendRoot = resolveQinFrontendRoot(config, root);
        Path frontendEntry = firstNonNullPath(
                resolvePathArg(args, "--frontend-file", root),
                resolveQinFrontendEntry(config, root, frontendRoot));
        Path frontendStaticDir = firstNonNullPath(
                resolvePathArg(args, "--static-dir", root),
                resolveQinFrontendStaticDir(config, root, frontendRoot));
        String runtimeMainClass = resolveQinRuntimeMainClass(runtimeClasspath, devMode);

        System.out.println(blue(devMode ? "-> Starting Qin dev runtime..." : "-> Running Qin runtime..."));
        System.out.println(gray("  Backend entry: " + formatRelativeOrGenerated(root, backendSource)));
        if (frontendRoot != null) {
            System.out.println(gray("  Frontend root: " + root.relativize(frontendRoot)));
        }
        if (frontendEntry != null) {
            System.out.println(gray("  Frontend entry: " + root.relativize(frontendEntry)));
        }

        List<String> command = new ArrayList<>();
        command.add("java");
        command.addAll(resolveJvmArgs(args));
        command.add("-cp");
        command.add(runtimeClasspath);
        command.add(runtimeMainClass);
        if (devMode) {
            command.add("--dev");
        }
        command.add("--root");
        command.add(root.toString());
        if (backendSource != null) {
            command.add("--backend-file");
            command.add(backendSource.toString());
        }
        command.add("--port");
        command.add(String.valueOf(port));
        if (hasArg(args, "--build-only")) {
            command.add("--build-only");
        }
        if (hasArg(args, "--print-ir")) {
            command.add("--print-ir");
        }
        if (frontendStaticDir != null) {
            command.add("--static-dir");
            command.add(frontendStaticDir.toString());
        }
        if (frontendEntry != null) {
            command.add("--frontend-file");
            command.add(frontendEntry.toString());
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        Process process = pb.start();
        int exitCode = ChildProcessSupport.waitFor(
                process,
                runtimeMainClass,
                () -> {});
        if (exitCode != 0) {
            throw new RuntimeException("Qin runtime exited with code " + exitCode + " (main class: " + runtimeMainClass + ")");
        }

        System.out.println(green(devMode ? "[OK] Qin dev runtime stopped" : "[OK] Done!"));
    }

    private static List<String> resolveJvmArgs(String[] args) {
        List<String> jvmArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--jvm-args=")) {
                jvmArgs.addAll(tokenizeArguments(arg.substring("--jvm-args=".length())));
                continue;
            }
            if ("--jvm-args".equals(arg)) {
                jvmArgs.addAll(tokenizeArguments(nextArg(args, ++i, "--jvm-args")));
            }
        }
        return jvmArgs;
    }

    private static int resolveQinRuntimePort(QinConfig config, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--port".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --port");
                }
                return Integer.parseInt(args[++i]);
            }
        }
        return config.port() != null && config.port() > 0 ? config.port() : QinConstants.DEFAULT_PORT;
    }

    private static Path resolvePathArg(String[] args, String flag, Path root) {
        for (int i = 0; i < args.length; i++) {
            if (flag.equals(args[i])) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + flag);
                }
                Path raw = Paths.get(args[++i]);
                return raw.isAbsolute() ? raw.normalize() : root.resolve(raw).normalize();
            }
        }
        return null;
    }

    private static Path firstNonNullPath(Path first, Path second) {
        return first != null ? first : second;
    }

    private static boolean hasArg(String[] args, String flag) {
        for (String arg : args) {
            if (flag.equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private static String resolveQinRuntimeMainClass(String runtimeClasspath, boolean devMode) {
        if (!devMode) {
            return QinConstants.FULLSTACK_MAIN_CLASS;
        }

        if (isClassAvailableOnClasspath(runtimeClasspath, QinConstants.DEV_SERVER_MAIN_CLASS)) {
            return QinConstants.DEV_SERVER_MAIN_CLASS;
        }

        if (isClassAvailableOnClasspath(runtimeClasspath, QinConstants.JITE_DEV_MAIN_CLASS)) {
            return QinConstants.JITE_DEV_MAIN_CLASS;
        }

        System.out.println(yellow("-> qin-plugin-jite not found in classpath, fallback to built-in dev runtime."));
        return QinConstants.FULLSTACK_MAIN_CLASS;
    }

    private static String appendBundledQinRuntimeClasspath(String runtimeClasspath) {
        Path qinHome = locateQinHomeFromCli();
        if (qinHome == null) {
            return runtimeClasspath;
        }

        List<String> entries = new ArrayList<>();
        addClasspathEntry(entries, qinHome.resolve("build").resolve("classes"));
        addCachedClasspathEntries(entries, qinHome.resolve(QinConstants.CLASSPATH_CACHE_PATH));
        Path packagesDir = qinHome.resolve("packages");
        if (Files.isDirectory(packagesDir)) {
            try (var paths = Files.list(packagesDir)) {
                paths
                        .map(path -> path.resolve("build").resolve("classes"))
                        .filter(Files::isDirectory)
                        .forEach(path -> addClasspathEntry(entries, path));
            } catch (IOException ignored) {
                // Fall back to whatever classpath the target project already resolved.
            }
        }
        addSiblingWorkspaceRuntimeClasspath(entries, qinHome.getParent());

        if (entries.isEmpty()) {
            return runtimeClasspath;
        }
        String separator = QinConstants.getClasspathSeparator();
        if (runtimeClasspath == null || runtimeClasspath.isBlank()) {
            return String.join(separator, entries);
        }
        return runtimeClasspath + separator + String.join(separator, entries);
    }

    private static Path locateQinHomeFromCli() {
        try {
            Path codeSource = Paths.get(QinCli.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI())
                    .toAbsolutePath()
                    .normalize();
            Path current = Files.isRegularFile(codeSource) ? codeSource.getParent() : codeSource;
            while (current != null) {
                if (Files.isDirectory(current.resolve("src").resolve("com").resolve("qin"))
                        && Files.isDirectory(current.resolve("packages").resolve("qin-runtime-core"))) {
                    return current;
                }
                current = current.getParent();
            }
        } catch (Exception ignored) {
            // Use the resolved project classpath only if the CLI home cannot be inferred.
        }
        return null;
    }

    private static void addClasspathEntry(List<String> entries, Path path) {
        if (Files.isDirectory(path)) {
            String value = path.toAbsolutePath().normalize().toString();
            if (!entries.contains(value)) {
                entries.add(value);
            }
        }
    }

    private static void addCachedClasspathEntries(List<String> entries, Path classpathCache) {
        if (!Files.isRegularFile(classpathCache)) {
            return;
        }
        try {
            String cachedClasspath = CacheValidator.getCachedClasspath(classpathCache.getParent().getParent().toString());
            if (cachedClasspath == null || cachedClasspath.isBlank()) {
                return;
            }
            String separator = QinConstants.getClasspathSeparator();
            for (String rawEntry : cachedClasspath.split(Pattern.quote(separator))) {
                if (rawEntry == null || rawEntry.isBlank()) {
                    continue;
                }
                Path path = Paths.get(rawEntry.trim());
                if (Files.exists(path)) {
                    String value = path.toAbsolutePath().normalize().toString();
                    if (!entries.contains(value)) {
                        entries.add(value);
                    }
                }
            }
        } catch (Exception ignored) {
            // A stale cache should not prevent the target project from resolving its own classpath.
        }
    }

    private static void addSiblingWorkspaceRuntimeClasspath(List<String> entries, Path workspaceRoot) {
        if (workspaceRoot == null || !Files.isDirectory(workspaceRoot)) {
            return;
        }
        List<Path> packageRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime"),
                workspaceRoot.resolve("subhuti"),
                workspaceRoot.resolve("cssts"),
                workspaceRoot.resolve("ovsjs"));
        for (Path packageRoot : packageRoots) {
            if (!Files.isDirectory(packageRoot)) {
                continue;
            }
            try (var paths = Files.walk(packageRoot, 4)) {
                paths
                        .filter(path -> path.getFileName() != null
                                && "classes".equals(path.getFileName().toString()))
                        .filter(path -> path.getParent() != null
                                && path.getParent().getFileName() != null
                                && "build".equals(path.getParent().getFileName().toString()))
                        .filter(Files::isDirectory)
                        .forEach(path -> addClasspathEntry(entries, path));
            } catch (IOException ignored) {
                // Missing sibling projects are fine; qin.config.js dependencies may still resolve them remotely.
            }
        }
    }

    private static boolean isClassAvailableOnClasspath(String classpath, String className) {
        if (classpath == null || classpath.isBlank() || className == null || className.isBlank()) {
            return false;
        }

        String classFile = className.replace('.', '/') + ".class";
        String separator = QinConstants.getClasspathSeparator();
        String[] entries = classpath.split(Pattern.quote(separator));
        for (String rawEntry : entries) {
            if (rawEntry == null || rawEntry.isBlank()) {
                continue;
            }

            Path entry = Paths.get(rawEntry.trim());
            if (Files.isDirectory(entry) && Files.exists(entry.resolve(classFile))) {
                return true;
            }

            if (Files.isRegularFile(entry) && entry.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")) {
                try (ZipFile zipFile = new ZipFile(entry.toFile())) {
                    if (zipFile.getEntry(classFile) != null) {
                        return true;
                    }
                } catch (IOException ignored) {
                    // Ignore broken classpath entry and keep probing.
                }
            }
        }
        return false;
    }

    private static boolean hasRootOption(String[] args) {
        for (String arg : args) {
            if ("--root".equals(arg) || (arg != null && arg.startsWith("--root="))) {
                return true;
            }
        }
        return false;
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
        private String mainClass;
        private final List<String> programArgs = new ArrayList<>();
        private final List<String> jvmArgs = new ArrayList<>();
        private boolean debug;
        private int debugPort = 5005;
        private Integer portOverride;
    }

    private static void buildProject(String[] args) throws Exception {
        boolean clean = Arrays.asList(args).contains("--clean");
        boolean skipTests = Arrays.asList(args).contains("--skip-tests") ||
                           Arrays.asList(args).contains("-DskipTests");

        if (clean) {
            cleanProject();
        }

        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        String qinBuildTarget = resolveBuildTargetToQinFile(config, args);
        if (qinBuildTarget != null) {
            buildQinProject(config, qinBuildTarget, args);
            return;
        }

        // Check environment
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJavac()) {
            System.err.println(red("Error: javac is not installed."));
            System.exit(1);
        }

        // 婵炶揪缍€濞夋洟寮?BuildLifecycle 闁哄鏅滅粙鏍€侀幋鐘亾閻熺増婀伴柡鍡秮瀵悂宕熼銈囧
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

    private static String resolveBuildTargetToQinFile(QinConfig config, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null || arg.isBlank() || arg.startsWith("-")) {
                if ("-o".equals(arg) || "--output".equals(arg)) {
                    i++;
                }
                continue;
            }

            String explicitTarget = resolveRunTargetToQinFile(config, arg);
            if (explicitTarget != null) {
                return explicitTarget;
            }

            return null;
        }

        return resolveDefaultQinEntry(config);
    }

    private static void buildQinProject(QinConfig config, String qinFile, String[] args) throws Exception {
        EnvironmentStatus envStatus = envChecker.checkAll();
        if (!envStatus.hasJava()) {
            System.err.println(red("Error: java is not installed."));
            System.out.println(envChecker.getInstallGuide("java"));
            System.exit(1);
        }

        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            throw new IllegalStateException(
                    "Qin runtime dependencies are missing. Add `com.qin:qin-runtime-core` to qin.config.js dependencies.");
        }

        String dependencyClasspath = ensureDependenciesSynced(config);
        String compileOutputDir = Paths.get(QinConstants.getCwd(), JavaCompileConfig.from(config).outputDir()).toString();
        String separator = QinConstants.getClasspathSeparator();
        String runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                ? compileOutputDir
                : compileOutputDir + separator + dependencyClasspath;

        Path root = Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize();
        Path backendSource = resolveQinBackendEntry(config, root, qinFile);
        if (backendSource == null) {
            throw new IllegalStateException("Qin backend entry is missing. Set backend.entry in qin.config.js or pass a backend file.");
        }
        Path frontendRoot = resolveQinFrontendRoot(config, root);
        Path frontendEntry = resolveQinFrontendEntry(config, root, frontendRoot);
        Path fullstackOutRoot = resolveQinBuildOutputRoot(config, args);
        Path classOutDir = fullstackOutRoot.resolve("server-classes").normalize();
        Path staticOutDir = fullstackOutRoot.resolve("web").normalize();

        System.out.println(blue("-> Building Qin fullstack artifacts..."));
        System.out.println(gray("  Backend entry: " + qinFile));
        if (frontendRoot != null) {
            System.out.println(gray("  Frontend root: " + root.relativize(frontendRoot)));
        }
        if (frontendEntry != null) {
            System.out.println(gray("  Frontend entry: " + root.relativize(frontendEntry)));
        }
        System.out.println(gray("  Output root: " + fullstackOutRoot));

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(runtimeClasspath);
        command.add(QinConstants.FULLSTACK_MAIN_CLASS);
        command.add("--build-only");
        command.add("--root");
        command.add(root.toString());
        command.add("--backend-file");
        command.add(backendSource.toString());
        command.add("--class-out");
        command.add(classOutDir.toString());
        command.add("--static-dir");
        command.add(staticOutDir.toString());
        if (frontendEntry != null) {
            command.add("--frontend-file");
            command.add(frontendEntry.toString());
        }
        if (Arrays.asList(args).contains("--print-ir")) {
            command.add("--print-ir");
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Qin fullstack build exited with code " + exitCode);
        }

        System.out.println(green("[OK] Qin build completed"));
        System.out.println(green("  Server classes: " + classOutDir));
        System.out.println(green("  Web assets: " + staticOutDir));
    }

    private static Path resolveQinBuildOutputRoot(QinConfig config, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-o".equals(arg) || "--output".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                String raw = args[++i];
                return Paths.get(raw).isAbsolute()
                        ? Paths.get(raw).normalize()
                        : Paths.get(QinConstants.getCwd()).resolve(raw).normalize();
            }
        }

        String distDir = QinConstants.getDistDir(config.output());
        return Paths.get(QinConstants.getCwd(), distDir, "fullstack").normalize();
    }

    private static Path resolveQinFrontendRoot(QinConfig config, Path projectRoot) {
        if (config.frontend() != null && config.frontend().srcDir() != null && !config.frontend().srcDir().isBlank()) {
            Path configured = projectRoot.resolve(config.frontend().srcDir()).normalize();
            if (Files.isDirectory(configured)) {
                return configured;
            }
        }

        if (config.client() != null && config.client().root() != null && !config.client().root().isBlank()) {
            Path configured = projectRoot.resolve(config.client().root()).normalize();
            if (Files.isDirectory(configured)) {
                return configured;
            }
        }

        Path appDir = projectRoot.resolve(QinConstants.APP_DIR).normalize();
        if (Files.isDirectory(appDir)) {
            return appDir;
        }

        return null;
    }

    private static Path resolveQinBackendEntry(QinConfig config, Path projectRoot, String qinFile) {
        if (qinFile != null && !qinFile.isBlank()) {
            Path raw = Paths.get(qinFile);
            return raw.isAbsolute() ? raw.normalize() : projectRoot.resolve(raw).normalize();
        }

        if (config.backend() != null && config.backend().entry() != null && !config.backend().entry().isBlank()) {
            Path configured = Paths.get(config.backend().entry());
            return configured.isAbsolute() ? configured.normalize() : projectRoot.resolve(configured).normalize();
        }

        if (config.entry() != null && hasQinBackendExtension(config.entry())) {
            Path configured = Paths.get(config.entry());
            return configured.isAbsolute() ? configured.normalize() : projectRoot.resolve(configured).normalize();
        }

        return null;
    }

    private static Path resolveQinFrontendStaticDir(QinConfig config, Path projectRoot, Path frontendRoot) {
        if (config.frontend() != null
                && config.frontend().staticDir() != null
                && !config.frontend().staticDir().isBlank()) {
            Path configured = Paths.get(config.frontend().staticDir());
            return configured.isAbsolute() ? configured.normalize() : projectRoot.resolve(configured).normalize();
        }
        if (Files.isRegularFile(projectRoot.resolve("index.html")) || Files.isRegularFile(projectRoot.resolve("index"))) {
            return projectRoot;
        }
        return frontendRoot;
    }

    private static String formatRelativeOrGenerated(Path root, Path source) {
        if (source == null) {
            return "<generated frontend-only backend>";
        }
        try {
            return root.relativize(source).toString();
        } catch (IllegalArgumentException ignored) {
            return source.toString();
        }
    }

    private static String resolveRunTargetToMainClass(String runTarget) {
        if (runTarget == null || runTarget.isBlank()) {
            return null;
        }

        String normalizedTarget = runTarget.trim();
        if (normalizedTarget.endsWith(".java")
                || normalizedTarget.endsWith(".qin")
                || normalizedTarget.endsWith(".js")
                || normalizedTarget.endsWith(".mjs")
                || normalizedTarget.endsWith(".ts")) {
            return null;
        }

        if (normalizedTarget.contains("/") || normalizedTarget.contains("\\")) {
            return null;
        }

        return normalizedTarget.contains(".") ? normalizedTarget : null;
    }

    private static Path resolveQinFrontendEntry(QinConfig config, Path projectRoot, Path frontendRoot) {
        if (frontendRoot == null) {
            return null;
        }

        LinkedHashSet<Path> candidates = new LinkedHashSet<>();
        if (config.frontend() != null
                && config.frontend().entry() != null
                && !config.frontend().entry().isBlank()) {
            Path configured = Paths.get(config.frontend().entry());
            if (configured.isAbsolute()) {
                candidates.add(configured.normalize());
            } else {
                candidates.add(projectRoot.resolve(configured).normalize());
                candidates.add(frontendRoot.resolve(configured).normalize());
            }
        }
        addExistingFrontendEntryCandidates(candidates, frontendRoot);

        if (frontendRoot.equals(projectRoot.resolve(QinConstants.APP_DIR).normalize())) {
            addExistingFrontendEntryCandidates(candidates, projectRoot.resolve(QinConstants.SHARED_DIR).normalize());
        }

        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        return null;
    }

    private static void addExistingFrontendEntryCandidates(Set<Path> out, Path baseDir) {
        if (baseDir == null) {
            return;
        }

        for (String fileName : List.of("main.js", "Main.js", "main.mjs", "Main.mjs", "main.ts", "Main.ts")) {
            out.add(baseDir.resolve(fileName).normalize());
        }
    }

    private static void devMode(String[] args) throws Exception {
        System.out.println(blue("-> Loading configuration..."));
        ConfigLoader configLoader = new ConfigLoader();
        QinConfig config = configLoader.load();

        String qinDevEntry = null;
        boolean frontendOnlyOverride = hasArg(args, "--frontend-file");
        boolean hasPositionalTarget = args.length > 0 && !args[0].startsWith("-");
        boolean configuredFrontendOnly = hasConfiguredFrontend(config)
                && !hasConfiguredBackend(config)
                && !hasArg(args, "--backend-file")
                && !hasPositionalTarget;
        if (hasPositionalTarget) {
            qinDevEntry = resolveRunTargetToQinFile(config, args[0]);
        } else if (!frontendOnlyOverride && !configuredFrontendOnly) {
            qinDevEntry = resolveDefaultQinEntry(config);
        }
        if (qinDevEntry != null || frontendOnlyOverride || configuredFrontendOnly) {
            String[] qinArgs = hasPositionalTarget
                    ? Arrays.copyOfRange(args, 1, args.length)
                    : args;
            if (configuredFrontendOnly && !hasArg(qinArgs, "--frontend-file")) {
                Path frontendRoot = resolveQinFrontendRoot(config, Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize());
                Path frontendEntry = resolveQinFrontendEntry(
                        config,
                        Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize(),
                        frontendRoot);
                if (frontendEntry != null) {
                    List<String> adjusted = new ArrayList<>(Arrays.asList(qinArgs));
                    adjusted.add("--frontend-file");
                    adjusted.add(Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize()
                            .relativize(frontendEntry)
                            .toString());
                    qinArgs = adjusted.toArray(String[]::new);
                }
            }
            runQinRuntime(config, qinDevEntry, true, qinArgs);
            return;
        }

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

    private static boolean hasConfiguredFrontend(QinConfig config) {
        return config.frontend() != null || config.client() != null;
    }

    private static boolean hasConfiguredBackend(QinConfig config) {
        return config.backend() != null
                && config.backend().entry() != null
                && !config.backend().entry().isBlank();
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

        // 濡絽鍟弬鈧?闂佺绻戞繛濠囧极椤撱垹缁╅柣鐔告緲琚熼梺鎸庣⊕閻＄挿mpile 婵炴垶鏌ㄩ鍛櫠閻樼粯鍤婃い蹇撳琚熺紒缁㈠弾閸犳洜鎹㈠顓犵懝婵犻潧锕﹂々顐も偓鐟版啞瑜板啴骞冮幘鎰佹桨闁靛鍨崇粈鍕熆鐠鸿櫣肖闁?sync 闂佹眹鍔岀€氼喚妲愰敂閿亾濞戞瑱鍏紒?
        String classpath = "";
        Map<String, String> deps = collectAllDependencies(config);
        if (skipSync) {
            if (!deps.isEmpty()) {
                classpath = CacheValidator.getCachedClasspath(QinConstants.getCwd());
                if (classpath == null || classpath.isBlank()) {
                    throw new IllegalStateException(
                            "No cached dependencies found for --no-sync. Run `qin sync` first or remove --no-sync.");
                }
                System.out.println(blue("-> Using cached dependencies (" + QinConstants.CLASSPATH_CACHE_PATH + ")"));
            }
        } else {
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
        // 闁荤喐鐟辩徊楣冩倵娴犲鐭楅柛灞剧⊕濞?
        List<String> argList = Arrays.asList(args);
        boolean syncAll = argList.contains(QinConstants.ARG_ALL);
        boolean force = argList.contains(QinConstants.ARG_FORCE);
        boolean withCompile = argList.contains(QinConstants.ARG_COMPILE);

        if (syncAll) {
            // 闂佸憡鑹鹃張顒勵敆閻愬搫绠ラ柍褜鍓熷鍨緞婵犲嫭鎲ゆ俊鐐€曞﹢鍗灻?
            syncAllProjects(force, withCompile);
        } else {
            // 闂佸憡鑹鹃張顒勵敆閻愯翰浜归柟鎯у暱椤ゅ懎顪冮妶鍛础婵?
            syncCurrentProject(force);

            // 婵犵鈧啿鈧綊鎮樻径鎰闁搞儜鍛瘞婵?--compile闂佹寧绋戦懟顖炲箖閹炬剚娼伴柕澶堝劜閸婄敻鏌ゆ總澶夌盎濠殿喒鏅濈槐鎾诲冀椤掑倹袧
            if (withCompile) {
                System.out.println();
                compileProject(new String[]{QinConstants.ARG_NO_SYNC});
            }
        }
    }

    /**
     * 闂佸憡鑹鹃張顒勵敆閻愯翰浜归柟鎯у暱椤ゅ懎顪冮妶鍛础婵炶弓鍗抽幆鍐礋椤忓棜绌块柣?
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
     * 闂佸憡鑹鹃張顒勵敆閻愬搫绠ラ柍褜鍓熷鍨緞婵犲嫭鎲ゆ俊鐐€曞﹢鍗灻烘导瀛樻櫖闁革富鎽皀orepo 闂侀潻濡囬崕銈呪枍濞嗘挻鏅?
     * 闂佸憡纰嶉崹宕囩箔閸岀偛钃熼柕澶涚畱椤ユ繈鏌″鍛础婵炲弶濯介妵鎰板即椤忓棛顦梻渚囧亝鐢帞绱炴繝鍥х妞ゆ劧绲芥导搴ㄦ煙绾版ê浜鹃梺?qin.config.js 婵＄偑鍊曞﹢鍗灻?
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

        // 缂傚倷鑳堕崰鏇㈩敇?
        int synced = 0;
        int skipped = 0;
        int failed = 0;

        // 闂備緡鍓欑粔鏉戭啅婵犳艾绠ラ柍褜鍓熷鍨緞閹扳斁鍋撳澶嬪剮妞ゆ洖妫涚粈澶屸偓娈垮枛閸婂綊顢楅悙鍝勭鐟滄垿銆?sync
        List<Thread> threads = new ArrayList<>();
        List<SyncResult> results = Collections.synchronizedList(new ArrayList<>());

        for (Path projectPath : projects) {
            Thread t = new Thread(() -> {
                try {
                    String projectName = projectPath.getFileName().toString();

                    // 婵犵鈧啿鈧綊鎮樻径瀣枖鐎广儱妫欑瑧閻庢鍠栭幖顐﹀春濡ゅ懎瑙﹂悘鐐佃檸閸斿嫰鏌ㄥ☉妯绘拱妞ゃ儱鎳樺濠氬Ψ瑜忔径鍕倵?
                    if (!force && CacheValidator.isCacheValid(projectPath)) {
                        System.out.println(gray("  [SKIP] " + projectName + " (cached, skipped)"));
                        results.add(new SyncResult(projectName, SyncStatus.SKIPPED));
                        return;
                    }

                    // 闂佸湱鐟抽崱鈺傛杸 qin sync --compile闂佹寧绋戦悧鍡涘箖閹炬剚娼?+ 缂傚倸鍊归悧鐐烘儊瑜旈弫?
                    System.out.println(blue("  -> Syncing " + projectName + "..."));

                    List<String> command = new ArrayList<>();
                    command.add(currentJavaCommand());
                    command.add("-Xms16m");
                    command.add("-Xmx256m");
                    command.add("-XX:+UseSerialGC");
                    command.add("-XX:-UseJVMCICompiler");
                    command.add("-XX:TieredStopAtLevel=1");
                    command.add("-Dfile.encoding=UTF-8");
                    command.add("-cp");
                    command.add(currentCliClasspath());
                    command.add(QinCli.class.getName());
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

                    // 闁荤姴娲╅褑銇愰崶銊︾秶闁规儳鍟垮В澶愭煥濞戞澧︽繛绗哄€栭—鈧俊顖欒濡查亶鏌ｉ悙鍙夛紨缂?
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                        while (reader.readLine() != null) {
                            // 闂傚倸鐗婇悷鈺冨垝椤栨埃妲堥柛顐到閻庮參寮堕崼鐔稿碍闁?
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

        // 缂備焦绋戦ˇ顖滄閻旂厧绠ラ柍褜鍓熷鍨緞鐏炶姤鐤囩紓浣割儏椤戝懘鎮鹃鍕?
        for (Thread t : threads) {
            t.join();
        }

        // 缂傚倷鑳堕崰鏇㈩敇閸濄儳纾奸柟鎯ь嚟娴?
        for (SyncResult r : results) {
            switch (r.status) {
                case SUCCESS -> synced++;
                case SKIPPED -> skipped++;
                case FAILED -> failed++;
            }
        }

        // 闁哄鐗婇幐鎼佸吹椤撶倫褔宕堕妸銏犱壕?
        System.out.println();
        if (failed > 0) {
            System.out.println(yellow("[WARN] Sync completed: " + synced + " synced, " + skipped + " skipped, " + failed + " failed"));
        } else {
            System.out.println(green("[OK] All projects synced: " + synced + " synced, " + skipped + " skipped"));
        }
    }

    /**
     * 闂佸憡鑹鹃張顒勵敆閻愮數纾奸柟鎯ь嚟娴滎垶鏌ｅΟ鍨厫闁?
     */
    private enum SyncStatus {
        SUCCESS, SKIPPED, FAILED
    }

    /**
     * 闂佸憡鑹鹃張顒勵敆閻愮數纾奸柟鎯ь嚟娴?
     */
    private record SyncResult(String projectName, SyncStatus status) {}

    private static String currentJavaCommand() {
        String executable = QinConstants.isWindows() ? "java.exe" : "java";
        return Paths.get(System.getProperty("java.home"), "bin", executable).toString();
    }

    private static String currentCliClasspath() {
        return System.getProperty("java.class.path");
    }

    /**
     * 闂佸憡鑹鹃張顒勵敆閻愬鐟规繝闈涳功椤╊偊鏌ｉ妸銉ヮ仾闁绘鐤囩粻娑㈠礃椤旂懓浜鹃柡鍕箳鐢棝鏌ㄥ☉妯肩劮缂佺粯鍨垮畷鍫曟倷閸偅娅㈤梺鐟扮摠閸旀鈻?classpath
     * 婵炶揪缍€濞夋洟寮妶鍡樻珷闁绘劖褰冪换渚€鏌ｅΔ鈧悧鎰暰闂佸搫顑嗛崝鏇炩枍閹烘鏅悘鐐靛亾閺嗘粓鏌熼梹鎰グ闁搞倖绮撳畷婵嬪Ω瑜忓浠嬫偣閸ャ劌鐏︽繝鈧导鏉戞嵍濞村吋鐣埀顒€绉归幆?
     */
    static String syncDependenciesCore(QinConfig config) throws Exception {
        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        System.out.println(blue("-> Syncing dependencies..."));
        String sep = QinConstants.getClasspathSeparator();
        List<String> classpaths = new ArrayList<>();

        // 1. 婵炶揪缍€濞夋洟寮妶鍡樻珷闁绘劖褰冪换渚€鏌ｅΔ鈧悧鎰暰闂佸搫顑嗛崝鏇炩枍閹烘鏅柛顐ゅ枑閺嗘粓鏌熼梹鎰グ闁搞倖绮撳畷婵嬪Ω瑜忓浠嬫偣閸ャ劌鐏︽繝鈧导鏉戞嵍濞村吋鐣埀顒€绉归幆鍕敋閸℃瑧顦?
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

        // 2. 闂佸憡鐟禍婵嗭耿娴ｈ浜ゆ繝濠傛噳閺屻倕銆掑顓犵畾缂佸倸妫濋獮宥咁吋閸垺顔嶉梺?Coursier
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

        // 闂佸湱顭堥ˇ鐢稿储閵堝洨纾炬い鏃€妲掗崢顒勬煟閵娿儱顏х紒鍫曨棑閹秆囧冀椤撴壕鍋撴惔銏″劅闊洦鏌ㄧ粭鎾诲箹?classpath
        classpath = sortClasspathByConfigOrder(classpath, deps);

        String json = buildClasspathJson(classpath);
        Files.writeString(QinConstants.getProjectClasspathCache(QinConstants.getCwd()), json);

        // 闂佹眹鍨婚崰鎰板垂?IDEA 闁圭厧鐡ㄩ幑鍥储閵堝洨纾炬い鏃囧Г閻庮喖霉閻樹警鍟囩紒?idea/libraries/*.xml闂?
        if (!classpath.isEmpty()) {
            try {
                System.out.println(blue("-> Generating IDEA library configs..."));
                IdeaLibraryGenerator ideaGen = new IdeaLibraryGenerator(QinConstants.getCwd());
                ideaGen.cleanLibraryConfigs(); // 濠电偞鎸搁幊鎰板箖婵犲洤绫嶇憸鏃堝储閵堝洨纾?
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
     * 缂佺虎鍙庨崰鏇犳崲濮橆厾鐟规繝闈涳功椤╊偆鈧懓鎲¤ぐ鍐箖閹炬剚娼伴柕澶樺灣缁€澶娾攽閳ュ啿鈧綊鎮樻径宀€纾介柟鎯х－閹界娀鏌￠崼婵愭Ч闁哄懌鍎靛畷姘枎鎼粹檧鏋忛梺娲绘娇閸斿海妲愰敂閿亾濞戞瑱鍏紒杈ㄧ箞瀹曘儵鏁冮埀顒勫垂椤栫偛绠ョ憸鎴︺€侀幋锕€瑙﹂悘鐐佃檸閸?
     * 
     * @return classpath 闁诲孩绋掗〃鍫ヮ敄娴ｅ湱鈻?
     */
    private static String ensureDependenciesSynced(QinConfig config) throws Exception {
        String cwd = QinConstants.getCwd();
        LocalProjectResolverEnhanced.ResolutionResult localResolution = inspectLocalDependencies(config);
        ensureNpmDependenciesInstalled(config);

        if (CacheValidator.isCacheValid(cwd)) {
            String classpath = CacheValidator.getCachedClasspath(cwd);
            if (classpath != null && cachedClasspathContainsLocalProjects(classpath, localResolution)) {
                ensureLocalDependenciesReady(config);
                System.out.println(
                        blue("-> Using cached dependencies (" + QinConstants.CLASSPATH_CACHE_PATH + ")"));
                return classpath;
            }
            System.out.println(yellow("-> Cached dependencies missing required local workspace entries, re-syncing..."));
        }

        return syncDependenciesCore(config);
    }

    private static void ensureNpmDependenciesInstalled(QinConfig config) throws IOException {
        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            return;
        }

        NpmPackageManager npm = new NpmPackageManager(QinConstants.getCwd());
        int installed = 0;
        for (Map.Entry<String, String> dep : deps.entrySet()) {
            String name = dep.getKey();
            if (!isNpmDependency(name) || isNpmDependencyInstalled(name)) {
                continue;
            }
            boolean ok = npm.install(name, dep.getValue());
            if (!ok) {
                throw new IOException("Failed to install npm dependency: " + name);
            }
            installed++;
        }
        if (installed > 0) {
            System.out.println(green("[OK] Installed " + installed + " npm dependency package(s)"));
        }
    }

    private static boolean isNpmDependency(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        if (name.startsWith("@")) {
            return name.contains("/");
        }
        return !name.contains(":") && !name.contains("@");
    }

    private static boolean isNpmDependencyInstalled(String name) {
        Path packageJson = Path.of(QinConstants.getCwd(), QinConstants.NODE_MODULES)
                .resolve(name.replace('/', java.io.File.separatorChar))
                .resolve(QinConstants.PACKAGE_JSON);
        return Files.isRegularFile(packageJson);
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

    private static LocalProjectResolverEnhanced.ResolutionResult ensureLocalDependenciesReady(QinConfig config) {
        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            return new LocalProjectResolverEnhanced.ResolutionResult("", new LinkedHashMap<>(), 0, 0, List.of());
        }

        LocalProjectResolverEnhanced resolver = new LocalProjectResolverEnhanced(QinConstants.getCwd());
        return resolver.resolveDependencies(deps);
    }

    private static LocalProjectResolverEnhanced.ResolutionResult inspectLocalDependencies(QinConfig config) {
        Map<String, String> deps = collectAllDependencies(config);
        if (deps.isEmpty()) {
            return new LocalProjectResolverEnhanced.ResolutionResult("", new LinkedHashMap<>(), 0, 0, List.of());
        }

        LocalProjectResolverEnhanced resolver = new LocalProjectResolverEnhanced(QinConstants.getCwd());
        return resolver.resolveDependencies(deps, false);
    }

    private static boolean cachedClasspathContainsLocalProjects(
            String classpath,
            LocalProjectResolverEnhanced.ResolutionResult localResolution) {
        if (localResolution == null || localResolution.localProjects == null || localResolution.localProjects.isEmpty()) {
            return true;
        }

        Set<String> entries = new LinkedHashSet<>();
        String separator = QinConstants.getClasspathSeparator();
        for (String rawEntry : classpath.split(Pattern.quote(separator))) {
            if (rawEntry == null || rawEntry.isBlank()) {
                continue;
            }
            entries.add(Paths.get(rawEntry.trim()).toAbsolutePath().normalize().toString());
        }

        for (LocalProjectResolverEnhanced.ProjectInfo project : localResolution.localProjects) {
            String expected = project.buildClassesPath.toAbsolutePath().normalize().toString();
            if (!entries.contains(expected)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 婵°倗濮撮惌渚€鎯?classpath 婵炴垶鎼╅崢鎯р枔閹达箑绠ラ柍褜鍓熷鍨緞鐎ｎ偆鈧喖霉閻樺搫鐓愭俊鍙夋倐瀹曘儵鏁冮埀顒勬偤閵娾晛鎹?
     * @deprecated 婵炶揪缍€濞夋洟寮?CacheValidator.validateClasspathFiles() 婵炲濯寸徊鎯?
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
     * qin jar - 闂佺懓鐏氶幐鍝モ偓鍨耿瀵煡顢氶埀顒勫焵?JAR闂佹寧绋戦悧鍛箔婢舵劕瑙︽い鎰剁磿鐠愨晠鎮硅閻楊厾妲?
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
     * qin fatjar - 闂佺懓鐏氶幐鍝モ偓?Fat JAR闂佹寧绋戦悧鍡欌偓鍨耿瀹曘儵顢曢敐鍜佹殹闂佸搫鐗嗛ˇ顔炬妞嬪孩灏庨柡宥囨暩缁€?
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
     * qin deps - 闂佸搫瀚晶浠嬪Φ濮橆厾鐟规繝闈涳功椤╊偊鏌?
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

    // ==================== BSP 闂佺儵鏅濋…鍫ュ矗瑜斿畷銊╁箣閹烘挸袘 ====================

    /**
     * 闂佸憡鍑归崹鐗堟叏?BSP Server
     * IDE 婵炴潙鍚嬫穱娲儍閻斿吋鍋ㄩ柕濠忓瘜閸斿啴鏌涘☉娆忕亰闁硅翰鍊栫粙澶嬫償閵忊懇鍋撻姘煎殘闁告繂瀚槐锝夋煕韫囨洖鍘撮柍褜鍓氶惌顔界┍?
     */
    private static void startBspServer() throws Exception {
        com.qin.bsp.QinBspServer server = new com.qin.bsp.QinBspServer(QinConstants.getCwd());
        server.start();
    }

    /**
     * 闂佸憡甯楃换鍌烇綖閹版澘绀?BSP 闂備焦婢樼粔鍫曟偪?
     * 闂佹眹鍨婚崰鎰板垂?.bsp/qin.json 闂佸搫鍊稿ú锝呪枎閵忋倖鏅€光偓閸愭儳鏁?IDE 闂佺厧鐤囧Λ鍕亹閸岀偞鍋濋柣妤€鐗滈崝鍐煛鐎ｎ亜顏╃紓鍌涙尭椤斿繘濡烽妷銉ョ樊
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
     * 闂佸搫绉堕崢褏妲愰敓鐘茬闁哄秲鍔嶉悗顔济归悩鐑樼【闁靛洤娲ㄦ禍姝岀疀閵壯咁槱闁哄鐗嗛幊搴㈡叏椤忓牆妫橀悷娆忓閵嗗﹪鏌?
     */
    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }

    private static void printHelp() {
        System.out.println("""
                Qin - Qin Native Build Tool
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
                  install     Install deps (npm first, fallback to Maven)
                  sync        Sync dependencies (auto-compiles local projects)
                  deps        Show dependency tree
                  dev         Start development mode (Qin: single-port dev server, Java: compile+run)
                  dist        Create distribution package
                  conformance Run Qin vs Chrome conformance suite

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
                  -o, --output <dir>  Output directory (compile / qin fullstack build)
                  -f, --filter <pattern>  Filter tests (test)
                  -v, --verbose   Show verbose output

                Examples:
                  qin init                    # Initialize new project
                  qin run                     # Compile and run (auto Qin runtime when entry is .qin/.js/.mjs/.ts)
                  qin run main/main.qin       # Run a specific Qin entry file
                  qin dev                     # Start Qin dev mode (watch + auto reload)
                  qin run src/app/Main.java   # Run a specific Java file
                  qin run com.app.Main        # Run by fully-qualified class name
                  qin compile                 # Compile only
                  qin build main/main.qin     # Build Qin fullstack artifacts to dist/fullstack
                  qin build -o dist/prod      # Override build output root
                  qin jar                     # Build JAR (without dependencies)
                  qin fatjar                  # Build Fat JAR (with dependencies)
                  qin build                   # Full build
                  qin build --skip-tests      # Build without running tests
                  qin install mitt            # Install npm package and update qin.config.js
                  qin install org.jsoup:jsoup # Install Maven dependency
                  qin install                 # Install deps declared in qin.config.js
                  qin sync                    # Sync deps (auto-compiles local projects)
                  qin conformance             # Run conformance baseline with Chrome
                  qin conformance --chrome "C:/Program Files/Google/Chrome/Application/chrome.exe"
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
     * 婵?.qin/classpath.json 闁荤喐鐟辩徊楣冩倵?classpath
     */
    private static String parseClasspathFromJson(String json) {
        try {
            // 缂備胶濮崑鎾绘煕濡や焦绀夌悮娆撴煛?JSON 闂佽桨鐒︽竟鍡欏垝?
            int start = json.indexOf("[");
            int end = json.lastIndexOf("]");
            if (start < 0 || end < 0)
                return "";

            String arrayContent = json.substring(start + 1, end);
            List<String> paths = new ArrayList<>();

            // 闁荤喐鐟辩徊楣冩倵閽樺－鎺曠疀鎼淬劌娈濋柣鐘辫閸ㄦ壆娆?
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
     * 闂佸搫顑呯€氼剛绱?.qin/classpath.json 闂佸搫绉堕崢褏妲?
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
     * 闂佸湱顭堥ˇ鐢稿储閵堝洨纾炬い鏃€妲掗崢顒勬煟閵娿儱顏х紒鍫曨棑閹秆囧冀椤撴壕鍋撴惔銏″劅闊洦鏌ㄧ粭鎾诲箹?classpath
     * 
     * @param classpath 闂佸憡顭囬崰搴綖?classpath闂佹寧绋戦悧鍡涘垂鎼淬劌鐭?闂佸憡鍔栫敮鎺曘亹閸ф绀嗛柛鈩冪⊕椤撻箖鏌?
     * @param deps      闂備焦婢樼粔鍫曟偪閸℃鈻旀い鎾跺У閻ｅ崬銆掑顓犵畾缂佸倸妫濋弫宥夊醇濠婂懐顔旈梺褰掓櫜閻掞箒銇愰崘顔肩闁靛鏂傞埀顒€瀛╅幆鏃囩疀閵壯咁槴
     * @return 闂佸湱鍎ょ敮鎺旇姳椤撱垹瑙﹂幖杈剧稻閻?classpath
     */
    private static String sortClasspathByConfigOrder(String classpath, Map<String, String> deps) {
        if (classpath == null || classpath.isEmpty() || deps == null || deps.isEmpty()) {
            return classpath;
        }

        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep.equals(";") ? ";" : ":");

        // 闂佸憡甯楃粙鎴犵磽?artifactId 闂佸憡甯婇崡鎶藉Υ鎼淬垺鍎熼煫鍥ㄦ礃閻ｉ亶鏌￠崟顓炐㈤柣?
        Map<String, Integer> orderMap = new LinkedHashMap<>();
        int order = 0;
        for (String depKey : deps.keySet()) {
            // depKey 闂佸搫绉堕崢褏妲? groupId@artifactId 闂?groupId:artifactId
            String artifactId = extractArtifactId(depKey);
            orderMap.put(artifactId.toLowerCase(), order++);
        }

        // 闂佸湱顭堥ˇ鐢稿储閵堝洨纾炬い鏇楀亾闁靛棗瀛╅幆鏃囩疀閺傝法鐟╅柟?
        List<String> sortedPaths = new ArrayList<>(Arrays.asList(paths));
        sortedPaths.sort((a, b) -> {
            String artifactA = extractArtifactIdFromPath(a).toLowerCase();
            String artifactB = extractArtifactIdFromPath(b).toLowerCase();

            int orderA = orderMap.getOrDefault(artifactA, Integer.MAX_VALUE);
            int orderB = orderMap.getOrDefault(artifactB, Integer.MAX_VALUE);

            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }
            // 婵犵鈧啿鈧綊鎮樻径鎰劸闁瑰瓨甯為悷婵嬫煕閿斿搫濮傞柛妯稿€楃槐鏃堫敊閺勫繐骞€闂佹寧绋戦張顒傗偓鍨矌閳ь剚绋掗〃鍡涙儊濠靛洢浜滈柛婵嗗绾?
            return a.compareToIgnoreCase(b);
        });

        return String.join(sep, sortedPaths);
    }

    /**
     * 婵炲濮寸花鑲╂妞嬪孩灏?key 婵炴垶鎼╅崢鍊熴亹娓氣偓瀹?artifactId
     */
    private static String extractArtifactId(String depKey) {
        // 闂佸搫绉堕崢褏妲? groupId@artifactId 闂?groupId:artifactId
        int sepIndex = depKey.lastIndexOf('@');
        if (sepIndex < 0)
            sepIndex = depKey.lastIndexOf(':');
        return sepIndex >= 0 ? depKey.substring(sepIndex + 1) : depKey;
    }

    /**
     * 婵?jar 闁荤姳璀﹂崹鎵閻愬鈻旀い鎾跺枎缁插綊鏌?artifactId
     */
    private static String extractArtifactIdFromPath(String path) {
        // 闁荤姳璀﹂崹鎵閻愬搫鍐€闁绘挸娴风涵鈧? .../groupId/artifactId/version/artifactId-version.jar
        // 闂? .../build/classes
        if (path.contains("build") || path.contains("classes")) {
            // 闂佸搫鐗滈崜娆忥耿鐎涙ǜ浜滈柛锔诲幗缁愭鏌ㄥ☉妯侯殭濠电偛娲幃浠嬪Ω瑜庣粣妤冩喐閻楀牊绀€闁?
            Path p = Paths.get(path);
            if (p.getParent() != null && p.getParent().getParent() != null) {
                return p.getParent().getParent().getFileName().toString();
            }
            return p.getFileName().toString();
        }

        // Maven jar 闁荤姳璀﹂崹鎵?
        String fileName = Paths.get(path).getFileName().toString();
        // 缂備礁顦…宄扳枍?.jar 闂佸憡绮岄惉鍏兼櫠濡ゅ懎瀚夋い鎺嗗亾鐟?
        if (fileName.endsWith(".jar")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        // 闁诲繐绻戠换鍡涙儊椤栫偛绠甸柟閭﹀墮缁?artifactId闂佹寧绋戦悧鍡楋耿椤忓牆瀚夐柍褜鍓熷畷銉︽償濠靛牜浼囨繛?- 婵炴垶鏌ㄩ鍛櫠閻樼粯鏅悘鐐电摂濞层倝鏌＄€ｎ偆鐭婇柟顔筋殜濡啴濮€閳╁唭锕傛煟濡も偓閻楀﹤锕㈡导鏉戠煑闁告垯鍊楃粈?
        int lastDash = fileName.lastIndexOf('-');
        if (lastDash > 0) {
            String suffix = fileName.substring(lastDash + 1);
            // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晜閼恒儛锕傛煟濡も偓閻楀﹤锕㈡导鏉戠煑闁告垯鍊楃粈鍕归悩顔煎姕闁哄棛鍠撻埀顒佺⊕椤ㄥ懐妲愰幋鐐茬窞閺夊牆澧界粈?
            if (!suffix.isEmpty() && Character.isDigit(suffix.charAt(0))) {
                return fileName.substring(0, lastDash);
            }
        }
        return fileName;
    }
}


