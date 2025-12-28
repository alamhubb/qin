package com.qin.types;

/**
 * 构建上下文 (Java 25 Record using composition)
 * 
 * @param pluginContext 插件上下文
 * @param outputDir     输出目录
 * @param outputName    输出文件名
 */
public record BuildContext(
        PluginContext pluginContext,
        String outputDir,
        String outputName) {

    /**
     * 便捷构造器
     */
    public BuildContext(String root, QinConfig config, boolean isDev,
            String outputDir, String outputName) {
        this(new PluginContext(root, config, isDev), outputDir, outputName);
    }

    /**
     * 委托方法 - 方便访问
     */
    public String getRoot() {
        return pluginContext.root();
    }

    public QinConfig getConfig() {
        return pluginContext.config();
    }

    public boolean isDev() {
        return pluginContext.isDev();
    }

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
    public String getOutputDir() {
        return outputDir;
    }

    public String getOutputName() {
        return outputName;
    }
}
