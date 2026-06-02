package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * 閺堫剙婀存い鍦窗鐟欙絾鐎介崳?
 * 閸欏倽鈧儻smanyprojectmanager-cli閻ㄥ嫰鈧槒绶?閼奉亜濮╅崣鎴犲箛閸滃矁袙閺嬫劖婀伴崷浼淬€嶉惄顔荤贩鐠?
 * 
 * 閺嶇绺鹃柅鏄忕帆:
 * 1. 娴犲骸缍嬮崜宥囨窗瑜版洖鎮滄稉濠冪叀閹电偓澧嶉張澶婂瘶閸氱幍in.config.json閻ㄥ嫮娲拌ぐ?
 * 2. 鐎佃鐦℃稉鐚und閻ㄥ嫮娲拌ぐ?閹殿偅寮块崗璺烘倱缁狙呮窗瑜版洘鐓￠幍鎯у従娴犳牠銆嶉惄?
 * 3. 鐏忚精绻庢导妯哄帥:鏉╂垹娈戞い鍦窗鐟曞棛娲婃潻婊呮畱閸氬苯鎮曟い鍦窗
 * 4. 閸栧綊鍘ependencies娑擃厾娈戠€瑰本鏆aven閸ф劖鐖?groupId:artifactId)
 */
public class LocalProjectResolver {
    private static final boolean DEBUG = isDebugEnabled();

    private final Path startDir;
    public LocalProjectResolver(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
    }

    // ==================== 閸忣剙绱戦惃鍕饯閹焦鏌熷▔?====================

    /**
     * 閹殿偅寮垮銉ょ稊閻╊喖缍嶆稉瀣畱閹碘偓閺?Qin 妞ゅ湱娲扮捄顖氱窞
     * 娓?IDEA 閹绘帊娆㈢粵澶婎樆闁劏鐨熼悽?
     * 
     * @param workingDir 瀹搞儰缍旈惄顔肩秿
     * @return 閹碘偓閺堝褰傞悳鎵畱 Qin 妞ゅ湱娲扮捄顖氱窞閸掓銆?
     */
    public static List<Path> scanAllProjects(String workingDir) {
        LocalProjectResolver resolver = new LocalProjectResolver(workingDir);
        List<Path> projects = new ArrayList<>();

        // 閸氭垳绗傞弻銉﹀ workspace root
        Path workspaceRoot = resolver.findWorkspaceRoot(resolver.startDir);

        // 娴?workspace root 閸氭垳绗呴幍顐ｅ伎
        resolver.scanProjects(workspaceRoot, projects, 0, QinConstants.MAX_SCAN_DEPTH);

        return projects;
    }

