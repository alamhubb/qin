package com.qin.types;

/**
 * 前端配置 (Java 25 Record)
 * 
 * @param srcDir  前端源码目录
 * @param outDir  构建输出目录
 * @param devPort Vite 开发服务器端口
 */
public record FrontendConfig(
        String srcDir,
        String outDir,
        int devPort) {

    /**
     * Compact Constructor with defaults
     */
    public FrontendConfig {
        srcDir = srcDir != null && !srcDir.isBlank() ? srcDir : "frontend";
        outDir = outDir != null && !outDir.isBlank() ? outDir : "dist/static";
        devPort = devPort > 0 ? devPort : 5173;
    }

    /**
     * 默认构造器
     */
    public FrontendConfig() {
        this(null, null, 0);
    }
}
