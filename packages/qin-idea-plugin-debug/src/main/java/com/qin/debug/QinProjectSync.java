package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.java.LanguageLevel;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.qin.constants.QinConstants.CONFIG_FILE;

/**
 * Synchronizes Qin projects with IDEA metadata.
 */
public class QinProjectSync {

    private final Project project;
    private final String basePath;
    private boolean silentMode = true;

    public QinProjectSync(@NotNull Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
    }

    public QinProjectSync setSilentMode(boolean silent) {
        this.silentMode = silent;
        return this;
    }

    public boolean isCacheValid(Path projectPath) {
        boolean valid = com.qin.core.CacheValidator.isCacheValid(projectPath);
        if (valid) {
            QinLogger.info("[Sync] Cache is valid, skipping unchanged project: " + projectPath.getFileName());
        }
        return valid;
    }

    public void syncProject(Path projectPath) {
        QinLogger.info("[Sync] ========== Syncing project: " + projectPath + " ==========");
        String projectName = projectPath.getFileName().toString();

        try {
            Path configPath = projectPath.resolve(CONFIG_FILE);
            if (!Files.exists(configPath)) {
                QinLogger.info("[Sync] Config file not found, skipping project");
                return;
            }

            QinConfig config = QinConfig.load(projectPath.toString());
            if (config == null) {
                QinLogger.info("[Sync] Invalid config file, skipping project");
                if (!silentMode) {
                    QinLogger.notifyError("Qin Sync Failed", "Failed to parse config for " + projectName);
                }
                return;
            }

            syncDependencies(projectPath);
            updateImlFile(projectPath, config);
            updateLanguageLevel(config);
            updateMiscXml(config);

            QinLogger.info("[Sync] ========== Project sync complete ==========");
            if (!silentMode) {
                QinLogger.notifySuccess("Qin Sync Complete", projectName + " sync completed successfully");
            }
        } catch (Exception e) {
            QinLogger.error("[Sync] Project sync failed: " + e.getMessage());
            if (!silentMode) {
                QinLogger.notifyError("Qin Sync Failed", projectName + " sync failed: " + e.getMessage());
            }
        }
    }

    public void syncAllProjects() {
        syncAllProjects(true);
    }

    public void syncAllProjects(boolean checkCache) {
        if (basePath == null) {
            return;
        }

        try {
            List<Path> qinProjects = DebugStartup.discoverQinProjects(Paths.get(basePath));
            if (qinProjects.isEmpty()) {
                QinLogger.info("[Sync] No Qin projects detected");
                return;
            }

            QinLogger.info("[Sync] Detected " + qinProjects.size() + " Qin projects");
            boolean refreshed = false;
            for (Path projectPath : qinProjects) {
                if (shouldSyncProject(projectPath, checkCache)) {
                    syncProject(projectPath);
                    refreshed = true;
                }
            }
            if (refreshed) {
                refreshProject();
            }
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to sync workspace projects: " + e.getMessage());
        }
    }

    public void refreshLanguageLevel(QinConfig config) {
        QinLogger.info("[Sync] Refreshing language level from .iml update...");
        updateLanguageLevel(config);
        refreshProject();
        QinLogger.info("[Sync] Language level refresh complete");
    }