    /**
     * 鐟欙絾鐎芥笟婵婄map,閸栧搫鍨庨張顒€婀存い鍦窗閸滃矁绻欑粙瀣╃贩鐠?
     * 
     * @param dependencies Maven閸ф劖鐖ｉ弽鐓庣础閻ㄥ嫪绶风挧?{"com.slime:slime-token": "1.0.0"}
     * @return ResolutionResult 閸栧懎鎯堥張顒€婀碿lasspath閸滃矂娓剁憰浣风矤鏉╂粎鈻兼稉瀣祰閻ㄥ嫪绶风挧?
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
     * 閸欐垹骞囬幍鈧張澶嬫拱閸︿即銆嶉惄?
     * 
     * 閺傛壆鐡ラ悾銉窗
     * 1. 閸氭垳绗傞弻銉﹀ workspace root
     * 2. 娴?workspace root 闁帒缍婇崥鎴滅瑓閹殿偅寮块幍鈧張澶愩€嶉惄?
     * 3. 閹稿绐涚粋缁樺笓鎼村骏绱欐潻鎴犳畱娴兼ê鍘涢敍?
     * 
     * 鏉╂柨娲朚ap: fullName -> ProjectInfo
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        // 娴ｈ法鏁?LinkedHashMap 娣囨繃瀵旈幓鎺戝弳妞ゅ搫绨敍鍫ｇ箮 -> 鏉╂粣绱?
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        // 1. 閸氭垳绗傞弻銉﹀ workspace root
        Path workspaceRoot = findWorkspaceRoot(startDir);
        debug("Workspace root: " + workspaceRoot);

        // 2. 娴?workspace root 闁帒缍婇崥鎴滅瑓閹殿偅寮块幍鈧張澶愩€嶉惄?
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        debug("Found " + projectPaths.size() + " project paths:");
        for (Path p : projectPaths) {
            debug("  - " + p);
        }

        // 3. 閹稿绐涚粋缁樺笓鎼村骏绱欐潻鎴犳畱娴兼ê鍘涢敍?
        projectPaths.sort(Comparator.comparingInt(p -> startDir.toAbsolutePath().normalize()
                .relativize(p.toAbsolutePath().normalize())
                .getNameCount()));

        // 4. 閸旂姾娴囨い鍦窗娣団剝浼呴敍鍫濇皑鏉╂垳绱崗鍫礉瀹告彃鐡ㄩ崷銊ф畱娑撳秷顩惄鏍电礆
        for (Path projectPath : projectPaths) {
            try {
                Path configPath = projectPath.resolve(QinConstants.CONFIG_FILE);
                QinConfig config = loadConfig(configPath);
                if (config == null || config.name() == null || config.name().isBlank()) {
                    debug("Skip invalid qin.config.js: " + projectPath);
                    continue;
                }
                String fullName = config.name(); // "com.slime:slime-token"

                // 鐏忚精绻庢导妯哄帥: 婵″倹鐏夊鎻掔摠閸︻煉绱濇稉宥堫洬閻?
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
     * 閸旂姾娴噏in.config.json
     */
    private QinConfig loadConfig(Path configPath) throws IOException {
        Path projectDir = configPath == null ? null : configPath.getParent();
        if (projectDir == null) {
            throw new IOException("qin.config.js path has no parent");
        }
        return new ConfigLoader(projectDir.toString()).load();
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

    // ==================== workspace 閹殿偅寮块柅鏄忕帆 ====================
    // 娴ｈ法鏁?QinConstants.PROJECT_ROOT_MARKERS 閸?QinConstants.MAX_SCAN_DEPTH

    /**
     * 閸氭垳绗傞弻銉﹀ workspace root
     * 
     * 娴兼ê鍘涚痪褝绱?
     * 1. IDEA 閻滎垰顣ㄩ崣姗€鍣洪敍鍦汥EA_INITIAL_DIRECTORY閿?
     * 2. VSCode 閻滎垰顣ㄩ崣姗€鍣洪敍鍦碨CODE_CWD閿?
     * 3. 閸氭垳绗傞弻銉﹀閿涘苯褰囬張鈧潻婊呮畱 .idea/.vscode/.git
     */
    public Path findWorkspaceRoot(Path startDir) {
        // 1. 娴兼ê鍘涙担璺ㄦ暏 IDEA 閻滎垰顣ㄩ崣姗€鍣?
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir).toAbsolutePath().normalize();
            if (Files.exists(ideaPath) && startDir.toAbsolutePath().normalize().startsWith(ideaPath)) {
                return ideaPath;
            }
        }

