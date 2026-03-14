package com.qin.debug;

import com.qin.core.ConfigLoader;
import com.qin.types.JavaConfig;
import com.qin.types.QinConfig;

import java.nio.file.Path;
import java.util.Map;

import static com.qin.constants.QinConstants.DEFAULT_JAVA_VERSION;

/**
 * Shared adapter for reading Qin config through qin-cli core classes.
 */
public final class QinConfigSupport {

    private QinConfigSupport() {
    }

    public static QinConfig load(Path projectPath) {
        if (projectPath == null) {
            return null;
        }
        try {
            ConfigLoader loader = new ConfigLoader(projectPath.toString());
            return loader.load();
        } catch (Exception e) {
            String message = e.getMessage() == null ? "" : e.getMessage();
            if (message.contains("qin.config.json is empty")
                    || message.contains("qin.config.json parsed to null")
                    || message.contains("No project detected")) {
                QinLogger.debug("[Config] Ignore invalid config at " + projectPath + ": " + message);
            } else {
                QinLogger.warn("[Config] Failed to load config from " + projectPath + ": " + message);
            }
            return null;
        }
    }

    public static QinConfig load(String projectPath) {
        if (projectPath == null || projectPath.isBlank()) {
            return null;
        }
        return load(Path.of(projectPath));
    }

    public static QinConfig loadNearest(Path start) {
        Path nearest = QinProjectLocator.findNearestQinProject(start);
        if (nearest == null) {
            return null;
        }
        return load(nearest);
    }

    public static String javaVersion(QinConfig config) {
        JavaConfig java = config != null ? config.java() : null;
        if (java == null) {
            return DEFAULT_JAVA_VERSION;
        }
        if (hasText(java.release())) {
            return java.release();
        }
        if (hasText(java.target())) {
            return java.target();
        }
        if (hasText(java.version())) {
            return java.version();
        }
        return DEFAULT_JAVA_VERSION;
    }

    public static String sourceVersion(QinConfig config) {
        JavaConfig java = config != null ? config.java() : null;
        if (java == null) {
            return DEFAULT_JAVA_VERSION;
        }
        if (hasText(java.release())) {
            return java.release();
        }
        if (hasText(java.source())) {
            return java.source();
        }
        if (hasText(java.version())) {
            return java.version();
        }
        return DEFAULT_JAVA_VERSION;
    }

    public static String projectName(QinConfig config, String fallback) {
        if (config != null && hasText(config.name())) {
            return config.name();
        }
        return fallback;
    }

    public static Map<String, String> dependencies(QinConfig config) {
        if (config == null || config.dependencies() == null) {
            return Map.of();
        }
        return config.dependencies();
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
