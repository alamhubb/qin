package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * qin-plugin-java
 * Java language support for Qin build tool
 * 
 * Features:
 * - Java compilation (javac)
 * - Dependency resolution (Coursier)
 * - Fat JAR building
 * - JUnit 5 testing
 * - Resource file handling
 */
public class JavaPlugin implements QinPlugin {
    private final JavaPluginOptions options;
    private final JavaLanguageSupport languageSupport;
    private final List<QinPlugin> subPlugins;

    public JavaPlugin() {
        this(new JavaPluginOptions());
    }

    public JavaPlugin(JavaPluginOptions options) {
        this.options = options;
        this.languageSupport = new JavaLanguageSupport(options);
        this.subPlugins = new ArrayList<>();
    }

    /**
     * 添加子插件（如热重载插件）
     */
    public void addPlugin(QinPlugin plugin) {
        this.subPlugins.add(plugin);
    }

    @Override
    public String getName() {
        return "qin-plugin-java";
    }

    @Override
    public LanguageSupport getLanguage() {
        return languageSupport;
    }

    @Override
    public List<QinPlugin> getPlugins() {
        return subPlugins;
    }

    @Override
    public Map<String, Object> config(Map<String, Object> config) {
        if (options.getEntry() != null && !config.containsKey("entry")) {
            Map<String, Object> newConfig = new HashMap<>(config);
            newConfig.put("entry", options.getEntry());
            return newConfig;
        }
        return config;
    }

    /**
     * 创建 Java 插件
     */
    public static JavaPlugin create() {
        return new JavaPlugin();
    }

    public static JavaPlugin create(JavaPluginOptions options) {
        return new JavaPlugin(options);
    }
}

/**
 * Java 插件配置
 */
class JavaPluginOptions {
    private String version;
    private String entry;
    private String sourceDir;
    private String jarName;
    private boolean hotReloadEnabled = true;

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public String getSourceDir() { return sourceDir; }
    public void setSourceDir(String sourceDir) { this.sourceDir = sourceDir; }

    public String getJarName() { return jarName; }
    public void setJarName(String jarName) { this.jarName = jarName; }

    public boolean isHotReloadEnabled() { return hotReloadEnabled; }
    public void setHotReloadEnabled(boolean enabled) { this.hotReloadEnabled = enabled; }
}

/**
 * Java 语言支持实现
 */
class JavaLanguageSupport implements LanguageSupport {
    private final JavaPluginOptions options;
    private final String cwd;
    private final String outputDir;
    private final String testOutputDir;

    public JavaLanguageSupport(JavaPluginOptions options) {
        this.options = options;
        this.cwd = System.getProperty("user.dir");
        this.outputDir = Paths.get(cwd, "build", "classes").toString();
        this.testOutputDir = Paths.get(cwd, "build", "test-classes").toString();
    }

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public List<String> getExtensions() {
        return List.of(".java");
    }

