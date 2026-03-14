package com.qin.core;

import com.google.gson.Gson;
import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * 鏈湴椤圭洰瑙ｆ瀽鍣?
 * 鍙傝€僯smanyprojectmanager-cli鐨勯€昏緫,鑷姩鍙戠幇鍜岃В鏋愭湰鍦伴」鐩緷璧?
 * 
 * 鏍稿績閫昏緫:
 * 1. 浠庡綋鍓嶇洰褰曞悜涓婃煡鎵炬墍鏈夊寘鍚玵in.config.json鐨勭洰褰?
 * 2. 瀵规瘡涓猣ound鐨勭洰褰?鎵弿鍏跺悓绾х洰褰曟煡鎵惧叾浠栭」鐩?
 * 3. 灏辫繎浼樺厛:杩戠殑椤圭洰瑕嗙洊杩滅殑鍚屽悕椤圭洰
 * 4. 鍖归厤dependencies涓殑瀹屾暣Maven鍧愭爣(groupId:artifactId)
 */
public class LocalProjectResolver {
    private static final boolean DEBUG = isDebugEnabled();

    private final Path startDir;
    private final Gson gson;

    public LocalProjectResolver(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
        this.gson = new Gson();
    }

    // ==================== 鍏紑鐨勯潤鎬佹柟娉?====================

    /**
     * 鎵弿宸ヤ綔鐩綍涓嬬殑鎵€鏈?Qin 椤圭洰璺緞
     * 渚?IDEA 鎻掍欢绛夊閮ㄨ皟鐢?
     * 
     * @param workingDir 宸ヤ綔鐩綍
     * @return 鎵€鏈夊彂鐜扮殑 Qin 椤圭洰璺緞鍒楄〃
     */
    public static List<Path> scanAllProjects(String workingDir) {
        LocalProjectResolver resolver = new LocalProjectResolver(workingDir);
        List<Path> projects = new ArrayList<>();

        // 鍚戜笂鏌ユ壘 workspace root
        Path workspaceRoot = resolver.findWorkspaceRoot(resolver.startDir);

        // 浠?workspace root 鍚戜笅鎵弿
        resolver.scanProjects(workspaceRoot, projects, 0, QinConstants.MAX_SCAN_DEPTH);

        return projects;
    }

    /**
     * 瑙ｆ瀽渚濊禆map,鍖哄垎鏈湴椤圭洰鍜岃繙绋嬩緷璧?
     * 
     * @param dependencies Maven鍧愭爣鏍煎紡鐨勪緷璧?{"com.slime:slime-token": "1.0.0"}
     * @return ResolutionResult 鍖呭惈鏈湴classpath鍜岄渶瑕佷粠杩滅▼涓嬭浇鐨勪緷璧?
     */
    public ResolutionResult resolveDependencies(Map<String, String> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new ResolutionResult("", new LinkedHashMap<>());
        }

        // 1) Discover all local projects.
        Map<String, ProjectInfo> localProjects = discoverLocalProjects();

        // 2) Split local and remote dependencies, including transitives.
        List<String> localClasspaths = new ArrayList<>();
        Map<String, String> remoteDependencies = new LinkedHashMap<>();
        Set<String> visited = new HashSet<>();
        Deque<Map.Entry<String, String>> queue = new ArrayDeque<>(dependencies.entrySet());

        while (!queue.isEmpty()) {
            Map.Entry<String, String> dep = queue.removeFirst();
            String fullName = dep.getKey();
            String version = dep.getValue();

            if (!visited.add(fullName)) {
                continue;
            }

            ProjectInfo project = findProject(localProjects, fullName);
            if (project != null) {
                String classesPath = project.buildClassesPath.toString();
                if (!localClasspaths.contains(classesPath)) {
                    localClasspaths.add(classesPath);
                }
                debug("Matched local: " + fullName + " -> " + project.buildClassesPath);

                // Add transitive dependencies declared by this local project.
                try {
                    Path configPath = project.projectDir.resolve(QinConstants.CONFIG_FILE);
                    if (Files.exists(configPath)) {
                        QinConfig projectConfig = loadConfig(configPath);
                        if (projectConfig.dependencies() != null && !projectConfig.dependencies().isEmpty()) {
                            queue.addAll(projectConfig.dependencies().entrySet());
                        }
                    }
                } catch (Exception e) {
                    debug("Failed to load transitives from " + project.projectDir + ": " + e.getMessage());
                }
            } else {
                remoteDependencies.putIfAbsent(fullName, version);
                debug("Not found locally: " + fullName);
            }
        }

        String separator = QinConstants.getClasspathSeparator();
        String localClasspath = localClasspaths.isEmpty() ? "" : String.join(separator, localClasspaths);

