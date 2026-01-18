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
        String sep = QinConstants.getClasspathSeparator();
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

        // 1.5 确保 sourceFolder 存在（修复自闭合的 content 标签）
        content = ensureSourceFolder(content);

        // 1.6 ✨ 设置模块级别的 LANGUAGE_LEVEL
        content = ensureLanguageLevel(content);

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

        // 4. ✨ 同时更新 misc.xml 的 languageLevel
        updateMiscXmlLanguageLevel();

        Files.writeString(imlFile, content);
    }

    /**
     * 确保 .iml 文件中有 LANGUAGE_LEVEL 属性
     */
    private String ensureLanguageLevel(String imlContent) {
        // 从 qin.config.json 读取 Java 版本
        String version = readJavaVersionFromConfig();
        String languageLevel = "JDK_" + version;

        // 检查是否已有 LANGUAGE_LEVEL 属性
        if (imlContent.contains("LANGUAGE_LEVEL=")) {
            // 更新现有属性
            imlContent = imlContent.replaceAll(
                    "LANGUAGE_LEVEL=\"[^\"]*\"",
                    "LANGUAGE_LEVEL=\"" + languageLevel + "\"");
        } else if (imlContent.contains("<component name=\"NewModuleRootManager\"")) {
            // 添加 LANGUAGE_LEVEL 属性
            imlContent = imlContent.replace(
                    "<component name=\"NewModuleRootManager\"",
                    "<component name=\"NewModuleRootManager\" LANGUAGE_LEVEL=\"" + languageLevel + "\"");
        }

        System.out.println("  [IDEA] 模块语言级别: " + languageLevel);
        return imlContent;
    }

    /**
     * 更新 misc.xml 中的 languageLevel
     */
    private void updateMiscXmlLanguageLevel() {
        try {
            Path miscXml = Paths.get(projectRoot, ".idea", "misc.xml");
            if (!Files.exists(miscXml)) {
                return;
            }

            String version = readJavaVersionFromConfig();
            String content = Files.readString(miscXml);
            String languageLevelAttr = "languageLevel=\"JDK_" + version + "\"";

            if (content.contains("languageLevel=")) {
                content = content.replaceAll("languageLevel=\"[^\"]*\"", languageLevelAttr);
            } else if (content.contains("<component name=\"ProjectRootManager\"")) {
                content = content.replace(
                        "<component name=\"ProjectRootManager\"",
                        "<component name=\"ProjectRootManager\" " + languageLevelAttr);
            }

            Files.writeString(miscXml, content);
            System.out.println("  [IDEA] 项目语言级别: JDK_" + version);
        } catch (IOException e) {
            System.err.println("  [IDEA] 更新 misc.xml 失败: " + e.getMessage());
        }
    }

    /**
     * 从 qin.config.json 读取 Java 版本
     */
    private String readJavaVersionFromConfig() {
        try {
            Path configPath = Paths.get(projectRoot, "qin.config.json");
            if (!Files.exists(configPath)) {
                return "21"; // 默认值
            }

            String json = Files.readString(configPath);

            // 简单 JSON 解析：查找 java.target 或 java.version
            // 优先使用 target
            String target = extractJsonValue(json, "target");
            if (target != null && !target.isBlank()) {
                return target;
            }

            // 其次使用 version
            String version = extractJsonValue(json, "version");
            if (version != null && !version.isBlank()) {
                return version;
            }

            return "21";
        } catch (IOException e) {
            return "21";
        }
    }

    /**
     * 从 JSON 中提取值（简单实现）
     */
    private String extractJsonValue(String json, String key) {
        // 查找 "java" 对象中的键
        int javaIdx = json.indexOf("\"java\"");
        if (javaIdx < 0)
            return null;

        int keyIdx = json.indexOf("\"" + key + "\"", javaIdx);
        if (keyIdx < 0)
            return null;

        int colonIdx = json.indexOf(":", keyIdx);
        if (colonIdx < 0)
            return null;

        int quoteStart = json.indexOf("\"", colonIdx);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        if (quoteStart < 0 || quoteEnd < 0)
            return null;

        return json.substring(quoteStart + 1, quoteEnd);
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
     * 确保 sourceFolder 存在
     * 修复自闭合的 content 标签，例如 <content url="..." /> 改为包含 sourceFolder 的完整形式
     */
    private String ensureSourceFolder(String imlContent) {
        // 检查是否已经有 sourceFolder
        if (imlContent.contains("<sourceFolder")) {
            return imlContent;
        }

        // 检测源代码目录
        String sourceDir = detectSourceDir();
        if (sourceDir == null) {
            return imlContent;
        }

        // 检查是否是自闭合的 content 标签
        // 匹配 <content url="file://$MODULE_DIR$" /> 或类似的自闭合形式
        java.util.regex.Pattern selfClosingPattern = java.util.regex.Pattern.compile(
                "<content\\s+url=\"[^\"]*\"\\s*/>");
        java.util.regex.Matcher matcher = selfClosingPattern.matcher(imlContent);

        if (matcher.find()) {
            // 找到自闭合的 content 标签，需要替换为完整形式
            String originalTag = matcher.group();
            // 提取 url 属性
            int urlStart = originalTag.indexOf("url=\"") + 5;
            int urlEnd = originalTag.indexOf("\"", urlStart);
            String url = originalTag.substring(urlStart, urlEnd);

            // 构建完整的 content 标签
            StringBuilder newContent = new StringBuilder();
            newContent.append("<content url=\"").append(url).append("\">\n");
            newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                    .append(sourceDir).append("\" isTestSource=\"false\" />\n");

            // 检查是否有测试目录
            String testDir = detectTestDir();
            if (testDir != null) {
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(testDir).append("\" isTestSource=\"true\" />\n");
            }

            // 添加排除目录（.iml 中常用的排除目录）
            String[] imlExcludedDirs = { "build", ".qin", "out", "libs", "node_modules", "dist", "target" };
            for (String excludeDir : imlExcludedDirs) {
                newContent.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                        .append(excludeDir).append("\" />\n");
            }

            newContent.append("    </content>");

            imlContent = imlContent.replace(originalTag, newContent.toString());
        }

        return imlContent;
    }

    /**
     * 检测源代码目录
     */
    private String detectSourceDir() {
        // 优先检测标准 Maven 结构
        Path mavenSrc = Paths.get(projectRoot, QinConstants.DEFAULT_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return QinConstants.DEFAULT_SOURCE_DIR;
        }
        // 其次检测简单结构
        Path simpleSrc = Paths.get(projectRoot, "src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        return null;
    }

    /**
     * 检测测试目录
     */
    private String detectTestDir() {
        Path testDir = Paths.get(projectRoot, "src/test/java");
        if (Files.exists(testDir)) {
            return "src/test/java";
        }
        return null;
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
