package com.qin.debug.run;

import com.intellij.openapi.project.Project;
import com.qin.debug.QinProjectLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class QinRunConfigurationDefaults {
    private QinRunConfigurationDefaults() {
    }

    public static @Nullable String projectPath(
            @NotNull Project project,
            @Nullable String configuredProjectPath) {
        return QinProjectLocator.resolveProjectPath(project, configuredProjectPath);
    }

    public static @Nullable String mainClass(
            @NotNull Project project,
            @Nullable String resolvedProjectPath,
            @Nullable String configuredMainClass) {
        return QinProjectLocator.resolveMainClass(project, resolvedProjectPath, configuredMainClass);
    }
}
