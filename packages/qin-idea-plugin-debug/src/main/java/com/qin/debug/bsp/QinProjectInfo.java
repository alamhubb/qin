package com.qin.debug.bsp;

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
        public String testDir = "src/test/java";
        public String outputDir = "build/classes";
    }

    public String getSourceDir() {
        if (java != null && java.sourceDir != null) {
            return java.sourceDir;
        }
        return "src";
    }

    public String getOutputDir() {
        if (java != null && java.outputDir != null) {
            return java.outputDir;
        }
        return "build/classes";
    }

    public String getJavaVersion() {
        if (java != null && java.version != null) {
            return java.version;
        }
        return "25";
    }
}
