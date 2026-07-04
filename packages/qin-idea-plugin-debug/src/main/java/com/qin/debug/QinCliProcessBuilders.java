package com.qin.debug;

import org.jetbrains.annotations.NotNull;

/**
 * Builds Qin CLI process commands for IDEA background and tool-window surfaces.
 */
public final class QinCliProcessBuilders {
    private QinCliProcessBuilders() {
    }

    public static @NotNull ProcessBuilder bspServer(@NotNull String projectPath) {
        return QinCommandResolver.createProcessBuilder(projectPath, "bsp");
    }

    public static @NotNull ProcessBuilder compileChangedJava(@NotNull String projectPath) {
        return QinCommandResolver.createProcessBuilder(projectPath, "compile");
    }

    public static @NotNull ProcessBuilder syncDependencies(@NotNull String projectPath) {
        return QinCommandResolver.createProcessBuilder(projectPath, "sync");
    }

    public static @NotNull ProcessBuilder syncProjectForce(@NotNull String projectPath) {
        return QinCommandResolver.createProcessBuilder(projectPath, "sync", "--force");
    }

    public static @NotNull ProcessBuilder syncWorkspaceForce(@NotNull String workspacePath) {
        return QinCommandResolver.createProcessBuilder(workspacePath, "sync", "--all", "--force");
    }

    public static @NotNull ProcessBuilder toolWindowTask(
            @NotNull String projectPath,
            @NotNull String command,
            String scriptName) {
        if ("sync".equals(command)) {
            return syncProjectForce(projectPath);
        }
        if ("script".equals(command)) {
            if ("dev".equals(scriptName)) {
                return QinCommandResolver.createProcessBuilder(projectPath, "dev");
            }
            return QinCommandResolver.createProcessBuilder(projectPath, "script", scriptName);
        }
        return QinCommandResolver.createProcessBuilder(projectPath, command);
    }
}
