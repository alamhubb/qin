package com.qin.types;

import java.util.Map;

/**
 * 前端配置（内置 Vite 支持）
 */
public class ClientConfig {
    /** 前端源码目录，默认 "src/client" */
    private String root = "src/client";
    
    /** 前端开发服务器端口，默认 5173 */
    private int port = 5173;
    
    /** API 代理配置 */
    private Map<String, String> proxy;
    
    /** 构建输出目录，默认 "dist/static" */
    private String outDir = "dist/static";

    public String getRoot() { return root; }
    public void setRoot(String root) { this.root = root; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public Map<String, String> getProxy() { return proxy; }
    public void setProxy(Map<String, String> proxy) { this.proxy = proxy; }

    public String getOutDir() { return outDir; }
    public void setOutDir(String outDir) { this.outDir = outDir; }
}
