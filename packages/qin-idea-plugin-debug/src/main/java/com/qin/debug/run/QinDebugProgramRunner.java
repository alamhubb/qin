package com.qin.debug.run;

import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 调试运行器
 * 负责启动带调试参数的 Qin 进程，并连接 IDEA 调试器
 */
public class QinDebugProgramRunner extends GenericDebuggerRunner {

    private static final String RUNNER_ID = "QinDebugRunner";

    @NotNull
    @Override
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID)
            && profile instanceof QinRunConfiguration;
    }

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state,
                                              @NotNull ExecutionEnvironment env)
            throws ExecutionException {

        QinRunConfiguration config = (QinRunConfiguration) env.getRunProfile();

        // 创建带调试模式的运行状态
        QinRunProfileState debugState = new QinRunProfileState(config, env, true);

        // 启动进程
        var processHandler = debugState.startProcess();

        // 等待进程启动并监听 JDWP 就绪
        int debugPort = config.getDebugPort();

        // 使用 XDebugger 创建调试会话
        return XDebuggerManager.getInstance(env.getProject())
            .startSession(env, new XDebugProcessStarter() {
                @NotNull
                @Override
                public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
                    return new QinDebugProcess(session, processHandler, config, debugPort);
                }
            }).getRunContentDescriptor();
    }
}
