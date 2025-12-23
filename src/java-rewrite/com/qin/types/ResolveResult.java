package com.qin.types;

import java.util.List;

/**
 * Dependency resolution result
 */
public class ResolveResult {
    private final boolean success;
    private final String classpath;
    private final List<String> jarPaths;
    private final String error;

    public ResolveResult(boolean success, String classpath, List<String> jarPaths, String error) {
        this.success = success;
        this.classpath = classpath;
        this.jarPaths = jarPaths;
        this.error = error;
    }

    public static ResolveResult success(String classpath, List<String> jarPaths) {
        return new ResolveResult(true, classpath, jarPaths, null);
    }

    public static ResolveResult failure(String error) {
        return new ResolveResult(false, null, null, error);
    }

    public boolean isSuccess() { return success; }
    public String getClasspath() { return classpath; }
    public List<String> getJarPaths() { return jarPaths; }
    public String getError() { return error; }
}
