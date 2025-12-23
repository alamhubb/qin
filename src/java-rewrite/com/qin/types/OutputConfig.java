package com.qin.types;

/**
 * 输出配置
 */
public class OutputConfig {
    /** Output directory for built artifacts, default "dist" */
    private String dir = "dist";
    
    /** JAR file name, default "app.jar" */
    private String jarName = "app.jar";

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }

    public String getJarName() { return jarName; }
    public void setJarName(String jarName) { this.jarName = jarName; }
}
