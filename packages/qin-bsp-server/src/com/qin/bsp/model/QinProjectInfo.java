package com.qin.bsp.model;

import java.util.Map;

/**
 * Qin 项目配置信息
 * 对应 qin.config.json 的简化版本
 */
public class QinProjectInfo {
    public String name;
    public String version;
    public String entry;
    public Map<String, String> dependencies;
    public JavaConfig java;

    public static class JavaConfig {
        public String version = "25";
        public String sourceDir = "src";
        public String outputDir = "build/classes";
    }

    public String getSourceDir() {
        if (java != null && java.sourceDir != null) {
            return java.sourceDir;
        }
        return "src";
    }

    public String getJavaVersion() {
        if (java != null && java.version != null) {
            return java.version;
        }
        return "25";
    }
}