    public void syncDependencies(Path projectPath) {
        try {
            QinLogger.info("[Sync] Running `qin sync` for project dependencies...");
            ProcessBuilder pb = QinCommandResolver.createProcessBuilder(projectPath.toString(), "sync");

            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    QinLogger.info("[qin sync] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                QinLogger.info("[Sync] Dependency sync complete");
            } else {
                QinLogger.error("[Sync] Dependency sync command failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to run dependency sync: " + e.getMessage());
        }
    }

    public void updateImlFile(Path projectPath, QinConfig config) {
        try {
            if (!DebugStartup.hasSourceDirectory(projectPath)) {
                QinLogger.info("[Sync] No source directory detected, skipping .iml generation for aggregate project: "
                        + projectPath.getFileName());
                return;
            }

            QinLogger.info("[Sync] Regenerating module .iml file...");
            Path ideaDir = Paths.get(basePath, ".idea");
            DebugStartup.generateImlFile(projectPath, true, ideaDir);

            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");
            if (Files.exists(imlPath)) {
                updateImlLanguageLevel(imlPath, config);
            }

            QinLogger.info("[Sync] .iml update complete");
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to update .iml file: " + e.getMessage());
        }
    }

    private void updateImlLanguageLevel(Path imlPath, QinConfig config) {
        try {
            String content = Files.readString(imlPath);
            String languageLevel = "JDK_" + config.getJavaVersion();

            if (content.contains("LANGUAGE_LEVEL=")) {
                content = content.replaceAll("LANGUAGE_LEVEL=\"[^\"]*\"",
                        "LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            } else if (content.contains("<component name=\"NewModuleRootManager\"")) {
                content = content.replace(
                        "<component name=\"NewModuleRootManager\"",
                        "<component name=\"NewModuleRootManager\" LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            }

            Files.writeString(imlPath, content);
            QinLogger.info("[Sync] Updated .iml LANGUAGE_LEVEL to " + languageLevel);
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to update .iml LANGUAGE_LEVEL: " + e.getMessage());
        }
    }

    public void updateLanguageLevel(QinConfig config) {
        try {
            QinLogger.info("[Sync] Updating IDEA language level...");
            String sourceVersion = config.getSourceVersion();
            LanguageLevel level = parseLanguageLevel(sourceVersion);
            if (level == null) {
                QinLogger.info("[Sync] Unsupported sourceVersion, skipping language level update: " + sourceVersion);
                return;
            }

            ApplicationManager.getApplication().invokeLater(() ->
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        LanguageLevelProjectExtension extension = LanguageLevelProjectExtension.getInstance(project);
                        if (extension != null) {
                            extension.setLanguageLevel(level);
                            QinLogger.info("[Sync] IDEA language level set to " + level.name());
                        }
                    }));
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to update IDEA language level: " + e.getMessage());
        }
    }

    public void updateMiscXml(QinConfig config) {
        try {
            QinLogger.info("[Sync] Updating .idea/misc.xml...");
            String targetVersion = config.getJavaVersion();
            Path miscXml = Paths.get(basePath, ".idea", "misc.xml");
            IdeaMiscXmlSupport.updateLanguageLevel(miscXml, targetVersion);
            QinLogger.info("[Sync] Updated misc.xml languageLevel=JDK_" + targetVersion);
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to update misc.xml: " + e.getMessage());
        }
    }

    public void refreshProject() {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                QinLogger.info("[Sync] Project structure refresh complete");
            } catch (Exception e) {
                QinLogger.error("[Sync] Failed to refresh project structure: " + e.getMessage());
            }
        });
    }

    private LanguageLevel parseLanguageLevel(String version) {
        try {
            int ver = Integer.parseInt(version);
            if (ver <= 8) {
                return LanguageLevel.JDK_1_8;
            }
            String levelName = "JDK_" + ver;
            for (LanguageLevel level : LanguageLevel.values()) {
                if (level.name().equals(levelName)) {
                    return level;
                }
            }
            return LanguageLevel.HIGHEST;
        } catch (Exception e) {
            return LanguageLevel.JDK_21;
        }
    }

    private boolean shouldSyncProject(Path projectPath, boolean checkCache) {
        if (!checkCache) {
            return true;
        }

        if (!isCacheValid(projectPath)) {
            return true;
        }

        if (needsIdeaModuleRepair(projectPath)) {
            QinLogger.info("[Sync] IDEA module metadata is incomplete, forcing resync: " + projectPath.getFileName());
            return true;
        }

        return false;
    }

    private boolean needsIdeaModuleRepair(Path projectPath) {
        try {
            if (basePath == null) {
                return true;
            }

            if (!DebugStartup.hasSourceDirectory(projectPath)) {
                QinLogger.info("[Sync] No source directory detected, skipping IDEA module repair check: "
                        + projectPath.getFileName());
                return false;
            }

            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");
            if (!Files.exists(imlPath)) {
                QinLogger.info("[Sync] Missing .iml file: " + imlPath);
                return true;
            }

            String imlContent = Files.readString(imlPath);
            if (!imlContent.contains("<sourceFolder ")) {
                QinLogger.info("[Sync] Missing sourceFolder in .iml: " + imlPath);
                return true;
            }

            Path modulesXml = Paths.get(basePath, ".idea", "modules.xml");
            if (!Files.exists(modulesXml)) {
                QinLogger.info("[Sync] Missing IDEA modules.xml: " + modulesXml);
                return true;
            }

            String modulesContent = Files.readString(modulesXml);
            Path relativeImlPath = Paths.get(basePath).relativize(imlPath);
            String moduleEntry = relativeImlPath.toString().replace("\\", "/");
            if (!modulesContent.contains(moduleEntry)) {
                QinLogger.info("[Sync] Module entry missing from modules.xml: " + moduleEntry);
                return true;
            }

            return false;
        } catch (Exception e) {
            QinLogger.error("[Sync] Failed to validate IDEA module metadata: " + e.getMessage());
            return true;
        }
    }
}
