
/**
 * BSP 鍔熻兘娴嬭瘯鑴氭湰
 * 涓嶄緷璧?IDE锛岀洿鎺ユ祴璇?BSP 閫昏緫
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BspTest {

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        String testProjectPath = "D:/project/qkyproject/slime-java/slime/slime-java/slime-token";

        System.out.println("============================================");
        System.out.println("BSP 鍔熻兘娴嬭瘯");
        System.out.println("娴嬭瘯椤圭洰: " + testProjectPath);
        System.out.println("============================================\n");

        // 1. 娴嬭瘯璇诲彇 qin.config.js
        testReadConfig(testProjectPath);

        // 2. 娴嬭瘯璇诲彇 classpath.json
        testReadClasspath(testProjectPath);

        // 3. 娴嬭瘯妫€娴嬫簮浠ｇ爜鐩綍
        testDetectSourceDir(testProjectPath);

        // 4. 娴嬭瘯鐢熸垚 .iml 鍐呭
        testGenerateImlContent(testProjectPath);

        System.out.println("\n============================================");
        System.out.println("鎵€鏈夋祴璇曞畬鎴愶紒");
        System.out.println("============================================");
    }

    /**
     * 娴嬭瘯 1: 璇诲彇 qin.config.js
     */
    static void testReadConfig(String projectPath) throws Exception {
        System.out.println("銆愭祴璇?1銆戣鍙?qin.config.js");

        Path configPath = Paths.get(projectPath, "qin.config.js");
        if (!Files.exists(configPath)) {
            System.out.println("  鉂?鏂囦欢涓嶅瓨鍦? " + configPath);
            return;
        }

        String json = Files.readString(configPath);
        JsonObject config = gson.fromJson(json, JsonObject.class);

        String name = config.has("name") ? config.get("name").getAsString() : "鏈畾涔?;
        System.out.println("  椤圭洰鍚嶇О: " + name);

        if (config.has("java")) {
            JsonObject javaConfig = config.getAsJsonObject("java");
            String sourceDir = javaConfig.has("sourceDir") ? javaConfig.get("sourceDir").getAsString() : "src";
            String outputDir = javaConfig.has("outputDir") ? javaConfig.get("outputDir").getAsString()
                    : "build/classes";
            String javaVersion = javaConfig.has("version") ? javaConfig.get("version").getAsString() : "21";

            System.out.println("  婧愪唬鐮佺洰褰? " + sourceDir);
            System.out.println("  杈撳嚭鐩綍: " + outputDir);
            System.out.println("  Java 鐗堟湰: " + javaVersion);
        }

        if (config.has("dependencies")) {
            JsonObject deps = config.getAsJsonObject("dependencies");
            System.out.println("  渚濊禆鏁伴噺: " + deps.size());
            for (String key : deps.keySet()) {
                System.out.println("    - " + key + ": " + deps.get(key).getAsString());
            }
        }

        System.out.println("  鉁?閰嶇疆璇诲彇鎴愬姛\n");
    }

    /**
     * 娴嬭瘯 2: 璇诲彇 classpath.json
     */
    static void testReadClasspath(String projectPath) throws Exception {
        System.out.println("銆愭祴璇?2銆戣鍙?.qin/classpath.json");

        Path cpPath = Paths.get(projectPath, ".qin", "classpath.json");
        if (!Files.exists(cpPath)) {
            System.out.println("  鈿狅笍 鏂囦欢涓嶅瓨鍦? " + cpPath);
            System.out.println("  鎻愮ず: 闇€瑕佸厛鎵ц qin sync\n");
            return;
        }

        String json = Files.readString(cpPath);
        JsonObject cpObj = gson.fromJson(json, JsonObject.class);

        if (cpObj.has("classpath")) {
            var cpArray = cpObj.getAsJsonArray("classpath");
            System.out.println("  Classpath 鏉＄洰鏁? " + cpArray.size());
            for (var entry : cpArray) {
                String path = entry.getAsString();
                boolean exists = Files.exists(Paths.get(path));
                System.out.println("    " + (exists ? "鉁? : "鉂?) + " " + path);
            }
        }

        if (cpObj.has("lastUpdated")) {
            System.out.println("  鏈€鍚庢洿鏂? " + cpObj.get("lastUpdated").getAsString());
        }

        System.out.println("  鉁?Classpath 璇诲彇鎴愬姛\n");
    }

    /**
     * 娴嬭瘯 3: 妫€娴嬫簮浠ｇ爜鐩綍
     */
    static void testDetectSourceDir(String projectPath) throws Exception {
        System.out.println("銆愭祴璇?3銆戞娴嬫簮浠ｇ爜鐩綍");

        Path mavenSrc = Paths.get(projectPath, "src/main/java");
        Path simpleSrc = Paths.get(projectPath, "src");

        String detected = null;
        if (Files.exists(mavenSrc)) {
            detected = "src/main/java";
            System.out.println("  妫€娴嬪埌 Maven 鏍囧噯缁撴瀯: " + detected);
        } else if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            detected = "src";
            System.out.println("  妫€娴嬪埌绠€鍗曠粨鏋? " + detected);
        }

        if (detected != null) {
            // 妫€鏌ユ槸鍚︽湁 Java 鏂囦欢
            long javaFiles = Files.walk(Paths.get(projectPath, detected))
                    .filter(p -> p.toString().endsWith(".java"))
                    .count();
            System.out.println("  Java 鏂囦欢鏁伴噺: " + javaFiles);
            System.out.println("  鉁?婧愪唬鐮佺洰褰曟娴嬫垚鍔焅n");
        } else {
            System.out.println("  鉂?鏈娴嬪埌婧愪唬鐮佺洰褰昞n");
        }
    }

    /**
     * 娴嬭瘯 4: 鐢熸垚 .iml 鍐呭
     */
    static void testGenerateImlContent(String projectPath) throws Exception {
        System.out.println("銆愭祴璇?4銆戠敓鎴?.iml 鍐呭");

        String projectName = Paths.get(projectPath).getFileName().toString();

        // 璇诲彇閰嶇疆
        Path configPath = Paths.get(projectPath, "qin.config.js");
        String sourceDir = "src/main/java";
        String outputDir = "build/classes";

        if (Files.exists(configPath)) {
            String json = Files.readString(configPath);
            JsonObject config = gson.fromJson(json, JsonObject.class);
            if (config.has("java")) {
                JsonObject javaConfig = config.getAsJsonObject("java");
                if (javaConfig.has("sourceDir")) {
                    sourceDir = javaConfig.get("sourceDir").getAsString();
                }
                if (javaConfig.has("outputDir")) {
                    outputDir = javaConfig.get("outputDir").getAsString();
                }
            }
        }

        // 璇诲彇 classpath
        List<String> classpath = new ArrayList<>();
        Path cpPath = Paths.get(projectPath, ".qin", "classpath.json");
        if (Files.exists(cpPath)) {
            String json = Files.readString(cpPath);
            JsonObject cpObj = gson.fromJson(json, JsonObject.class);
            if (cpObj.has("classpath")) {
                cpObj.getAsJsonArray("classpath").forEach(e -> classpath.add(e.getAsString()));
            }
        }

        // 鐢熸垚渚濊禆鏉＄洰
        StringBuilder dependencyEntries = new StringBuilder();
        for (String path : classpath) {
            String entryPath = path.replace("\\", "/");
            if (entryPath.endsWith(".jar")) {
                dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                        .append("      <library>\n")
                        .append("        <CLASSES>\n")
                        .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                        .append("        </CLASSES>\n")
                        .append("      </library>\n")
                        .append("    </orderEntry>\n");
            } else {
                dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                        .append("      <library>\n")
                        .append("        <CLASSES>\n")
                        .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                        .append("        </CLASSES>\n")
                        .append("      </library>\n")
                        .append("    </orderEntry>\n");
            }
        }

        // 鐢熸垚 .iml 鍐呭
        String imlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <module type="JAVA_MODULE" version="4">
                  <component name="NewModuleRootManager" inherit-compiler-output="false">
                    <exclude-output />
                    <output url="file://$MODULE_DIR$/%s" />
                    <output-test url="file://$MODULE_DIR$/%s" />
                    <content url="file://$MODULE_DIR$">
                      <sourceFolder url="file://$MODULE_DIR$/%s" isTestSource="false" />
                      <excludeFolder url="file://$MODULE_DIR$/.qin" />
                      <excludeFolder url="file://$MODULE_DIR$/build" />
                      <excludeFolder url="file://$MODULE_DIR$/libs" />
                    </content>
                    <orderEntry type="inheritedJdk" />
                    <orderEntry type="sourceFolder" forTests="false" />
                %s  </component>
                </module>
                """.formatted(outputDir, outputDir.replace("classes", "test-classes"),
                sourceDir, dependencyEntries.toString());

        System.out.println("  椤圭洰鍚嶇О: " + projectName);
        System.out.println("  婧愪唬鐮佺洰褰? " + sourceDir);
        System.out.println("  杈撳嚭鐩綍: " + outputDir);
        System.out.println("  渚濊禆鏁伴噺: " + classpath.size());
        System.out.println("\n  鐢熸垚鐨?.iml 鍐呭棰勮:");
        System.out.println("  " + "-".repeat(50));

        // 鍙樉绀哄墠鍑犺
        String[] lines = imlContent.split("\n");
        for (int i = 0; i < Math.min(15, lines.length); i++) {
            System.out.println("  " + lines[i]);
        }
        if (lines.length > 15) {
            System.out.println("  ... (鍏?" + lines.length + " 琛?");
        }

        System.out.println("  " + "-".repeat(50));
        System.out.println("  鉁?.iml 鍐呭鐢熸垚鎴愬姛\n");
    }
}

