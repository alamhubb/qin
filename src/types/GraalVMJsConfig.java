package com.qin.types;

import java.util.List;

/**
 * GraalVM JavaScript 配置
 */
public class GraalVMJsConfig {
    /** JavaScript 入口文件 */
    private String entry;
    
    /** 热重载配置 */
    private boolean hotReload = true;
    
    /** 额外的 Node.js 参数 */
    private List<String> nodeArgs;
    
    /** 是否启用 Java 互操作 */
    private boolean javaInterop = false;

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public boolean isHotReload() { return hotReload; }
    public void setHotReload(boolean hotReload) { this.hotReload = hotReload; }

    public List<String> getNodeArgs() { return nodeArgs; }
    public void setNodeArgs(List<String> nodeArgs) { this.nodeArgs = nodeArgs; }

    public boolean isJavaInterop() { return javaInterop; }
    public void setJavaInterop(boolean javaInterop) { this.javaInterop = javaInterop; }
}
