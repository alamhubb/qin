package com.qin.types;

import java.util.Map;

/**
 * 前端配置 (Java 25 Record) - 内置 Vite 支持
 * 
 * @param root   前端源码目录
 * @param port   前端开发服务器端口
 * @param proxy  API 代理配置
 * @param outDir 构建输出目录
 */
public record ClientConfig(
        String root,
        int port,
        Map<String, String> proxy,
        String outDir) {

    /**
     * Compact Constructor with defaults
     */
    public ClientConfig {
        root = root != null && !root.isBlank() ? root : "src/client";
        port = port > 0 ? port : 5173;
        proxy = proxy != null ? Map.copyOf(proxy) : Map.of();
        outDir = outDir != null && !outDir.isBlank() ? outDir : "dist/static";
    }

    /**
     * 默认构造器
     */
    public ClientConfig() {
        this(null, 0, null, null);
    }

    /**
     * 只指定根目录
     */
    public ClientConfig(String root) {
        this(root, 0, null, null);
    }
}
