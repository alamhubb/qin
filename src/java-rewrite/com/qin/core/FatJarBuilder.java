package com.qin.core;

import com.qin.types.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.*;

/**
 * Fat Jar Builder for Qin
 * Creates Uber JARs containing all dependencies
 */
public class FatJarBuilder {
    private final QinConfig config;
    private final boolean debug;
    private final String cwd;
    private final String tempDir;
    private final String outputDir;
    private String jarCommand = "jar";

    public FatJarBuilder(QinConfig config, boolean debug) {
        this(config, debug, System.getProperty("user.dir"));
    }

    public FatJarBuilder(QinConfig config, boolean debug, String cwd) {
        this.config = config;
        this.debug = debug;
        this.cwd = cwd;
        this.tempDir = Paths.get(cwd, "build", "temp").toString();
        this.outputDir = Paths.get(cwd, config.output() != null ? config.output().dir() : "dist").toString();
    }

    /**
     * Build Fat Jar
     */
    public BuildResult build() {
        try {
            initJarCommand();
            createTempDir();

            // Resolve dependencies
            List<String> jarPaths = resolveDependencies();
            if (!jarPaths.isEmpty()) {
                extractJars(jarPaths);
                cleanSignatures();
            }

            // Compile source
            compileSource();

            // Generate manifest
            ConfigLoader configLoader = new ConfigLoader(cwd);
            ParsedEntry parsed = configLoader.parseEntry(config.entry());
            generateManifest(parsed.className());

            // Package JAR
            String jarName = config.output() != null && config.output().jarName() != null
                ? config.output().jarName() : "app.jar";
            String outputPath = Paths.get(outputDir, jarName).toString();
            packageJar(outputPath);

            // Cleanup
            if (!debug) {
                cleanup();
            }

            return BuildResult.success(outputPath);
        } catch (Exception e) {
            return BuildResult.failure(e.getMessage());
        }
    }

    private void initJarCommand() throws Exception {
        // Try direct jar command
        if (runCommand("jar", "--version")) {
            jarCommand = "jar";
            return;
        }

        // Try JAVA_HOME
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            String jarPath = Paths.get(javaHome, "bin", isWindows() ? "jar.exe" : "jar").toString();
            if (Files.exists(Paths.get(jarPath))) {
                jarCommand = jarPath;
                return;
            }
        }

        // Try to get java.home from java command
        String javaHomeFromCmd = getJavaHome();
        if (javaHomeFromCmd != null) {
            String jarPath = Paths.get(javaHomeFromCmd, "bin", isWindows() ? "jar.exe" : "jar").toString();
            if (Files.exists(Paths.get(jarPath))) {
                jarCommand = jarPath;
                return;
            }
        }

