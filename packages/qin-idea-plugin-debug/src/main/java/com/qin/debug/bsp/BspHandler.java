package com.qin.debug.bsp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        loadProject();
        loadClasspath();
    }

    /**
     * 获取项目信息
     */
    public QinProjectInfo getProject() {
        return project;
    }

    /**
     * 获取 classpath
     */
    public List<String> getClasspath() {
        return classpath;
    }

    /**
     * 获取源代码目录
     */
    public String getSourceDir() {
        return project.getSourceDir();
    }

    /**
     * 获取输出目录
     */
    public String getOutputDir() {
        return project.getOutputDir();
    }

    /**
     * 获取 Java 版本
     */
    public String getJavaVersion() {
        return project.getJavaVersion();
    }

    /**
     * 重新加载项目信息
     */
    public void reload() {
        loadProject();
        loadClasspath();
    }

    private void loadProject() {
        try {
            Path configPath = Paths.get(workDir, "qin.config.json");
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                project = gson.fromJson(json, QinProjectInfo.class);
            } else {
                project = new QinProjectInfo();
            }
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
}
