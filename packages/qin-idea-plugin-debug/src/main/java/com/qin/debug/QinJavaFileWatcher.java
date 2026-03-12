package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.qin.constants.QinConstants.CHARSET_UTF8;
import static com.qin.constants.QinConstants.CONFIG_FILE;
import static com.qin.constants.QinConstants.NODE_MODULES;
import static com.qin.constants.QinConstants.QIN_DIR;

/**
 * Watches Java source files and triggers incremental `qin compile`.
 */
public class QinJavaFileWatcher {

    private final Project project;
    private final String basePath;
    private MessageBusConnection connection;

    private static final long DEBOUNCE_DELAY_MS = 500;
    private static final long MIN_COMPILE_INTERVAL_MS = 2000;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "QinJavaFileWatcher-Scheduler");
        t.setDaemon(true);
        return t;
    });

    private final ConcurrentHashMap<String, ScheduledFuture<?>> pendingCompileTasks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> pendingFiles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> compilingProjects = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastCompileTime = new ConcurrentHashMap<>();

    public QinJavaFileWatcher(Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
    }

    public void startWatching() {
        if (basePath == null) {
            return;
        }

        connection = project.getMessageBus().connect();
        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
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
                    if (!fileName.endsWith(".java") || isInExcludedDirectory(filePath)) {
                        continue;
                    }

                    QinLogger.info("[JavaWatcher] Detected Java file change: " + filePath);
                    onJavaFileChanged(file);
                }
            }
        });

        QinLogger.info("[JavaWatcher] Java file watcher started (debounce: " + DEBOUNCE_DELAY_MS + "ms)");
    }

    public void stopWatching() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

        for (ScheduledFuture<?> task : pendingCompileTasks.values()) {
            task.cancel(false);
        }
        pendingCompileTasks.clear();
        pendingFiles.clear();

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        QinLogger.info("[JavaWatcher] Java file watcher stopped");
    }

    private boolean isInExcludedDirectory(String filePath) {
        String normalizedPath = filePath.replace('\\', '/');
        return normalizedPath.contains("/build/")
                || normalizedPath.contains("/out/")
                || normalizedPath.contains("/target/")
                || normalizedPath.contains("/.idea/")
                || normalizedPath.contains("/" + QIN_DIR + "/")
                || normalizedPath.contains("/" + NODE_MODULES + "/");
    }

    private void onJavaFileChanged(VirtualFile javaFile) {
        String projectPath = findQinProjectPath(javaFile);
        if (projectPath == null) {
            QinLogger.info("[JavaWatcher] File does not belong to a Qin project, skipping: " + javaFile.getPath());
            return;
        }

        pendingFiles.computeIfAbsent(projectPath, k -> ConcurrentHashMap.newKeySet()).add(javaFile.getPath());

        ScheduledFuture<?> existingTask = pendingCompileTasks.get(projectPath);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false);
            QinLogger.info("[JavaWatcher] Debounce: canceled pending task, waiting for more changes...");
        }

        ScheduledFuture<?> newTask = scheduler.schedule(() -> executeCompile(projectPath),
                DEBOUNCE_DELAY_MS,
                TimeUnit.MILLISECONDS);
        pendingCompileTasks.put(projectPath, newTask);

        int fileCount = pendingFiles.getOrDefault(projectPath, Set.of()).size();
        QinLogger.info("[JavaWatcher] Debounce: scheduled compile in " + DEBOUNCE_DELAY_MS
                + "ms, pending files: " + fileCount);
    }

    private String findQinProjectPath(VirtualFile file) {
        VirtualFile current = file.getParent();
        while (current != null) {
            VirtualFile configFile = current.findChild(CONFIG_FILE);
            if (configFile != null && configFile.exists()) {
                return current.getPath();
            }
            current = current.getParent();
        }
        return null;
    }

    private void executeCompile(String projectPath) {
        if (Boolean.TRUE.equals(compilingProjects.get(projectPath))) {
            QinLogger.info("[JavaWatcher] Project is already compiling, skipping: " + projectPath);
            return;
        }

        Long lastTime = lastCompileTime.get(projectPath);
        if (lastTime != null) {
            long elapsed = System.currentTimeMillis() - lastTime;
            if (elapsed < MIN_COMPILE_INTERVAL_MS) {
                QinLogger.info("[JavaWatcher] Compile interval too short (" + elapsed + "ms), skipping: " + projectPath);
                return;
            }
        }

        Set<String> files = pendingFiles.remove(projectPath);
        int fileCount = files != null ? files.size() : 0;
        compilingProjects.put(projectPath, true);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                QinLogger.info("[JavaWatcher] ========== Starting incremental compile ==========");
                QinLogger.info("[JavaWatcher] Project: " + projectPath);
                QinLogger.info("[JavaWatcher] Changed files: " + fileCount);

                long startTime = System.currentTimeMillis();
                boolean success = runQinCompile(projectPath);
                long duration = System.currentTimeMillis() - startTime;

                if (success) {
                    QinLogger.info("[JavaWatcher] Compile succeeded (" + duration + "ms)");
                    refreshProject();
                } else {
                    QinLogger.error("[JavaWatcher] Compile failed");
                    QinLogger.notifyError("Qin Auto Compile", "Compile failed. Check the console output.");
                }

                lastCompileTime.put(projectPath, System.currentTimeMillis());
                QinLogger.info("[JavaWatcher] ========== Incremental compile finished ==========");
            } finally {
                compilingProjects.remove(projectPath);
                pendingCompileTasks.remove(projectPath);
            }
        });
    }

    private boolean runQinCompile(String projectPath) {
        try {
            ProcessBuilder pb = QinCommandResolver.createProcessBuilder(projectPath, "compile");

            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), CHARSET_UTF8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    QinLogger.info("[qin compile] " + line);
                }
            }

            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            QinLogger.error("[JavaWatcher] Failed to execute compile command: " + e.getMessage());
            return false;
        }
    }

    private void refreshProject() {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                QinLogger.info("[JavaWatcher] Project structure refresh complete");
            } catch (Exception e) {
                QinLogger.error("[JavaWatcher] Failed to refresh project: " + e.getMessage());
            }
        });
    }

    public int getPendingProjectCount() {
        return pendingCompileTasks.size();
    }

    public int getPendingFileCount(String projectPath) {
        Set<String> files = pendingFiles.get(projectPath);
        return files != null ? files.size() : 0;
    }

    public boolean isCompiling(String projectPath) {
        return Boolean.TRUE.equals(compilingProjects.get(projectPath));
    }
}
