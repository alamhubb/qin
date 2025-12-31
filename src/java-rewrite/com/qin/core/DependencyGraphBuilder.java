package com.qin.core;

import com.qin.types.QinConfig;
import java.nio.file.Path;
import java.util.*;

/**
 * 依赖图构建器
 * 用于构建本地项目的依赖关系图并进行拓扑排序
 */
public class DependencyGraphBuilder {

    /**
     * 依赖图节点
     */
    public static class DependencyNode {
        public final String projectName; // 项目名称（Maven坐标）
        public final Path projectDir; // 项目目录
        public final Set<String> dependencies; // 依赖的其他项目

        public DependencyNode(String projectName, Path projectDir) {
            this.projectName = projectName;
            this.projectDir = projectDir;
            this.dependencies = new HashSet<>();
        }

        public void addDependency(String dep) {
            dependencies.add(dep);
        }
    }

    /**
     * 依赖图
     */
    public static class DependencyGraph {
        private final Map<String, DependencyNode> nodes = new LinkedHashMap<>();

        public void addNode(DependencyNode node) {
            nodes.put(node.projectName, node);
        }

        public DependencyNode getNode(String name) {
            return nodes.get(name);
        }

        public Collection<DependencyNode> getAllNodes() {
            return nodes.values();
        }

        public boolean containsNode(String name) {
            return nodes.containsKey(name);
        }
    }

    /**
     * 构建依赖图
     * 
     * @param rootProjectName  根项目名称
     * @param allLocalProjects 所有本地项目的配置映射 (项目名 -> 项目信息)
     * @return 依赖图
     */
    public DependencyGraph buildGraph(
            String rootProjectName,
            Map<String, LocalProjectResolver.ProjectInfo> allLocalProjects) {

        DependencyGraph graph = new DependencyGraph();
        Set<String> visited = new HashSet<>();

        // 从根项目开始递归构建
        buildGraphRecursive(rootProjectName, allLocalProjects, graph, visited);

        return graph;
    }

    /**
     * 递归构建依赖图
     */
    private void buildGraphRecursive(
            String projectName,
            Map<String, LocalProjectResolver.ProjectInfo> allLocalProjects,
            DependencyGraph graph,
            Set<String> visited) {

        if (visited.contains(projectName)) {
            return; // 避免循环依赖
        }
        visited.add(projectName);

        LocalProjectResolver.ProjectInfo projectInfo = allLocalProjects.get(projectName);
        if (projectInfo == null) {
            return; // 不是本地项目，跳过
        }

        // 创建节点
        DependencyNode node = new DependencyNode(projectName, projectInfo.projectDir);

        // 加载项目配置，获取其依赖
        try {
            QinConfig config = loadConfig(projectInfo.projectDir);
            if (config.dependencies() != null) {
                for (String depName : config.dependencies().keySet()) {
                    // 只处理本地依赖
                    if (allLocalProjects.containsKey(depName)) {
                        node.addDependency(depName);
                        // 递归处理依赖
                        buildGraphRecursive(depName, allLocalProjects, graph, visited);
                    }
                }
            }
        } catch (Exception e) {
            // 忽略配置加载失败
        }

        graph.addNode(node);
    }

    /**
     * 拓扑排序 - 返回编译顺序
     * 使用 Kahn 算法
     * 
     * @param graph 依赖图
     * @return 按依赖顺序排列的项目列表（被依赖的在前）
     */
    public List<String> topologicalSort(DependencyGraph graph) {
        List<String> result = new ArrayList<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // 计算入度
        for (DependencyNode node : graph.getAllNodes()) {
            inDegree.putIfAbsent(node.projectName, 0);
            for (String dep : node.dependencies) {
                inDegree.put(dep, inDegree.getOrDefault(dep, 0) + 1);
            }
        }

        // 找出入度为 0 的节点（没有依赖其他项目）
        Queue<String> queue = new LinkedList<>();
        for (DependencyNode node : graph.getAllNodes()) {
            if (inDegree.get(node.projectName) == 0) {
                queue.offer(node.projectName);
            }
        }

        // Kahn 算法
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            DependencyNode node = graph.getNode(current);
            if (node != null) {
                for (String dep : node.dependencies) {
                    int newDegree = inDegree.get(dep) - 1;
                    inDegree.put(dep, newDegree);
                    if (newDegree == 0) {
                        queue.offer(dep);
                    }
                }
            }
        }

        // 检测循环依赖
        if (result.size() != graph.getAllNodes().size()) {
            throw new RuntimeException("Circular dependency detected in local projects");
        }

        return result;
    }

    /**
     * 加载项目配置
     */
    private QinConfig loadConfig(Path projectDir) throws Exception {
        Path configPath = projectDir.resolve("qin.config.json");
        String json = java.nio.file.Files.readString(configPath);
        return new com.google.gson.Gson().fromJson(json, QinConfig.class);
    }
}
