package com.qin.debug.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Qin 普通运行器
 * 负责执行普通的 qin run 命令（非调试模式）
 */
public class QinProgramRunner implements ProgramRunner<RunnerSettings> {

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

    @Override
    public void execute(@NotNull ExecutionEnvironment environment) throws ExecutionException {
        try {
            RunProfileState state = environment.getState();
            if (state == null) {
                throw new ExecutionException("Run profile state is null");
            }

            ExecutionResult executionResult = state.execute(environment.getExecutor(), this);
            if (executionResult == null) {
                throw new ExecutionException("Execution result is null");
            }

            RunContentDescriptor descriptor = new RunContentBuilder(executionResult, environment)
                    .showRunContent(environment.getContentToReuse());

            ProgramRunner.Callback callback = environment.getCallback();
            if (callback != null) {
                callback.processStarted(descriptor);
            }
        } catch (ExecutionException e) {
            QinLogger.error("[RUNNER] Execution failed: " + e.getMessage(), e);
            throw e;
        } catch (Throwable t) {
            QinLogger.error("[RUNNER] Unexpected execution failure", t);
            throw new ExecutionException(
                    "Qin runner failed: " + t.getClass().getSimpleName() + ": " + t.getMessage(),
                    t);
        }
    }
}
