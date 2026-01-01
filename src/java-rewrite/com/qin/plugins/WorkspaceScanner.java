package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Workspace 扫描器
 * 扫描 monorepo 中的所有包，收集 monorepo 入口配置
 */
public class WorkspaceScanner {

    /**
     * 包信息
     */
    public record PackageInfo(
            String name,
            Path dir,
            String monorepoEntry) {
    }

    /**
     * 从指定目录开始扫描所有 workspace 包
     * 
     * @param startDir 起始目录
     * @return 包名到包信息的映射
     */
    public Map<String, PackageInfo> scan(Path startDir) {
        Map<String, PackageInfo> packages = new LinkedHashMap<>();

        // 向上查找所有 workspace root
        List<Path> workspaceRoots = findAllWorkspaceRoots(startDir);

        // 从近到远收集所有包（就近优先）
        for (Path wsRoot : workspaceRoots) {
            collectWorkspacePackages(wsRoot, packages);
        }

        return packages;
    }

    /**
     * 向上查找所有包含 workspaces 配置的 package.json
     */
    private List<Path> findAllWorkspaceRoots(Path startDir) {
        List<Path> roots = new ArrayList<>();
        Path currentDir = startDir.toAbsolutePath().normalize();

        while (currentDir != null && currentDir.getParent() != null) {
            Path pkgPath = currentDir.resolve("package.json");

            if (Files.exists(pkgPath)) {
                try {
                    String content = Files.readString(pkgPath);
                    if (hasWorkspacesField(content)) {
                        roots.add(currentDir);
                    }
                } catch (IOException e) {
                    // 忽略读取错误
                }
            }

            currentDir = currentDir.getParent();
        }

        return roots;
    }

    /**
     * 收集单个 workspace 的所有包
     */
    private void collectWorkspacePackages(Path wsRoot, Map<String, PackageInfo> packages) {
        Path rootPkgPath = wsRoot.resolve("package.json");
        if (!Files.exists(rootPkgPath))
            return;

        try {
            String content = Files.readString(rootPkgPath);
            List<String> patterns = parseWorkspacesPatterns(content);

            for (String pattern : patterns) {
                List<Path> dirs = findMatchingDirs(wsRoot, pattern);

                for (Path dir : dirs) {
                    Path pkgPath = dir.resolve("package.json");

                    if (Files.exists(pkgPath)) {
                        try {
                            String pkgContent = Files.readString(pkgPath);
                            String name = parseJsonField(pkgContent, "name");
                            String monorepo = parseJsonField(pkgContent, "monorepo");

                            if (name != null && !packages.containsKey(name)) {
                                packages.put(name, new PackageInfo(name, dir, monorepo));
                            }

                            // 如果这个包也是 workspace，递归收集
                            if (hasWorkspacesField(pkgContent)) {
                                collectWorkspacePackages(dir, packages);
                            }

                        } catch (IOException e) {
                            // 忽略
                        }
                    }
                }
            }

        } catch (IOException e) {
            // 忽略
        }
    }

    /**
     * 检查 JSON 内容是否包含 workspaces 字段
     */
    private boolean hasWorkspacesField(String json) {
        return json.contains("\"workspaces\"");
    }

    /**
     * 解析 workspaces 模式列表
     */
    private List<String> parseWorkspacesPatterns(String json) {
        List<String> patterns = new ArrayList<>();

        // 简单解析：查找 "workspaces": [...] 或 "workspaces": { "packages": [...] }
        int idx = json.indexOf("\"workspaces\"");
        if (idx == -1)
            return patterns;

        int colonIdx = json.indexOf(':', idx);
        if (colonIdx == -1)
            return patterns;

        // 找到 [ 开始
        int bracketStart = json.indexOf('[', colonIdx);
        if (bracketStart == -1) {
            // 可能是对象格式 { "packages": [...] }
            int braceStart = json.indexOf('{', colonIdx);
            if (braceStart != -1) {
                int packagesIdx = json.indexOf("\"packages\"", braceStart);
                if (packagesIdx != -1) {
                    bracketStart = json.indexOf('[', packagesIdx);
                }
            }
        }

        if (bracketStart == -1)
            return patterns;

        int bracketEnd = json.indexOf(']', bracketStart);
        if (bracketEnd == -1)
            return patterns;

        String arrayContent = json.substring(bracketStart + 1, bracketEnd);

        // 解析数组中的字符串
        int i = 0;
        while (i < arrayContent.length()) {
            int quoteStart = arrayContent.indexOf('"', i);
            if (quoteStart == -1)
                break;

            int quoteEnd = arrayContent.indexOf('"', quoteStart + 1);
            if (quoteEnd == -1)
                break;

            patterns.add(arrayContent.substring(quoteStart + 1, quoteEnd));
            i = quoteEnd + 1;
        }

        return patterns;
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

    /**
     * 查找匹配模式的目录
     */
    private List<Path> findMatchingDirs(Path root, String pattern) {
        List<Path> dirs = new ArrayList<>();

        if (pattern.endsWith("/*")) {
            // packages/* 模式
            Path baseDir = root.resolve(pattern.substring(0, pattern.length() - 2));

            if (Files.exists(baseDir) && Files.isDirectory(baseDir)) {
                try (var stream = Files.list(baseDir)) {
                    stream.filter(Files::isDirectory)
                            .filter(p -> !p.getFileName().toString().equals("node_modules"))
                            .forEach(dirs::add);
                } catch (IOException e) {
                    // 忽略
                }
            }
        } else if (pattern.endsWith("/**")) {
            // packages/** 递归模式
            Path baseDir = root.resolve(pattern.substring(0, pattern.length() - 3));
            findDirsRecursive(baseDir, dirs);
        } else {
            // 具体目录
            Path dir = root.resolve(pattern);
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                dirs.add(dir);
            }
        }

        return dirs;
    }

    /**
     * 递归查找目录
     */
    private void findDirsRecursive(Path dir, List<Path> result) {
        if (!Files.exists(dir) || !Files.isDirectory(dir))
            return;

        try (var stream = Files.list(dir)) {
            stream.filter(Files::isDirectory)
                    .filter(p -> !p.getFileName().toString().equals("node_modules"))
                    .forEach(subDir -> {
                        result.add(subDir);
                        findDirsRecursive(subDir, result);
                    });
        } catch (IOException e) {
            // 忽略
        }
    }
}
