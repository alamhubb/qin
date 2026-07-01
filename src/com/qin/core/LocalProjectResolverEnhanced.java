package com.qin.core;

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
    public LocalProjectResolverEnhanced(String workingDir) {
        this.startDir = Paths.get(workingDir).toAbsolutePath();
    }

    public ResolutionResult resolveDependencies(Map<String, String> dependencies) {
        return resolveDependencies(dependencies, !isLocalAutoCompileDisabled());
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
            if (!providesJvmClasspath(project)) {
                ensureNonJvmProjectReady(project);
                continue;
            }
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

        QinConfig config = project.config;
        if (config.dependencies() != null) {
            for (String depName : config.dependencies().keySet()) {
                ProjectInfo dependency = findProject(requiredLocalProjects, depName);
                if (dependency != null) {
                    visitProjectForCompilation(dependency, requiredLocalProjects, visiting, visited, ordered);
                }
            }
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
                    "-XX:+UnlockExperimentalVMOptions",
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
            pb.environment().put("QIN_DISABLE_LOCAL_AUTO_COMPILE", "1");
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

    private boolean isLocalAutoCompileDisabled() {
        String envValue = System.getenv("QIN_DISABLE_LOCAL_AUTO_COMPILE");
        if (envValue != null && (envValue.equals("1") || envValue.equalsIgnoreCase("true"))) {
            return true;
        }
        return Boolean.getBoolean("qin.disableLocalAutoCompile");
    }

    private boolean needsRecompilation(ProjectInfo project) {
        try {
            Path classesDir = project.buildClassesPath;

            if (!Files.exists(classesDir)) {
                return true;
            }

            if (!hasAnyCompiledClass(classesDir)) {
                return true;
            }

            QinConfig config = loadConfig(project.projectDir.resolve(QinConstants.CONFIG_FILE));
            List<String> sourceDirs = javaSourceDirs(config);
            if (sourceDirs.isEmpty()) {
                return false;
            }

            IncrementalCompilationChecker checker =
                    new IncrementalCompilationChecker(project.projectDir.toString());
            for (String sourceDir : sourceDirs) {
                if (checker.hasDeletedFiles(sourceDir) || checker.needsCompilationByHash(sourceDir)) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return true;
        }
    }

    public boolean providesJvmClasspath(ProjectInfo project) {
        if (Files.exists(project.buildClassesPath)) {
            try {
                return hasAnyCompiledClass(project.buildClassesPath);
            } catch (IOException ignored) {
                return true;
            }
        }

        QinConfig config = project.config;
        if (hasJvmEntry(config.entry())
                || hasJvmEntry(config.backend() != null ? config.backend().entry() : null)) {
            return true;
        }

        for (String sourceDir : javaSourceDirs(config)) {
            if (containsJavaSource(project.projectDir.resolve(sourceDir))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasJvmEntry(String entry) {
        return entry != null && entry.replace('\\', '/').endsWith(".java");
    }

    private boolean containsJavaSource(Path sourceDir) {
        if (!Files.isDirectory(sourceDir)) {
            return false;
        }
        try (var stream = Files.walk(sourceDir)) {
            return stream.anyMatch(path -> Files.isRegularFile(path)
                    && path.getFileName().toString().endsWith(".java"));
        } catch (IOException ignored) {
            return false;
        }
    }

    private void ensureNonJvmProjectReady(ProjectInfo project) {
        String entry = project.config.entry();
        if (entry == null || entry.isBlank()) {
            return;
        }
        Path entryPath = project.projectDir.resolve(entry).normalize();
        if (!Files.isRegularFile(entryPath)) {
            throw new RuntimeException("Local non-JVM dependency entry does not exist: "
                    + project.fullName + " -> " + entry);
        }
    }

    private List<String> javaSourceDirs(QinConfig config) {
        if (config == null || config.java() == null) {
            return List.of(QinConstants.JAVA_SOURCE_DIR);
        }
        List<String> sourceDirs = new ArrayList<>();
        String sourceDir = config.java().sourceDir();
        if (sourceDir != null && !sourceDir.isBlank()) {
            sourceDirs.add(sourceDir);
        }
        String testDir = config.java().testDir();
        if (testDir != null && !testDir.isBlank() && !sourceDirs.contains(testDir)) {
            sourceDirs.add(testDir);
        }
        return List.copyOf(sourceDirs);
    }

    private String outputDir(QinConfig config) {
        if (config != null && config.java() != null && config.java().outputDir() != null
                && !config.java().outputDir().isBlank()) {
            return config.java().outputDir();
        }
        return QinConstants.BUILD_CLASSES_DIR;
    }

    private boolean hasAnyCompiledClass(Path classesDir) throws IOException {
        if (!Files.exists(classesDir)) {
            return false;
        }
        try (var stream = Files.walk(classesDir)) {
            return stream.anyMatch(path -> Files.isRegularFile(path)
                    && path.getFileName().toString().endsWith(".class"));
        }
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
                    Path buildPath = projectPath.resolve(outputDir(config));
                    projects.put(fullName, new ProjectInfo(fullName, projectPath, buildPath, config));
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
        Path projectDir = configPath == null ? null : configPath.getParent();
        if (projectDir == null) {
            throw new IOException("qin.config.js path has no parent");
        }
        return new ConfigLoader(projectDir.toString()).load();
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

            Path parent = current.getParent();
            if (parent != null && Files.exists(current.resolve(QinConstants.CONFIG_FILE))) {
                // Promote the direct parent of a Qin project so sibling workspaces can be discovered
                // without requiring IDE/VCS markers on that parent itself.
                topMost = parent;
            }
            if (parent != null && hasSiblingQinProjects(parent, current)) {
                topMost = parent;
            }

            current = current.getParent();
        }

        return topMost;
    }

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
        public final QinConfig config;

        public ProjectInfo(String fullName, Path projectDir, Path buildClassesPath, QinConfig config) {
            this.fullName = fullName;
            this.projectDir = projectDir;
            this.buildClassesPath = buildClassesPath;
            this.config = config;
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
