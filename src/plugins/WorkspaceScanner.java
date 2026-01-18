package com.qin.plugins;

import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Workspace 扫描器（增强版）
 * 
 * 改进：
 * 1. 不依赖 npm workspaces 配置，直接递归扫描所有包含 package.json 的目录
 * 2. 默认 monorepoEntry 为 "./src/index.ts"（如果未配置）
 * 3. 自动查找项目根目录（通过 .git, qin.config.json, package.json 等标志）
 */
public class WorkspaceScanner {

    // 默认的源码入口
    private static final String DEFAULT_MONOREPO_ENTRY = "./src/index.ts";

    /**
     * 包信息
     */
    public record PackageInfo(
            String name,
            Path dir,
            String monorepoEntry) {
    }

    /**
     * 从指定目录开始扫描所有包
     * 
     * @param startDir 起始目录（通常是命令执行的目录）
     * @return 包名到包信息的映射
     */
    public Map<String, PackageInfo> scan(Path startDir) {
        Map<String, PackageInfo> packages = new LinkedHashMap<>();

        // 1. 查找项目根目录
        Path projectRoot = findProjectRoot(startDir);

        // 2. 从项目根目录递归扫描所有包
        scanPackagesRecursive(projectRoot, packages);

        return packages;
    }

    /**
     * 查找项目根目录
     * 优先级：
     * 1. IDE 环境变量（VSCODE_CWD, IDEA_INITIAL_DIRECTORY）
     * 2. 向上查找，取最上层的 .vscode / .idea / qin.config.json / package.json
     */
    public Path findProjectRoot(Path startDir) {
        // 1. 优先使用 IDE 环境变量
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null && !vscodeCwd.isEmpty()) {
            Path vscodePath = Path.of(vscodeCwd);
            if (Files.exists(vscodePath)) {
                return vscodePath;
            }
        }

        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir);
            if (Files.exists(ideaPath)) {
                return ideaPath;
            }
        }

        // 2. 向上查找，记录所有匹配，最后取最上层的
        Path current = startDir.toAbsolutePath().normalize();
        Path topMostMatch = null; // 最上层的匹配（路径最短）

        while (current != null && current.getParent() != null) {
            // 检查是否是项目标志
            final Path finalCurrent = current;
            boolean isProjectRoot = QinConstants.WORKSPACE_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker)));

            if (isProjectRoot) {
                topMostMatch = current; // 继续向上找，取最上层的
            }

            current = current.getParent();
        }

        // 返回最上层的匹配
        if (topMostMatch != null) {
            return topMostMatch;
        }

        // 都找不到，返回起始目录
        return startDir.toAbsolutePath().normalize();
    }

    /**
     * 递归扫描目录，收集所有包含 package.json 且有 name 字段的包
     */
    private void scanPackagesRecursive(Path dir, Map<String, PackageInfo> packages) {
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return;
        }

        Path pkgPath = dir.resolve(QinConstants.PACKAGE_JSON);

        // 如果当前目录有 package.json，检查是否是一个包
        if (Files.exists(pkgPath)) {
            try {
                String content = Files.readString(pkgPath);
                String name = parseJsonField(content, "name");

                if (name != null && !packages.containsKey(name)) {
                    // 获取 monorepo 入口，如果没有配置则使用默认值
                    String monorepo = parseJsonField(content, "monorepo");
                    String entry = (monorepo != null) ? monorepo : DEFAULT_MONOREPO_ENTRY;

                    // 只有当 src/index.ts 存在时才添加（或者明确配置了 monorepo）
                    Path entryPath = dir.resolve(entry.replace("./", ""));
                    if (monorepo != null || Files.exists(entryPath)) {
                        packages.put(name, new PackageInfo(name, dir, entry));
                    }
                }
            } catch (IOException e) {
                // 忽略读取错误
            }
        }

        // 递归扫描子目录
        try (var stream = Files.list(dir)) {
            stream.filter(Files::isDirectory)
                    .filter(p -> !QinConstants.EXCLUDED_DIRS.contains(p.getFileName().toString()))
                    .forEach(subDir -> scanPackagesRecursive(subDir, packages));
        } catch (IOException e) {
            // 忽略
        }
    }

    /**
     * 解析 JSON 字段值
     */
    private String parseJsonField(String json, String field) {
        String search = "\"" + field + "\"";
        int idx = json.indexOf(search);
        if (idx == -1)
            return null;

        int colonIdx = json.indexOf(':', idx);
        if (colonIdx == -1)
            return null;

        // 跳过空白
        int valueStart = colonIdx + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart >= json.length())
            return null;

        char c = json.charAt(valueStart);
        if (c == '"') {
            // 字符串值
            int valueEnd = json.indexOf('"', valueStart + 1);
            if (valueEnd == -1)
                return null;
            return json.substring(valueStart + 1, valueEnd);
        }

        return null;
    }
}
