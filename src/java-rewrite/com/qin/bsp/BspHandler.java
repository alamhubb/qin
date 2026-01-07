package com.qin.bsp;

import com.qin.constants.QinConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qin.bsp.model.*;
import com.qin.constants.QinConstants;
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
    private List<String> classpath;

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
            case "buildTarget/compile" -> handleCompile(params);
            case "buildTarget/run" -> handleRun(params);
            default -> Map.of("error", "Unknown method: " + method);
        };
    }

    private Object handleInitialize(JsonObject params) {
        loadProject();
        loadClasspath();
        Map<String, Object> caps = new HashMap<>();
        caps.put("compileProvider", Map.of("languageIds", List.of("java")));
        caps.put("runProvider", Map.of("languageIds", List.of("java")));
        caps.put("canReload", true);
        return Map.of(
                "displayName", "Qin BSP Server",
                "version", "0.2.0",
                "bspVersion", "2.1.0",
                "capabilities", caps);
    }

    private void loadProject() {
        try {
            Path configPath = Paths.get(workDir, QinConstants.CONFIG_FILE);
            String json = Files.readString(configPath);
            project = gson.fromJson(json, QinProjectInfo.class);
        } catch (IOException e) {
            project = new QinProjectInfo();
        }
    }

    private void loadClasspath() {
        classpath = new ArrayList<>();
        try {
            Path cpPath = Paths.get(workDir, ".qin", "classpath.json");
            if (Files.exists(cpPath)) {
                String json = Files.readString(cpPath);
                JsonObject obj = gson.fromJson(json, JsonObject.class);
                if (obj.has("classpath")) {
                    obj.getAsJsonArray("classpath").forEach(e -> classpath.add(e.getAsString()));
                }
            }
        } catch (IOException e) {
            // 忽略
        }
    }

    // ==================== 公共方法供 IDEA 插件使用 ====================

    /**
     * 获取源代码目录
     */
    public String getSourceDir() {
        if (project == null) {
            loadProject();
        }
        return project.getSourceDir();
    }

    /**
     * 获取输出目录
     */
    public String getOutputDir() {
        if (project == null) {
            loadProject();
        }
        if (project.java != null && project.java.outputDir != null) {
            return project.java.outputDir;
        }
        return QinConstants.BUILD_CLASSES_DIR;
    }

    /**
     * 获取 classpath
     */
    public List<String> getClasspath() {
        if (classpath == null) {
            loadClasspath();
        }
        return classpath;
    }

    private Object handleBuildTargets() {
        String uri = Paths.get(workDir).toUri().toString();
        Map<String, Object> target = new HashMap<>();
        target.put("id", Map.of("uri", uri));
        target.put("displayName", project.name != null ? project.name : "qin-project");
        target.put("languageIds", List.of("java"));
        target.put("tags", List.of("application"));
        target.put("capabilities", Map.of("canCompile", true, "canRun", true, "canTest", false));
        target.put("baseDirectory", uri);

        // 数据类型标识
        target.put("dataKind", "jvm");
        target.put("data", Map.of(
                "javaHome", System.getProperty("java.home"),
                "javaVersion", project.getJavaVersion()));

        return Map.of("targets", List.of(target));
    }

    private Object handleSources(JsonObject params) {
        String sourceDir = project.getSourceDir();
        Path srcPath = Paths.get(workDir, sourceDir);
        String uri = srcPath.toUri().toString();

        String targetUri = Paths.get(workDir).toUri().toString();

        Map<String, Object> item = Map.of(
                "uri", uri,
                "kind", 1, // 1 = directory
                "generated", false);
        Map<String, Object> sources = Map.of(
                "target", Map.of("uri", targetUri),
                "sources", List.of(item),
                "roots", List.of(uri));
        return Map.of("items", List.of(sources));
    }

    private Object handleDependencySources(JsonObject params) {
        String targetUri = Paths.get(workDir).toUri().toString();

        // 将 classpath 转换为 URI
        List<String> sourceUris = new ArrayList<>();
        for (String path : classpath) {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                if (path.endsWith(".jar")) {
                    sourceUris.add("jar:" + p.toUri().toString() + "!/");
                } else {
                    sourceUris.add(p.toUri().toString());
                }
            }
        }

        Map<String, Object> item = Map.of(
                "target", Map.of("uri", targetUri),
                "sources", sourceUris);
        return Map.of("items", List.of(item));
    }

    private Object handleJavacOptions(JsonObject params) {
        String javaVersion = project.getJavaVersion();
        String targetUri = Paths.get(workDir).toUri().toString();

        List<String> options = new ArrayList<>();
        options.add("--release");
        options.add(javaVersion);

        // classpath URIs
        List<String> cpUris = new ArrayList<>();
        for (String path : classpath) {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                cpUris.add(p.toUri().toString());
            }
        }

        Map<String, Object> item = Map.of(
                "target", Map.of("uri", targetUri),
                "options", options,
                "classpath", cpUris,
                "classDirectory", Paths.get(workDir, QinConstants.BUILD_CLASSES_DIR).toUri().toString());
        return Map.of("items", List.of(item));
    }

    private Object handleCompile(JsonObject params) {
        // 调用 qin compile
        try {
            ProcessBuilder pb = new ProcessBuilder("qin", "compile");
            pb.directory(new File(workDir));
            pb.inheritIO();
            Process p = pb.start();
            int exitCode = p.waitFor();
            return Map.of(
                    "originId", params.has("originId") ? params.get("originId").getAsString() : "",
                    "statusCode", exitCode == 0 ? 1 : 2, // 1 = ok, 2 = error
                    "dataKind", "compile-report",
                    "data", Map.of());
        } catch (Exception e) {
            return Map.of(
                    "originId", "",
                    "statusCode", 2,
                    "dataKind", "compile-report",
                    "data", Map.of("error", e.getMessage()));
        }
    }

    private Object handleRun(JsonObject params) {
        // 调用 qin run
        try {
            ProcessBuilder pb = new ProcessBuilder("qin", "run");
            pb.directory(new File(workDir));
            pb.inheritIO();
            Process p = pb.start();
            int exitCode = p.waitFor();
            return Map.of(
                    "originId", params.has("originId") ? params.get("originId").getAsString() : "",
                    "statusCode", exitCode == 0 ? 1 : 2);
        } catch (Exception e) {
            return Map.of(
                    "originId", "",
                    "statusCode", 2);
        }
    }
}
