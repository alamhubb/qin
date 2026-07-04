package com.qin.debug.test;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.ui.ConsoleView;
import com.qin.debug.run.QinRunCommandLines;
import org.jetbrains.annotations.NotNull;

/**
 * Qin test run profile state.
 */
public class QinTestRunProfileState extends CommandLineState {

    private final QinTestConfiguration configuration;

    public QinTestRunProfileState(QinTestConfiguration configuration,
                                   ExecutionEnvironment environment) {
        super(environment);
        this.configuration = configuration;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = createCommandLine();

        OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
            .createColoredProcessHandler(commandLine);
        ProcessTerminatedListener.attach(processHandler);

        return processHandler;
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor,
                                    @NotNull ProgramRunner<?> runner) throws ExecutionException {
        ProcessHandler processHandler = startProcess();

        ConsoleView console = SMTestRunnerConnectionUtil.createAndAttachConsole(
            "Qin",
            processHandler,
            new QinTestConsoleProperties(configuration, executor)
        );

        return new DefaultExecutionResult(console, processHandler);
    }

    private GeneralCommandLine createCommandLine() throws ExecutionException {
        return QinRunCommandLines.test(configuration);
    }
}