        return new ResolutionResult(localClasspath, remoteDependencies);
    }

    /**
     * 鍙戠幇鎵€鏈夋湰鍦伴」鐩?
     * 
     * 鏂扮瓥鐣ワ細
     * 1. 鍚戜笂鏌ユ壘 workspace root
     * 2. 浠?workspace root 閫掑綊鍚戜笅鎵弿鎵€鏈夐」鐩?
     * 3. 鎸夎窛绂绘帓搴忥紙杩戠殑浼樺厛锛?
     * 
     * 杩斿洖Map: fullName -> ProjectInfo
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        // 浣跨敤 LinkedHashMap 淇濇寔鎻掑叆椤哄簭锛堣繎 -> 杩滐級
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        // 1. 鍚戜笂鏌ユ壘 workspace root
        Path workspaceRoot = findWorkspaceRoot(startDir);
        debug("Workspace root: " + workspaceRoot);

        // 2. 浠?workspace root 閫掑綊鍚戜笅鎵弿鎵€鏈夐」鐩?
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        debug("Found " + projectPaths.size() + " project paths:");
        for (Path p : projectPaths) {
            debug("  - " + p);
        }

        // 3. 鎸夎窛绂绘帓搴忥紙杩戠殑浼樺厛锛?
        projectPaths.sort(Comparator.comparingInt(p -> startDir.toAbsolutePath().normalize()
                .relativize(p.toAbsolutePath().normalize())
                .getNameCount()));

        // 4. 鍔犺浇椤圭洰淇℃伅锛堝氨杩戜紭鍏堬紝宸插瓨鍦ㄧ殑涓嶈鐩栵級
        for (Path projectPath : projectPaths) {
            try {
                Path configPath = projectPath.resolve(QinConstants.CONFIG_FILE);
                QinConfig config = loadConfig(configPath);
                if (config == null || config.name() == null || config.name().isBlank()) {
                    debug("Skip invalid qin.config.json: " + projectPath);
                    continue;
                }
                String fullName = config.name(); // "com.slime:slime-token"

                // 灏辫繎浼樺厛: 濡傛灉宸插瓨鍦紝涓嶈鐩?
                Path buildPath = projectPath.resolve(QinConstants.BUILD_CLASSES_DIR);
                ProjectInfo projectInfo = new ProjectInfo(fullName, projectPath, buildPath);
                for (String alias : collectProjectAliases(fullName, projectPath)) {
                    projects.putIfAbsent(alias, projectInfo);
                }
                debug("Added project: " + fullName + " -> " + buildPath);
            } catch (Exception e) {
                debug("Failed to load config from " + projectPath + ": " + e.getMessage());
            }
        }

        return projects;
    }

    private ProjectInfo findProject(Map<String, ProjectInfo> projects, String dependencyName) {
        if (dependencyName == null || dependencyName.isBlank()) {
            return null;
        }

        ProjectInfo direct = projects.get(dependencyName);
        if (direct != null) {
            return direct;
        }

        String normalized = dependencyName.trim().toLowerCase(Locale.ROOT);
        ProjectInfo normalizedDirect = projects.get(normalized);
        if (normalizedDirect != null) {
            return normalizedDirect;
        }

        for (ProjectInfo candidate : new LinkedHashSet<>(projects.values())) {
            if (matchesProjectAlias(candidate, normalized)) {
                return candidate;
            }
        }

        return null;
    }

    private List<String> collectProjectAliases(String fullName, Path projectPath) {
        LinkedHashSet<String> aliases = new LinkedHashSet<>();
        aliases.add(fullName);
        aliases.add(fullName.toLowerCase(Locale.ROOT));

        String qinCoordinate = QinConstants.toQinCoordinate(fullName);
        aliases.add(qinCoordinate);
        aliases.add(qinCoordinate.toLowerCase(Locale.ROOT));

        String artifactId = extractArtifactId(fullName);
        if (!artifactId.isBlank()) {
            aliases.add(artifactId);
            aliases.add(artifactId.toLowerCase(Locale.ROOT));

            if (artifactId.endsWith("-java")) {
                String shortAlias = artifactId.substring(0, artifactId.length() - "-java".length());
                aliases.add(shortAlias);
                aliases.add(shortAlias.toLowerCase(Locale.ROOT));
            }
        }

        String dirName = projectPath.getFileName().toString();
        aliases.add(dirName);
        aliases.add(dirName.toLowerCase(Locale.ROOT));

        return new ArrayList<>(aliases);
    }

    private String extractArtifactId(String fullName) {
        int atIndex = fullName.lastIndexOf('@');
        int colonIndex = fullName.lastIndexOf(':');
        int splitIndex = Math.max(atIndex, colonIndex);
        return splitIndex >= 0 ? fullName.substring(splitIndex + 1) : fullName;
    }

    private boolean matchesProjectAlias(ProjectInfo project, String dependencyName) {
        String fullName = project.fullName.toLowerCase(Locale.ROOT);
        if (fullName.equals(dependencyName) || QinConstants.toQinCoordinate(project.fullName).toLowerCase(Locale.ROOT).equals(dependencyName)) {
            return true;
        }

        String artifactId = extractArtifactId(project.fullName).toLowerCase(Locale.ROOT);
        if (artifactId.equals(dependencyName)) {
            return true;
        }

        if (artifactId.endsWith("-java")
                && artifactId.substring(0, artifactId.length() - "-java".length()).equals(dependencyName)) {
            return true;
        }

        return project.projectDir.getFileName().toString().toLowerCase(Locale.ROOT).equals(dependencyName);
    }

    /**
     * 鍔犺浇qin.config.json
     */
    private QinConfig loadConfig(Path configPath) throws IOException {
        String json = Files.readString(configPath);
        if (json == null || json.isBlank()) {
            throw new IOException("qin.config.json is empty");
        }
        QinConfig config = gson.fromJson(json, QinConfig.class);
        if (config == null) {
            throw new IOException("qin.config.json parsed to null");
        }
        return config;
    }

    private static boolean isDebugEnabled() {
        String env = System.getenv("QIN_DEBUG");
        if (env != null) {
            return "1".equals(env) || "true".equalsIgnoreCase(env);
        }
        String prop = System.getProperty("qin.debug");
        return "1".equals(prop) || "true".equalsIgnoreCase(prop);
    }

    private static void debug(String message) {
        if (DEBUG) {
            System.err.println("[DEBUG] " + message);
        }
    }

    // ==================== workspace 鎵弿閫昏緫 ====================
    // 浣跨敤 QinConstants.PROJECT_ROOT_MARKERS 鍜?QinConstants.MAX_SCAN_DEPTH

    /**
     * 鍚戜笂鏌ユ壘 workspace root
     * 
     * 浼樺厛绾э細
     * 1. IDEA 鐜鍙橀噺锛圛DEA_INITIAL_DIRECTORY锛?
     * 2. VSCode 鐜鍙橀噺锛圴SCODE_CWD锛?
     * 3. 鍚戜笂鏌ユ壘锛屽彇鏈€杩滅殑 .idea/.vscode/.git
     */
    public Path findWorkspaceRoot(Path startDir) {
        // 1. 浼樺厛浣跨敤 IDEA 鐜鍙橀噺
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir).toAbsolutePath().normalize();
            if (Files.exists(ideaPath) && startDir.toAbsolutePath().normalize().startsWith(ideaPath)) {
                return ideaPath;
            }
        }

        // 2. 鍏舵浣跨敤 VSCode 鐜鍙橀噺
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null && !vscodeCwd.isEmpty()) {
            Path vscodePath = Path.of(vscodeCwd).toAbsolutePath().normalize();
            if (Files.exists(vscodePath) && startDir.toAbsolutePath().normalize().startsWith(vscodePath)) {
                return vscodePath;
            }
        }

        // 2. 鍚戜笂鏌ユ壘锛屽彇鏈€杩滅殑锛堟渶椤跺眰鐨勶級
        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir; // 榛樿浣跨敤璧峰鐩綍

        while (current != null && current.getParent() != null) {
            // 妫€鏌ユ槸鍚︽湁椤圭洰鏍囧織
            final Path finalCurrent = current; // lambda 闇€瑕?final
            boolean isProjectRoot = QinConstants.WORKSPACE_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker)));

            if (isProjectRoot) {
                topMost = current; // 缁х画鍚戜笂锛屽彇鏈€椤跺眰鐨?
            }

            current = current.getParent();
        }

        return topMost;
    }

    /**
     * 閫掑綊鎵弿鐩綍鏌ユ壘 qin.config.json
     */
    public void scanProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth || !Files.exists(dir)) {
            return;
        }

        // 鍏堟鏌ュ綋鍓嶇洰褰曟槸鍚︽湁閰嶇疆鏂囦欢
        if (Files.exists(dir.resolve(QinConstants.CONFIG_FILE)) && !projects.contains(dir)) {
            projects.add(dir);
        }

        // 鎵弿瀛愮洰褰?
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                // 浣跨敤甯搁噺鎺掗櫎鐗规畩鐩綍
                if (QinConstants.EXCLUDED_DIRS.contains(dirName) ||
                        dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 蹇界暐鐩綍閬嶅巻閿欒
        }
    }

    /**
     * 鏈湴椤圭洰淇℃伅
     */
    public static class ProjectInfo {
        public final String fullName; // "com.slime:slime-token"
        public final Path projectDir; // 椤圭洰鏍圭洰褰?
        public final Path buildClassesPath; // build/classes璺緞

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
     * 渚濊禆瑙ｆ瀽缁撴灉
     * 鍖呭惈鏈湴classpath鍜岄渶瑕佷粠杩滅▼涓嬭浇鐨勪緷璧?
     */
    public static class ResolutionResult {
        public final String localClasspath; // 鏈湴椤圭洰鐨刢lasspath瀛楃涓?
        public final Map<String, String> remoteDependencies; // 闇€瑕佷粠Maven涓嬭浇鐨勪緷璧?

        public ResolutionResult(String localClasspath, Map<String, String> remoteDependencies) {
            this.localClasspath = localClasspath;
            this.remoteDependencies = remoteDependencies;
        }
    }
}
