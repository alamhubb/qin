package com.qin.types;

/**
 * GraalVM 配置
 */
public class GraalVMConfig {
    /** GraalVM 安装路径 */
    private String home;
    
    /** JavaScript 支持配置 */
    private GraalVMJsConfig js;

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public GraalVMJsConfig getJs() { return js; }
    public void setJs(GraalVMJsConfig js) { this.js = js; }
}
