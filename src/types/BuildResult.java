package com.qin.types;

/**
 * Build result for Fat Jar (Java 25 Record)
 * 
 * @param success    是否成功
 * @param outputPath 输出路径
 * @param error      错误信息
 */
public record BuildResult(
        boolean success,
        String outputPath,
        String error) {

    /**
     * 成功结果
     */
    public static BuildResult success(String outputPath) {
        return new BuildResult(true, outputPath, null);
    }

    /**
     * 失败结果
     */
    public static BuildResult failure(String error) {
        return new BuildResult(false, null, error);
    }

    /**
     * Record accessor alias for compatibility
     */
    public boolean isSuccess() {
        return success;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getError() {
        return error;
    }
}
