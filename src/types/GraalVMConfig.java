package com.qin.types;

/**
 * GraalVM 配置 (Java 25 Record)
 * 
 * @param home GraalVM 安装路径
 * @param js   JavaScript 支持配置
 */
public record GraalVMConfig(
        String home,
        GraalVMJsConfig js) {

    /**
     * 默认构造器
     */
    public GraalVMConfig() {
        this(null, null);
    }

    /**
     * 只指定路径
     */
    public GraalVMConfig(String home) {
        this(home, null);
    }
}
