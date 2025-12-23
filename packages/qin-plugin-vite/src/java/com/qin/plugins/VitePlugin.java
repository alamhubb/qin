package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * qin-plugin-vite
 * Vite frontend integration for Qin build tool
 * 
 * Features:
 * - Vite dev server with HMR
 * - API proxy to backend
 * - Production build
 */
public class VitePlugin implements QinPlugin {
    private final VitePluginOptions options;
    private final String cwd;
    private String clientDir;
    private Process viteProcess;

    public VitePlugin() {
        this(new VitePluginOptions());
    }

    public VitePlugin(VitePluginOptions options) {
        this.options = options;
        this.cwd = System.getProperty("user.dir");
    }

    @Override
    public String getName() {
        return "qin-plugin-vite";
    }

    @Override
    public Map<String, Object> config(Map<String, Object> config) {
        // 检测前端目录
        clientDir = options.getRoot() != null 
            ? Paths.get(cwd, options.getRoot()).toString()
            : detectClientDir();

        if (clientDir != null && Files.exists(Paths.get(clientDir))) {
            try {
                // 生成 Vite 配置
                int backendPort = config.containsKey("port") 
                    ? (Integer) config.get("port") : 8080;
                generateViteConfig(backendPort);
            } catch (IOException e) {
                System.err.println("[vite] 生成配置失败: " + e.getMessage());
            }
        }

        return config;
    }

    @Override
    public void devServer(PluginContext ctx) {
        if (clientDir == null || !Files.exists(Paths.get(clientDir))) {
            ctx.warn("No frontend directory found, skipping Vite dev server");
            return;
        }

        try {
            startViteDevServer();
        } catch (Exception e) {
            ctx.error("Failed to start Vite dev server: " + e.getMessage());
        }
    }

    @Override
    public void beforeBuild(PluginContext ctx) {
        if (clientDir == null || !Files.exists(Paths.get(clientDir))) {
            return;
        }

        ctx.log("Building frontend with Vite...");
        try {
            buildVite();
        } catch (Exception e) {
            ctx.error("Vite build failed: " + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
        if (viteProcess != null) {
            viteProcess.destroy();
            viteProcess = null;
        }
    }

    private String detectClientDir() {
        String[] candidates = {
            "src/client",
            "client",
            "web",
            "frontend"
        };

        for (String candidate : candidates) {
            Path dir = Paths.get(cwd, candidate);
            if (Files.exists(dir)) {
                // 检查是否有 index.html 或 package.json
                if (Files.exists(dir.resolve("index.html")) || 
                    Files.exists(dir.resolve("package.json"))) {
                    return dir.toString();
                }
            }
        }
        return null;
    }

    private void generateViteConfig(int backendPort) throws IOException {
        int port = options.getPort() != null ? options.getPort() : 5173;
        String outDir = options.getOutDir() != null ? options.getOutDir() : "dist/static";

        // 默认代理 /api 到后端
        Map<String, String> proxy = options.getProxy();
        if (proxy == null) {
            proxy = new HashMap<>();
            proxy.put("/api", "http://localhost:" + backendPort);
        }

        StringBuilder proxyConfig = new StringBuilder();
        for (Map.Entry<String, String> entry : proxy.entrySet()) {
            if (proxyConfig.length() > 0) proxyConfig.append(",\n");
            proxyConfig.append(String.format(
                "      \"%s\": { target: \"%s\", changeOrigin: true }",
                entry.getKey(), entry.getValue()
            ));
        }

        String config = String.format("""
            import { defineConfig } from "vite";
            
            export default defineConfig({
              root: "%s",
              server: {
                port: %d,
                proxy: {
            %s
                },
              },
              build: {
                outDir: "%s",
                emptyOutDir: true,
              },
            });
            """, 
            clientDir.replace("\\", "/"),
            port,
            proxyConfig.toString(),
            outDir
        );

        Path configPath = Paths.get(clientDir, "vite.config.js");
        Path configTsPath = Paths.get(clientDir, "vite.config.ts");

        // 只在没有配置文件时生成
        if (!Files.exists(configPath) && !Files.exists(configTsPath)) {
            Files.writeString(configPath, config);
        }
    }

    private void startViteDevServer() throws Exception {
        int port = options.getPort() != null ? options.getPort() : 5173;

        // 检查是否安装了 vite
        Path viteLocal = Paths.get(clientDir, "node_modules", ".bin", 
            isWindows() ? "vite.cmd" : "vite");
        Path viteGlobal = Paths.get(cwd, "node_modules", ".bin",
            isWindows() ? "vite.cmd" : "vite");

        if (!Files.exists(viteLocal) && !Files.exists(viteGlobal)) {
            throw new Exception(
                "未找到 vite。请先安装：\n" +
                "  npm install -D vite\n" +
                "或在 package.json 的 devDependencies 中添加 vite"
            );
        }

        // 启动 Vite
        List<String> command = new ArrayList<>();
        if (isWindows()) {
            command.add("cmd");
            command.add("/c");
            command.add("npx");
        } else {
            command.add("npx");
        }
        command.add("vite");
        command.add("--port");
        command.add(String.valueOf(port));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(clientDir));
        pb.inheritIO();
        viteProcess = pb.start();

        System.out.println("[qin-plugin-vite] Dev server started at http://localhost:" + port);
    }

    private void buildVite() throws Exception {
        String outDir = options.getOutDir() != null ? options.getOutDir() : "dist/static";

        List<String> command = new ArrayList<>();
        if (isWindows()) {
            command.add("cmd");
            command.add("/c");
            command.add("npx");
        } else {
            command.add("npx");
        }
        command.add("vite");
        command.add("build");
        command.add("--outDir");
        command.add(outDir);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(clientDir));
        pb.inheritIO();
        Process proc = pb.start();

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new Exception("Vite build failed");
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * 创建 Vite 插件
     */
    public static VitePlugin create() {
        return new VitePlugin();
    }

    public static VitePlugin create(VitePluginOptions options) {
        return new VitePlugin(options);
    }
}

/**
 * Vite 插件配置
 */
class VitePluginOptions {
    /** 前端源码目录，默认 "src/client" */
    private String root;
    
    /** 开发服务器端口，默认 5173 */
    private Integer port;
    
    /** API 代理配置 */
    private Map<String, String> proxy;
    
    /** 构建输出目录，默认 "dist/static" */
    private String outDir;

    public String getRoot() { return root; }
    public void setRoot(String root) { this.root = root; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public Map<String, String> getProxy() { return proxy; }
    public void setProxy(Map<String, String> proxy) { this.proxy = proxy; }

    public String getOutDir() { return outDir; }
    public void setOutDir(String outDir) { this.outDir = outDir; }
}
