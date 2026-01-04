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

import java.nio.file.*;

/**
 * 项目启动监听器
 * 自动检测 Qin 项目并打开工具窗口
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
}
