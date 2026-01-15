package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.nio.file.*;
import java.util.*;

import static com.qin.constants.QinConstants.*;

/**
 * Qin 配置文件监听器
 * 监听 qin.config.json 变化，自动触发项目同步
 */
public class QinConfigWatcher {

    private final Project project;
    private final String basePath;
    private final QinProjectSync projectSync;
    private MessageBusConnection connection;

    // 标志位：当前是否正在同步中（用于避免循环触发）
    private volatile boolean syncing = false;

    public QinConfigWatcher(Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
        this.projectSync = new QinProjectSync(project);
    }

    /**
     * 设置同步状态（供外部调用，避免循环）
     */
    public void setSyncing(boolean syncing) {
        this.syncing = syncing;
    }

    /**
     * 启动配置文件监听
     */
    public void startWatching() {
        if (basePath == null)
            return;

        connection = project.getMessageBus().connect();
        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                // 如果正在同步中，跳过事件处理（避免循环）
                if (syncing) {
                    return;
                }

                for (VFileEvent event : events) {
                    VirtualFile file = event.getFile();
                    if (file == null)
                        continue;

                    String fileName = file.getName();
                    String filePath = file.getPath();

                    // 检查是否是当前项目或子项目的文件
                    if (!filePath.startsWith(basePath.replace('\\', '/'))) {
                        continue;
                    }

                    // 场景 5: 监听 qin.config.json 变化
                    if (fileName.equals(CONFIG_FILE)) {
                        QinLogger.info("[Watcher] 检测到配置文件变化: " + filePath);
                        onConfigChanged(file);
                    }

                    // 场景 2: 监听 .iml 文件变化（仅命令行 qin sync 后触发）
                    // 插件自己修改 .iml 时，syncing=true，会跳过这里
                    else if (fileName.endsWith(".iml")) {
                        QinLogger.info("[Watcher] 检测到 .iml 文件变化 (外部): " + filePath);
                        onImlChanged(file);
                    }
                }
            }
        });

        QinLogger.info("[Watcher] 配置文件监听已启动");
    }

    /**
     * 停止监听
     */
    public void stopWatching() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
            QinLogger.info("[Watcher] 配置文件监听已停止");
        }
    }

    /**
     * 配置文件变化时的处理
     */
    private void onConfigChanged(VirtualFile configFile) {
        VirtualFile projectDir = configFile.getParent();
        if (projectDir == null)
            return;

        Path projectPath = Paths.get(projectDir.getPath());

        // 在后台线程执行同步
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                syncing = true; // 设置标志，避免 .iml 变化事件循环触发
                projectSync.syncProject(projectPath);
            } finally {
                syncing = false; // 同步完成，恢复监听
            }
        });
    }

    /**
     * .iml 文件变化时的处理（场景 2: 命令行 qin sync 后触发）
     * CLI 已更新文件，只需刷新 IDEA 内存中的设置
     */
    private void onImlChanged(VirtualFile imlFile) {
        try {
            // 从 .iml 文件中读取 LANGUAGE_LEVEL
            String content = new String(imlFile.contentsToByteArray());

            // 解析 LANGUAGE_LEVEL 属性
            int idx = content.indexOf("LANGUAGE_LEVEL=\"");
            if (idx < 0)
                return;

            int start = idx + "LANGUAGE_LEVEL=\"".length();
            int end = content.indexOf("\"", start);
            if (end < 0)
                return;

            String languageLevel = content.substring(start, end); // e.g., "JDK_21"

            // 从 LANGUAGE_LEVEL 提取版本号
            String version = languageLevel.replace("JDK_", "").replace("_", ".");

            // 创建一个临时的 QinConfig 用于刷新语言级别
            QinConfig tempConfig = new QinConfig(version);

            QinLogger.info("[Watcher] 从 .iml 读取到语言级别: " + languageLevel);

            // 使用统一的 refreshLanguageLevel 方法（T3 + T5）
            projectSync.refreshLanguageLevel(tempConfig);

        } catch (Exception e) {
            QinLogger.error("[Watcher] 处理 .iml 变化失败: " + e.getMessage());
        }
    }
}
