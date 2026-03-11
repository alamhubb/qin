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
import com.qin.debug.QinLogger;
import com.qin.debug.console.QinConsoleFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Qin 运行状态
 * 负责执行 qin run 命令并处理进程输出
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
        QinLogger.info("[RUN] Execute requested: executor=" + executor.getId()
                + ", mainClass=" + configuration.getMainClass()
                + ", projectPath=" + configuration.getProjectPath()
                + ", debugMode=" + debugMode);
        ProcessHandler processHandler = startProcess();

        // 创建控制台并添加错误过滤器
        ConsoleView console = createConsole(executor);
        console.addMessageFilter(new QinConsoleFilter(getEnvironment().getProject()));
        console.attachToProcess(processHandler);

        // 显示启动信息
        console.print("Starting Qin application...\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        console.print("Project: " + configuration.getProjectPath() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        if (debugMode) {
            console.print("Debug port: " + configuration.getDebugPort() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        }
        console.print("\n", ConsoleViewContentType.SYSTEM_OUTPUT);

        return new DefaultExecutionResult(console, processHandler);
    }

    /**
     * 创建命令行
     */
    private GeneralCommandLine createCommandLine() throws ExecutionException {
        String projectPath = configuration.getProjectPath();
        if (projectPath == null || projectPath.isEmpty()) {
            QinLogger.error("[RUN] Project path is empty before command creation");
            throw new ExecutionException("Project path is not specified");
        }

        List<String> command = new ArrayList<>();

        // Windows 使用 cmd /c，Unix 直接使用 qin
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add("qin");
        } else {
            command.add("qin");
        }

        command.add("run");

        // 调试模式添加 --debug 参数
        if (debugMode) {
            command.add("--debug");
            command.add("--debug-port=" + configuration.getDebugPort());
        }

        // 添加主类参数（如果指定）
        String mainClass = configuration.getMainClass();
        if (mainClass != null && !mainClass.isEmpty()) {
            command.add("--main=" + mainClass);
        }

        // 添加 JVM 参数
        String jvmArgs = configuration.getJvmArguments();
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            command.add("--jvm-args=" + jvmArgs);
        }

        // 添加程序参数
        String programArgs = configuration.getProgramArguments();
        if (programArgs != null && !programArgs.isEmpty()) {
            command.add("--");
            for (String arg : programArgs.split("\\s+")) {
                if (!arg.isEmpty()) {
                    command.add(arg);
                }
            }
        }

        GeneralCommandLine commandLine = new GeneralCommandLine(command);
        commandLine.setWorkDirectory(new File(projectPath));
        commandLine.setCharset(StandardCharsets.UTF_8);

        // 设置环境变量
        commandLine.withEnvironment("JAVA_TOOL_OPTIONS", "-Dfile.encoding=UTF-8");
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
