package com.qin.debug.run;

import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.DefaultDebugEnvironment;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.JavaDebugProcess;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Qin 调试进程
 * 管理 Qin 应用的调试会话，连接到 JDWP 端口
 */
public class QinDebugProcess extends XDebugProcess {

    private final ProcessHandler processHandler;
    private final QinRunConfiguration configuration;
    private final int debugPort;
    private final Project project;
    private JavaDebugProcess javaDebugProcess;
    private ConsoleView console;

    public QinDebugProcess(@NotNull XDebugSession session,
                           @NotNull ProcessHandler processHandler,
                           @NotNull QinRunConfiguration configuration,
                           int debugPort) {
        super(session);
        this.processHandler = processHandler;
        this.configuration = configuration;
        this.debugPort = debugPort;
        this.project = session.getProject();

        // 延迟连接调试器（等待 Qin 进程启动）
        scheduleDebuggerAttach();
    }

    /**
     * 延迟连接调试器
     */
    private void scheduleDebuggerAttach() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    attachDebugger();
                } catch (Exception e) {
                    getSession().getConsoleView().print(
                        "Failed to attach debugger: " + e.getMessage() + "\n",
                        ConsoleViewContentType.ERROR_OUTPUT
                    );
                }
            });
        }, 2, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * 连接到 JDWP 调试端口
     */
    private void attachDebugger() throws ExecutionException {
        // 创建远程连接配置
        RemoteConnection connection = new RemoteConnection(
            true,       // useSockets
            "localhost",
            String.valueOf(debugPort),
            false       // serverMode (我们是客户端)
        );

        getSession().getConsoleView().print(
            "Connecting to debugger on port " + debugPort + "...\n",
            ConsoleViewContentType.SYSTEM_OUTPUT
        );

        try {
            // 使用 DebuggerManagerEx 创建调试会话
            DebuggerManagerEx debuggerManager = DebuggerManagerEx.getInstanceEx(project);

            // 创建调试环境
            DefaultDebugEnvironment debugEnvironment = new DefaultDebugEnvironment(
                getSession().getRunContentDescriptor().getExecutionConsole(),
                processHandler,
                connection,
                5000  // 连接超时
            );

            DebuggerSession debuggerSession = debuggerManager.attachVirtualMachine(debugEnvironment);

            if (debuggerSession != null) {
                getSession().getConsoleView().print(
                    "Debugger attached successfully!\n",
                    ConsoleViewContentType.SYSTEM_OUTPUT
                );
            }

        } catch (Exception e) {
            getSession().getConsoleView().print(
                "Debugger connection failed: " + e.getMessage() + "\n",
                ConsoleViewContentType.ERROR_OUTPUT
            );
            throw new ExecutionException("Failed to attach debugger", e);
        }
    }

    @Nullable
    @Override
    protected ProcessHandler doGetProcessHandler() {
        return processHandler;
    }

    @NotNull
    @Override
    public XDebuggerEditorsProvider getEditorsProvider() {
        // 返回 Java 调试编辑器提供者
        return new com.intellij.debugger.impl.DebuggerUtilsEx().getEditorsProvider();
    }

    @Override
    public void startStepOver(@Nullable XSuspendContext suspendContext) {
        if (javaDebugProcess != null) {
            javaDebugProcess.startStepOver(suspendContext);
        }
    }

    @Override
    public void startStepInto(@Nullable XSuspendContext suspendContext) {
        if (javaDebugProcess != null) {
            javaDebugProcess.startStepInto(suspendContext);
        }
    }

    @Override
    public void startStepOut(@Nullable XSuspendContext suspendContext) {
        if (javaDebugProcess != null) {
            javaDebugProcess.startStepOut(suspendContext);
        }
    }

    @Override
    public void resume(@Nullable XSuspendContext suspendContext) {
        if (javaDebugProcess != null) {
            javaDebugProcess.resume(suspendContext);
        }
    }

    @Override
    public void stop() {
        processHandler.destroyProcess();
    }

    @Override
    public void runToPosition(@NotNull XSourcePosition position,
                               @Nullable XSuspendContext suspendContext) {
        if (javaDebugProcess != null) {
            javaDebugProcess.runToPosition(position, suspendContext);
        }
    }
}
