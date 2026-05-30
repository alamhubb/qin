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
                case "jar" -> jarProject(cmdArgs);           // 妫ｅ啫鏅?闁哄倹婢橀·?
                case "fatjar" -> fatjarProject(cmdArgs);     // 妫ｅ啫鏅?闁哄倹婢橀·?
                case "build" -> buildProject(cmdArgs);
                case "clean" -> cleanProject();
                case "install" -> QinInstallCommand.execute(cmdArgs);
                case "sync" -> syncDependencies(cmdArgs);
                case "deps" -> showDependencies(cmdArgs);    // 妫ｅ啫鏅?闁哄倹婢橀·?
                case "dev" -> devMode(cmdArgs);
                case "dist" -> distProject();
                case "conformance" -> runConformance(cmdArgs);
                case "bsp" -> startBspServer();              // 妫ｅ啫鏅?BSP Server
                case "bsp-init" -> initBspConfig();          // 妫ｅ啫鏅?闁汇垻鍠愰崹?BSP 闂佹澘绉堕悿?
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
                    Add it to qin.config.json dependencies, then run `qin sync`.
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

        // 婵☆偀鍋撻柡灞诲劜濡叉悂宕ラ敂钘夌樄閻庤鐭花?.java 闁哄倸娲ｅ▎?
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
        if (hasQinScriptExtension(normalizedTarget)) {
            String pathTarget = normalizeRelativePath(normalizedTarget);
            Path absolute = Paths.get(QinConstants.getCwd()).resolve(pathTarget).normalize();
            return Files.exists(absolute) ? pathTarget : null;
        }

        if (normalizedTarget.contains("/") || normalizedTarget.contains("\\")) {
            String pathTarget = normalizeRelativePath(normalizedTarget);
            if (hasQinScriptExtension(pathTarget)) {
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
        if (config.entry() != null && !config.entry().isBlank()) {
            if (hasQinScriptExtension(config.entry())) {
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
                    "Qin runtime dependencies are missing. Add `com.qin:qin-runtime-core` to qin.config.json dependencies.");
        }

        String dependencyClasspath = ensureDependenciesSynced(config);
        String compileOutputDir = Paths.get(QinConstants.getCwd(), JavaCompileConfig.from(config).outputDir()).toString();
        String separator = QinConstants.getClasspathSeparator();
        String runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                ? compileOutputDir
                : compileOutputDir + separator + dependencyClasspath;

        int port = resolveQinRuntimePort(config, args);
        Path root = Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize();
        Path backendSource = Paths.get(QinConstants.getCwd(), qinFile).toAbsolutePath().normalize();
        Path frontendRoot = resolveQinFrontendRoot(config, root);
        Path frontendEntry = resolveQinFrontendEntry(config, root, frontendRoot);
        Path frontendStaticDir = frontendRoot;
        String runtimeMainClass = resolveQinRuntimeMainClass(runtimeClasspath, devMode);

        System.out.println(blue(devMode ? "-> Starting Qin dev runtime..." : "-> Running Qin runtime..."));
        System.out.println(gray("  Backend entry: " + qinFile));
        if (frontendRoot != null) {
            System.out.println(gray("  Frontend root: " + root.relativize(frontendRoot)));
        }
        if (frontendEntry != null) {
            System.out.println(gray("  Frontend entry: " + root.relativize(frontendEntry)));
        }

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(runtimeClasspath);
        command.add(runtimeMainClass);
        if (devMode) {
            command.add("--dev");
        }
        command.add("--root");
        command.add(root.toString());
        command.add("--backend-file");
        command.add(backendSource.toString());
        command.add("--port");
        command.add(String.valueOf(port));
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

        // 濞达綀娉曢弫?BuildLifecycle 閺夆晜绋栭、鎴犫偓鐟版湰閺嗭綁寮搁崟顐ょ处
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
                    "Qin runtime dependencies are missing. Add `com.qin:qin-runtime-core` to qin.config.json dependencies.");
        }

        String dependencyClasspath = ensureDependenciesSynced(config);
        String compileOutputDir = Paths.get(QinConstants.getCwd(), JavaCompileConfig.from(config).outputDir()).toString();
        String separator = QinConstants.getClasspathSeparator();
        String runtimeClasspath = dependencyClasspath == null || dependencyClasspath.isBlank()
                ? compileOutputDir
                : compileOutputDir + separator + dependencyClasspath;

        Path root = Paths.get(QinConstants.getCwd()).toAbsolutePath().normalize();
        Path backendSource = root.resolve(qinFile).normalize();
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
        if (args.length > 0 && !args[0].startsWith("-")) {
            qinDevEntry = resolveRunTargetToQinFile(config, args[0]);
        } else {
            qinDevEntry = resolveDefaultQinEntry(config);
        }
        if (qinDevEntry != null) {
            String[] qinArgs = args.length > 0 && !args[0].startsWith("-")
                    ? Arrays.copyOfRange(args, 1, args.length)
                    : args;
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

        // 妫ｅ啯鏂€ 闁稿繑濞婇弫顓㈠绩閻熸澘袟闁挎稒鐡璷mpile 濞戞柨顑呮晶鐘绘嚊椤忓嫬袟缁绢収鍠曠换姘瑹濠靛﹦顩€瑰憡褰冮幃鎾愁潰閵夘垳绀勫璺虹Ф閺?sync 闁汇劌瀚槐锔锯偓娑欙公缁?
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
        // 閻熸瑱绲鹃悗浠嬪矗閸屾稒娈?
        List<String> argList = Arrays.asList(args);
        boolean syncAll = argList.contains(QinConstants.ARG_ALL);
        boolean force = argList.contains(QinConstants.ARG_FORCE);
        boolean withCompile = argList.contains(QinConstants.ARG_COMPILE);

        if (syncAll) {
            // 闁告艾鏈鐐哄箥閳ь剟寮垫径濠勬憤濡炪倕婀卞ú?
            syncAllProjects(force, withCompile);
        } else {
            // 闁告艾鏈鐐躲亹閹惧啿顤呭銈呮贡濞?
            syncCurrentProject(force);

            // 濠碘€冲€归悘澶愬箰閸パ呮毎濞?--compile闁挎稑鑻幃鎾愁潰閵夈儲鍊甸柤濂変簻婵晝绱撻弽顒傛Н
            if (withCompile) {
                System.out.println();
                compileProject(new String[]{QinConstants.ARG_NO_SYNC});
            }
        }
    }

    /**
     * 闁告艾鏈鐐躲亹閹惧啿顤呭銈呮贡濞蹭即鎯冮崟顏嗚穿閻?
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
     * 闁告艾鏈鐐哄箥閳ь剟寮垫径濠勬憤濡炪倕婀卞ú浼存晬閸︻摰norepo 闁革妇鍎ゅ▍娆撴晬?
     * 闁告碍鍨崇粭鍌炲蓟閵夛箑顥濋柡宥呮贡濞叉媽銇愰弴顏嗙闂侇偅甯掔紞濠囧箥椤愶絽浼庨柟纰樺亾闁?qin.config.json 濡炪倕婀卞ú?
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

        // 缂備胶鍠曢?
        int synced = 0;
        int skipped = 0;
        int failed = 0;

        // 闂侇剙绉村濠氬箥閳ь剟寮垫径鎰┾偓宥夋儎椤曞棛绀夌€殿喖鍊归鐐哄箥瑜戦、?sync
        List<Thread> threads = new ArrayList<>();
        List<SyncResult> results = Collections.synchronizedList(new ArrayList<>());

        for (Path projectPath : projects) {
            Thread t = new Thread(() -> {
                try {
                    String projectName = projectPath.getFileName().toString();

                    // 濠碘€冲€归悘澶嬬▔瀹ュ棙笑鐎殿喖鎼崺妤呭触鐏炵虎鍔勯柨娑樻湰椤ュ懘寮婚妷褏澶勯悗?
                    if (!force && CacheValidator.isCacheValid(projectPath)) {
                        System.out.println(gray("  [SKIP] " + projectName + " (cached, skipped)"));
                        results.add(new SyncResult(projectName, SyncStatus.SKIPPED));
                        return;
                    }

                    // 闁圭瑳鍡╂斀 qin sync --compile闁挎稑鐗嗛幃鎾愁潰?+ 缂傚倹鐗為惁褔鏁?
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

                    // 閻犲洩顕цぐ鍥ㄦ綇閹惧啿姣夐柨娑樼墦濞笺倖顪€濡警妲遍柣鐐叉４缁?
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                        while (reader.readLine() != null) {
                            // 闂傚牊鐟╃划顖氣槈閸絽鐎弶鍫熸尭閸?
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

        // 缂佹稑顦欢鐔煎箥閳ь剟寮垫径灞芥疇缂佸顑呴悾顒勫箣?
        for (Thread t : threads) {
            t.join();
        }

        // 缂備胶鍠曢鍝ョ磼閹惧浜?
        for (SyncResult r : results) {
            switch (r.status) {
                case SUCCESS -> synced++;
                case SKIPPED -> skipped++;
                case FAILED -> failed++;
            }
        }

        // 閺夊牊鎸搁崵顓炐ч崶銊㈠亾?
        System.out.println();
        if (failed > 0) {
            System.out.println(yellow("[WARN] Sync completed: " + synced + " synced, " + skipped + " skipped, " + failed + " failed"));
        } else {
            System.out.println(green("[OK] All projects synced: " + synced + " synced, " + skipped + " skipped"));
        }
    }

    /**
     * 闁告艾鏈鐐电磼閹惧浜柣妯垮煐閳?
     */
    private enum SyncStatus {
        SUCCESS, SKIPPED, FAILED
    }

    /**
     * 闁告艾鏈鐐电磼閹惧浜?
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
     * 闁告艾鏈鐐寸瑹濠靛﹦顩柣銊ュ閻楀疇绠涢崘顔瑰亾閺勫繒甯嗛柨娑樼焷缁绘垿宕堕悙鍨櫢闁瑰瓨鍔楀▓?classpath
     * 濞达綀娉曢弫銈嗘櫠閻愭彃绻侀柣妤€鐗愯闁哄鍔曞▍鎺楁晬鐏炵偓鏆滈柟闀愭祰閸ゆ粓宕濋妸褏妞介悹鍥ㄥ灦濠€浼村捶娴兼番鈧秹鎯?
     */
    static String syncDependenciesCore(QinConfig config) throws Exception {
        Map<String, String> deps = new HashMap<>();
        if (config.dependencies() != null) deps.putAll(config.dependencies());
        if (config.devDependencies() != null) deps.putAll(config.devDependencies());

        System.out.println(blue("-> Syncing dependencies..."));
        String sep = QinConstants.getClasspathSeparator();
        List<String> classpaths = new ArrayList<>();

        // 1. 濞达綀娉曢弫銈嗘櫠閻愭彃绻侀柣妤€鐗愯闁哄鍔曞▍鎺楁晬閸喐鏆滈柟闀愭祰閸ゆ粓宕濋妸褏妞介悹鍥ㄥ灦濠€浼村捶娴兼番鈧秹鎯勯鍡欑
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

        // 2. 闁告瑯浜濆﹢浣规交濠婂應鏌ゅ〒姘箚缁傚棝骞嶅鍫㈡闁?Coursier
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

        // 闁圭顦甸崢銈囩磾椤旀槒鍘柣銊ュ缁堕鎸ч弽顓溾偓搴㈡償韫囨柨绗撻幖?classpath
        classpath = sortClasspathByConfigOrder(classpath, deps);

        String json = buildClasspathJson(classpath);
        Files.writeString(QinConstants.getProjectClasspathCache(QinConstants.getCwd()), json);

        // 闁汇垻鍠愰崹?IDEA 閹煎瓨鎹囬崢銈囩磾椤旇姤鐎ù鐘侯啇缁?idea/libraries/*.xml闁?
        if (!classpath.isEmpty()) {
            try {
                System.out.println(blue("-> Generating IDEA library configs..."));
                IdeaLibraryGenerator ideaGen = new IdeaLibraryGenerator(QinConstants.getCwd());
                ideaGen.cleanLibraryConfigs(); // 婵炴挸鎳愰幃濠囧籍瑜旈崢銈囩磾?
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
     * 缁绢収鍠曠换姘瑹濠靛﹦顩€瑰憡褰冮幃鎾愁潰閵夘垳绀夊┑鈥冲€归悘澶岀磽閹惧磭鎽犻柡鍫濐槹閺呫儵宕氬▎搴♀枏闁活潿鍔庣槐锔锯偓娑欙公缁辨繈宕ラ敃鈧崹顖炲箥瑜戦、鎴﹀触鐏炵虎鍔?
     * 
     * @return classpath 閻庢稒顨堥浣圭▔?
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
     * 濡ょ姴鐭侀惁?classpath 濞戞搩鍘惧▓鎴﹀箥閳ь剟寮垫径瀣€ù鐘哄煐濡叉悂宕ラ敃鈧悺銊╁捶?
     * @deprecated 濞达綀娉曢弫?CacheValidator.validateClasspathFiles() 濞寸媴绲惧ù?
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
     * qin jar - 闁瑰灚鎸哥€垫﹢寮查鈧埀?JAR闁挎稑鐗呯粭澶愬触椤愶紕璐╅悹褎鐗槐?
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
     * qin fatjar - 闁瑰灚鎸哥€?Fat JAR闁挎稑鐗嗙€垫﹢宕ラ锝咁暡闁哄牆顦欢椋庢導閺嶇數绀?
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
     * qin deps - 闁哄嫬澧介妵姘瑹濠靛﹦顩柡?
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

    // ==================== BSP 闁烩晝顭堥崣褔宕ㄩ幋鎺撳Б ====================

    /**
     * 闁告凹鍨版慨?BSP Server
     * IDE 濞村吋淇洪惃鐔兼偨閵婏富鍔冮柛娑欏灊閹躲倖绋夋惔銏⑩偓顖氼嚈閸濆嫪绱ｉ柛蹇曞厴閳ь剚鐭穱?
     */
    private static void startBspServer() throws Exception {
        com.qin.bsp.QinBspServer server = new com.qin.bsp.QinBspServer(QinConstants.getCwd());
        server.start();
    }

    /**
     * 闁告帗绻傞～鎰板礌?BSP 闂佹澘绉堕悿?
     * 闁汇垻鍠愰崹?.bsp/qin.json 闁哄倸娲ｅ▎銏ゆ晬瀹€鍐惧敤 IDE 闁煎疇妫勮ぐ鍌炴偝閻楀牜鍔冮柡瀣缂傛挸顔忛妷銉ュ緮
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
     * 闁哄秶鍘х槐锟犲礌閺嶃劍鐎ù鐘烘硾閵囧洨浜歌箛銉х閺夊牆鎳庢慨顏堝棘鐟欏嫮銆婇柨?
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
                  qin install mitt            # Install npm package and update qin.config.json
                  qin install org.jsoup:jsoup # Install Maven dependency
                  qin install                 # Install deps declared in qin.config.json
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
     * 濞?.qin/classpath.json 閻熸瑱绲鹃悗?classpath
     */
    private static String parseClasspathFromJson(String json) {
        try {
            // 缂佺姭鍋撻柛妤佹礉琚欓柡?JSON 闁轰焦澹嗙划?
            int start = json.indexOf("[");
            int end = json.lastIndexOf("]");
            if (start < 0 || end < 0)
                return "";

            String arrayContent = json.substring(start + 1, end);
            List<String> paths = new ArrayList<>();

            // 閻熸瑱绲鹃悗钘壭掕箛搴ㄥ殝閻犱警鍨扮欢?
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
     * 闁哄瀚紓?.qin/classpath.json 闁哄秶鍘х槐?
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
     * 闁圭顦甸崢銈囩磾椤旀槒鍘柣銊ュ缁堕鎸ч弽顓溾偓搴㈡償韫囨柨绗撻幖?classpath
     * 
     * @param classpath 闁告鍠庨～?classpath闁挎稑鐗嗛崹搴ㄥ矗?闁告劖甯掕ぐ鍧楀礆閸℃稒顓鹃柨?
     * @param deps      闂佹澘绉堕悿鍡樼▔椤撶姵鐣卞〒姘箚缁傚棝鏁嶉崼婊呯闁归晲鐒﹁ぐ鍐礂閵夆斂鈧孩鎯旇箛銉х
     * @return 闁圭儤甯掔花顓㈠触鎼达絾鐣?classpath
     */
    private static String sortClasspathByConfigOrder(String classpath, Map<String, String> deps) {
        if (classpath == null || classpath.isEmpty() || deps == null || deps.isEmpty()) {
            return classpath;
        }

        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep.equals(";") ? ";" : ":");

        // 闁告帗绋戠紓?artifactId 闁告帊鍗抽妴搴㈡償韫囨洘鐣遍柡鍕Т閻?
        Map<String, Integer> orderMap = new LinkedHashMap<>();
        int order = 0;
        for (String depKey : deps.keySet()) {
            // depKey 闁哄秶鍘х槐? groupId@artifactId 闁?groupId:artifactId
            String artifactId = extractArtifactId(depKey);
            orderMap.put(artifactId.toLowerCase(), order++);
        }

        // 闁圭顦甸崢銈囩磾椤曗偓閵嗗孩鎯旇箛鏂跨瑩閹?
        List<String> sortedPaths = new ArrayList<>(Arrays.asList(paths));
        sortedPaths.sort((a, b) -> {
            String artifactA = extractArtifactIdFromPath(a).toLowerCase();
            String artifactB = extractArtifactIdFromPath(b).toLowerCase();

            int orderA = orderMap.getOrDefault(artifactA, Integer.MAX_VALUE);
            int orderB = orderMap.getOrDefault(artifactB, Integer.MAX_VALUE);

            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }
            // 濠碘€冲€归悘澶愭焾閹存帞鐟濋柛锔哄姂閸樸倗绱旈鏄忓幀闁挎稑鏈€垫粎鈧稒顨嗛惁婵囥亜閸濆嫮纰?
            return a.compareToIgnoreCase(b);
        });

        return String.join(sep, sortedPaths);
    }

    /**
     * 濞寸姴绨肩欢椋庢導?key 濞戞搩鍘借ぐ渚€宕?artifactId
     */
    private static String extractArtifactId(String depKey) {
        // 闁哄秶鍘х槐? groupId@artifactId 闁?groupId:artifactId
        int sepIndex = depKey.lastIndexOf('@');
        if (sepIndex < 0)
            sepIndex = depKey.lastIndexOf(':');
        return sepIndex >= 0 ? depKey.substring(sepIndex + 1) : depKey;
    }

    /**
     * 濞?jar 閻犱警鍨扮欢鐐寸▔椤撶喎绲归柛?artifactId
     */
    private static String extractArtifactIdFromPath(String path) {
        // 閻犱警鍨扮欢鐐哄冀閻撳海纭€: .../groupId/artifactId/version/artifactId-version.jar
        // 闁? .../build/classes
        if (path.contains("build") || path.contains("classes")) {
            // 闁哄牜鍓欏﹢瀛樸亜閸︻厽绐楅柨娑樺婵炲洭鎮介妸褎绐楃憸鐗堟礀閹?
            Path p = Paths.get(path);
            if (p.getParent() != null && p.getParent().getParent() != null) {
                return p.getParent().getParent().getFileName().toString();
            }
            return p.getFileName().toString();
        }

        // Maven jar 閻犱警鍨扮欢?
        String fileName = Paths.get(path).getFileName().toString();
        // 缂佸顭峰▍?.jar 闁告粌鐬兼晶妤呭嫉椤掆偓瑜?
        if (fileName.endsWith(".jar")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        // 閻忓繑绻嗛惁顖炲箵閹邦剙绲?artifactId闁挎稑鐗嗗﹢顏堝嫉閳ь剟宕ユ惔婵堫伇濞?- 濞戞柨顑呮晶鐘绘晬鐏炵瓔娲ら柡瀣矊閹妫冮姀鈩冃﹂柣妤€鐗婂﹢浼村矗閸戙倗绀?
        int lastDash = fileName.lastIndexOf('-');
        if (lastDash > 0) {
            String suffix = fileName.substring(lastDash + 1);
            // 婵☆偀鍋撻柡灞诲劜濡叉悂宕ラ敂鑺バ﹂柣妤€鐗婂﹢浼村矗閸戙倗绀勫ù鐘劜閺嗙喓鈧稒顨呯槐鎴炲緞鏉堝墽绀?
            if (!suffix.isEmpty() && Character.isDigit(suffix.charAt(0))) {
                return fileName.substring(0, lastDash);
            }
        }
        return fileName;
    }
}

