package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.*;
import com.qin.utils.QinUtils;

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
        this(config, debug, QinConstants.getCwd());
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
            System.out.println("  [1/6] Initializing...");
            initJarCommand();
            createTempDir();

            // Resolve dependencies
            System.out.println("  [2/6] Resolving dependencies...");
            List<String> jarPaths = resolveDependencies();
            if (!jarPaths.isEmpty()) {
                System.out.println("  [3/6] Extracting " + jarPaths.size() + " JARs...");
                extractJars(jarPaths);
                cleanSignatures();
                cleanModuleInfo(); // 删除 module-info.class，禁用模块系统（Fat JAR 标准做法）
            }

            // Compile source with classpath
            System.out.println("  [4/6] Compiling source...");
            compileSourceWithClasspath(jarPaths);

            // Generate manifest
            System.out.println("  [5/6] Generating manifest...");
            ConfigLoader configLoader = new ConfigLoader(cwd);
            ParsedEntry parsed = configLoader.parseEntry(config.entry());
            generateManifest(parsed.className());

            // Package JAR
            System.out.println("  [6/6] Packaging JAR...");
            String jarName = config.output() != null && config.output().jarName() != null
                    ? config.output().jarName()
                    : "app.jar";
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
            String jarPath = Paths.get(javaHome, "bin", QinConstants.isWindows() ? "jar.exe" : "jar").toString();
            if (Files.exists(Paths.get(jarPath))) {
                jarCommand = jarPath;
                return;
            }
        }

        // Try to get java.home from java command
        String javaHomeFromCmd = getJavaHome();
        if (javaHomeFromCmd != null) {
            String jarPath = Paths.get(javaHomeFromCmd, "bin", QinConstants.isWindows() ? "jar.exe" : "jar").toString();
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
            QinUtils.deleteDir(temp);
        }
        Files.createDirectories(temp);
    }

    private List<String> resolveDependencies() throws IOException {
        // Check for cached classpath
        Path classpathCache = QinPaths.getClasspathCache(cwd);
        if (Files.exists(classpathCache)) {
            String content = Files.readString(classpathCache);
            // 移除换行符，确保正确解析
            content = content.replace("\n", "").replace("\r", "");
            // Simple JSON parsing
            int start = content.indexOf("[");
            int end = content.lastIndexOf("]");
            if (start >= 0 && end > start) {
                String arrayContent = content.substring(start + 1, end);
                List<String> result = Arrays.stream(arrayContent.split(","))
                        .map(s -> s.trim().replace("\"", ""))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                if (debug) {
                    System.out.println("[FatJar] Resolved " + result.size() + " dependencies from cache:");
                    result.forEach(p -> System.out.println("[FatJar]   - " + p));
                }
                return result;
            }
        }

        // Fallback to real-time resolution
        if (config.dependencies() == null || config.dependencies().isEmpty()) {
            return new ArrayList<>();
        }

        DependencyResolver resolver = new DependencyResolver(
                "cs", config.repositories(), null, cwd, config.localRep());

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
        if (!Files.exists(metaInf))
            return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(metaInf)) {
            for (Path file : stream) {
                String name = file.getFileName().toString().toLowerCase();
                if (name.endsWith(".sf") || name.endsWith(".dsa") || name.endsWith(".rsa")) {
                    Files.delete(file);
                }
            }
        }
    }

    /**
     * 删除 module-info.class 文件
     * 这是创建 Fat JAR 的标准做法（Maven Shade Plugin / Gradle Shadow 都这样做）
     * 删除后，模块化 JAR 变成普通 JAR，不会触发 Java 模块系统检查
     */
    private void cleanModuleInfo() throws IOException {
        Path moduleInfo = Paths.get(tempDir, "module-info.class");
        if (Files.exists(moduleInfo)) {
            Files.delete(moduleInfo);
            if (debug) {
                System.out.println("[FatJar] Removed module-info.class");
            }
        }
    }

    private void compileSourceWithClasspath(List<String> jarPaths) throws Exception {
        ConfigLoader configLoader = new ConfigLoader(cwd);
        ParsedEntry parsed = configLoader.parseEntry(config.entry());

        // 使用 QinConstants.getSourceDir() 统一获取源代码目录
        String sourceDirPath = QinConstants.getSourceDir(config.java());
        Path srcDir = Paths.get(cwd, sourceDirPath);

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
        args.add("-encoding");
        args.add(QinConstants.CHARSET_UTF8);

        // 构建 classpath：tempDir + 所有依赖 JAR
        List<String> cpParts = new ArrayList<>();
        cpParts.add(tempDir);
        cpParts.addAll(jarPaths);

        if (!cpParts.isEmpty()) {
            String separator = QinConstants.getClasspathSeparator();
            args.add("-cp");
            args.add(String.join(separator, cpParts));
        }

        if (debug) {
            System.out.println(
                    "[FatJar] Compiling " + javaFiles.size() + " files with " + jarPaths.size() + " JAR dependencies");
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
                mainClass);

        Files.writeString(metaInf.resolve("MANIFEST.MF"), content);
    }

    private void packageJar(String outputPath) throws Exception {
        Files.createDirectories(Paths.get(outputDir));

        String manifestPath = Paths.get(tempDir, "META-INF", "MANIFEST.MF").toString();

        // 使用 -cfm 而不是 -cvfm，避免产生大量输出导致缓冲区满
        ProcessBuilder pb = new ProcessBuilder(
                jarCommand, "-cfm", outputPath, manifestPath, "-C", tempDir, ".");
        pb.directory(new File(cwd));
        pb.redirectErrorStream(true); // 合并 stderr 到 stdout
        Process proc = pb.start();

        // 消费输出，避免缓冲区满导致卡住
        proc.getInputStream().transferTo(OutputStream.nullOutputStream());

        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            throw new Exception("Failed to create JAR (exit code: " + exitCode + ")");
        }
    }

    private void cleanup() throws IOException {
        QinUtils.deleteDir(Paths.get(tempDir));
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

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
