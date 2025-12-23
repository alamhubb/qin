package com.qin.types;

/**
 * 前端配置
 */
public class FrontendConfig {
    /** 前端源码目录 */
    private String srcDir;
    
    /** 构建输出目录，默认 "dist/static" */
    private String outDir = "dist/static";
    
    /** Vite 开发服务器端口，默认 5173 */
    private int devPort = 5173;

    public String getSrcDir() { return srcDir; }
    public void setSrcDir(String srcDir) { this.srcDir = srcDir; }

    public String getOutDir() { return outDir; }
    public void setOutDir(String outDir) { this.outDir = outDir; }

    public int getDevPort() { return devPort; }
    public void setDevPort(int devPort) { this.devPort = devPort; }
}
