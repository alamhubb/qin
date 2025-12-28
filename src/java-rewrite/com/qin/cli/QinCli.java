package com.qin.cli;

import com.qin.core.*;
import com.qin.types.*;

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
        Path configFile = cwd.resolve("qin.config.json");
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

        // Resolve dependencies
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            System.out.println(blue("→ Resolving dependencies..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                csCommand, config.repositories(), null,
                System.getProperty("user.dir"), config.localRep()
            );
            classpath = resolver.resolveFromObject(deps);
        }

        // Compile and run
        System.out.println(blue("→ Compiling and running..."));
        JavaRunner runner = new JavaRunner(config, classpath);
        runner.compileAndRun(Arrays.asList(args));

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
                System.getProperty("user.dir"), config.localRep()
            );
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
        String outputDir = "build/classes";
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

        // Resolve dependencies
        String classpath = "";
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            System.out.println(blue("→ Resolving dependencies..."));
            String csCommand = ensureCoursier();
            DependencyResolver resolver = new DependencyResolver(
                csCommand, config.repositories(), null,
                System.getProperty("user.dir"), config.localRep()
            );
            classpath = resolver.resolveFromObject(deps);
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

        System.out.println(blue("→ Checking environment..."));
        String csCommand = ensureCoursier();

        System.out.println(blue("→ Syncing dependencies..."));
        DependencyResolver resolver = new DependencyResolver(
            csCommand, config.repositories(), null,
            System.getProperty("user.dir"), config.localRep()
        );
        
        ResolveResult result = resolver.resolveWithDetails(
            deps.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .toList()
        );

        if (!result.isSuccess()) {
            throw new Exception(result.getError());
        }

        // Save classpath cache
        Path cacheDir = Paths.get(System.getProperty("user.dir"), "build", ".cache");
        Files.createDirectories(cacheDir);
        
        List<String> jarPaths = result.jarPaths();
        String json = "{\n  \"classpath\": [\n    \"" + 
            String.join("\",\n    \"", jarPaths) + "\"\n  ]\n}";
        Files.writeString(cacheDir.resolve("classpath.json"), json);

        System.out.println(green("✓ Dependencies synced (" + jarPaths.size() + " JARs)"));
        System.out.println(gray("  Cache: build/.cache/classpath.json"));
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
                System.getProperty("user.dir"), config.localRep()
            );
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
        if (!Files.exists(dir)) return;
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
    private static String blue(String s) { return "\u001B[34m" + s + "\u001B[0m"; }
    private static String green(String s) { return "\u001B[32m" + s + "\u001B[0m"; }
    private static String red(String s) { return "\u001B[31m" + s + "\u001B[0m"; }
    private static String yellow(String s) { return "\u001B[33m" + s + "\u001B[0m"; }
    private static String gray(String s) { return "\u001B[90m" + s + "\u001B[0m"; }
}