        // 2. 閸忚埖顐兼担璺ㄦ暏 VSCode 閻滎垰顣ㄩ崣姗€鍣?
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null && !vscodeCwd.isEmpty()) {
            Path vscodePath = Path.of(vscodeCwd).toAbsolutePath().normalize();
            if (Files.exists(vscodePath) && startDir.toAbsolutePath().normalize().startsWith(vscodePath)) {
                return vscodePath;
            }
        }

        // 2. 閸氭垳绗傞弻銉﹀閿涘苯褰囬張鈧潻婊呮畱閿涘牊娓舵い璺虹湴閻ㄥ嫸绱?
        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir; // 姒涙顓绘担璺ㄦ暏鐠у嘲顫愰惄顔肩秿

        while (current != null && current.getParent() != null) {
            // 濡偓閺屻儲妲搁崥锔芥箒妞ゅ湱娲伴弽鍥х箶
            final Path finalCurrent = current; // lambda 闂団偓鐟?final
            boolean isProjectRoot = QinConstants.WORKSPACE_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker)));

            if (isProjectRoot) {
                topMost = current; // 缂佈呯敾閸氭垳绗傞敍灞藉絿閺堚偓妞よ泛鐪伴惃?
            }

            Path parent = current.getParent();
            if (parent != null && Files.exists(current.resolve(QinConstants.CONFIG_FILE))) {
                // A Qin project should let its direct parent participate in workspace discovery,
                // even when that parent has no IDE/VCS marker of its own.
                topMost = parent;
            }
            if (parent != null && hasSiblingQinProjects(parent, current)) {
                topMost = parent;
            }

            current = current.getParent();
        }

        return topMost;
    }

    /**
     * 闁帒缍婇幍顐ｅ伎閻╊喖缍嶉弻銉﹀ qin.config.js
     */
    private boolean hasSiblingQinProjects(Path candidateRoot, Path currentChildOrDescendant) {
        if (candidateRoot == null || currentChildOrDescendant == null || !Files.isDirectory(candidateRoot)) {
            return false;
        }
        Path currentNormalized = currentChildOrDescendant.toAbsolutePath().normalize();
        int qinProjectChildren = 0;
        boolean containsCurrentProject = false;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(candidateRoot, Files::isDirectory)) {
            for (Path child : stream) {
                String dirName = child.getFileName().toString();
                if (QinConstants.EXCLUDED_DIRS.contains(dirName) || dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }
                if (!Files.exists(child.resolve(QinConstants.CONFIG_FILE))) {
                    continue;
                }
                qinProjectChildren++;
                if (currentNormalized.startsWith(child.toAbsolutePath().normalize())) {
                    containsCurrentProject = true;
                }
            }
        } catch (IOException ignored) {
            return false;
        }
        return containsCurrentProject && qinProjectChildren >= 2;
    }

    public void scanProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth || !Files.exists(dir)) {
            return;
        }

        // 閸忓牊顥呴弻銉ョ秼閸撳秶娲拌ぐ鏇熸Ц閸氾附婀侀柊宥囩枂閺傚洣娆?
        if (Files.exists(dir.resolve(QinConstants.CONFIG_FILE)) && !projects.contains(dir)) {
            projects.add(dir);
        }

        // 閹殿偅寮跨€涙劗娲拌ぐ?
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                // 娴ｈ法鏁ょ敮鎼佸櫤閹烘帡娅庨悧瑙勭暕閻╊喖缍?
                if (QinConstants.EXCLUDED_DIRS.contains(dirName) ||
                        dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 韫囩晫鏆愰惄顔肩秿闁秴宸婚柨娆掝嚖
        }
    }

    /**
     * 閺堫剙婀存い鍦窗娣団剝浼?
     */
    public static class ProjectInfo {
        public final String fullName; // "com.slime:slime-token"
        public final Path projectDir; // 妞ゅ湱娲伴弽鍦窗瑜?
        public final Path buildClassesPath; // build/classes鐠侯垰绶?

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
     * 娓氭繆绂嗙憴锝嗙€界紒鎾寸亯
     * 閸栧懎鎯堥張顒€婀碿lasspath閸滃矂娓剁憰浣风矤鏉╂粎鈻兼稉瀣祰閻ㄥ嫪绶风挧?
     */
    public static class ResolutionResult {
        public final String localClasspath; // 閺堫剙婀存い鍦窗閻ㄥ垻lasspath鐎涙顑佹稉?
        public final Map<String, String> remoteDependencies; // 闂団偓鐟曚椒绮燤aven娑撳娴囬惃鍕贩鐠?

        public ResolutionResult(String localClasspath, Map<String, String> remoteDependencies) {
            this.localClasspath = localClasspath;
            this.remoteDependencies = remoteDependencies;
        }
    }
}