        throw new Exception("找不到 jar 命令。请设置 JAVA_HOME 环境变量");
    }

    private String getJavaHome() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-XshowSettings:properties", "-version");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            proc.waitFor();

            for (String line : output.split("\n")) {
                if (line.contains("java.home")) {
                    int idx = line.indexOf("=");
                    if (idx > 0) {
                        return line.substring(idx + 1).trim();
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    private void createTempDir() throws IOException {
        Path temp = Paths.get(tempDir);
        if (Files.exists(temp)) {
            deleteDir(temp);
        }
        Files.createDirectories(temp);
    }

    private List<String> resolveDependencies() throws IOException {
        // Check for cached classpath
        Path classpathCache = Paths.get(cwd, "build", ".cache", "classpath.json");
        if (Files.exists(classpathCache)) {
            String content = Files.readString(classpathCache);
            // Simple JSON parsing
            int start = content.indexOf("[");
            int end = content.lastIndexOf("]");
            if (start >= 0 && end > start) {
                String arrayContent = content.substring(start + 1, end);
                return Arrays.stream(arrayContent.split(","))
                    .map(s -> s.trim().replace("\"", ""))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            }
        }

        // Fallback to real-time resolution
        if (config.dependencies() == null || config.dependencies().isEmpty()) {
            return new ArrayList<>();
        }

        DependencyResolver resolver = new DependencyResolver(
            "cs", config.repositories(), null, cwd, config.localRep()
        );
        
        List<String> deps = config.dependencies().entrySet().stream()
            .map(e -> e.getKey() + ":" + e.getValue())
            .collect(Collectors.toList());

        ResolveResult result = resolver.resolveWithDetails(deps);
        return result.isSuccess() ? result.jarPaths() : new ArrayList<>();
    }

    private void extractJars(List<String> jarPaths) throws Exception {
        for (String jarPath : jarPaths) {
            ProcessBuilder pb = new ProcessBuilder(jarCommand, "-xf", jarPath);
            pb.directory(new File(tempDir));
            Process proc = pb.start();
            int exitCode = proc.waitFor();
            if (exitCode != 0) {
                throw new Exception("Failed to extract " + jarPath);
            }
        }
    }

    private void cleanSignatures() throws IOException {
        Path metaInf = Paths.get(tempDir, "META-INF");
        if (!Files.exists(metaInf)) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(metaInf)) {
            for (Path file : stream) {
                String name = file.getFileName().toString().toLowerCase();
                if (name.endsWith(".sf") || name.endsWith(".dsa") || name.endsWith(".rsa")) {
                    Files.delete(file);
                }
            }
        }
    }

    private void compileSource() throws Exception {
        ConfigLoader configLoader = new ConfigLoader(cwd);
        ParsedEntry parsed = configLoader.parseEntry(config.entry());
        Path srcDir = Paths.get(cwd, parsed.srcDir());

        List<String> javaFiles;
        try (Stream<Path> walk = Files.walk(srcDir)) {
            javaFiles = walk
                .filter(p -> p.toString().endsWith(".java"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }

        if (javaFiles.isEmpty()) {
            throw new Exception("No Java files found in " + srcDir);
        }

        List<String> args = new ArrayList<>();
        args.add("javac");
        args.add("-d");
        args.add(tempDir);

        // Add temp dir to classpath for dependencies
        if (hasClasses(Paths.get(tempDir))) {
            args.add("-cp");
            args.add(tempDir);
        }

        args.addAll(javaFiles);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(new File(cwd));
        Process proc = pb.start();
        String stderr = readStream(proc.getErrorStream());
        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            throw new Exception("Compilation failed: " + stderr);
        }

        // Copy resources
        copyResources(parsed.srcDir());
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
                copyDir(resPath, Paths.get(tempDir));
            }
        }
    }

    private void generateManifest(String mainClass) throws IOException {
        Path metaInf = Paths.get(tempDir, "META-INF");
        Files.createDirectories(metaInf);

        String content = String.format(
            "Manifest-Version: 1.0\nMain-Class: %s\nCreated-By: Qin (Java-Vite Build Tool)\n",
            mainClass
        );

        Files.writeString(metaInf.resolve("MANIFEST.MF"), content);
    }

    private void packageJar(String outputPath) throws Exception {
        Files.createDirectories(Paths.get(outputDir));

        String manifestPath = Paths.get(tempDir, "META-INF", "MANIFEST.MF").toString();

        ProcessBuilder pb = new ProcessBuilder(
            jarCommand, "-cvfm", outputPath, manifestPath, "-C", tempDir, "."
        );
        pb.directory(new File(cwd));
        Process proc = pb.start();
        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            String stderr = readStream(proc.getErrorStream());
            throw new Exception("Failed to create JAR: " + stderr);
        }
    }

    private void cleanup() throws IOException {
        deleteDir(Paths.get(tempDir));
    }

    private boolean hasClasses(Path dir) throws IOException {
        if (!Files.exists(dir)) return false;
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk.anyMatch(p -> p.toString().endsWith(".class"));
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

    private void deleteDir(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        // Ignore
                    }
                });
        }
    }

    private boolean runCommand(String... args) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            proc.getInputStream().transferTo(OutputStream.nullOutputStream());
            return proc.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
