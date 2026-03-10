package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.JarResult;
import com.qin.types.QinConfig;

import java.io.*;
import java.nio.file.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;

/**
 * JAR 打包器
 * 负责打包普通 JAR（不包含依赖）
 */
public class JarBuilder {

    private final String projectDir;
    private final QinConfig config;

    public JarBuilder(String projectDir, QinConfig config) {
        this.projectDir = projectDir;
        this.config = config;
    }

    /**
     * 构建普通 JAR
     */
    public JarResult buildJar() {
        try {
            // 1. 确定输出路径
            Path buildDir = Paths.get(projectDir, "build");
            Path libsDir = buildDir.resolve("libs");
            Files.createDirectories(libsDir);

            String jarName = getJarName();
            Path jarPath = libsDir.resolve(jarName);

            // 2. 确认 classes 目录存在
            Path classesDir = buildDir.resolve("classes");
            if (!Files.exists(classesDir)) {
                return JarResult.failure("Classes directory not found: " + classesDir +
                                        ". Run 'qin compile' first.");
            }

            System.out.println("📦 Packaging JAR...");
            System.out.println("  Output: " + jarPath);

            // 3. 创建 JAR 文件
            try (JarOutputStream jos = new JarOutputStream(
                    new FileOutputStream(jarPath.toFile()),
                    createManifest())) {

                // 添加 classes 目录中的所有文件
                addDirectory(jos, classesDir, classesDir);

                // 添加资源文件
                addResources(jos);
            }

            long jarSize = Files.size(jarPath);
            System.out.println("  Size: " + formatSize(jarSize));

            return JarResult.success(jarPath.toString(), jarSize);

        } catch (IOException e) {
            return JarResult.failure("Failed to build JAR: " + e.getMessage());
        }
    }

    /**
     * 创建 MANIFEST.MF
     */
    private Manifest createManifest() {
        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();

        // Manifest 版本（必需）
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");

        // 项目信息
        mainAttributes.putValue("Created-By", "Qin Build Tool");
        mainAttributes.putValue("Built-By", System.getProperty("user.name"));
        mainAttributes.putValue("Build-Time", java.time.Instant.now().toString());

        // 如果有主类，添加 Main-Class
        String mainClass = getMainClass();
        if (mainClass != null) {
            mainAttributes.put(Attributes.Name.MAIN_CLASS, mainClass);
            System.out.println("  Main-Class: " + mainClass);
        }

        return manifest;
    }

    /**
     * 递归添加目录到 JAR
     */
    private void addDirectory(JarOutputStream jos, Path sourceDir, Path baseDir) throws IOException {
        try (var stream = Files.walk(sourceDir)) {
            stream.filter(Files::isRegularFile)
                  .forEach(file -> {
                      try {
                          Path relativePath = baseDir.relativize(file);
                          String entryName = relativePath.toString().replace('\\', '/');

                          JarEntry entry = new JarEntry(entryName);
                          entry.setTime(Files.getLastModifiedTime(file).toMillis());
                          jos.putNextEntry(entry);

                          Files.copy(file, jos);
                          jos.closeEntry();

                      } catch (IOException e) {
                          throw new UncheckedIOException(e);
                      }
                  });
        }
    }

    /**
     * 添加资源文件
     */
    private void addResources(JarOutputStream jos) throws IOException {
        // 检查是否有 resources 目录
        Path resourcesDir = Paths.get(projectDir, "src/main/resources");
        if (Files.exists(resourcesDir)) {
            System.out.println("  Adding resources from: src/main/resources");
            addDirectory(jos, resourcesDir, resourcesDir);
        }
    }

    /**
     * 获取 JAR 文件名
     */
    private String getJarName() {
        // 优先使用 output.jarName
        if (config.output() != null && config.output().jarName() != null) {
            return config.output().jarName();
        }

        // 否则使用 name-version.jar
        String version = config.version() != null ? config.version() : "1.0.0";
        return config.name() + "-" + version + ".jar";
    }

    /**
     * 获取主类
     */
    private String getMainClass() {
        // 从 entry 推断主类
        if (config.entry() != null) {
            String entry = config.entry();
            // 例如: src/Main.java -> Main
            // 例如: src/com/example/App.java -> com.example.App
            if (entry.endsWith(".java")) {
                String className = entry.substring(0, entry.length() - 5);

                // 移除 src/ 或 src/main/java/ 前缀
                className = className.replaceFirst("^src/(main/java/)?", "");

                // 转换路径分隔符为包名
                className = className.replace('/', '.').replace('\\', '.');

                return className;
            }
        }

        return null;
    }

    /**
     * 格式化文件大小
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
}
