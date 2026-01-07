package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Configuration Loader for Qin (Java 25)
 * 加载配置文件，QinConfig 现在是不可变 Record
 */
public class ConfigLoader {
    private final String cwd;
    private final Gson gson;

    public ConfigLoader() {
        this(QinConstants.getCwd());
    }

    public ConfigLoader(String cwd) {
        this.cwd = cwd;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Load configuration from qin.config.json
     */
    public QinConfig load() throws IOException {
        // 尝试加载 JSON 配置
        Path jsonConfig = Paths.get(cwd, QinConstants.CONFIG_FILE);
        if (Files.exists(jsonConfig)) {
            String content = Files.readString(jsonConfig);
            QinConfig config = gson.fromJson(content, QinConfig.class);
            return applyDefaults(config);
        }

        // 自动检测项目类型
        PluginDetector detector = new PluginDetector(cwd);
        DetectionResult detection = detector.detect();

        if (detection.getLanguages().isEmpty() && detection.getFeatures().isEmpty()) {
            throw new IOException(
                    "No project detected. Create src/Main.java or create qin.config.json");
        }

        // 零配置模式 - 使用简化构造器
        return applyDefaults(new QinConfig(
                detectProjectName(),
                "1.0.0"));
    }

    /**
     * Auto-detect project name from directory
     */
    private String detectProjectName() {
        return Paths.get(cwd).getFileName().toString();
    }

    /**
     * Auto-detect entry file
     */
    public String findEntry() {
        String[] candidates = {
                "src/main/java/com/subhuti/Main.java",
                "src/Main.java",
                "src/server/Main.java",
                "src/App.java",
                "src/Application.java"
        };

        for (String candidate : candidates) {
            if (Files.exists(Paths.get(cwd, candidate))) {
                return candidate;
            }
        }

        // Search src/*/Main.java
        Path srcDir = Paths.get(cwd, "src");
        if (Files.exists(srcDir) && Files.isDirectory(srcDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(srcDir)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        Path mainJava = entry.resolve("Main.java");
                        if (Files.exists(mainJava)) {
                            return "src/" + entry.getFileName() + "/Main.java";
                        }
                    }
                }
            } catch (IOException e) {
                // Ignore
            }
        }

        return null;
    }

    /**
     * Validate configuration
     */
    public ValidationResult validate(QinConfig config) {
        List<String> errors = new ArrayList<>();

        if (config == null) {
            errors.add("Configuration is empty");
            return ValidationResult.failure(errors);
        }

        if (config.entry() != null && !config.entry().endsWith(".java")) {
            errors.add("'entry' must be a .java file");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    /**
     * Apply default values to configuration
     * 因为 Record 不可变，所以返回新的 QinConfig 实例
     */
    private QinConfig applyDefaults(QinConfig config) {
        // 如果已经有完整配置，直接返回
        if (config.entry() != null && config.output() != null && config.java() != null) {
            return config;
        }

        // 构建带默认值的新 config
        String entry = config.entry() != null ? config.entry() : findEntry();
        OutputConfig output = config.output() != null ? config.output() : new OutputConfig();
        JavaConfig java = config.java() != null ? config.java() : new JavaConfig("25");

        // 创建新的不可变配置
        return new QinConfig(
                config.name(),
                config.version(),
                config.description(),
                config.scope(),
                config.port(),
                config.localRep(),
                config.client(),
                config.plugins(),
                entry,
                config.dependencies(),
                config.devDependencies(),
                config.packages(),
                output,
                java,
                config.graalvm(),
                config.frontend(),
                config.scripts(),
                config.repositories());
    }

    /**
     * Parse entry path to extract source directory and class name
     */
    public ParsedEntry parseEntry(String entry) {
        if (entry == null) {
            return new ParsedEntry(QinConstants.DEFAULT_SOURCE_DIR, QinConstants.DEFAULT_MAIN_CLASS,
                    QinConstants.DEFAULT_SOURCE_DIR + "/Main.java");
        }

        String normalized = entry.replace("\\", "/");
        int lastSlash = normalized.lastIndexOf("/");

        String fileName = lastSlash >= 0 ? normalized.substring(lastSlash + 1) : normalized;
        String srcDir = lastSlash >= 0 ? normalized.substring(0, lastSlash) : ".";
        String simpleClassName = fileName.replace(".java", "");

        // Try to read package declaration
        String className = simpleClassName;
        try {
            Path filePath = Paths.get(cwd, entry);
            if (Files.exists(filePath)) {
                String content = Files.readString(filePath);
                Pattern pattern = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;", Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    className = matcher.group(1) + "." + simpleClassName;
                }
            }
        } catch (IOException e) {
            // Use simple class name
        }

        return new ParsedEntry(srcDir, className, entry);
    }
}
