package com.qin.core;

import com.google.gson.Gson;
import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 澧炲己鐗堟湰鍦伴」鐩В鏋愬櫒
 * 鏂板鍔熻兘锛?
 * 1. 鑷姩缂栬瘧鏈湴椤圭洰渚濊禆
 * 2. 鏅鸿兘澧為噺缂栬瘧妫€娴?
 * 3. 鏇村弸濂界殑杈撳嚭淇℃伅
 */
public class LocalProjectResolverEnhanced {

    private final Path startDir;
    private final Gson gson;

    public LocalProjectResolverEnhanced(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
        this.gson = new Gson();
    }

    /**
     * 瑙ｆ瀽渚濊禆锛屾敮鎸佽嚜鍔ㄧ紪璇戞湰鍦伴」鐩?
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

            ProjectInfo project = localProjects.get(fullName);
            if (project != null) {
                localCount++;
                System.out.println("  ✓ Local: " + fullName + " -> " + project.projectDir.getFileName());

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
            System.out.println("  ✓ Auto-compiled " + autoCompiledCount + " local project(s)");
        }

        String separator = QinConstants.getClasspathSeparator();
        String localClasspath = localClasspaths.isEmpty() ? "" :
                               String.join(separator, localClasspaths);

        return new ResolutionResult(localClasspath, remoteDependencies,
                                   localCount, autoCompiledCount);
    }

    /**
     * 纭繚椤圭洰宸茬紪璇?
     * @return true 濡傛灉鎵ц浜嗙紪璇?
     */
    private boolean ensureCompiled(ProjectInfo project) {
        Path classesDir = project.buildClassesPath;

        // 妫€鏌ユ槸鍚﹂渶瑕佺紪璇?
        if (!Files.exists(classesDir) || needsRecompilation(project)) {
            System.out.println("    鈫?Compiling " +
                             project.projectDir.getFileName() + "...");

            try {
                // 璋冪敤 qin compile
                ProcessBuilder pb = new ProcessBuilder(
                    QinConstants.CMD_PREFIX,
                    QinConstants.CMD_FLAG,
                    QinConstants.QIN_CMD,
                    "compile"
                );
                pb.directory(project.projectDir.toFile());
                pb.redirectErrorStream(true);

                Process process = pb.start();

                // 闈欓粯娑堣垂杈撳嚭
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    while (reader.readLine() != null) {
                        // 闈欓粯
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.err.println("    鉁?Compilation failed: " +
                                     project.fullName);
                    throw new RuntimeException(
                        "Failed to compile: " + project.fullName);
                }

                System.out.println("    鉁?Compiled successfully");
                return true;

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(
                    "Failed to compile: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * 妫€鏌ユ槸鍚﹂渶瑕侀噸鏂扮紪璇?
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
     * 鍙戠幇鎵€鏈夋湰鍦伴」鐩?
     */
    private Map<String, ProjectInfo> discoverLocalProjects() {
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        Path workspaceRoot = findWorkspaceRoot(startDir);
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        // 鎸夎窛绂绘帓搴?
        projectPaths.sort(Comparator.comparingInt(p ->
            startDir.toAbsolutePath().normalize()
                   .relativize(p.toAbsolutePath().normalize())
                   .getNameCount()));

        // 鍔犺浇椤圭洰淇℃伅
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
                // 蹇界暐
            }
        }

        return projects;
    }

    private QinConfig loadConfig(Path configPath) throws IOException {
        String json = Files.readString(configPath);
        return gson.fromJson(json, QinConfig.class);
    }

    private Path findWorkspaceRoot(Path startDir) {
        // 浣跨敤鐜鍙橀噺
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir).toAbsolutePath().normalize();
            if (Files.exists(ideaPath) && startDir.toAbsolutePath().normalize().startsWith(ideaPath)) {
                return ideaPath;
            }
        }

        // 鍚戜笂鏌ユ壘
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
            // 蹇界暐
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
