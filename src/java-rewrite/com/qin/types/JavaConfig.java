package com.qin.types;

/**
 * Java 特定配置
 */
public class JavaConfig {
    /** Java version, default "17" */
    private String version = "17";
    
    /** Source directory */
    private String sourceDir;

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getSourceDir() { return sourceDir; }
    public void setSourceDir(String sourceDir) { this.sourceDir = sourceDir; }
}
