package com.qin.bsp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qin.bsp.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * BSP 请求处理器
 * 将 BSP 协议方法映射到具体实现
 */
public class BspHandler {
    private final String workDir;
    private final Gson gson = new Gson();
    private QinProjectInfo project;

    public BspHandler(String workDir) {
        this.workDir = workDir;
    }

    /**
     * 处理 BSP 请求
     */
    public Object handle(String method, JsonObject params) {
        return switch (method) {
            case "build/initialize" -> handleInitialize(params);
            case "build/initialized" -> null; // 通知，无需响应
            case "workspace/buildTargets" -> handleBuildTargets();
            case "buildTarget/sources" -> handleSources(params);
            case "buildTarget/dependencySources" -> handleDependencySources(params);
            case "buildTarget/javacOptions" -> handleJavacOptions(params);
            default -> Map.of("error", "Unknown method: " + method);
        };
    }

    private Object handleInitialize(JsonObject params) {
        loadProject();
        Map<String, Object> caps = new HashMap<>();
        caps.put("compileProvider", Map.of("languageIds", List.of("java")));
        caps.put("canReload", true);
        return Map.of(
                "displayName", "Qin BSP Server",
                "version", "0.1.0",
                "bspVersion", "2.1.0",
                "capabilities", caps);
    }

    private void loadProject() {
        try {
            Path configPath = Paths.get(workDir, "qin.config.json");
            String json = Files.readString(configPath);
            project = gson.fromJson(json, QinProjectInfo.class);
        } catch (IOException e) {
            project = new QinProjectInfo();
        }
    }

    private Object handleBuildTargets() {
        String uri = Paths.get(workDir).toUri().toString();
        Map<String, Object> target = new HashMap<>();
        target.put("id", Map.of("uri", uri));
        target.put("displayName", project.name != null ? project.name : "qin-project");
        target.put("languageIds", List.of("java"));
        target.put("tags", List.of("application"));
        target.put("capabilities", Map.of("canCompile", true, "canRun", true));
        return Map.of("targets", List.of(target));
    }

    private Object handleSources(JsonObject params) {
        String sourceDir = project.getSourceDir();
        Path srcPath = Paths.get(workDir, sourceDir);
        String uri = srcPath.toUri().toString();
        Map<String, Object> item = Map.of(
                "uri", uri, "kind", 1, "generated", false);
        Map<String, Object> sources = Map.of(
                "target", params.getAsJsonObject("targets").getAsJsonArray().get(0),
                "sources", List.of(item));
        return Map.of("items", List.of(sources));
    }

    private Object handleDependencySources(JsonObject params) {
        // TODO: 解析 classpath
        return Map.of("items", List.of());
    }

    private Object handleJavacOptions(JsonObject params) {
        String javaVersion = project.getJavaVersion();
        List<String> options = List.of(
                "--release", javaVersion,
                "--enable-preview");
        Map<String, Object> item = Map.of(
                "target", params.getAsJsonObject("targets").getAsJsonArray().get(0),
                "options", options,
                "classpath", List.of(),
                "classDirectory", Paths.get(workDir, "build/classes").toUri().toString());
        return Map.of("items", List.of(item));
    }
}
