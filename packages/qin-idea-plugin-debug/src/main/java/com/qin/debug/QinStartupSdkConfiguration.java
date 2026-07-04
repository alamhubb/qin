package com.qin.debug;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public final class QinStartupSdkConfiguration {
    private QinStartupSdkConfiguration() {
    }

    public static void configure(@NotNull Project project) {
        try {
            QinLogger.info("[SDK] ========== Configuring Project SDK ==========");
            String basePath = project.getBasePath();
            if (basePath == null) {
                QinLogger.info("[SDK] Project base path is unavailable, skipping");
                return;
            }

            ProjectRootManager rootManager = ProjectRootManager.getInstance(project);
            Sdk currentSdk = rootManager.getProjectSdk();
            QinLogger.info("[SDK] Current Project SDK = " + (currentSdk != null ? currentSdk.getName() : "null"));

            String desiredJavaVersion = QinWorkspaceSdkDefaults.preferredJavaVersion(Paths.get(basePath));
            int desiredVersion = QinWorkspaceSdkDefaults.parseJavaVersion(desiredJavaVersion);
            QinLogger.info("[SDK] Required Java version from Qin workspace = " + desiredJavaVersion);

            int currentVersion = 0;
            if (currentSdk != null) {
                currentVersion = QinProjectSdkSelection.sdkVersion(currentSdk);
                if (currentVersion >= desiredVersion) {
                    QinLogger.info("[SDK] Existing Project SDK is compatible (current: "
                            + currentVersion + ", required: " + desiredVersion + "), keeping as-is");
                    return;
                }
                QinLogger.warn("[SDK] Existing Project SDK is lower than required (current: "
                        + currentVersion + ", required: " + desiredVersion + "), attempting auto-upgrade");
            } else {
                QinLogger.info("[SDK] No Project SDK configured, selecting one automatically...");
            }

            QinProjectSdkSelection.Selection bestSelection =
                    QinProjectSdkSelection.selectConfiguredJdk(desiredVersion);

            if (bestSelection != null) {
                Sdk sdkToSet = bestSelection.sdk();
                String sdkName = sdkToSet.getName();
                int selectedVersion = bestSelection.version();
                QinLogger.info("[SDK] Selected JDK: " + sdkName + " (version: " + selectedVersion
                        + ", desired: " + desiredVersion + ")");

                if (currentSdk != null && sdkName.equals(currentSdk.getName())) {
                    QinLogger.info("[SDK] Selected JDK is the same as current SDK, no update needed");
                    return;
                }

                if (selectedVersion < desiredVersion) {
                    QinLogger.warn("[SDK] Best available JDK is still lower than required (selected: "
                            + selectedVersion + ", required: " + desiredVersion + ")");
                }

                QinLogger.info("[SDK] Applying selected Project SDK...");
                QinProjectSdkPersistence.applyAndPersist(project, rootManager, sdkToSet);
            } else {
                Sdk javaHomeSdk = QinProjectSdkSelection.registerJavaHomeJdk();
                if (javaHomeSdk != null) {
                    QinProjectSdkPersistence.applyAndPersist(project, rootManager, javaHomeSdk);
                }
            }

            QinLogger.info("[SDK] Refreshing IDEA project structure after SDK update...");
            QinProjectSdkPersistence.refreshProjectStructure(project);

            QinLogger.info("[SDK] ========== Project SDK configuration complete ==========");
        } catch (Exception e) {
            QinLogger.error("[SDK] Failed to configure Project SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
