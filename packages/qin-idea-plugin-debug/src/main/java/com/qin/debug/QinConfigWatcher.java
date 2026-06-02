package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.qin.constants.QinConstants.CONFIG_FILE;

/**
 * Watches workspace files that affect Qin project structure and compilation.
 */
public class QinConfigWatcher {
    private static final long IML_REFRESH_DEBOUNCE_MS = 1_000L;

    private final Project project;
    private final String basePath;
    private final QinProjectSync projectSync;
    private final Object imlRefreshLock = new Object();
    private MessageBusConnection connection;

    private volatile boolean syncing = false;
    private volatile long lastImlRefreshAtMs = 0L;
    private volatile String lastImlVersion = "";

    public QinConfigWatcher(Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
        this.projectSync = new QinProjectSync(project);
    }

    public void setSyncing(boolean syncing) {
        this.syncing = syncing;
    }

    public void startWatching() {
        if (basePath == null) {
            return;
        }

        connection = project.getMessageBus().connect();
        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                if (syncing) {
                    return;
                }

                boolean workspaceConfigChanged = false;
                String imlSourceVersion = null;
                int imlChangedCount = 0;
                Set<String> imlVersions = new LinkedHashSet<>();
                String sampleImlPath = null;
                for (VFileEvent event : events) {
                    VirtualFile file = event.getFile();
                    if (file == null) {
                        continue;
                    }

                    String fileName = file.getName();
                    String filePath = file.getPath();
                    if (!filePath.startsWith(basePath.replace('\\', '/'))) {
                        continue;
                    }

                    if (fileName.equals(CONFIG_FILE)) {
                        workspaceConfigChanged = true;
                        QinLogger.info("[Watcher] Detected qin.config.js change: " + filePath);
                    } else if (fileName.endsWith(".iml")) {
                        imlChangedCount++;
                        if (sampleImlPath == null) {
                            sampleImlPath = filePath;
                        }
                        String version = extractLanguageLevelVersion(file);
                        if (version != null) {
                            imlSourceVersion = version;
                            imlVersions.add(version);
                        }
                    }
                }

                if (workspaceConfigChanged) {
                    onWorkspaceConfigChanged();
                    return;
                }
                if (imlSourceVersion != null) {
                    QinLogger.debug("[Watcher] .iml batch changed: count=" + imlChangedCount
                            + ", versions=" + imlVersions
                            + ", sample=" + sampleImlPath);
                    onImlChanged(imlSourceVersion);
                }
            }
        });

        QinLogger.info("[Watcher] Config file watcher started");
    }

    public void stopWatching() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
            QinLogger.info("[Watcher] Config file watcher stopped");
        }
    }

    private void onWorkspaceConfigChanged() {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                syncing = true;
                projectSync.syncAllProjects(false);
            } finally {
                syncing = false;
            }
        });
    }

    private void onImlChanged(String sourceVersion) {
        long now = System.currentTimeMillis();
        synchronized (imlRefreshLock) {
            if (sourceVersion.equals(lastImlVersion)
                    && (now - lastImlRefreshAtMs) < IML_REFRESH_DEBOUNCE_MS) {
                QinLogger.debug("[Watcher] Skip duplicate .iml language refresh: " + sourceVersion);
                return;
            }
            lastImlVersion = sourceVersion;
            lastImlRefreshAtMs = now;
        }

        QinLogger.info("[Watcher] Apply LANGUAGE_LEVEL from .iml: " + sourceVersion);
        projectSync.refreshLanguageLevel(sourceVersion);
    }

    private String extractLanguageLevelVersion(VirtualFile imlFile) {
        try {
            String content = new String(imlFile.contentsToByteArray(), StandardCharsets.UTF_8);
            int idx = content.indexOf("LANGUAGE_LEVEL=\"");
            if (idx < 0) {
                return null;
            }

            int start = idx + "LANGUAGE_LEVEL=\"".length();
            int end = content.indexOf("\"", start);
            if (end < 0) {
                return null;
            }

            String languageLevel = content.substring(start, end);
            return languageLevel.replace("JDK_", "").replace("_", ".");
        } catch (Exception e) {
            QinLogger.error("[Watcher] Failed to process .iml change: " + e.getMessage());
            return null;
        }
    }
}

