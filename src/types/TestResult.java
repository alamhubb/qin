package com.qin.types;

/**
 * Test result
 */
public class TestResult {
    private final boolean success;
    private final int testsRun;
    private final int failures;
    private final int errors;
    private final int skipped;
    private final double time;
    private final String output;
    private final String error;

    public TestResult(boolean success, int testsRun, int failures, int errors, 
                      int skipped, double time, String output, String error) {
        this.success = success;
        this.testsRun = testsRun;
        this.failures = failures;
        this.errors = errors;
        this.skipped = skipped;
        this.time = time;
        this.output = output;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public int getTestsRun() { return testsRun; }
    public int getFailures() { return failures; }
    public int getErrors() { return errors; }
    public int getSkipped() { return skipped; }
    public double getTime() { return time; }
    public String getOutput() { return output; }
    public String getError() { return error; }
}
