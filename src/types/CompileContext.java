package com.qin.types;

import java.util.List;

/**
 * 编译上下文 (Java 25 Record using composition)
 * 
 * @param pluginContext 插件上下文
 * @param sourceFiles   源文件列表
 * @param outputDir     输出目录
 * @param classpath     classpath字符串
 */
public record CompileContext(
        PluginContext pluginContext,
        List<String> sourceFiles,
        String outputDir,
        String classpath) {

    /**
     * Compact Constructor - ensure immutability
     */
    public CompileContext {
        sourceFiles = sourceFiles != null ? List.copyOf(sourceFiles) : List.of();
    }

    /**
     * 便捷构造器
     */
    public CompileContext(String root, QinConfig config, boolean isDev,
            List<String> sourceFiles, String outputDir, String classpath) {
        this(new PluginContext(root, config, isDev), sourceFiles, outputDir, classpath);
    }

    /**
     * 委托方法
     */
    public void log(String msg) {
        pluginContext.log(msg);
    }

    public void warn(String msg) {
        pluginContext.warn(msg);
    }

    public void error(String msg) {
        pluginContext.error(msg);
    }

    // Getters for compatibility
    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getClasspath() {
        return classpath;
    }
}
