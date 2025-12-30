package com.qin.core;

import com.google.gson.Gson;
import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * 本地项目解析器
 * 参考jsmanyprojectmanager-cli的逻辑,自动发现和解析本地项目依赖
 * 
 * 核心逻辑:
 * 1. 从当前目录向上查找所有包含qin.config.json的目录
 * 2. 对每个found的目录,扫描其同级目录查找其他项目
 * 3. 就近优先:近的项目覆盖远的同名项目
 * 4. 匹配dependencies中的完整Maven坐标(groupId:artifactId)
 */
public class LocalProjectResolver {

    private final Path startDir;
    private final Gson gson;

    public LocalProjectResolver(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
        this.gson = new Gson();
    }

    /**
     * 解析依赖map,区分本地项目和远程依赖
     * 
     * @param dependencies Maven坐标格式的依赖 {"com.slime:slime-token": "1.0.0"}
     * @return ResolutionResult 包含本地classpath和需要从远程下载的依赖
     */
    public ResolutionResult resolveDependencies(Map<String, String> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new ResolutionResult("", new LinkedHashMap<>());
        }

        // 1. 发现所有本地项目
        Map<String, ProjectInfo> localProjects = discoverLocalProjects();

        // 2. 分类依赖:本地 vs 远程
        List<String> localClasspaths = new ArrayList<>();
        Map<String, String> remoteDependencies = new LinkedHashMap<>();

        for (Map.Entry<String, String> dep : dependencies.entrySet()) {
            String fullName = dep.getKey(); // "com.slime:slime-token"
            String version = dep.getValue(); // "1.0.0"

            ProjectInfo project = localProjects.get(fullName);
            if (project != null) {
                // 本地项目:使用.qin/classes路径
                localClasspaths.add(project.buildClassesPath.toString());
            } else {
                // 远程依赖:需要下载
                remoteDependencies.put(fullName, version);
            }
        }

        String separator = System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";
        String localClasspath = localClasspaths.isEmpty() ? "" : String.join(separator, localClasspaths);

        return new ResolutionResult(localClasspath, remoteDependencies);
    }

    /**
     * 发现所有本地项目
     * 返回Map: fullName -> ProjectInfo
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        // 使用LinkedHashMap保持插入顺序(近->远)
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        // 1. 向上查找所有包含qin.config.json的根目录
        List<Path> projectRoots = findAllProjectRoots();

        // 2. 从近到远遍历,收集同级项目
        for (Path root : projectRoots) {
            collectSiblingProjects(root, projects);
        }

        return projects;
    }

    /**
     * 从当前目录向上查找所有包含qin.config.json的目录
     * 返回从近到远的列表
     */
    private List<Path> findAllProjectRoots() {
        List<Path> roots = new ArrayList<>();
        Path current = startDir;

        // 向上遍历直到文件系统根目录
        while (current != null) {
            Path configPath = current.resolve("qin.config.json");
            if (Files.exists(configPath)) {
                roots.add(current);
            }
            current = current.getParent();
        }

        return roots;
    }

    /**
     * 收集某个项目目录的所有同级项目
     * 
     * @param projectRoot 项目根目录
     * @param projects    结果map(就近优先,已存在的不覆盖)
     */
    private void collectSiblingProjects(Path projectRoot, Map<String, ProjectInfo> projects) {
        Path parent = projectRoot.getParent();
        if (parent == null)
            return;

        try (DirectoryStream<Path> siblings = Files.newDirectoryStream(parent, Files::isDirectory)) {
            for (Path sibling : siblings) {
                // 跳过特殊目录
                String dirName = sibling.getFileName().toString();
                if (dirName.equals("node_modules") || dirName.startsWith(".")) {
                    continue;
                }

                Path configPath = sibling.resolve("qin.config.json");
                if (Files.exists(configPath)) {
                    try {
                        QinConfig config = loadConfig(configPath);
                        String fullName = config.name(); // "com.slime:slime-token"

                        // 就近优先:如果已存在,不覆盖
                        if (!projects.containsKey(fullName)) {
                            Path buildPath = sibling.resolve(".qin/classes");
                            projects.put(fullName, new ProjectInfo(
                                    fullName,
                                    sibling,
                                    buildPath));
                        }
                    } catch (Exception e) {
                        // 忽略无法解析的配置
                    }
                }
            }
        } catch (IOException e) {
            // 忽略目录遍历错误
        }
    }

    /**
     * 加载qin.config.json
     */
    private QinConfig loadConfig(Path configPath) throws IOException {
        String json = Files.readString(configPath);
        return gson.fromJson(json, QinConfig.class);
    }

    /**
     * 本地项目信息
     */
    public static class ProjectInfo {
        public final String fullName; // "com.slime:slime-token"
        public final Path projectDir; // 项目根目录
        public final Path buildClassesPath; // .qin/classes路径

        public ProjectInfo(String fullName, Path projectDir, Path buildClassesPath) {
            this.fullName = fullName;
            this.projectDir = projectDir;
            this.buildClassesPath = buildClassesPath;
        }

        @Override
        public String toString() {
            return String.format("ProjectInfo{name=%s, dir=%s}", fullName, projectDir);
        }
    }

    /**
     * 依赖解析结果
     * 包含本地classpath和需要从远程下载的依赖
     */
    public static class ResolutionResult {
        public final String localClasspath; // 本地项目的classpath字符串
        public final Map<String, String> remoteDependencies; // 需要从Maven下载的依赖

        public ResolutionResult(String localClasspath, Map<String, String> remoteDependencies) {
            this.localClasspath = localClasspath;
            this.remoteDependencies = remoteDependencies;
        }
    }
}
