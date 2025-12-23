package com.qin.plugins;

import java.util.*;

/**
 * 插件系统接口定义
 */

/**
 * Qin 插件接口
 */
interface QinPlugin {
    String getName();
    default LanguageSupport getLanguage() { return null; }
    default List<QinPlugin> getPlugins() { return null; }
    default Map<String, Object> config(Map<String, Object> config) { return config; }
    default void configResolved(Map<String, Object> config) {}
    default void beforeCompile(PluginContext ctx) {}
    default void afterCompile(PluginContext ctx) {}
    default void beforeRun(PluginContext ctx) {}
    default void afterRun(PluginContext ctx) {}
    default void beforeBuild(PluginContext ctx) {}
    default void afterBuild(PluginContext ctx) {}
    default void devServer(PluginContext ctx) {}
    default void cleanup() {}
}

/**
 * 语言支持接口
 */
interface LanguageSupport {
    String getName();
    List<String> getExtensions();
    default CompileResult compile(CompileContext ctx) { return null; }
    default void run(RunContext ctx) throws Exception {}
    default TestResult test(TestContext ctx) { return null; }
    default BuildResult build(BuildContext ctx) { return null; }
}

/**
 * 插件上下文
 */
class PluginContext {
    private final String root;
    private final Map<String, Object> config;
    private final boolean isDev;

    public PluginContext(String root, Map<String, Object> config, boolean isDev) {
        this.root = root;
        this.config = config;
        this.isDev = isDev;
    }

    public String getRoot() { return root; }
    public Map<String, Object> getConfig() { return config; }
    public boolean isDev() { return isDev; }

    public void log(String msg) { System.out.println("[qin] " + msg); }
    public void warn(String msg) { System.out.println("[qin] ⚠ " + msg); }
    public void error(String msg) { System.err.println("[qin] ✗ " + msg); }
}

/**
 * 编译上下文
 */
class CompileContext extends PluginContext {
    private final List<String> sourceFiles;
    private final String outputDir;
    private final String classpath;

    public CompileContext(String root, Map<String, Object> config, boolean isDev,
                          List<String> sourceFiles, String outputDir, String classpath) {
        super(root, config, isDev);
        this.sourceFiles = sourceFiles;
        this.outputDir = outputDir;
        this.classpath = classpath;
    }

    public List<String> getSourceFiles() { return sourceFiles; }
    public String getOutputDir() { return outputDir; }
    public String getClasspath() { return classpath; }
}

/**
 * 运行上下文
 */
class RunContext extends PluginContext {
    private final List<String> args;
    private final String classpath;

    public RunContext(String root, Map<String, Object> config, boolean isDev,
                      List<String> args, String classpath) {
        super(root, config, isDev);
        this.args = args;
        this.classpath = classpath;
    }

    public List<String> getArgs() { return args; }
    public String getClasspath() { return classpath; }
}

/**
 * 测试上下文
 */
class TestContext extends PluginContext {
    private final String filter;
    private final boolean verbose;

    public TestContext(String root, Map<String, Object> config, boolean isDev,
                       String filter, boolean verbose) {
        super(root, config, isDev);
        this.filter = filter;
        this.verbose = verbose;
    }

    public String getFilter() { return filter; }
    public boolean isVerbose() { return verbose; }
}

/**
 * 构建上下文
 */
class BuildContext extends PluginContext {
    private final String outputDir;
    private final String outputName;

    public BuildContext(String root, Map<String, Object> config, boolean isDev,
                        String outputDir, String outputName) {
        super(root, config, isDev);
        this.outputDir = outputDir;
        this.outputName = outputName;
    }

    public String getOutputDir() { return outputDir; }
    public String getOutputName() { return outputName; }
}

/**
 * 编译结果
 */
class CompileResult {
    private final boolean success;
    private final String error;
    private final int compiledFiles;
    private final String outputDir;

    private CompileResult(boolean success, String error, int compiledFiles, String outputDir) {
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

/**
 * 构建结果
 */
class BuildResult {
    private final boolean success;
    private final String outputPath;
    private final String error;

    private BuildResult(boolean success, String outputPath, String error) {
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

/**
 * 测试结果
 */
class TestResult {
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
