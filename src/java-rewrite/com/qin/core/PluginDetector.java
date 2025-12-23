package com.qin.core;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

/**
 * Plugin Auto-Detector
 * 根据项目内容自动检测需要启用的插件
 */
public class PluginDetector {
    private final String cwd;

    public PluginDetector(String cwd) {
        this.cwd = cwd;
    }

    /**
     * 检测项目并返回建议的插件配置
     */
    public DetectionResult detect() {
        DetectionResult result = new DetectionResult();

        // 检测 Java
        if (hasJavaFiles()) {
            result.addLanguage("java");
            result.addSuggestedPlugin("qin-plugin-java");
            String entry = findJavaEntry();
            if (entry != null) {
                result.setEntry(entry);
            }
        }

        // 检测 Kotlin
        if (hasKotlinFiles()) {
            result.addLanguage("kotlin");
            result.addSuggestedPlugin("qin-plugin-kotlin");
        }

        // 检测前端
        String clientDir = findClientDir();
        if (clientDir != null) {
            result.addFeature("frontend");
            result.addSuggestedPlugin("qin-plugin-vite");
            result.setClientDir(clientDir);
        }

        // 检测 Spring Boot
        if (hasSpringBoot()) {
            result.addFeature("spring-boot");
        }

        return result;
    }

    private boolean hasJavaFiles() {
        Path srcDir = Paths.get(cwd, "src");
        if (!Files.exists(srcDir)) return false;

        try (Stream<Path> walk = Files.walk(srcDir)) {
            return walk.anyMatch(p -> p.toString().endsWith(".java"));
        } catch (IOException e) {
            return false;
        }
    }

    private boolean hasKotlinFiles() {
        Path srcDir = Paths.get(cwd, "src");
        if (!Files.exists(srcDir)) return false;

        try (Stream<Path> walk = Files.walk(srcDir)) {
            return walk.anyMatch(p -> p.toString().endsWith(".kt"));
        } catch (IOException e) {
            return false;
        }
    }

    private String findJavaEntry() {
        String[] candidates = {
            "src/Main.java",
            "src/App.java",
            "src/Application.java",
            "src/server/Main.java",
            "src/main/java/Main.java"
        };

        for (String candidate : candidates) {
            if (Files.exists(Paths.get(cwd, candidate))) {
                return candidate;
            }
        }

        // 查找包含 main 方法的文件
        Path srcDir = Paths.get(cwd, "src");
        if (!Files.exists(srcDir)) return null;

        try (Stream<Path> walk = Files.walk(srcDir)) {
            return walk
                .filter(p -> p.toString().endsWith(".java"))
                .filter(this::hasMainMethod)
                .map(p -> srcDir.getParent().relativize(p).toString().replace("\\", "/"))
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean hasMainMethod(Path javaFile) {
        try {
            String content = Files.readString(javaFile);
            return content.contains("public static void main");
        } catch (IOException e) {
            return false;
        }
    }

    private String findClientDir() {
        String[][] candidates = {
            {"src/client", "index.html"},
            {"client", "index.html"},
            {"web", "index.html"},
            {"frontend", "index.html"},
            {"src/client", "package.json"},
            {"client", "package.json"}
        };

        for (String[] candidate : candidates) {
            Path dir = Paths.get(cwd, candidate[0]);
            Path check = dir.resolve(candidate[1]);
            if (Files.exists(dir) && Files.exists(check)) {
                return candidate[0];
            }
        }

        return null;
    }

    private boolean hasSpringBoot() {
        String[] configFiles = {
            "src/resources/application.yml",
            "src/resources/application.yaml",
            "src/resources/application.properties",
            "src/main/resources/application.yml",
            "src/main/resources/application.properties"
        };

        for (String file : configFiles) {
            if (Files.exists(Paths.get(cwd, file))) {
                return true;
            }
        }

        return false;
    }
}
