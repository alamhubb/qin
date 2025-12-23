package com.qin.core;

import com.qin.types.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Configuration Loader for Qin
 * 加载配置文件，支持多种格式
 */
public class ConfigLoader {
    private final String cwd;
    private final Gson gson;

    public ConfigLoader() {
        this(System.getProperty("user.dir"));
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
        Path jsonConfig = Paths.get(cwd, "qin.config.json");
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
                "No project detected. Create src/Main.java or create qin.config.json"
            );
        }

        // 零配置模式
        QinConfig config = new QinConfig();
        config.setEntry(detection.getEntry());
        
        if (detection.getClientDir() != null) {
            ClientConfig client = new ClientConfig();
            client.setRoot(detection.getClientDir());
            config.setClient(client);
        }

        return applyDefaults(config);
    }

    /**
     * Auto-detect entry file
     */
    public String findEntry() {
        String[] candidates = {
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

        if (config.getEntry() != null && !config.getEntry().endsWith(".java")) {
            errors.add("'entry' must be a .java file");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    /**
     * Apply default values to configuration
     */
    private QinConfig applyDefaults(QinConfig config) {
        if (config.getEntry() == null) {
            config.setEntry(findEntry());
        }

        // Output defaults
        if (config.getOutput() == null) {
            config.setOutput(new OutputConfig());
        }
        if (config.getOutput().getDir() == null) {
            config.getOutput().setDir("dist");
        }
        if (config.getOutput().getJarName() == null) {
            String jarName = config.getName() != null ? config.getName() + ".jar" : "app.jar";
            config.getOutput().setJarName(jarName);
        }

        // Java defaults
        if (config.getEntry() != null) {
            if (config.getJava() == null) {
                config.setJava(new JavaConfig());
            }
            if (config.getJava().getVersion() == null) {
                config.getJava().setVersion("17");
            }
            if (config.getJava().getSourceDir() == null) {
                ParsedEntry parsed = parseEntry(config.getEntry());
                config.getJava().setSourceDir(parsed.getSrcDir());
            }
        }

        return config;
    }

    /**
     * Parse entry path to extract source directory and class name
     */
    public ParsedEntry parseEntry(String entry) {
        if (entry == null) {
            return new ParsedEntry("src", "Main", "src/Main.java");
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