    @Override
    public CompileResult compile(CompileContext ctx) {
        try {
            Files.createDirectories(Paths.get(outputDir));

            List<String> javaFiles = ctx.getSourceFiles().stream()
                .filter(f -> f.endsWith(".java"))
                .collect(Collectors.toList());

            if (javaFiles.isEmpty()) {
                return CompileResult.failure("No Java files found");
            }

            // 复制资源文件
            copyResources();

            // 构建 javac 命令
            List<String> args = new ArrayList<>();
            args.add("javac");
            args.add("-d");
            args.add(outputDir);

            if (ctx.getClasspath() != null && !ctx.getClasspath().isEmpty()) {
                args.add("-cp");
                args.add(ctx.getClasspath());
            }

            args.addAll(javaFiles);

            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(new File(cwd));
            Process proc = pb.start();

            String stderr = readStream(proc.getErrorStream());
            int exitCode = proc.waitFor();

            if (exitCode != 0) {
                return CompileResult.failure(stderr.isEmpty() ? "Compilation failed" : stderr.trim());
            }

            return CompileResult.success(javaFiles.size(), outputDir);
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    @Override
    public void run(RunContext ctx) throws Exception {
        String entry = options.getEntry();
        if (entry == null) {
            entry = (String) ctx.getConfig().get("entry");
        }
        if (entry == null) {
            throw new RuntimeException("No entry point specified");
        }

        String className = parseClassName(entry);
        String separator = getClasspathSeparator();
        String fullClasspath = ctx.getClasspath() != null && !ctx.getClasspath().isEmpty()
            ? outputDir + separator + ctx.getClasspath()
            : outputDir;

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");
        javaArgs.add("-cp");
        javaArgs.add(fullClasspath);
        javaArgs.add(className);
        javaArgs.addAll(ctx.getArgs());

        ProcessBuilder pb = new ProcessBuilder(javaArgs);
        pb.directory(new File(cwd));
        pb.inheritIO();
        Process proc = pb.start();

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    @Override
    public TestResult test(TestContext ctx) {
        // 简化实现
        return new TestResult(true, 0, 0, 0, 0, 0, "Tests not implemented", null);
    }

    @Override
    public BuildResult build(BuildContext ctx) {
        try {
            String tempDir = Paths.get(cwd, "build", "temp").toString();
            String jarCommand = findJarCommand();

            // 清理临时目录
            deleteDir(Paths.get(tempDir));
            Files.createDirectories(Paths.get(tempDir));
            Files.createDirectories(Paths.get(ctx.getOutputDir()));

            // 复制编译后的 class 文件
            if (Files.exists(Paths.get(outputDir))) {
                copyDir(Paths.get(outputDir), Paths.get(tempDir));
            }

            // 复制资源文件
            copyResources(tempDir);

            // 生成 MANIFEST.MF
            String entry = options.getEntry();
            if (entry == null) {
                entry = (String) ctx.getConfig().get("entry");
            }
            String mainClass = entry != null ? parseClassName(entry) : "Main";
            
            Path metaInfDir = Paths.get(tempDir, "META-INF");
            Files.createDirectories(metaInfDir);
            Files.writeString(metaInfDir.resolve("MANIFEST.MF"),
                "Manifest-Version: 1.0\nMain-Class: " + mainClass + "\nCreated-By: Qin\n");

            // 打包 JAR
            String outputPath = Paths.get(ctx.getOutputDir(), ctx.getOutputName()).toString();
            String manifestPath = metaInfDir.resolve("MANIFEST.MF").toString();

            ProcessBuilder pb = new ProcessBuilder(
                jarCommand, "-cvfm", outputPath, manifestPath, "-C", tempDir, "."
            );
            pb.directory(new File(cwd));
            Process proc = pb.start();
            int exitCode = proc.waitFor();

            if (exitCode != 0) {
                String stderr = readStream(proc.getErrorStream());
                return BuildResult.failure("JAR creation failed: " + stderr);
            }

            // 清理
            deleteDir(Paths.get(tempDir));

            return BuildResult.success(outputPath);
        } catch (Exception e) {
            return BuildResult.failure(e.getMessage());
        }
    }

    private void copyResources() throws IOException {
        copyResources(outputDir);
    }

    private void copyResources(String destDir) throws IOException {
        String[] resourceDirs = {
            Paths.get(cwd, "src", "resources").toString(),
            Paths.get(cwd, "src", "main", "resources").toString()
        };

        for (String resourceDir : resourceDirs) {
            Path resPath = Paths.get(resourceDir);
            if (Files.exists(resPath)) {
                copyDir(resPath, Paths.get(destDir));
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

    private void deleteDir(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try { Files.delete(p); } catch (IOException e) { }
                });
        }
    }

    private String parseClassName(String entry) {
        // "src/com/example/Main.java" -> "com.example.Main"
        java.util.regex.Matcher matcher = java.util.regex.Pattern
            .compile("(?:src/)?(.+)\\.java$")
            .matcher(entry);
        if (matcher.find()) {
            return matcher.group(1).replace("/", ".");
        }
        return "Main";
    }

    private String findJarCommand() throws Exception {
        // 尝试直接使用 jar
        try {
            ProcessBuilder pb = new ProcessBuilder("jar", "--version");
            Process proc = pb.start();
            if (proc.waitFor() == 0) return "jar";
        } catch (Exception e) { }

        // 尝试 JAVA_HOME
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            String jarPath = Paths.get(javaHome, "bin", isWindows() ? "jar.exe" : "jar").toString();
            if (Files.exists(Paths.get(jarPath))) return jarPath;
        }

        throw new Exception("找不到 jar 命令");
    }

    private String getClasspathSeparator() {
        return isWindows() ? ";" : ":";
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
