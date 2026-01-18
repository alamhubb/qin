package com.qin.bsp;

import com.qin.constants.QinConstants;
import java.util.Map;

/**
 * Qin 项目配置信息
 * 对应 qin.config.json
 */
public class QinProjectInfo {
    public String name;
    public String version;
    public String entry;
    public Map<String, String> dependencies;
    public Map<String, String> devDependencies;
    public JavaConfig java;

    public static class JavaConfig {
        public String version = "25";
        public String sourceDir = QinConstants.JAVA_SOURCE_DIR;
        public String testDir = QinConstants.DEFAULT_TEST_DIR;
        public String outputDir = QinConstants.BUILD_CLASSES_DIR;
    }

    public String getSourceDir() {
        if (java != null && java.sourceDir != null && !java.sourceDir.isEmpty()) {
            return java.sourceDir;
        }
        return QinConstants.JAVA_SOURCE_DIR;
    }

    public String getTestDir() {
        if (java != null && java.testDir != null && !java.testDir.isEmpty()) {
            return java.testDir;
        }
        return QinConstants.DEFAULT_TEST_DIR;
    }

    public String getJavaVersion() {
        if (java != null && java.version != null) {
            return java.version;
        }
        return "25";
    }
}
