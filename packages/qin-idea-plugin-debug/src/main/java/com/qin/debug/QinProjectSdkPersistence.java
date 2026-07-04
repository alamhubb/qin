package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class QinProjectSdkPersistence {
    private QinProjectSdkPersistence() {
    }

    public static void applyAndPersist(Project project,
            ProjectRootManager rootManager,
            com.intellij.openapi.projectRoots.Sdk sdk) {
        String sdkName = sdk.getName();
        QinLogger.info("[SDK]   Applying SDK: " + sdkName);

        ApplicationManager.getApplication().runWriteAction(() -> {
            rootManager.setProjectSdk(sdk);
        });
        QinLogger.info("[SDK]   Writing Project SDK to misc.xml");

        String basePath = project.getBasePath();
        if (basePath != null) {
            Path miscXml = Paths.get(basePath, ".idea", "misc.xml");
            updateMiscXmlWithSdk(miscXml, sdkName);
        }

        refreshProjectStructure(project);

        com.intellij.openapi.projectRoots.Sdk afterSdk = rootManager.getProjectSdk();
        if (afterSdk != null && afterSdk.getName().equals(sdkName)) {
            QinLogger.info("[SDK] Project SDK persisted to misc.xml: " + sdkName);
        } else {
            QinLogger.info("[SDK]   misc.xml updated, you may need to reopen the project for changes to fully apply");
        }
    }

    public static void refreshProjectStructure(Project project) {
        try {
            String basePath = project.getBasePath();

            if (basePath != null) {
                Path miscXmlPath = Paths.get(basePath, ".idea", "misc.xml");
                com.intellij.openapi.vfs.VirtualFile miscVf = com.intellij.openapi.vfs.LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(miscXmlPath.toString().replace('\\', '/'));
                if (miscVf != null) {
                    miscVf.refresh(false, false);
                    QinLogger.info("[SDK]   Refreshed misc.xml VirtualFile");
                }
            }

            com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
            QinLogger.info("[SDK]   VirtualFileManager refresh complete");

            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    com.intellij.openapi.project.DumbService dumbService = com.intellij.openapi.project.DumbService
                            .getInstance(project);

                    dumbService.runWhenSmart(() -> {
                        QinLogger.info("[SDK]   Project index rebuild complete");

                        ProjectRootManager rootManager = ProjectRootManager.getInstance(project);
                        com.intellij.openapi.projectRoots.Sdk sdk = rootManager.getProjectSdk();
                        QinLogger.info("[SDK]   Project SDK after refresh = " + (sdk != null ? sdk.getName() : "null"));
                    });
                } catch (Exception e) {
                    QinLogger.error("[SDK]   Failed during deferred project refresh: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            QinLogger.error("[SDK]   Failed to refresh project structure: " + e.getMessage());
        }
    }

    private static void updateMiscXmlWithSdk(Path miscXml, String sdkName) {
        try {
            QinLogger.info("[SDK]   Updating misc.xml: " + miscXml);
            IdeaMiscXmlSupport.updateProjectSdk(miscXml, sdkName);
            String verify = Files.readString(miscXml, StandardCharsets.UTF_8);
            if (verify.contains("project-jdk-name=\"" + sdkName + "\"")) {
                QinLogger.info("[SDK] misc.xml write verification succeeded");
            } else {
                QinLogger.error("[SDK] misc.xml write verification failed");
            }
        } catch (Exception e) {
            QinLogger.error("[SDK]   Failed to update misc.xml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
