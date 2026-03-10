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
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import static com.qin.constants.QinConstants.*;

/**
 * Qin Java 文件监听器
 * 监听 .java 文件变化，自动触发增量编译
 *
 * 核心特性：
 * 1. 防抖机制（Debounce）- 500ms 内的多次保存合并为一次编译
 * 2. 智能缓存 - 只编译有变更的项目
 * 3. 增量编译 - 调用 qin compile 进行增量编译
 * 4. 项目隔离 - 按项目分别触发编译
 */
public class QinJavaFileWatcher {

    private final Project project;
    private final String basePath;
    private MessageBusConnection connection;

    // ==================== 防抖配置 ====================

    /**
     * 防抖延迟时间（毫秒）
     * 在这个时间内的多次文件变更会合并为一次编译
     */
    private static final long DEBOUNCE_DELAY_MS = 500;

    /**
     * 调度器（用于延迟执行）
     */
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "QinJavaFileWatcher-Scheduler");
            t.setDaemon(true);
            return t;
        });

    /**
     * 待处理的编译任务（按项目路径去重）
     * key: 项目路径
     * value: 延迟任务
     */
    private final ConcurrentHashMap<String, ScheduledFuture<?>> pendingCompileTasks =
        new ConcurrentHashMap<>();

    /**
     * 每个项目待编译的文件列表（用于增量编译统计）
     */
    private final ConcurrentHashMap<String, Set<String>> pendingFiles =
        new ConcurrentHashMap<>();

    // ==================== 编译状态 ====================

    /**
     * 当前是否正在编译中（防止重复编译）
     */
    private final ConcurrentHashMap<String, Boolean> compilingProjects =
        new ConcurrentHashMap<>();

    /**
     * 最近编译时间（用于缓存判断）
     */
    private final ConcurrentHashMap<String, Long> lastCompileTime =
        new ConcurrentHashMap<>();

    /**
     * 最小编译间隔（毫秒）- 同一项目两次编译之间的最小间隔
     */
    private static final long MIN_COMPILE_INTERVAL_MS = 2000;

    // ==================== 构造函数 ====================

    public QinJavaFileWatcher(Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
    }

    // ==================== 启动/停止 ====================

    /**
     * 启动 Java 文件监听
     */
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
                    if (file == null) continue;

                    String fileName = file.getName();
                    String filePath = file.getPath();

                    // 只处理当前项目内的文件
                    if (!filePath.startsWith(basePath.replace('\\', '/'))) {
                        continue;
                    }

                    // 只监听 .java 文件
                    if (fileName.endsWith(".java")) {
                        // 排除 build 目录和其他生成目录
                        if (isInExcludedDirectory(filePath)) {
                            continue;
                        }

                        QinLogger.info("[JavaWatcher] 检测到 Java 文件变化: " + filePath);
                        onJavaFileChanged(file);
                    }
                }
            }
        });

        QinLogger.info("[JavaWatcher] Java 文件监听已启动 (防抖: " + DEBOUNCE_DELAY_MS + "ms)");
    }

    /**
     * 停止监听
     */
    public void stopWatching() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

        // 取消所有待处理的任务
        for (ScheduledFuture<?> task : pendingCompileTasks.values()) {
            task.cancel(false);
        }
        pendingCompileTasks.clear();
        pendingFiles.clear();

        // 关闭调度器
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }

        QinLogger.info("[JavaWatcher] Java 文件监听已停止");
    }

    // ==================== 文件变化处理 ====================

    /**
     * 检查文件是否在排除目录中
     */
    private boolean isInExcludedDirectory(String filePath) {
        String normalizedPath = filePath.replace('\\', '/');
        return normalizedPath.contains("/build/") ||
               normalizedPath.contains("/out/") ||
               normalizedPath.contains("/target/") ||
               normalizedPath.contains("/.idea/") ||
               normalizedPath.contains("/.qin/") ||
               normalizedPath.contains("/node_modules/");
    }

    /**
     * Java 文件变化时的处理（带防抖）
     */
    private void onJavaFileChanged(VirtualFile javaFile) {
        // 1. 找到该文件所属的 Qin 项目
        String projectPath = findQinProjectPath(javaFile);
        if (projectPath == null) {
            QinLogger.info("[JavaWatcher] 文件不属于任何 Qin 项目，跳过: " + javaFile.getPath());
            return;
        }

        // 2. 记录待编译文件
        pendingFiles.computeIfAbsent(projectPath, k -> ConcurrentHashMap.newKeySet())
                    .add(javaFile.getPath());

        // 3. 防抖：取消之前的待处理任务（如果有）
        ScheduledFuture<?> existingTask = pendingCompileTasks.get(projectPath);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false);
            QinLogger.info("[JavaWatcher] 防抖：取消待处理任务，等待更多变更...");
        }

        // 4. 创建新的延迟任务
        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
            executeCompile(projectPath);
        }, DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS);

        pendingCompileTasks.put(projectPath, newTask);

        int fileCount = pendingFiles.getOrDefault(projectPath, Set.of()).size();
        QinLogger.info("[JavaWatcher] 防抖：已调度编译任务，延迟 " + DEBOUNCE_DELAY_MS +
                      "ms，待编译文件数: " + fileCount);
    }

    /**
     * 查找文件所属的 Qin 项目路径
     * 向上查找包含 qin.config.json 的目录
     */
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

    // ==================== 编译执行 ====================

    /**
     * 执行编译（带缓存检查）
     */
    private void executeCompile(String projectPath) {
        // 1. 检查是否正在编译
        if (Boolean.TRUE.equals(compilingProjects.get(projectPath))) {
            QinLogger.info("[JavaWatcher] 项目正在编译中，跳过: " + projectPath);
            return;
        }

        // 2. 检查编译间隔（缓存）
        Long lastTime = lastCompileTime.get(projectPath);
        if (lastTime != null) {
            long elapsed = System.currentTimeMillis() - lastTime;
            if (elapsed < MIN_COMPILE_INTERVAL_MS) {
                QinLogger.info("[JavaWatcher] 编译间隔过短 (" + elapsed + "ms)，跳过: " + projectPath);
                return;
            }
        }

        // 3. 获取待编译文件列表并清空
        Set<String> files = pendingFiles.remove(projectPath);
        int fileCount = files != null ? files.size() : 0;

        // 4. 标记为正在编译
        compilingProjects.put(projectPath, true);

        // 5. 在后台线程执行编译
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                QinLogger.info("[JavaWatcher] ========== 开始增量编译 ==========");
                QinLogger.info("[JavaWatcher] 项目: " + projectPath);
                QinLogger.info("[JavaWatcher] 变更文件数: " + fileCount);

                long startTime = System.currentTimeMillis();

                // 调用 qin compile 命令
                boolean success = runQinCompile(projectPath);

                long duration = System.currentTimeMillis() - startTime;

                if (success) {
                    QinLogger.info("[JavaWatcher] ✓ 编译成功 (" + duration + "ms)");

                    // 刷新 IDEA 项目结构
                    refreshProject();

                    // 显示成功通知（可选，避免通知过多可以注释掉）
                    // QinLogger.notifySuccess("Qin Auto Compile",
                    //     "编译成功 (" + fileCount + " 文件, " + duration + "ms)");
                } else {
                    QinLogger.error("[JavaWatcher] ✗ 编译失败");
                    QinLogger.notifyError("Qin Auto Compile", "编译失败，请检查控制台");
                }

                // 更新最后编译时间
                lastCompileTime.put(projectPath, System.currentTimeMillis());

                QinLogger.info("[JavaWatcher] ========== 编译完成 ==========");

            } finally {
                // 清除编译标志
                compilingProjects.remove(projectPath);
                pendingCompileTasks.remove(projectPath);
            }
        });
    }

    /**
     * 执行 qin compile 命令
     */
    private boolean runQinCompile(String projectPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(CMD_PREFIX, CMD_FLAG, QIN_CMD, "compile");
            pb.directory(new File(projectPath));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 读取输出
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
            QinLogger.error("[JavaWatcher] 执行编译命令失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 刷新 IDEA 项目结构
     */
    private void refreshProject() {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                QinLogger.info("[JavaWatcher] 项目结构已刷新");
            } catch (Exception e) {
                QinLogger.error("[JavaWatcher] 刷新项目失败: " + e.getMessage());
            }
        });
    }

    // ==================== 状态查询 ====================

    /**
     * 获取待编译的项目数
     */
    public int getPendingProjectCount() {
        return pendingCompileTasks.size();
    }

    /**
     * 获取指定项目待编译的文件数
     */
    public int getPendingFileCount(String projectPath) {
        Set<String> files = pendingFiles.get(projectPath);
        return files != null ? files.size() : 0;
    }

    /**
     * 检查指定项目是否正在编译
     */
    public boolean isCompiling(String projectPath) {
        return Boolean.TRUE.equals(compilingProjects.get(projectPath));
    }
}
