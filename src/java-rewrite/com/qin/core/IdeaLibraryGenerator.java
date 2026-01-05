package com.qin.core;

import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * IDEA 库配置生成器
 * 生成 .idea/libraries/*.xml 文件，让 IDEA 识别项目依赖
 */
public class IdeaLibraryGenerator {
    private final String projectRoot;
    private final List<String> generatedLibraryNames = new ArrayList<>();

    public IdeaLibraryGenerator(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    /**
     * 生成 IDEA 库配置文件
     * 
     * @param classpath 分号分隔的 jar 路径列表
     * @return 生成的库数量
     */
    public int generateLibraryConfigs(String classpath) throws IOException {
        if (classpath == null || classpath.isEmpty()) {
            return 0;
        }

        // 创建 .idea/libraries 目录
        Path librariesDir = Paths.get(projectRoot, ".idea", "libraries");
        Files.createDirectories(librariesDir);

        // 解析 classpath
        String sep = System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";
        String[] jarPaths = classpath.split(sep);

        generatedLibraryNames.clear();
        int count = 0;
        for (String jarPath : jarPaths) {
            if (jarPath.isEmpty() || !jarPath.endsWith(".jar")) {
                continue;
            }

            // 从路径中提取库名
            LibraryInfo info = extractLibraryInfo(jarPath);
            if (info == null) {
                continue;
            }

            // 生成 XML 配置文件
            String xmlContent = generateLibraryXml(info.name, jarPath);
            String safeFileName = info.name.replace(":", "_")
                    .replace("@", "_")
                    .replace(".", "_") + ".xml";
            Path xmlPath = librariesDir.resolve(safeFileName);
            Files.writeString(xmlPath, xmlContent);

            generatedLibraryNames.add(info.name);
            count++;
        }

        // 更新模块配置，添加库引用
        updateModuleConfig();

        return count;
    }

    /**
     * 更新模块配置文件（.iml），添加库引用并配置编译输出路径
     */
    private void updateModuleConfig() throws IOException {
        // 查找 .iml 文件
        Path imlFile = findImlFile();
        if (imlFile == null) {
            return;
        }

        String content = Files.readString(imlFile);

        // 1. 配置编译输出路径为 build/classes（与 qin 一致）
        content = configureOutputPath(content);

        // 2. 移除所有旧的 Qin 库引用（可能有多个）
        while (content.contains("<!-- Qin Libraries -->")) {
            int startIdx = content.indexOf("<!-- Qin Libraries -->");
            int endIdx = content.indexOf("<!-- End Qin Libraries -->", startIdx);
            if (endIdx != -1) {
                endIdx += "<!-- End Qin Libraries -->".length();
                // 删除这一段（包括前后的空白）
                String before = content.substring(0, startIdx).replaceAll("\\s+$", "");
                String after = content.substring(endIdx).replaceAll("^\\s+", "");
                content = before + "\n" + after;
            } else {
                break; // 没有找到结束标记，跳出
            }
        }

        // 3. 构建新的库引用
        if (!generatedLibraryNames.isEmpty()) {
            StringBuilder libraryEntries = new StringBuilder();
            libraryEntries.append("    <!-- Qin Libraries -->\n");
            for (String libName : generatedLibraryNames) {
                libraryEntries.append("    <orderEntry type=\"library\" name=\"")
                        .append(libName)
                        .append("\" level=\"project\" />\n");
            }
            libraryEntries.append("    <!-- End Qin Libraries -->\n");

            // 在 </component> 之前插入库引用（使用正则匹配任意缩进）
            String insertion = libraryEntries.toString();
            content = content.replaceFirst(
                    "(\\s*)</component>",
                    insertion + "$1</component>");
        }

        Files.writeString(imlFile, content);
    }

    /**
     * 配置编译输出路径为 build/classes
     */
    private String configureOutputPath(String imlContent) {
        // 检查是否使用了继承的编译输出路径
        if (imlContent.contains("inherit-compiler-output=\"true\"")) {
            // 替换为自定义输出路径
            imlContent = imlContent.replace(
                    "inherit-compiler-output=\"true\"",
                    "inherit-compiler-output=\"false\"");

            // 在 <exclude-output /> 后添加输出路径配置
            String outputConfig = "\n    <output url=\"file://$MODULE_DIR$/" + QinConstants.BUILD_CLASSES_DIR + "\" />"
                    +
                    "\n    <output-test url=\"file://$MODULE_DIR$/build/test-classes\" />";
            imlContent = imlContent.replace(
                    "<exclude-output />",
                    "<exclude-output />" + outputConfig);
        } else if (imlContent.contains("inherit-compiler-output=\"false\"")) {
            // 已经使用自定义输出路径，检查是否需要更新
            if (!imlContent.contains("$MODULE_DIR$/" + QinConstants.BUILD_CLASSES_DIR)) {
                // 移除旧的 output 配置
                imlContent = imlContent.replaceAll(
                        "\\s*<output url=\"[^\"]+\" />\\s*",
                        "\n");
                imlContent = imlContent.replaceAll(
                        "\\s*<output-test url=\"[^\"]+\" />\\s*",
                        "\n");

                // 添加新的输出路径配置
                String outputConfig = "\n    <output url=\"file://$MODULE_DIR$/" + QinConstants.BUILD_CLASSES_DIR
                        + "\" />" +
                        "\n    <output-test url=\"file://$MODULE_DIR$/build/test-classes\" />";
                imlContent = imlContent.replace(
                        "<exclude-output />",
                        "<exclude-output />" + outputConfig);
            }
        }

        return imlContent;
    }

    /**
     * 查找项目的 .iml 文件
     */
    private Path findImlFile() throws IOException {
        // 1. 首先在项目根目录查找
        try (var stream = Files.list(Paths.get(projectRoot))) {
            Optional<Path> iml = stream
                    .filter(p -> p.toString().endsWith(".iml"))
                    .findFirst();
            if (iml.isPresent()) {
                return iml.get();
            }
        }

        // 2. 在 .idea 目录查找
        Path ideaDir = Paths.get(projectRoot, ".idea");
        if (Files.exists(ideaDir)) {
            try (var stream = Files.list(ideaDir)) {
                Optional<Path> iml = stream
                        .filter(p -> p.toString().endsWith(".iml"))
                        .findFirst();
                if (iml.isPresent()) {
                    return iml.get();
                }
            }
        }

        return null;
    }

    /**
     * 清理旧的库配置文件
     */
    public void cleanLibraryConfigs() throws IOException {
        Path librariesDir = Paths.get(projectRoot, ".idea", "libraries");
        if (!Files.exists(librariesDir)) {
            return;
        }

        try (var stream = Files.list(librariesDir)) {
            stream.filter(p -> p.toString().endsWith(".xml"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // 忽略删除失败
                        }
                    });
        }
    }

