package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * JavaScript/TypeScript 运行器插件
 * 使用 tsx + monorepo-loader 运行 JS/TS 文件
 */
public class JsPlugin implements RunnerPlugin {

    private static final Set<String> EXTENSIONS = Set.of(
            ".js", ".mjs", ".cjs",
            ".ts", ".mts", ".cts",
            ".tsx", ".jsx");

    @Override
    public String name() {
        return "JavaScript/TypeScript";
    }

    @Override
    public Set<String> supportedExtensions() {
        return EXTENSIONS;
    }

    @Override
    public void run(Path file, String[] args, Path workDir) throws Exception {
        // 扫描 workspace 并生成配置
        WorkspaceScanner scanner = new WorkspaceScanner();

        // 查找项目根目录
        Path projectRoot = scanner.findProjectRoot(workDir);

        // 调试信息
        System.out.println("[qin] 工作目录: " + workDir);
        System.out.println("[qin] 项目根目录: " + projectRoot);
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null) {
            System.out.println("[qin] VS Code 工作区: " + vscodeCwd);
        }

        Map<String, WorkspaceScanner.PackageInfo> packages = scanner.scan(workDir);
        System.out.println("[qin] 发现 " + packages.size() + " 个包");

        // 确保 loader.mjs 存在（使用项目根目录）
        Path loaderPath = ensureLoader(projectRoot);

        // 生成 monorepo-config.json（使用项目根目录）
        Path configPath = generateConfig(projectRoot, packages);

        // 构建命令 - 使用 npx 运行 tsx
        List<String> command = new ArrayList<>();

        // Windows 需要通过 cmd.exe 运行
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            command.add("cmd.exe");
            command.add("/c");
        }

        command.add("npx");
        command.add("tsx");
        command.add("--import");
        // Node.js --import 需要 file:// URL 格式
        command.add(loaderPath.toAbsolutePath().toUri().toString());
        command.add(file.toAbsolutePath().toString());
        command.addAll(Arrays.asList(args));

        // 设置环境变量传递配置路径
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        pb.directory(workDir.toFile());
        pb.environment().put("QIN_MONOREPO_CONFIG", configPath.toAbsolutePath().toString());

        System.out.println("[qin] Running: " + String.join(" ", command.subList(isWindows ? 2 : 0, command.size())));

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Process exited with code " + exitCode);
        }
    }

    /**
     * 确保 loader.mjs 和 hooks.mjs 存在于 .qin 目录
     */
    private Path ensureLoader(Path workDir) throws IOException {
        Path qinDir = workDir.resolve(".qin");
        Files.createDirectories(qinDir);

        Path loaderPath = qinDir.resolve("loader.mjs");
        Path hooksPath = qinDir.resolve("hooks.mjs");

        // 先写入 hooks，因为 loader 需要 hooks 的路径
        Files.writeString(hooksPath, getEmbeddedHooksCode());
        Files.writeString(loaderPath, getEmbeddedLoaderCode(hooksPath));

        return loaderPath;
    }

    /**
     * 生成 monorepo-config.json
     */
    private Path generateConfig(Path workDir, Map<String, WorkspaceScanner.PackageInfo> packages) throws IOException {
        Path qinDir = workDir.resolve(".qin");
        Files.createDirectories(qinDir);

        Path configPath = qinDir.resolve("monorepo-config.json");

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"packages\": {\n");

        int count = 0;
        for (Map.Entry<String, WorkspaceScanner.PackageInfo> entry : packages.entrySet()) {
            if (count > 0)
                json.append(",\n");

            String name = entry.getKey();
            WorkspaceScanner.PackageInfo info = entry.getValue();

            json.append("    \"").append(escapeJson(name)).append("\": {\n");
            json.append("      \"dir\": \"").append(escapeJson(info.dir().toString())).append("\",\n");
            json.append("      \"monorepoEntry\": ");
            if (info.monorepoEntry() != null) {
                json.append("\"").append(escapeJson(info.monorepoEntry())).append("\"");
            } else {
                json.append("null");
            }
            json.append("\n    }");

            count++;
        }

        json.append("\n  }\n");
        json.append("}\n");

        Files.writeString(configPath, json.toString());

        return configPath;
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * 内嵌的 ESM Loader 入口代码
     */
    private String getEmbeddedLoaderCode(Path hooksPath) {
        String hooksUrl = hooksPath.toAbsolutePath().toUri().toString();
        return """
                /**
                 * Qin Monorepo ESM Loader
                 * 拦截模块解析，将 workspace 包的入口重定向到源码
                 */
                import { register } from 'node:module';

                // 注册 hooks - 使用绝对 URL
                register('%s');
                """.formatted(hooksUrl);
    }

    /**
     * 内嵌的 ESM Hooks 代码
     */
    private String getEmbeddedHooksCode() {
        return """
                /**
                 * Qin Monorepo ESM Loader Hooks
                 * 拦截模块解析，将 workspace 包的入口重定向到源码
                 */
                import { readFileSync, existsSync } from 'node:fs';
                import { join } from 'node:path';
                import { pathToFileURL } from 'node:url';

                // 从环境变量读取配置路径
                const configPath = process.env.QIN_MONOREPO_CONFIG;
                let config = null;

                if (configPath && existsSync(configPath)) {
                    try {
                        const content = readFileSync(configPath, 'utf-8');
                        config = JSON.parse(content);
                    } catch (e) {
                        console.error('[qin loader] Failed to load config:', e.message);
                    }
                }

                function isMainEntryImport(specifier) {
                    if (specifier.startsWith('.') || specifier.startsWith('/')) return false;
                    if (specifier.includes(':')) return false;
                    if (specifier.startsWith('@')) {
                        return specifier.split('/').length === 2;
                    }
                    return !specifier.includes('/');
                }

                export async function resolve(specifier, context, nextResolve) {
                    if (!config || !config.packages) {
                        return nextResolve(specifier, context);
                    }

                    if (!isMainEntryImport(specifier)) {
                        return nextResolve(specifier, context);
                    }

                    const pkg = config.packages[specifier];

                    if (!pkg || !pkg.monorepoEntry) {
                        return nextResolve(specifier, context);
                    }

                    const newEntry = join(pkg.dir, pkg.monorepoEntry);
                    const newUrl = pathToFileURL(newEntry).href;

                    if (process.env.QIN_DEBUG) {
                        console.log('[qin] ' + specifier + ' -> ' + pkg.monorepoEntry);
                    }

                    return { url: newUrl, shortCircuit: true };
                }
                """;
    }
}
