package com.qin.debug;

import com.google.gson.Gson;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 解析 qin.config.json
 */
public class QinConfig {
    public String name;
    public String version;
    public String entry;
    public Map<String, String> dependencies;
    public JavaConfig java;

    public static class JavaConfig {
        public String version;
        public String source;
        public String target;
        public String sourceDir;
        public String outputDir;
    }

    /**
     * 默认构造器
     */
    public QinConfig() {
    }

    /**
     * 简化构造器，用于创建只有 Java 版本的配置
     */
    public QinConfig(String javaVersion) {
        this.java = new JavaConfig();
        this.java.version = javaVersion;
    }

    /**
     * 获取 Java 版本（优先使用 target，否则使用 version）
     */
    public String getJavaVersion() {
        if (java == null) {
            return "21";
        }
        // target 优先（用于编译目标版本）
        if (java.target != null && !java.target.isBlank()) {
            return java.target;
        }
        // 然后是 version
        if (java.version != null && !java.version.isBlank()) {
            return java.version;
        }
        return "21";
    }

    /**
     * 获取 source 版本（用于 IDEA 语言级别）
     */
    public String getSourceVersion() {
        if (java == null) {
            return "21";
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
            Path configPath = Paths.get(projectPath, "qin.config.json");
            if (!Files.exists(configPath))
                return null;
            String json = Files.readString(configPath);
            return new Gson().fromJson(json, QinConfig.class);
        } catch (Exception e) {
            return null;
        }
    }
}
