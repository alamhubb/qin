package com.qin.types;

/**
 * Build result for Fat Jar
 */
public class BuildResult {
    private final boolean success;
    private final String outputPath;
    private final String error;

    public BuildResult(boolean success, String outputPath, String error) {
        this.success = success;
        this.outputPath = outputPath;
        this.error = error;
    }

    public static BuildResult success(String outputPath) {
        return new BuildResult(true, outputPath, null);
    }

    public static BuildResult failure(String error) {
        return new BuildResult(false, null, error);
    }

    public boolean isSuccess() { return success; }
    public String getOutputPath() { return outputPath; }
    public String getError() { return error; }
}
