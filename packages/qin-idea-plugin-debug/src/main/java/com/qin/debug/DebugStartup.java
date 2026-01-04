package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * 项目启动监听器
 * 自动检测 Qin 项目并执行 sync
 */
public class DebugStartup implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 记录日志
        QinLogger logger = new QinLogger(project.getName());
        logger.info("项目打开: " + project.getName());
        logger.info("路径: " + basePath);

        // 检测是否为 Qin 项目
        Path configPath = Paths.get(basePath, "qin.config.json");
        if (Files.exists(configPath)) {
            logger.info("检测到 Qin 项目");

            // 在后台线程执行 sync
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                try {
                    logger.info("开始自动同步依赖...");
                    runQinSync(basePath, logger);
                    logger.info("依赖同步完成");

                    // 刷新项目，让 IDEA 重新加载库配置
                    ApplicationManager.getApplication().invokeLater(() -> {
                        try {
                            // 通知 IDEA 刷新项目模型
                            com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                            logger.info("项目模型已刷新");
                        } catch (Exception e) {
                            logger.error("刷新项目失败: " + e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    logger.error("自动同步失败: " + e.getMessage());
                }
            });

            // 自动打开 Qin 工具窗口
            ApplicationManager.getApplication().invokeLater(() -> {
                ToolWindowManager manager = ToolWindowManager.getInstance(project);
                ToolWindow toolWindow = manager.getToolWindow("Qin");
                if (toolWindow != null) {
                    toolWindow.show();
                    logger.info("已打开 Qin 工具窗口");
                }
            });
        }

        return Unit.INSTANCE;
    }

    /**
     * 执行 qin sync 命令
     */
    private void runQinSync(String projectPath, QinLogger logger) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "qin", "sync");
        pb.directory(new File(projectPath));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // 读取输出
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("[sync] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.error("qin sync 退出码: " + exitCode);
        }
    }
}
