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
import com.intellij.openapi.project.Project;
import com.qin.debug.QinCommandResolver;
import com.qin.debug.QinLogger;
import com.qin.debug.console.QinConsoleFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Qin 杩愯鐘舵€?
 * 璐熻矗鎵ц qin run 鍛戒护骞跺鐞嗚繘绋嬭緭鍑?
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

        // 鍒涘缓鎺у埗鍙板苟娣诲姞閿欒杩囨护鍣?
        ConsoleView console = createConsole(executor);
        console.addMessageFilter(new QinConsoleFilter(getEnvironment().getProject()));
        console.attachToProcess(processHandler);

        // 鏄剧ず鍚姩淇℃伅
        console.print("Starting Qin application...\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        console.print("Project: " + configuration.getProjectPath() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        if (debugMode) {
            console.print("Debug port: " + configuration.getDebugPort() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        }
        console.print("\n", ConsoleViewContentType.SYSTEM_OUTPUT);

        return new DefaultExecutionResult(console, processHandler);
    }

    /**
     * 鍒涘缓鍛戒护琛?
     */
    private GeneralCommandLine createCommandLine() throws ExecutionException {
        String projectPath = configuration.getResolvedProjectPath();
        if (projectPath == null || projectPath.isEmpty()) {
            QinLogger.error("[RUN] Project path is empty before command creation");
            throw new ExecutionException("Project path is not specified");
        }

        List<String> command = new ArrayList<>();
        command.add("run");

        // 璋冭瘯妯″紡娣诲姞 --debug 鍙傛暟
        if (debugMode) {
            command.add("--debug");
            command.add("--debug-port=" + configuration.getDebugPort());
        }

        // 娣诲姞涓荤被鍙傛暟锛堝鏋滄寚瀹氾級
        String mainClass = configuration.getResolvedMainClass();
        if (mainClass != null && !mainClass.isEmpty()) {
            command.add(mainClass);
        }

        // 娣诲姞 JVM 鍙傛暟
        String jvmArgs = configuration.getJvmArguments();
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            command.add("--jvm-args=" + jvmArgs);
        }

        // 娣诲姞绋嬪簭鍙傛暟
        String programArgs = configuration.getProgramArguments();
        if (programArgs != null && !programArgs.isEmpty()) {
            command.add("--");
            for (String arg : programArgs.split("\\s+")) {
                if (!arg.isEmpty()) {
                    command.add(arg);
                }
            }
        }

        GeneralCommandLine commandLine = QinCommandResolver.createGeneralCommandLine(
                projectPath,
                command.toArray(String[]::new));
        QinLogger.info("[RUN] Command prepared for mainClass=" + mainClass);

        return commandLine;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public QinRunConfiguration getConfiguration() {
        return configuration;
    }
}
