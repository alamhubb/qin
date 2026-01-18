package com.qin.types;

import java.util.List;

/**
 * 运行上下文 (Java 25 Record using composition)
 * 
 * @param pluginContext 插件上下文
 * @param args          运行参数
 * @param classpath     classpath字符串
 */
public record RunContext(
        PluginContext pluginContext,
        List<String> args,
        String classpath) {

    /**
     * Compact Constructor - ensure immutability
     */
    public RunContext {
        args = args != null ? List.copyOf(args) : List.of();
    }

    /**
     * 便捷构造器
     */
    public RunContext(String root, QinConfig config, boolean isDev,
            List<String> args, String classpath) {
        this(new PluginContext(root, config, isDev), args, classpath);
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
    public List<String> getArgs() {
        return args;
    }

    public String getClasspath() {
        return classpath;
    }
}
