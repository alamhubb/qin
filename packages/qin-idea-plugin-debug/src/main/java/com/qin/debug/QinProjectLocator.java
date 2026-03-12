package com.qin.debug;

import com.intellij.openapi.project.Project;
import com.qin.constants.QinConstants;
import com.qin.core.ConfigLoader;
import com.qin.types.QinConfig;
import com.qin.types.ParsedEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Resolves Qin project/runtime defaults for IDEA run configurations.
 */
public final class QinProjectLocator {

    private QinProjectLocator() {
    }

    public static String resolveProjectPath(Project ideaProject, String configuredProjectPath) {
        Path configured = normalizeConfiguredProjectPath(configuredProjectPath);
        if (configured != null) {
            return toSystemIndependentPath(configured);
        }

        if (ideaProject == null || ideaProject.getBasePath() == null || ideaProject.getBasePath().isBlank()) {
            return null;
        }

        Path basePath = Paths.get(ideaProject.getBasePath()).toAbsolutePath().normalize();
        Path nearest = findNearestQinProject(basePath);
        if (nearest != null) {
            return toSystemIndependentPath(nearest);
        }

        try {
            List<Path> projects = DebugStartup.discoverQinProjects(basePath);
            if (projects.size() == 1) {
                return toSystemIndependentPath(projects.get(0).toAbsolutePath().normalize());
            }
        } catch (Exception ignored) {
            // Validation will report unresolved paths later.
        }

        return null;
    }

    public static String resolveMainClass(Project ideaProject, String projectPath, String configuredMainClass) {
        if (configuredMainClass != null && !configuredMainClass.isBlank()) {
            return configuredMainClass.trim();
        }

        String resolvedProjectPath = resolveProjectPath(ideaProject, projectPath);
        if (resolvedProjectPath == null || resolvedProjectPath.isBlank()) {
            return null;
        }

        try {
            ConfigLoader loader = new ConfigLoader(resolvedProjectPath);
            QinConfig config = loader.load();
            if (config.entry() == null || config.entry().isBlank() || !config.entry().endsWith(".java")) {
                return null;
            }

            ParsedEntry parsedEntry = loader.parseEntry(config.entry());
            return parsedEntry.className();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Path findNearestQinProject(Path start) {
        if (start == null) {
            return null;
        }

        Path current = Files.isDirectory(start) ? start : start.getParent();
        while (current != null) {
            if (Files.exists(current.resolve(QinConstants.CONFIG_FILE))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static Path normalizeConfiguredProjectPath(String configuredProjectPath) {
        if (configuredProjectPath == null || configuredProjectPath.isBlank()) {
            return null;
        }

        try {
            Path path = Paths.get(configuredProjectPath.trim()).toAbsolutePath().normalize();
            if (Files.exists(path.resolve(QinConstants.CONFIG_FILE))) {
                return path;
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }

    private static String toSystemIndependentPath(Path path) {
        return path.toString().replace('\\', '/');
    }
}
