package com.qin.debug.run;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.DefaultProgramRunnerKt;
import com.intellij.execution.runners.GenericProgramRunner;
import org.jetbrains.annotations.NotNull;

/**
 * Qin 普通运行器
 * 负责执行普通的 qin run 命令（非调试模式）
 */
public class QinProgramRunner extends GenericProgramRunner<Object> {

    private static final String RUNNER_ID = "QinProgramRunner";

    @NotNull
    @Override
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return executorId.equals(DefaultRunExecutor.EXECUTOR_ID)
            && profile instanceof QinRunConfiguration;
    }
}
