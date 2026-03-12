package com.qin.debug;

import com.google.gson.Gson;
import java.nio.file.*;
import java.util.*;

import static com.qin.constants.QinConstants.CONFIG_FILE;

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
            return "21";
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
        return "21";
    }

    public String getSourceVersion() {
        if (java == null) {
            return "21";
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
        return "21";
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
}
