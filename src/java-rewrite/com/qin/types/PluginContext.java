package com.qin.types;

import java.util.function.Consumer;

/**
 * 插件上下文 (Java 25 Record)
 * 
 * @param root         项目根目录
 * @param config       当前配置
 * @param isDev        是否开发模式
 * @param logHandler   日志处理器
 * @param warnHandler  警告处理器
 * @param errorHandler 错误处理器
 */
public record PluginContext(
        String root,
        QinConfig config,
        boolean isDev,
        Consumer<String> logHandler,
        Consumer<String> warnHandler,
        Consumer<String> errorHandler) {

    /**
     * Compact Constructor with default handlers
     */
    public PluginContext {
        if (logHandler == null) {
            logHandler = msg -> System.out.println("[qin] " + msg);
        }
        if (warnHandler == null) {
            warnHandler = msg -> System.out.println("[qin] ⚠ " + msg);
        }
        if (errorHandler == null) {
            errorHandler = msg -> System.err.println("[qin] ✗ " + msg);
        }
    }

    /**
     * 简化构造器 - 使用默认处理器
     */
    public PluginContext(String root, QinConfig config, boolean isDev) {
        this(root, config, isDev, null, null, null);
    }

    /**
     * 便捷方法
     */
    public void log(String msg) {
        logHandler.accept(msg);
    }

    public void warn(String msg) {
        warnHandler.accept(msg);
    }

    public void error(String msg) {
        errorHandler.accept(msg);
    }

    /**
     * Compatibility getters
     */
    public String getRoot() {
        return root;
    }

    public QinConfig getConfig() {
        return config;
    }
}
