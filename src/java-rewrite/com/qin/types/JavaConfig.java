package com.qin.types;

/**
 * Java 特定配置 (Java 25 Record)
 * 
 * @param version   Java 版本（默认 "25"）
 * @param sourceDir 源码目录
 * @param testDir   测试目录
 * @param outputDir 输出目录
 * @param encoding  编码（默认 UTF-8）
 */
public record JavaConfig(
        String version,
        String sourceDir,
        String testDir,
        String outputDir,
        String encoding) {

    /**
     * Compact Constructor with defaults
     */
    public JavaConfig {
        version = version != null && !version.isBlank() ? version : "25";
        sourceDir = sourceDir != null && !sourceDir.isBlank() ? sourceDir : "src/main/java";
        testDir = testDir != null && !testDir.isBlank() ? testDir : "src/test/java";
        outputDir = outputDir != null && !outputDir.isBlank() ? outputDir : "target/classes";
        encoding = encoding != null && !encoding.isBlank() ? encoding : "UTF-8";
    }

    /**
     * 默认构造器
     */
    public JavaConfig() {
        this(null, null, null, null, null);
    }

    /**
     * 只指定版本
     */
    public JavaConfig(String version) {
        this(version, null, null, null, null);
    }

    @Override
    public String toString() {
        return String.format("JavaConfig[version=%s, sourceDir=%s]", version, sourceDir);
    }
}
