package com.qin.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 项目启动监听器
 * 当 IDEA 打开项目时触发
 */
public class QinStartupActivity implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> continuation) {
        QinLogger.info("项目打开: " + project.getName());
        QinLogger.info("项目路径: " + project.getBasePath());

        // 检查是否为 Qin 项目
        QinBspConnector connector = new QinBspConnector(project);
        if (connector.isQinProject()) {
            QinLogger.info("检测到 Qin 项目！");
        } else {
            QinLogger.info("非 Qin 项目");
        }

        return Unit.INSTANCE;
    }
}
