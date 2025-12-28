package com.qin.types;

/**
 * 输出配置 (Java 25 Record)
 * 
 * @param dir     输出目录
 * @param jarName JAR 文件名
 * @param fatJar  是否构建 Fat JAR（包含所有依赖）
 */
public record OutputConfig(
        String dir,
        String jarName,
        boolean fatJar) {

    /**
     * Compact Constructor with defaults
     */
    public OutputConfig {
        dir = dir != null && !dir.isBlank() ? dir : "dist";
        jarName = jarName != null && !jarName.isBlank() ? jarName : "app.jar";
    }

    /**
     * 默认构造器
     */
    public OutputConfig() {
        this(null, null, true);
    }

    /**
     * 只指定目录
     */
    public OutputConfig(String dir) {
        this(dir, null, true);
    }
}
