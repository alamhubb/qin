package com.qin.types;

/**
 * Java 特定配置 (Java 25 Record)
 * 
 * @param version   Java 主版本（默认 "25"），source 和 target 的默认值
 * @param source    源码语法版本（默认使用 version）
 * @param target    编译目标版本（默认使用 version）
 * @param sourceDir 源码目录
 * @param testDir   测试目录
 * @param outputDir 输出目录
 * @param encoding  编码（默认 UTF-8）
 */
public record JavaConfig(
        String version,
        String source,
        String target,
        String sourceDir,
        String testDir,
        String outputDir,
        String encoding) {

    /**
     * Compact Constructor with defaults
     */
    public JavaConfig {
        version = version != null && !version.isBlank() ? version : "25";
        // source 和 target 默认使用 version
        source = source != null && !source.isBlank() ? source : version;
        target = target != null && !target.isBlank() ? target : version;
        sourceDir = sourceDir != null && !sourceDir.isBlank() ? sourceDir : "src/main/java";
        testDir = testDir != null && !testDir.isBlank() ? testDir : "src/test/java";
        outputDir = outputDir != null && !outputDir.isBlank() ? outputDir : "build/classes";
        encoding = encoding != null && !encoding.isBlank() ? encoding : "UTF-8";
    }

    /**
     * 默认构造器
     */
    public JavaConfig() {
        this(null, null, null, null, null, null, null);
    }

    /**
     * 只指定版本（source 和 target 都用这个版本）
     */
    public JavaConfig(String version) {
        this(version, null, null, null, null, null, null);
    }

    /**
     * 获取有效的 source 版本
     */
    public String getEffectiveSource() {
        return source != null ? source : version;
    }

    /**
     * 获取有效的 target 版本
     */
    public String getEffectiveTarget() {
        return target != null ? target : version;
    }

    @Override
    public String toString() {
        return String.format("JavaConfig[version=%s, source=%s, target=%s, sourceDir=%s]",
                version, source, target, sourceDir);
    }
}
