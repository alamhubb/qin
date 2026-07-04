package com.qin.debug.run;

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
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.qin.debug.QinLogger;
import com.qin.debug.console.QinConsoleFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Qin application run profile state.
 */
public class QinRunProfileState extends CommandLineState {

    private final QinRunConfiguration configuration;
    private final boolean debugMode;

    public QinRunProfileState(QinRunConfiguration configuration,
                               ExecutionEnvironment environment) {
        this(configuration, environment, false);
    }

    public QinRunProfileState(QinRunConfiguration configuration,
                               ExecutionEnvironment environment,
                               boolean debugMode) {
        super(environment);
        this.configuration = configuration;
        this.debugMode = debugMode;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        try {
            QinLogger.ensureInitialized(getEnvironment().getProject(), configuration.getProjectPath());
            GeneralCommandLine commandLine = createCommandLine();
            QinLogger.info("[RUN] Starting process");
            QinLogger.info("[RUN] Work directory: " + commandLine.getWorkDirectory());
            QinLogger.info("[RUN] Command: " + commandLine.getCommandLineString());

            OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
                .createColoredProcessHandler(commandLine);
            ProcessTerminatedListener.attach(processHandler);
            return processHandler;
        } catch (ExecutionException e) {
            QinLogger.error("[RUN] Failed to start process", e);
            throw e;
        } catch (Exception e) {
            QinLogger.error("[RUN] Unexpected process startup failure", e);
            throw new ExecutionException("Failed to start Qin process: " + e.getMessage(), e);
        }
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor,
                                    @NotNull ProgramRunner<?> runner) throws ExecutionException {
        QinLogger.ensureInitialized(getEnvironment().getProject(), configuration.getProjectPath());
        QinLogger.info("[RUN] Execute requested: executor=" + executor.getId()
                + ", mainClass=" + configuration.getMainClass()
                + ", projectPath=" + configuration.getProjectPath()
                + ", debugMode=" + debugMode);
        ProcessHandler processHandler = startProcess();

        ConsoleView console = createConsole(executor);
        console.addMessageFilter(new QinConsoleFilter(getEnvironment().getProject()));
        console.attachToProcess(processHandler);

        console.print("Starting Qin application...\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        console.print("Project: " + configuration.getProjectPath() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        if (debugMode) {
            console.print("Debug port: " + configuration.getDebugPort() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        }
        console.print("\n", ConsoleViewContentType.SYSTEM_OUTPUT);

        return new DefaultExecutionResult(console, processHandler);
    }

    private GeneralCommandLine createCommandLine() throws ExecutionException {
        GeneralCommandLine commandLine = QinRunCommandLines.run(configuration, debugMode);
        QinLogger.info("[RUN] Command prepared for mainClass=" + configuration.getResolvedMainClass());
        return commandLine;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public QinRunConfiguration getConfiguration() {
        return configuration;
    }
}
