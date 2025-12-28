package com.qin.types;

/**
 * 测试上下文 (Java 25 Record using composition)
 * 
 * @param pluginContext 插件上下文
 * @param filter        测试过滤器
 * @param verbose       是否详细输出
 */
public record TestContext(
        PluginContext pluginContext,
        String filter,
        boolean verbose) {

    /**
     * 便捷构造器
     */
    public TestContext(String root, QinConfig config, boolean isDev,
            String filter, boolean verbose) {
        this(new PluginContext(root, config, isDev), filter, verbose);
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
    public String getFilter() {
        return filter;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
