package com.qin.debug;

import com.google.gson.Gson;
import java.nio.file.*;
import java.util.*;

import static com.qin.constants.QinConstants.CONFIG_FILE;
import static com.qin.constants.QinConstants.DEFAULT_JAVA_VERSION;

/**
 * Parses qin.config.json for the IDEA plugin.
 */
public class QinConfig {
    public String name;
    public String version;
    public String entry;
    public Map<String, String> dependencies;
    public JavaConfig java;

    public static class JavaConfig {
        public String version;
        public String release;
        public String source;
        public String target;
        public String sourceDir;
        public String outputDir;
    }

    public QinConfig() {
    }

    public QinConfig(String javaVersion) {
        this.java = new JavaConfig();
        this.java.version = javaVersion;
    }

    public String getJavaVersion() {
        if (java == null) {
            return DEFAULT_JAVA_VERSION;
        }
        if (java.release != null && !java.release.isBlank()) {
            return java.release;
        }
        if (java.target != null && !java.target.isBlank()) {
            return java.target;
        }
        if (java.version != null && !java.version.isBlank()) {
            return java.version;
        }
        return DEFAULT_JAVA_VERSION;
    }

    public String getSourceVersion() {
        if (java == null) {
            return DEFAULT_JAVA_VERSION;
        }
        if (java.release != null && !java.release.isBlank()) {
            return java.release;
        }
        if (java.source != null && !java.source.isBlank()) {
            return java.source;
        }
        if (java.version != null && !java.version.isBlank()) {
            return java.version;
        }
        return DEFAULT_JAVA_VERSION;
    }

    public static QinConfig load(String projectPath) {
        try {
            Path configPath = Paths.get(projectPath, CONFIG_FILE);
            if (!Files.exists(configPath)) {
                return null;
            }
            String json = Files.readString(configPath);
            return new Gson().fromJson(json, QinConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static QinConfig load(Path projectPath) {
        return projectPath == null ? null : load(projectPath.toString());
    }

    public static QinConfig loadNearest(Path start) {
        if (start == null) {
            return null;
        }

        Path current = Files.isDirectory(start) ? start : start.getParent();
        while (current != null) {
            QinConfig config = load(current);
            if (config != null) {
                return config;
            }
            current = current.getParent();
        }
        return null;
    }
}
