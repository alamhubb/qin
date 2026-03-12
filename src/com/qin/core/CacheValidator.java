package com.qin.core;

import com.qin.constants.QinConstants;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cache validation helpers shared by CLI and IDEA plugin.
 */
public final class CacheValidator {

    private CacheValidator() {}

    public static boolean isCacheValid(String projectPath) {
        try {
            Path configFile = Paths.get(projectPath, QinConstants.CONFIG_FILE);
            Path cacheFile = QinPaths.getClasspathCache(projectPath);

            if (!Files.exists(cacheFile) || !Files.exists(configFile)) {
                return false;
            }

            if (Files.getLastModifiedTime(cacheFile).compareTo(
                    Files.getLastModifiedTime(configFile)) <= 0) {
                return false;
            }

            String cacheContent = Files.readString(cacheFile);
            if (cacheContent.trim().isEmpty() || !cacheContent.contains("classpath")) {
                return false;
            }

            String classpath = parseClasspathFromJson(cacheContent);
            if (classpath.isEmpty()) {
                return false;
            }

            return validateClasspathFiles(classpath);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCacheValid(Path projectPath) {
        return isCacheValid(projectPath.toString());
    }

    public static boolean validateClasspathFiles(String classpath) {
        if (classpath == null || classpath.isEmpty()) {
            return true;
        }

        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep);
        for (String path : paths) {
            if (path.isEmpty()) {
                continue;
            }
            if (!Files.exists(Paths.get(path))) {
                return false;
            }
        }
        return true;
    }

    public static String parseClasspathFromJson(String json) {
        try {
            int start = json.indexOf("\"classpath\"");
            if (start < 0) {
                return "";
            }

            int colonIndex = json.indexOf(':', start);
            if (colonIndex < 0) {
                return "";
            }

            String sep = QinConstants.getClasspathSeparator();
            int arrayStart = json.indexOf('[', colonIndex);
            if (arrayStart >= 0) {
                int arrayEnd = json.indexOf(']', arrayStart);
                if (arrayEnd > arrayStart) {
                    String arrayContent = json.substring(arrayStart + 1, arrayEnd);
                    List<String> paths = new ArrayList<>();
                    int pos = 0;
                    while (pos < arrayContent.length()) {
                        int quoteStart = arrayContent.indexOf('"', pos);
                        if (quoteStart < 0) {
                            break;
                        }
                        int quoteEnd = arrayContent.indexOf('"', quoteStart + 1);
                        if (quoteEnd < 0) {
                            break;
                        }
                        String value = arrayContent.substring(quoteStart + 1, quoteEnd).trim();
                        if (!value.isEmpty()) {
                            paths.add(value);
                        }
                        pos = quoteEnd + 1;
                    }
                    return String.join(sep, paths);
                }
            }

            int valueStart = json.indexOf('"', colonIndex);
            if (valueStart < 0) {
                return "";
            }
            int valueEnd = json.indexOf('"', valueStart + 1);
            if (valueEnd < 0) {
                return "";
            }
            return json.substring(valueStart + 1, valueEnd);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCachedClasspath(String projectPath) {
        try {
            Path cacheFile = QinPaths.getClasspathCache(projectPath);
            if (!Files.exists(cacheFile)) {
                return null;
            }

            String cacheContent = Files.readString(cacheFile);
            String classpath = parseClasspathFromJson(cacheContent);
            if (!classpath.isEmpty() && validateClasspathFiles(classpath)) {
                return classpath;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}