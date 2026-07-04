package com.qin.debug.run;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.qin.debug.QinLogger;
import com.qin.debug.test.QinTestConfigurationType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class QinLegacyRunConfigurations {
    private QinLegacyRunConfigurations() {
    }

    public static void remove(@NotNull Project project) {
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            RunManager runManager = RunManager.getInstance(project);
            List<RunnerAndConfigurationSettings> allSettings = new ArrayList<>(runManager.getAllSettings());
            int removed = 0;

            for (RunnerAndConfigurationSettings settings : allSettings) {
                String typeId = settings.getType().getId();
                if (QinRunConfigurationType.ID.equals(typeId) || QinTestConfigurationType.ID.equals(typeId)) {
                    runManager.removeConfiguration(settings);
                    removed++;
                }
            }

            if (removed > 0) {
                QinLogger.info("[RUN] Removed legacy Qin run configurations: " + removed);
            } else {
                QinLogger.info("[RUN] No legacy Qin run configurations found.");
            }
        }));
    }
}
