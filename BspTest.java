
/**
 * BSP 功能测试脚本
 * 不依赖 IDE，直接测试 BSP 逻辑
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
        System.out.println("BSP 功能测试");
        System.out.println("测试项目: " + testProjectPath);
        System.out.println("============================================\n");

        // 1. 测试读取 qin.config.json
        testReadConfig(testProjectPath);

        // 2. 测试读取 classpath.json
        testReadClasspath(testProjectPath);

        // 3. 测试检测源代码目录
        testDetectSourceDir(testProjectPath);

        // 4. 测试生成 .iml 内容
        testGenerateImlContent(testProjectPath);

        System.out.println("\n============================================");
        System.out.println("所有测试完成！");
        System.out.println("============================================");
    }

    /**
     * 测试 1: 读取 qin.config.json
     */
    static void testReadConfig(String projectPath) throws Exception {
        System.out.println("【测试 1】读取 qin.config.json");

        Path configPath = Paths.get(projectPath, "qin.config.json");
        if (!Files.exists(configPath)) {
            System.out.println("  ❌ 文件不存在: " + configPath);
            return;
        }

        String json = Files.readString(configPath);
        JsonObject config = gson.fromJson(json, JsonObject.class);

        String name = config.has("name") ? config.get("name").getAsString() : "未定义";
        System.out.println("  项目名称: " + name);

        if (config.has("java")) {
            JsonObject javaConfig = config.getAsJsonObject("java");
            String sourceDir = javaConfig.has("sourceDir") ? javaConfig.get("sourceDir").getAsString() : "src";
            String outputDir = javaConfig.has("outputDir") ? javaConfig.get("outputDir").getAsString()
                    : "build/classes";
            String javaVersion = javaConfig.has("version") ? javaConfig.get("version").getAsString() : "21";

            System.out.println("  源代码目录: " + sourceDir);
            System.out.println("  输出目录: " + outputDir);
            System.out.println("  Java 版本: " + javaVersion);
        }

        if (config.has("dependencies")) {
            JsonObject deps = config.getAsJsonObject("dependencies");
            System.out.println("  依赖数量: " + deps.size());
            for (String key : deps.keySet()) {
                System.out.println("    - " + key + ": " + deps.get(key).getAsString());
            }
        }

        System.out.println("  ✅ 配置读取成功\n");
    }

    /**
     * 测试 2: 读取 classpath.json
     */
    static void testReadClasspath(String projectPath) throws Exception {
        System.out.println("【测试 2】读取 .qin/classpath.json");

        Path cpPath = Paths.get(projectPath, ".qin", "classpath.json");
        if (!Files.exists(cpPath)) {
            System.out.println("  ⚠️ 文件不存在: " + cpPath);
            System.out.println("  提示: 需要先执行 qin sync\n");
            return;
        }

        String json = Files.readString(cpPath);
        JsonObject cpObj = gson.fromJson(json, JsonObject.class);

        if (cpObj.has("classpath")) {
            var cpArray = cpObj.getAsJsonArray("classpath");
            System.out.println("  Classpath 条目数: " + cpArray.size());
            for (var entry : cpArray) {
                String path = entry.getAsString();
                boolean exists = Files.exists(Paths.get(path));
                System.out.println("    " + (exists ? "✅" : "❌") + " " + path);
            }
        }

        if (cpObj.has("lastUpdated")) {
            System.out.println("  最后更新: " + cpObj.get("lastUpdated").getAsString());
        }

        System.out.println("  ✅ Classpath 读取成功\n");
    }

    /**
     * 测试 3: 检测源代码目录
     */
    static void testDetectSourceDir(String projectPath) throws Exception {
        System.out.println("【测试 3】检测源代码目录");

        Path mavenSrc = Paths.get(projectPath, "src/main/java");
        Path simpleSrc = Paths.get(projectPath, "src");

        String detected = null;
        if (Files.exists(mavenSrc)) {
            detected = "src/main/java";
            System.out.println("  检测到 Maven 标准结构: " + detected);
        } else if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            detected = "src";
            System.out.println("  检测到简单结构: " + detected);
        }

        if (detected != null) {
            // 检查是否有 Java 文件
            long javaFiles = Files.walk(Paths.get(projectPath, detected))
                    .filter(p -> p.toString().endsWith(".java"))
                    .count();
            System.out.println("  Java 文件数量: " + javaFiles);
            System.out.println("  ✅ 源代码目录检测成功\n");
        } else {
            System.out.println("  ❌ 未检测到源代码目录\n");
        }
    }

    /**
     * 测试 4: 生成 .iml 内容
     */
    static void testGenerateImlContent(String projectPath) throws Exception {
        System.out.println("【测试 4】生成 .iml 内容");

        String projectName = Paths.get(projectPath).getFileName().toString();

        // 读取配置
        Path configPath = Paths.get(projectPath, "qin.config.json");
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

        // 读取 classpath
        List<String> classpath = new ArrayList<>();
        Path cpPath = Paths.get(projectPath, ".qin", "classpath.json");
        if (Files.exists(cpPath)) {
            String json = Files.readString(cpPath);
            JsonObject cpObj = gson.fromJson(json, JsonObject.class);
            if (cpObj.has("classpath")) {
                cpObj.getAsJsonArray("classpath").forEach(e -> classpath.add(e.getAsString()));
            }
        }

        // 生成依赖条目
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

        // 生成 .iml 内容
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

        System.out.println("  项目名称: " + projectName);
        System.out.println("  源代码目录: " + sourceDir);
        System.out.println("  输出目录: " + outputDir);
        System.out.println("  依赖数量: " + classpath.size());
        System.out.println("\n  生成的 .iml 内容预览:");
        System.out.println("  " + "-".repeat(50));

        // 只显示前几行
        String[] lines = imlContent.split("\n");
        for (int i = 0; i < Math.min(15, lines.length); i++) {
            System.out.println("  " + lines[i]);
        }
        if (lines.length > 15) {
            System.out.println("  ... (共 " + lines.length + " 行)");
        }

        System.out.println("  " + "-".repeat(50));
        System.out.println("  ✅ .iml 内容生成成功\n");
    }
}
