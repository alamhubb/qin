package com.qin.core;

import com.google.gson.Gson;
import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Resolves local Qin workspace dependencies and ensures required projects are compiled.
 */
public class LocalProjectResolverEnhanced {

    private final Path startDir;
    private final Gson gson;

    public LocalProjectResolverEnhanced(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
        this.gson = new Gson();
    }

    public ResolutionResult resolveDependencies(Map<String, String> dependencies) {
        return resolveDependencies(dependencies, true);
    }

    public ResolutionResult resolveDependencies(Map<String, String> dependencies, boolean autoCompile) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new ResolutionResult("", new LinkedHashMap<>(), 0, 0, List.of());
        }

        Map<String, ProjectInfo> localProjects = discoverLocalProjects();

        Map<String, ProjectInfo> requiredLocalProjects = new LinkedHashMap<>();
        Map<String, String> remoteDependencies = new LinkedHashMap<>();
        int autoCompiledCount = 0;

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
                if (!requiredLocalProjects.containsKey(project.fullName)) {
                    requiredLocalProjects.put(project.fullName, project);
                    System.out.println("  [local] " + fullName + " -> " + project.projectDir.getFileName());
                }

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

        List<String> localClasspaths = new ArrayList<>();
        for (ProjectInfo project : orderProjectsForCompilation(requiredLocalProjects)) {
            if (autoCompile && ensureCompiled(project)) {
                autoCompiledCount++;
            }
            String classesPath = project.buildClassesPath.toString();
            if (!localClasspaths.contains(classesPath)) {
                localClasspaths.add(classesPath);
            }
        }

        if (autoCompiledCount > 0) {
            System.out.println("  [ok] Auto-compiled " + autoCompiledCount + " local project(s)");
        }

        String separator = QinConstants.getClasspathSeparator();
        String localClasspath = localClasspaths.isEmpty()
                ? ""
                : String.join(separator, localClasspaths);

        return new ResolutionResult(
                localClasspath,
                remoteDependencies,
                requiredLocalProjects.size(),
                autoCompiledCount,
                new ArrayList<>(requiredLocalProjects.values()));
    }

    private List<ProjectInfo> orderProjectsForCompilation(Map<String, ProjectInfo> requiredLocalProjects) {
        List<ProjectInfo> ordered = new ArrayList<>();
        Set<String> visiting = new HashSet<>();
        Set<String> visited = new HashSet<>();

        for (ProjectInfo project : requiredLocalProjects.values()) {
            visitProjectForCompilation(project, requiredLocalProjects, visiting, visited, ordered);
        }
        return ordered;
    }

    private void visitProjectForCompilation(
            ProjectInfo project,
            Map<String, ProjectInfo> requiredLocalProjects,
            Set<String> visiting,
            Set<String> visited,
            List<ProjectInfo> ordered) {
        if (project == null || visited.contains(project.fullName)) {
            return;
        }
        if (!visiting.add(project.fullName)) {
            throw new RuntimeException("Circular dependency detected in local projects: " + project.fullName);
        }

        try {
            Path configPath = project.projectDir.resolve(QinConstants.CONFIG_FILE);
            if (Files.exists(configPath)) {
                QinConfig config = loadConfig(configPath);
                if (config.dependencies() != null) {
                    for (String depName : config.dependencies().keySet()) {
                        ProjectInfo dependency = findProject(requiredLocalProjects, depName);
                        if (dependency != null) {
                            visitProjectForCompilation(dependency, requiredLocalProjects, visiting, visited, ordered);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dependencies for " + project.fullName + ": " + e.getMessage(), e);
        }

        visiting.remove(project.fullName);
        visited.add(project.fullName);
        ordered.add(project);
    }

    private boolean ensureCompiled(ProjectInfo project) {
        Path classesDir = project.buildClassesPath;
        if (Files.exists(classesDir) && !needsRecompilation(project)) {
            return false;
        }

        System.out.println("    [build] Compiling " + project.projectDir.getFileName() + "...");

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    currentJavaCommand(),
                    "-Xms16m",
                    "-Xmx256m",
                    "-XX:+UseSerialGC",
                    "-XX:-UseJVMCICompiler",
                    "-XX:TieredStopAtLevel=1",
                    "-Dfile.encoding=UTF-8",
                    "-Dstdout.encoding=UTF-8",
                    "-Dstderr.encoding=UTF-8",
                    "-cp",
                    currentCliClasspath(),
                    "com.qin.cli.QinCli",
                    "compile");
            pb.directory(project.projectDir.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("    [error] Compilation failed: " + project.fullName);
                if (!output.isEmpty()) {
                    System.err.print(output);
                }
                throw new RuntimeException("Failed to compile: " + project.fullName);
            }

            System.out.println("    [ok] Compiled successfully");
            return true;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to compile: " + e.getMessage());
        }
    }

    private String currentJavaCommand() {
        String executable = QinConstants.isWindows() ? "java.exe" : "java";
        return Path.of(System.getProperty("java.home"), "bin", executable).toString();
    }

    private String currentCliClasspath() {
        return System.getProperty("java.class.path");
    }

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

    private long getLatestModificationTime(Path dir, String extension) throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] latestTime = {0};
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
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

    private long getOldestModificationTime(Path dir, String extension) throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] oldestTime = {Long.MAX_VALUE};
        final boolean[] found = {false};

        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
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

    private Map<String, ProjectInfo> discoverLocalProjects() {
        Map<String, ProjectInfo> projects = new LinkedHashMap<>();

        Path workspaceRoot = findWorkspaceRoot(startDir);
        List<Path> projectPaths = new ArrayList<>();
        scanProjects(workspaceRoot, projectPaths, 0, QinConstants.MAX_SCAN_DEPTH);

        projectPaths.sort(Comparator.comparingInt(p ->
                startDir.toAbsolutePath().normalize()
                        .relativize(p.toAbsolutePath().normalize())
                        .getNameCount()));

        for (Path projectPath : projectPaths) {
            try {
                Path configPath = projectPath.resolve(QinConstants.CONFIG_FILE);
                QinConfig config = loadConfig(configPath);
                String fullName = config.name();

                if (!projects.containsKey(fullName)) {
                    Path buildPath = projectPath.resolve(QinConstants.BUILD_CLASSES_DIR);
                    projects.put(fullName, new ProjectInfo(fullName, projectPath, buildPath));
                }
            } catch (Exception ignored) {
                // Ignore malformed or unrelated projects during workspace scan.
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

    private String extractArtifactId(String fullName) {
        int atIndex = fullName.lastIndexOf('@');
        int colonIndex = fullName.lastIndexOf(':');
        int splitIndex = Math.max(atIndex, colonIndex);
        return splitIndex >= 0 ? fullName.substring(splitIndex + 1) : fullName;
    }

    private boolean matchesProjectAlias(ProjectInfo project, String dependencyName) {
        String fullName = project.fullName.toLowerCase(Locale.ROOT);
        if (fullName.equals(dependencyName)
                || QinConstants.toQinCoordinate(project.fullName).toLowerCase(Locale.ROOT).equals(dependencyName)) {
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
        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir).toAbsolutePath().normalize();
            if (Files.exists(ideaPath) && startDir.toAbsolutePath().normalize().startsWith(ideaPath)) {
                return ideaPath;
            }
        }

        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir;

        while (current != null && current.getParent() != null) {
            Path finalCurrent = current;
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

        if (Files.exists(dir.resolve(QinConstants.CONFIG_FILE)) && !projects.contains(dir)) {
            projects.add(dir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                if (QinConstants.EXCLUDED_DIRS.contains(dirName) || dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException ignored) {
            // Ignore unreadable directories during workspace scan.
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
        public final List<ProjectInfo> localProjects;

        public ResolutionResult(
                String localClasspath,
                Map<String, String> remoteDependencies,
                int localCount,
                int autoCompiledCount,
                List<ProjectInfo> localProjects) {
            this.localClasspath = localClasspath;
            this.remoteDependencies = remoteDependencies;
            this.localCount = localCount;
            this.autoCompiledCount = autoCompiledCount;
            this.localProjects = localProjects;
        }
    }
}
