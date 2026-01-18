package com.qin.java;

import com.qin.constants.QinConstants;

import java.util.*;
import java.util.stream.*;

/**
 * Classpath utilities
 */
public class ClasspathUtils {

    /**
     * Get platform-specific classpath separator
     */
    public static String getClasspathSeparator() {
        return QinConstants.getClasspathSeparator();
    }

    /**
     * Check if running on Windows
     */
    public static boolean isWindows() {
        return QinConstants.isWindows();
    }

    /**
     * Build classpath from paths
     */
    public static String buildClasspath(List<String> paths) {
        if (paths == null || paths.isEmpty()) {
            return "";
        }
        return String.join(getClasspathSeparator(), paths);
    }

    /**
     * Build classpath from paths array
     */
    public static String buildClasspath(String... paths) {
        return buildClasspath(Arrays.asList(paths));
    }

    /**
     * Build full classpath including output directory
     */
    public static String buildFullClasspath(String outputDir, List<String> jarPaths) {
        List<String> allPaths = new ArrayList<>();
        allPaths.add(outputDir);
        if (jarPaths != null) {
            allPaths.addAll(jarPaths);
        }
        return buildClasspath(allPaths);
    }

    /**
     * Parse classpath string into paths
     */
    public static List<String> parseClasspath(String classpath) {
        if (classpath == null || classpath.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(classpath.split(getClasspathSeparator()))
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Merge multiple classpaths
     */
    public static String mergeClasspaths(String... classpaths) {
        Set<String> paths = new LinkedHashSet<>();
        for (String cp : classpaths) {
            if (cp != null && !cp.isEmpty()) {
                paths.addAll(parseClasspath(cp));
            }
        }
        return buildClasspath(new ArrayList<>(paths));
    }
}
