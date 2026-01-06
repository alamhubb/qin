package com.qin.core;

import com.google.gson.Gson;
import com.qin.constants.QinConstants;
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

    // ==================== 公开的静态方法 ====================

    /**
     * 扫描工作目录下的所有 Qin 项目路径
     * 供 IDEA 插件等外部调用
     * 
     * @param workingDir 工作目录
     * @return 所有发现的 Qin 项目路径列表
     */
    public static List<Path> scanAllProjects(String workingDir) {
        LocalProjectResolver resolver = new LocalProjectResolver(workingDir);
        List<Path> projects = new ArrayList<>();

        // 向上查找 workspace root
        Path workspaceRoot = resolver.findWorkspaceRoot(resolver.startDir);

        // 从 workspace root 向下扫描
        resolver.scanProjects(workspaceRoot, projects, 0, QinConstants.MAX_SCAN_DEPTH);

        return projects;
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
                // 本地项目:使用 build/classes 路径
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
     * 
     * 新策略：
     * 1. 向上查找 workspace root
     * 2. 从 workspace root 递归向下扫描所有项目
     * 3. 按距离排序（近的优先）
     * 
     * 返回Map: fullName -> ProjectInfo
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        // 使用 LinkedHashMap 保持插入顺序（近 -> 远）
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        // 1. 向上查找 workspace root
        Path workspaceRoot = findWorkspaceRoot(startDir);
        System.err.println("[DEBUG] Workspace root: " + workspaceRoot);

        // 2. 从 workspace root 递归向下扫描所有项目
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        System.err.println("[DEBUG] Found " + projectPaths.size() + " project paths:");
        for (Path p : projectPaths) {
            System.err.println("[DEBUG]   - " + p);
        }

        // 3. 按距离排序（近的优先）
        projectPaths.sort(Comparator.comparingInt(p -> startDir.toAbsolutePath().normalize()
                .relativize(p.toAbsolutePath().normalize())
                .getNameCount()));

        // 4. 加载项目信息（就近优先，已存在的不覆盖）
        for (Path projectPath : projectPaths) {
            try {
                Path configPath = projectPath.resolve("qin.config.json");
                QinConfig config = loadConfig(configPath);
                String fullName = config.name(); // "com.slime:slime-token"

                // 就近优先: 如果已存在，不覆盖
                if (!projects.containsKey(fullName)) {
                    Path buildPath = projectPath.resolve(QinConstants.BUILD_CLASSES_DIR);
                    projects.put(fullName, new ProjectInfo(
                            fullName,
                            projectPath,
                            buildPath));
                    System.err.println("[DEBUG] Added project: " + fullName + " -> " + buildPath);
                }
            } catch (Exception e) {
                System.err.println("[DEBUG] Failed to load config from " + projectPath + ": " + e.getMessage());
            }
        }

        return projects;
    }

    /**
     * 加载qin.config.json
     */
    private QinConfig loadConfig(Path configPath) throws IOException {
        String json = Files.readString(configPath);
        return gson.fromJson(json, QinConfig.class);
    }

    // ==================== workspace 扫描逻辑 ====================
    // 使用 QinConstants.PROJECT_ROOT_MARKERS 和 QinConstants.MAX_SCAN_DEPTH

    /**
     * 向上查找 workspace root
     * 
     * 优先级：
     * 1. IDEA 环境变量（IDEA_INITIAL_DIRECTORY）
     * 2. VSCode 环境变量（VSCODE_CWD）
     * 3. 向上查找，取最远的 .idea/.vscode/.git
     */
    public Path findWorkspaceRoot(Path startDir) {
        // 1. 优先使用 IDEA 环境变量
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir);
            if (Files.exists(ideaPath)) {
                return ideaPath;
            }
        }

        // 2. 其次使用 VSCode 环境变量
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null && !vscodeCwd.isEmpty()) {
            Path vscodePath = Path.of(vscodeCwd);
            if (Files.exists(vscodePath)) {
                return vscodePath;
            }
        }

        // 2. 向上查找，取最远的（最顶层的）
        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir; // 默认使用起始目录

        while (current != null && current.getParent() != null) {
            // 检查是否有项目标志
            final Path finalCurrent = current; // lambda 需要 final
            boolean isProjectRoot = QinConstants.PROJECT_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker))) ||
                    Files.exists(current.resolve("qin.config.json"));

            if (isProjectRoot) {
                topMost = current; // 继续向上，取最顶层的
            }

            current = current.getParent();
        }

        return topMost;
    }

    /**
     * 递归扫描目录查找 qin.config.json
     */
    public void scanProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth || !Files.exists(dir)) {
            return;
        }

        // 先检查当前目录是否有配置文件
        if (Files.exists(dir.resolve(QinConstants.CONFIG_FILE)) && !projects.contains(dir)) {
            projects.add(dir);
        }

        // 扫描子目录
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                // 使用常量排除特殊目录
                if (QinConstants.EXCLUDED_DIRS.contains(dirName) ||
                        dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 忽略目录遍历错误
        }
    }

    /**
     * 本地项目信息
     */
    public static class ProjectInfo {
        public final String fullName; // "com.slime:slime-token"
        public final Path projectDir; // 项目根目录
        public final Path buildClassesPath; // build/classes路径

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
