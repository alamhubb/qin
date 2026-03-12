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
import com.qin.debug.QinCommandResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Qin 娴嬭瘯杩愯鐘舵€?
 * 鎵ц qin test 鍛戒护骞朵娇鐢?TeamCity 鏍煎紡瑙ｆ瀽娴嬭瘯缁撴灉
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

        // 浣跨敤 SMTestRunnerConnectionUtil 鍒涘缓娴嬭瘯鎺у埗鍙?
        // 杩欎細鑷姩瑙ｆ瀽 TeamCity 鏍煎紡鐨勬祴璇曡緭鍑?
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
        command.add("test");

        // 鍚敤 TeamCity 鏍煎紡杈撳嚭
        command.add("--teamcity");

        // 鎸囧畾娴嬭瘯绫伙紙濡傛灉鏈夛級
        String testClass = configuration.getTestClass();
        if (testClass != null && !testClass.isEmpty()) {
            command.add("--class=" + testClass);
        }

        // 鎸囧畾娴嬭瘯鏂规硶锛堝鏋滄湁锛?
        String testMethod = configuration.getTestMethod();
        if (testMethod != null && !testMethod.isEmpty()) {
            command.add("--method=" + testMethod);
        }

        return QinCommandResolver.createGeneralCommandLine(
                projectPath,
                command.toArray(String[]::new));
    }
}
