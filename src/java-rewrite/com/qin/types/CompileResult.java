package com.qin.types;

/**
 * Compilation result
 */
public class CompileResult {
    private final boolean success;
    private final String error;
    private final int compiledFiles;
    private final String outputDir;

    public CompileResult(boolean success, String error, int compiledFiles, String outputDir) {
        this.success = success;
        this.error = error;
        this.compiledFiles = compiledFiles;
        this.outputDir = outputDir;
    }

    public static CompileResult success(int compiledFiles, String outputDir) {
        return new CompileResult(true, null, compiledFiles, outputDir);
    }

    public static CompileResult failure(String error) {
        return new CompileResult(false, error, 0, null);
    }

    public boolean isSuccess() { return success; }
    public String getError() { return error; }
    public int getCompiledFiles() { return compiledFiles; }
    public String getOutputDir() { return outputDir; }
}
