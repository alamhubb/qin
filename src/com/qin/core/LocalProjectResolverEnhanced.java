package com.qin.core;

import com.google.gson.Gson;
import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 濠⒀呭仜瀹搁亶鎮ч崼鐔告嫳闁革缚鍗抽妴宥夋儎椤斿晝鎺楀几閹邦剚鐝?
 * 闁哄倹婢橀·鍐礉閻旇鍘撮柨?
 * 1. 闁煎浜滄慨鈺冪磽閺嶎剛妲柡鍫墮濠€瀛樸亜閸︻厽绐楀〒姘箚缁?
 * 2. 闁哄懘缂氶崗妯绘櫠閻愬搫娅ょ紓鍌涚墳閻ρ兾涢埀顒€霉?
 * 3. 闁哄洦娼欏鍛婄附閻ｅ本鐣遍弶鍫熸尭閸ゎ厽绌遍埄鍐х礀
 */
public class LocalProjectResolverEnhanced {

    private final Path startDir;
    private final Gson gson;

    public LocalProjectResolverEnhanced(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
        this.gson = new Gson();
    }

    /**
     * 閻熸瑱绲鹃悗鑺ョ瑹濠靛﹦顩柨娑樻湰閺侇噣骞愭担钘夋闁告柣鍔庣槐顏嗘嫚閹寸偞鎷遍柛锔垮嵆閵嗗秹鎯?
     */
    public ResolutionResult resolveDependencies(Map<String, String> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new ResolutionResult("", new LinkedHashMap<>(), 0, 0);
        }

        // 1) Discover all local projects.
        Map<String, ProjectInfo> localProjects = discoverLocalProjects();

        // 2) Resolve direct + transitive dependencies.
        List<String> localClasspaths = new ArrayList<>();
        Map<String, String> remoteDependencies = new LinkedHashMap<>();
        int autoCompiledCount = 0;
        int localCount = 0;

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
                localCount++;
                System.out.println("  [local] " + fullName + " -> " + project.projectDir.getFileName());

                if (ensureCompiled(project)) {
                    autoCompiledCount++;
                }

                String classesPath = project.buildClassesPath.toString();
                if (!localClasspaths.contains(classesPath)) {
                    localClasspaths.add(classesPath);
                }

                // Add transitive dependencies from local project config.
                try {
                    Path configPath = project.projectDir.resolve(QinConstants.CONFIG_FILE);
                    if (Files.exists(configPath)) {
                        QinConfig projectConfig = loadConfig(configPath);
                        if (projectConfig.dependencies() != null && !projectConfig.dependencies().isEmpty()) {
                            queue.addAll(projectConfig.dependencies().entrySet());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("  [WARN] Failed to load transitives from "
                            + project.projectDir + ": " + e.getMessage());
                }
            } else {
                remoteDependencies.putIfAbsent(fullName, version);
            }
        }

        if (autoCompiledCount > 0) {
            System.out.println("  [ok] Auto-compiled " + autoCompiledCount + " local project(s)");
        }

        String separator = QinConstants.getClasspathSeparator();
        String localClasspath = localClasspaths.isEmpty() ? "" :
                               String.join(separator, localClasspaths);

