package com.qin.types;

/**
 * JAR 打包结果 (Java 25 Record)
 *
 * @param success  是否成功
 * @param jarPath  JAR 文件路径
 * @param jarSize  JAR 文件大小（字节）
 * @param error    错误信息
 */
public record JarResult(
        boolean success,
        String jarPath,
        long jarSize,
        String error) {

    /**
     * 成功结果
     */
    public static JarResult success(String jarPath, long jarSize) {
        return new JarResult(true, jarPath, jarSize, null);
    }

    /**
     * 失败结果
     */
    public static JarResult failure(String error) {
        return new JarResult(false, null, 0, error);
    }

    /**
     * Record accessor aliases for compatibility
     */
    public boolean isSuccess() {
        return success;
    }

    public String getJarPath() {
        return jarPath;
    }

    public long getJarSize() {
        return jarSize;
    }

    public String getError() {
        return error;
    }
}
