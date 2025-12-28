package com.qin.types;

import java.util.List;

/**
 * Dependency resolution result (Java 25 Record)
 * 
 * @param success   是否成功
 * @param classpath classpath 字符串
 * @param jarPaths  JAR 文件路径列表
 * @param error     错误信息
 */
public record ResolveResult(
        boolean success,
        String classpath,
        List<String> jarPaths,
        String error) {

    /**
     * Compact Constructor - ensure immutability
     */
    public ResolveResult {
        jarPaths = jarPaths != null ? List.copyOf(jarPaths) : null;
    }

    /**
     * 成功结果
     */
    public static ResolveResult success(String classpath, List<String> jarPaths) {
        return new ResolveResult(true, classpath, jarPaths, null);
    }

    /**
     * 失败结果
     */
    public static ResolveResult failure(String error) {
        return new ResolveResult(false, null, null, error);
    }

    /**
     * Record accessor aliases for compatibility
     */
    public boolean isSuccess() {
        return success;
    }

    public String getClasspath() {
        return classpath;
    }

    public List<String> getJarPaths() {
        return jarPaths;
    }

    public String getError() {
        return error;
    }
}