        return new ResolutionResult(localClasspath, remoteDependencies,
                                   localCount, autoCompiledCount);
    }

    /**
     * 缁绢収鍠曠换姘亜閸︻厽绐楃€规瓕灏欑槐顏嗘嫚?
     * @return true 濠碘€冲€归悘澶愬箥瑜戦、鎴炵閸℃瑧妞介悹?
     */
    private boolean ensureCompiled(ProjectInfo project) {
        Path classesDir = project.buildClassesPath;

        // 婵☆偀鍋撻柡灞诲劜濡叉悂宕ラ敃鍌涗粯閻熸洑鑳剁槐顏嗘嫚?
        if (!Files.exists(classesDir) || needsRecompilation(project)) {
            System.out.println("    [build] Compiling " + project.projectDir.getFileName() + "...");

            try {
                // 閻犲鍟伴弫?qin compile
                ProcessBuilder pb = new ProcessBuilder(
                    QinConstants.CMD_PREFIX,
                    QinConstants.CMD_FLAG,
                    QinConstants.QIN_CMD,
                    "compile"
                );
                pb.directory(project.projectDir.toFile());
                pb.redirectErrorStream(true);

                Process process = pb.start();

                // 闂傚牊鐟╃划顖氣槈閸絽鐎弶鍫熸尭閸?
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    while (reader.readLine() != null) {
                        // 闂傚牊鐟╃划?
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.err.println("    [error] Compilation failed: " + project.fullName);
                    throw new RuntimeException(
                        "Failed to compile: " + project.fullName);
                }

                System.out.println("    [ok] Compiled successfully");
                return true;

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(
                    "Failed to compile: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * 婵☆偀鍋撻柡灞诲劜濡叉悂宕ラ敃鍌涗粯閻熸洑绶氶崳鎼佸棘閹殿喚妞介悹?
     */
    private boolean needsRecompilation(ProjectInfo project) {
        try {
            Path srcDir = project.projectDir.resolve("src");
            Path classesDir = project.buildClassesPath;

            if (!Files.exists(classesDir)) {
                return true;
            }

            long latestSrcTime = getLatestModificationTime(srcDir, ".java");
            if (latestSrcTime == 0) {
                return false;
            }

            long oldestClassTime = getOldestModificationTime(classesDir, ".class");
            if (oldestClassTime == 0) {
                return true;
            }

            return latestSrcTime > oldestClassTime;

        } catch (IOException e) {
            return true;
        }
    }

    private long getLatestModificationTime(Path dir, String extension)
            throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] latestTime = { 0 };

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(extension)) {
                    long mtime = attrs.lastModifiedTime().toMillis();
                    if (mtime > latestTime[0]) {
                        latestTime[0] = mtime;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return latestTime[0];
    }

    private long getOldestModificationTime(Path dir, String extension)
            throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] oldestTime = { Long.MAX_VALUE };
        final boolean[] found = { false };

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(extension)) {
                    found[0] = true;
                    long mtime = attrs.lastModifiedTime().toMillis();
                    if (mtime < oldestTime[0]) {
                        oldestTime[0] = mtime;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return found[0] ? oldestTime[0] : 0;
    }

    /**
     * 闁告瑦鍨归獮鍥箥閳ь剟寮垫径瀣嫳闁革缚鍗抽妴宥夋儎?
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        Path workspaceRoot = findWorkspaceRoot(startDir);
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        // 闁圭顦崇粣娑氱矉缂佹ê绗撻幖?
        projectPaths.sort(Comparator.comparingInt(p ->
            startDir.toAbsolutePath().normalize()
                   .relativize(p.toAbsolutePath().normalize())
                   .getNameCount()));

        // 闁告梻濮惧ù鍥ㄣ亜閸︻厽绐楀ǎ鍥ｅ墲娴?
        for (Path projectPath : projectPaths) {
            try {
                Path configPath = projectPath.resolve(QinConstants.CONFIG_FILE);
                QinConfig config = loadConfig(configPath);
                String fullName = config.name();

                if (!projects.containsKey(fullName)) {
                    Path buildPath = projectPath.resolve(
                        QinConstants.BUILD_CLASSES_DIR);
                    projects.put(fullName, new ProjectInfo(
                        fullName, projectPath, buildPath));
                }
            } catch (Exception e) {
                // 闊洨鏅弳?
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
    private QinConfig loadConfig(Path configPath) throws IOException {
        String json = Files.readString(configPath);
        return gson.fromJson(json, QinConfig.class);
    }

    private Path findWorkspaceRoot(Path startDir) {
        // 濞达綀娉曢弫銈夋偝椤栨凹鏆旈柛娆愶耿閸?
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir).toAbsolutePath().normalize();
            if (Files.exists(ideaPath) && startDir.toAbsolutePath().normalize().startsWith(ideaPath)) {
                return ideaPath;
            }
        }

        // 闁告碍鍨崇粭鍌炲蓟閵夛箑顥?
        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir;

        while (current != null && current.getParent() != null) {
            final Path finalCurrent = current;
            boolean isProjectRoot = QinConstants.WORKSPACE_ROOT_MARKERS.stream()
                .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker)));

            if (isProjectRoot) {
                topMost = current;
            }

            current = current.getParent();
        }

        return topMost;
    }

    private void scanProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth || !Files.exists(dir)) {
            return;
        }

        if (Files.exists(dir.resolve(QinConstants.CONFIG_FILE)) &&
            !projects.contains(dir)) {
            projects.add(dir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                if (QinConstants.EXCLUDED_DIRS.contains(dirName) ||
                    dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 闊洨鏅弳?
        }
    }

    public static class ProjectInfo {
        public final String fullName;
        public final Path projectDir;
        public final Path buildClassesPath;

        public ProjectInfo(String fullName, Path projectDir, Path buildClassesPath) {
            this.fullName = fullName;
            this.projectDir = projectDir;
            this.buildClassesPath = buildClassesPath;
        }
    }

    public static class ResolutionResult {
        public final String localClasspath;
        public final Map<String, String> remoteDependencies;
        public final int localCount;
        public final int autoCompiledCount;

        public ResolutionResult(String localClasspath,
                              Map<String, String> remoteDependencies,
                              int localCount,
                              int autoCompiledCount) {
            this.localClasspath = localClasspath;
            this.remoteDependencies = remoteDependencies;
            this.localCount = localCount;
            this.autoCompiledCount = autoCompiledCount;
        }
    }
}
