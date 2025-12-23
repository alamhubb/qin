package com.qin.types;

import java.util.function.Consumer;

/**
 * 插件上下文
 */
public class PluginContext {
    /** 项目根目录 */
    private final String root;
    
    /** 当前配置 */
    private final QinConfig config;
    
    /** 是否开发模式 */
    private final boolean isDev;
    
    /** 日志函数 */
    private final Consumer<String> log;
    
    /** 警告函数 */
    private final Consumer<String> warn;
    
    /** 错误函数 */
    private final Consumer<String> error;

    public PluginContext(String root, QinConfig config, boolean isDev) {
        this.root = root;
        this.config = config;
        this.isDev = isDev;
        this.log = msg -> System.out.println("[qin] " + msg);
        this.warn = msg -> System.out.println("[qin] ⚠ " + msg);
        this.error = msg -> System.err.println("[qin] ✗ " + msg);
    }

    public String getRoot() { return root; }
    public QinConfig getConfig() { return config; }
    public boolean isDev() { return isDev; }
    
    public void log(String msg) { log.accept(msg); }
    public void warn(String msg) { warn.accept(msg); }
    public void error(String msg) { error.accept(msg); }
}
