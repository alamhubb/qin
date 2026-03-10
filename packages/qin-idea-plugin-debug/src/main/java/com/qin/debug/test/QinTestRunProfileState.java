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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Qin 测试运行状态
 * 执行 qin test 命令并使用 TeamCity 格式解析测试结果
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

        // 使用 SMTestRunnerConnectionUtil 创建测试控制台
        // 这会自动解析 TeamCity 格式的测试输出
        ConsoleView console = SMTestRunnerConnectionUtil.createAndAttachConsole(
            "Qin",
            processHandler,
            new QinTestConsoleProperties(configuration, executor)
        );

        return new DefaultExecutionResult(console, processHandler);
    }

    private GeneralCommandLine createCommandLine() throws ExecutionException {
        String projectPath = configuration.getProjectPath();
        if (projectPath == null || projectPath.isEmpty()) {
            throw new ExecutionException("Project path is not specified");
        }

        List<String> command = new ArrayList<>();

        // Windows 使用 cmd /c
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add("qin");
        } else {
            command.add("qin");
        }

        command.add("test");

        // 启用 TeamCity 格式输出
        command.add("--teamcity");

        // 指定测试类（如果有）
        String testClass = configuration.getTestClass();
        if (testClass != null && !testClass.isEmpty()) {
            command.add("--class=" + testClass);
        }

        // 指定测试方法（如果有）
        String testMethod = configuration.getTestMethod();
        if (testMethod != null && !testMethod.isEmpty()) {
            command.add("--method=" + testMethod);
        }

        GeneralCommandLine commandLine = new GeneralCommandLine(command);
        commandLine.setWorkDirectory(new File(projectPath));
        commandLine.setCharset(StandardCharsets.UTF_8);

        return commandLine;
    }
}
