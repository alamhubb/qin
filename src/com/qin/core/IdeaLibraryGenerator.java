package com.qin.core;

import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * IDEA 搴撻厤缃敓鎴愬櫒
 * 鐢熸垚 .idea/libraries/*.xml 鏂囦欢锛岃 IDEA 璇嗗埆椤圭洰渚濊禆
 */
public class IdeaLibraryGenerator {
    private final String projectRoot;
    private final List<String> generatedLibraryNames = new ArrayList<>();

    public IdeaLibraryGenerator(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    /**
     * 鐢熸垚 IDEA 搴撻厤缃枃浠?
     * 
     * @param classpath 鍒嗗彿鍒嗛殧鐨?jar 璺緞鍒楄〃
     * @return 鐢熸垚鐨勫簱鏁伴噺
     */
    public int generateLibraryConfigs(String classpath) throws IOException {
        if (classpath == null || classpath.isEmpty()) {
            return 0;
        }

        // 鍒涘缓 .idea/libraries 鐩綍
        Path librariesDir = Paths.get(projectRoot, ".idea", "libraries");
        Files.createDirectories(librariesDir);

        // 瑙ｆ瀽 classpath
        String sep = QinConstants.getClasspathSeparator();
        String[] jarPaths = classpath.split(sep);

        generatedLibraryNames.clear();
        int count = 0;
        for (String jarPath : jarPaths) {
            if (jarPath.isEmpty() || !jarPath.endsWith(".jar")) {
                continue;
            }

            // 浠庤矾寰勪腑鎻愬彇搴撳悕
            LibraryInfo info = extractLibraryInfo(jarPath);
            if (info == null) {
                continue;
            }

            // 鐢熸垚 XML 閰嶇疆鏂囦欢
            String xmlContent = generateLibraryXml(info.name, jarPath);
            String safeFileName = info.name.replace(":", "_")
                    .replace("@", "_")
                    .replace(".", "_") + ".xml";
            Path xmlPath = librariesDir.resolve(safeFileName);
            Files.writeString(xmlPath, xmlContent);

            generatedLibraryNames.add(info.name);
            count++;
        }

        // 鏇存柊妯″潡閰嶇疆锛屾坊鍔犲簱寮曠敤
        updateModuleConfig();

        return count;
    }

    /**
     * 鏇存柊妯″潡閰嶇疆鏂囦欢锛?iml锛夛紝娣诲姞搴撳紩鐢ㄥ苟閰嶇疆缂栬瘧杈撳嚭璺緞
     */
    private void updateModuleConfig() throws IOException {
        // 鏌ユ壘 .iml 鏂囦欢
        Path imlFile = findImlFile();
        if (imlFile == null) {
            return;
        }

        String content = Files.readString(imlFile);

        // 1. 閰嶇疆缂栬瘧杈撳嚭璺緞涓?build/classes锛堜笌 qin 涓€鑷达級
        content = configureOutputPath(content);

        // 1.5 纭繚 sourceFolder 瀛樺湪锛堜慨澶嶈嚜闂悎鐨?content 鏍囩锛?
        content = ensureSourceFolder(content);

        // 1.6 鉁?璁剧疆妯″潡绾у埆鐨?LANGUAGE_LEVEL
        content = ensureLanguageLevel(content);

        // 2. 绉婚櫎鎵€鏈夋棫鐨?Qin 搴撳紩鐢紙鍙兘鏈夊涓級
        while (content.contains("<!-- Qin Libraries -->")) {
            int startIdx = content.indexOf("<!-- Qin Libraries -->");
            int endIdx = content.indexOf("<!-- End Qin Libraries -->", startIdx);
            if (endIdx != -1) {
                endIdx += "<!-- End Qin Libraries -->".length();
                // 鍒犻櫎杩欎竴娈碉紙鍖呮嫭鍓嶅悗鐨勭┖鐧斤級
                String before = content.substring(0, startIdx).replaceAll("\\s+$", "");
                String after = content.substring(endIdx).replaceAll("^\\s+", "");
                content = before + "\n" + after;
            } else {
                break; // 娌℃湁鎵惧埌缁撴潫鏍囪锛岃烦鍑?
            }
        }

        // 3. 鏋勫缓鏂扮殑搴撳紩鐢?
        if (!generatedLibraryNames.isEmpty()) {
            StringBuilder libraryEntries = new StringBuilder();
            libraryEntries.append("    <!-- Qin Libraries -->\n");
            for (String libName : generatedLibraryNames) {
                libraryEntries.append("    <orderEntry type=\"library\" name=\"")
                        .append(libName)
                        .append("\" level=\"project\" />\n");
            }
            libraryEntries.append("    <!-- End Qin Libraries -->\n");

            // 鍦?</component> 涔嬪墠鎻掑叆搴撳紩鐢紙浣跨敤姝ｅ垯鍖归厤浠绘剰缂╄繘锛?
            String insertion = libraryEntries.toString();
            content = content.replaceFirst(
                    "(\\s*)</component>",
                    insertion + "$1</component>");
        }

        // 4. 鉁?鍚屾椂鏇存柊 misc.xml 鐨?languageLevel
        updateMiscXmlLanguageLevel();

        Files.writeString(imlFile, content);
    }

    /**
     * 纭繚 .iml 鏂囦欢涓湁 LANGUAGE_LEVEL 灞炴€?
     */
    private String ensureLanguageLevel(String imlContent) {
        // 浠?qin.config.js 璇诲彇 Java 鐗堟湰
        String version = readJavaVersionFromConfig();
        String languageLevel = "JDK_" + version;

        // 妫€鏌ユ槸鍚﹀凡鏈?LANGUAGE_LEVEL 灞炴€?
        if (imlContent.contains("LANGUAGE_LEVEL=")) {
            // 鏇存柊鐜版湁灞炴€?
            imlContent = imlContent.replaceAll(
                    "LANGUAGE_LEVEL=\"[^\"]*\"",
                    "LANGUAGE_LEVEL=\"" + languageLevel + "\"");
        } else if (imlContent.contains("<component name=\"NewModuleRootManager\"")) {
            // 娣诲姞 LANGUAGE_LEVEL 灞炴€?
            imlContent = imlContent.replace(
                    "<component name=\"NewModuleRootManager\"",
                    "<component name=\"NewModuleRootManager\" LANGUAGE_LEVEL=\"" + languageLevel + "\"");
        }

        System.out.println("  [IDEA] 妯″潡璇█绾у埆: " + languageLevel);
        return imlContent;
    }

    /**
     * 鏇存柊 misc.xml 涓殑 languageLevel
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
            System.out.println("  [IDEA] 椤圭洰璇█绾у埆: JDK_" + version);
        } catch (IOException e) {
            System.err.println("  [IDEA] 鏇存柊 misc.xml 澶辫触: " + e.getMessage());
        }
    }

    /**
     * 浠?qin.config.js 璇诲彇 Java 鐗堟湰
     */
    private String readJavaVersionFromConfig() {
        try {
            Path configPath = Paths.get(projectRoot, QinConstants.CONFIG_FILE);
            if (!Files.exists(configPath)) {
                return QinConstants.DEFAULT_JAVA_VERSION;
            }

            String json = Files.readString(configPath);

            // 绠€鍗?JSON 瑙ｆ瀽锛氭煡鎵?java.target 鎴?java.version
            // 浼樺厛浣跨敤 release
            String release = extractJsonValue(json, "release");
            if (release != null && !release.isBlank()) {
                return release;
            }

            // 鍏舵浣跨敤 target
            String target = extractJsonValue(json, "target");
            if (target != null && !target.isBlank()) {
                return target;
            }

            // 鍏舵浣跨敤 version
            String version = extractJsonValue(json, "version");
            if (version != null && !version.isBlank()) {
                return version;
            }

            return QinConstants.DEFAULT_JAVA_VERSION;
        } catch (IOException e) {
            return QinConstants.DEFAULT_JAVA_VERSION;
        }
    }

    /**
     * 浠?JSON 涓彁鍙栧€硷紙绠€鍗曞疄鐜帮級
     */
    private String extractJsonValue(String json, String key) {
        // 鏌ユ壘 "java" 瀵硅薄涓殑閿?
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
     * 閰嶇疆缂栬瘧杈撳嚭璺緞涓?build/classes
     */
    private String configureOutputPath(String imlContent) {
        // 妫€鏌ユ槸鍚︿娇鐢ㄤ簡缁ф壙鐨勭紪璇戣緭鍑鸿矾寰?
        if (imlContent.contains("inherit-compiler-output=\"true\"")) {
            // 鏇挎崲涓鸿嚜瀹氫箟杈撳嚭璺緞
            imlContent = imlContent.replace(
                    "inherit-compiler-output=\"true\"",
                    "inherit-compiler-output=\"false\"");

            // 鍦?<exclude-output /> 鍚庢坊鍔犺緭鍑鸿矾寰勯厤缃?
            String outputConfig = "\n    <output url=\"file://$MODULE_DIR$/" + QinConstants.BUILD_CLASSES_DIR + "\" />"
                    +
                    "\n    <output-test url=\"file://$MODULE_DIR$/build/test-classes\" />";
            imlContent = imlContent.replace(
                    "<exclude-output />",
                    "<exclude-output />" + outputConfig);
        } else if (imlContent.contains("inherit-compiler-output=\"false\"")) {
            // 宸茬粡浣跨敤鑷畾涔夎緭鍑鸿矾寰勶紝妫€鏌ユ槸鍚﹂渶瑕佹洿鏂?
            if (!imlContent.contains("$MODULE_DIR$/" + QinConstants.BUILD_CLASSES_DIR)) {
                // 绉婚櫎鏃х殑 output 閰嶇疆
                imlContent = imlContent.replaceAll(
                        "\\s*<output url=\"[^\"]+\" />\\s*",
                        "\n");
                imlContent = imlContent.replaceAll(
                        "\\s*<output-test url=\"[^\"]+\" />\\s*",
                        "\n");

                // 娣诲姞鏂扮殑杈撳嚭璺緞閰嶇疆
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
     * 纭繚 sourceFolder 瀛樺湪
     * 淇鑷棴鍚堢殑 content 鏍囩锛屼緥濡?<content url="..." /> 鏀逛负鍖呭惈 sourceFolder 鐨勫畬鏁村舰寮?
     */
    private String ensureSourceFolder(String imlContent) {
        // 妫€鏌ユ槸鍚﹀凡缁忔湁 sourceFolder
        if (imlContent.contains("<sourceFolder")) {
            return imlContent;
        }

        // 妫€娴嬫簮浠ｇ爜鐩綍
        String sourceDir = detectSourceDir();
        if (sourceDir == null) {
            return imlContent;
        }

        // 妫€鏌ユ槸鍚︽槸鑷棴鍚堢殑 content 鏍囩
        // 鍖归厤 <content url="file://$MODULE_DIR$" /> 鎴栫被浼肩殑鑷棴鍚堝舰寮?
        java.util.regex.Pattern selfClosingPattern = java.util.regex.Pattern.compile(
                "<content\\s+url=\"[^\"]*\"\\s*/>");
        java.util.regex.Matcher matcher = selfClosingPattern.matcher(imlContent);

        if (matcher.find()) {
            // 鎵惧埌鑷棴鍚堢殑 content 鏍囩锛岄渶瑕佹浛鎹负瀹屾暣褰㈠紡
            String originalTag = matcher.group();
            // 鎻愬彇 url 灞炴€?
            int urlStart = originalTag.indexOf("url=\"") + 5;
            int urlEnd = originalTag.indexOf("\"", urlStart);
            String url = originalTag.substring(urlStart, urlEnd);

            // 鏋勫缓瀹屾暣鐨?content 鏍囩
            StringBuilder newContent = new StringBuilder();
            newContent.append("<content url=\"").append(url).append("\">\n");
            newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                    .append(sourceDir).append("\" isTestSource=\"false\" />\n");

            // 妫€鏌ユ槸鍚︽湁娴嬭瘯鐩綍
            String testDir = detectTestDir();
            if (testDir != null) {
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(testDir).append("\" isTestSource=\"true\" />\n");
            }

            // 娣诲姞鎺掗櫎鐩綍锛?iml 涓父鐢ㄧ殑鎺掗櫎鐩綍锛?
            for (String excludeDir : QinConstants.IML_EXCLUDED_DIRS) {
                newContent.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                        .append(excludeDir).append("\" />\n");
            }

            newContent.append("    </content>");

            imlContent = imlContent.replace(originalTag, newContent.toString());
        }

        return imlContent;
    }

    /**
     * 妫€娴嬫簮浠ｇ爜鐩綍
     */
    private String detectSourceDir() {
        // 浼樺厛妫€娴嬫爣鍑?Maven 缁撴瀯
        Path mavenSrc = Paths.get(projectRoot, QinConstants.JAVA_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return QinConstants.JAVA_SOURCE_DIR;
        }
        // 鍏舵妫€娴嬬畝鍗曠粨鏋?
        Path simpleSrc = Paths.get(projectRoot, "src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        return null;
    }

    /**
     * 妫€娴嬫祴璇曠洰褰?
     */
    private String detectTestDir() {
        Path testDir = Paths.get(projectRoot, "src/test/java");
        if (Files.exists(testDir)) {
            return "src/test/java";
        }
        return null;
    }

    /**
     * 鏌ユ壘椤圭洰鐨?.iml 鏂囦欢
     */
    private Path findImlFile() throws IOException {
        // 1. 棣栧厛鍦ㄩ」鐩牴鐩綍鏌ユ壘
        try (var stream = Files.list(Paths.get(projectRoot))) {
            Optional<Path> iml = stream
                    .filter(p -> p.toString().endsWith(".iml"))
                    .findFirst();
            if (iml.isPresent()) {
                return iml.get();
            }
        }

        // 2. 鍦?.idea 鐩綍鏌ユ壘
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
     * 娓呯悊鏃х殑搴撻厤缃枃浠?
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
                            // 蹇界暐鍒犻櫎澶辫触
                        }
                    });
        }
    }

    /**
     * 浠?jar 璺緞鎻愬彇搴撲俊鎭?
     */
    private LibraryInfo extractLibraryInfo(String jarPath) {
        String normalized = jarPath.replace("\\", "/");

        // 灏濊瘯浠庤矾寰勪腑鎻愬彇 groupId@artifactId-version 鏍煎紡
        // 渚嬪锛?qin/libs/com.google.code.gson@gson/com.google.code.gson@gson-2.10.1/xxx.jar
        String[] parts = normalized.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];
            if (part.contains("@") && part.contains("-")) {
                // 鏍煎紡锛歝om.google.code.gson@gson-2.10.1
                int dashIdx = part.lastIndexOf("-");
                if (dashIdx > 0) {
                    String coordinate = part.substring(0, dashIdx);
                    String version = part.substring(dashIdx + 1);
                    return new LibraryInfo(coordinate, version);
                }
            }
        }

        // fallback锛氫娇鐢ㄦ枃浠跺悕
        String fileName = Paths.get(jarPath).getFileName().toString();
        String name = fileName.replace(".jar", "");
        return new LibraryInfo(name, "");
    }

    /**
     * 鐢熸垚 IDEA 搴?XML 閰嶇疆
     */
    private String generateLibraryXml(String libraryName, String jarPath) {
        // 灏嗚矾寰勮浆鎹负 jar:// URL 鏍煎紡
        String normalizedPath = jarPath.replace("\\", "/");

        // Windows 璺緞濡?C:/Users/... 涓嶉渶瑕佸墠瀵?/
        // Unix 璺緞濡?/home/... 宸茬粡鏈夊墠瀵?/
        // IDEA 鐨?jar:// 鍗忚鏍煎紡锛歫ar://璺緞!/

        // IDEA 浣跨敤 jar:// 鍗忚鍜?!/ 鍚庣紑
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
     * 搴撲俊鎭?
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

