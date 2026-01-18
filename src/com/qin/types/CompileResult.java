package com.qin.types;

/**
 * Compilation result (Java 25 Record)
 * 
 * @param success       是否成功
 * @param error         错误信息
 * @param compiledFiles 编译的文件数
 * @param outputDir     输出目录
 */
public record CompileResult(
        boolean success,
        String error,
        int compiledFiles,
        String outputDir) {

    /**
     * 成功结果
     */
    public static CompileResult success(int compiledFiles, String outputDir) {
        return new CompileResult(true, null, compiledFiles, outputDir);
    }

    /**
     * 失败结果
     */
    public static CompileResult failure(String error) {
        return new CompileResult(false, error, 0, null);
    }

    /**
     * Record accessor aliases for compatibility
     */
    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public int getCompiledFiles() {
        return compiledFiles;
    }

    public String getOutputDir() {
        return outputDir;
    }
}