    /**
     * 从 jar 路径提取库信息
     */
    private LibraryInfo extractLibraryInfo(String jarPath) {
        String normalized = jarPath.replace("\\", "/");

        // 尝试从路径中提取 groupId@artifactId-version 格式
        // 例如：.qin/libs/com.google.code.gson@gson/com.google.code.gson@gson-2.10.1/xxx.jar
        String[] parts = normalized.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];
            if (part.contains("@") && part.contains("-")) {
                // 格式：com.google.code.gson@gson-2.10.1
                int dashIdx = part.lastIndexOf("-");
                if (dashIdx > 0) {
                    String coordinate = part.substring(0, dashIdx);
                    String version = part.substring(dashIdx + 1);
                    return new LibraryInfo(coordinate, version);
                }
            }
        }

        // fallback：使用文件名
        String fileName = Paths.get(jarPath).getFileName().toString();
        String name = fileName.replace(".jar", "");
        return new LibraryInfo(name, "");
    }

    /**
     * 生成 IDEA 库 XML 配置
     */
    private String generateLibraryXml(String libraryName, String jarPath) {
        // 将路径转换为 jar:// URL 格式
        String normalizedPath = jarPath.replace("\\", "/");

        // Windows 路径如 C:/Users/... 不需要前导 /
        // Unix 路径如 /home/... 已经有前导 /
        // IDEA 的 jar:// 协议格式：jar://路径!/

        // IDEA 使用 jar:// 协议和 !/ 后缀
        String jarUrl = "jar://" + normalizedPath + "!/";

        return """
                <component name="libraryTable">
                  <library name="%s">
                    <CLASSES>
                      <root url="%s" />
                    </CLASSES>
                    <JAVADOC />
                    <SOURCES />
                  </library>
                </component>
                """.formatted(libraryName, jarUrl);
    }

    /**
     * 库信息
     */
    private static class LibraryInfo {
        final String name;
        final String version;

        LibraryInfo(String name, String version) {
            this.name = name;
            this.version = version;
        }
    }
}
