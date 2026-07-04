package com.qin.debug.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.qin.debug.QinCommandResolver;
import com.qin.debug.test.QinTestConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds Qin run and test command lines for IDEA run profile states.
 */
public final class QinRunCommandLines {
    private QinRunCommandLines() {
    }

    public static @NotNull GeneralCommandLine run(
            @NotNull QinRunConfiguration configuration,
            boolean debugMode) throws ExecutionException {
        List<String> command = new ArrayList<>();
        command.add("run");

        if (debugMode) {
            command.add("--debug");
            command.add("--debug-port=" + configuration.getDebugPort());
        }

        String mainClass = configuration.getResolvedMainClass();
        if (mainClass != null && !mainClass.isEmpty()) {
            command.add(mainClass);
        }

        String jvmArgs = configuration.getJvmArguments();
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            command.add("--jvm-args=" + jvmArgs);
        }

        String programArgs = configuration.getProgramArguments();
        if (programArgs != null && !programArgs.isEmpty()) {
            command.add("--");
            for (String arg : programArgs.split("\\s+")) {
                if (!arg.isEmpty()) {
                    command.add(arg);
                }
            }
        }

        return createCommandLine(configuration.getResolvedProjectPath(), command);
    }

    public static @NotNull GeneralCommandLine test(
            @NotNull QinTestConfiguration configuration) throws ExecutionException {
        List<String> command = new ArrayList<>();
        command.add("test");
        command.add("--teamcity");

        String testClass = configuration.getTestClass();
        if (testClass != null && !testClass.isEmpty()) {
            command.add("--class=" + testClass);
        }

        String testMethod = configuration.getTestMethod();
        if (testMethod != null && !testMethod.isEmpty()) {
            command.add("--method=" + testMethod);
        }

        return createCommandLine(configuration.getResolvedProjectPath(), command);
    }

    private static @NotNull GeneralCommandLine createCommandLine(
            String projectPath,
            @NotNull List<String> command) throws ExecutionException {
        return QinCommandResolver.createGeneralCommandLine(
                requireProjectPath(projectPath),
                command.toArray(String[]::new));
    }

    private static @NotNull String requireProjectPath(String projectPath) throws ExecutionException {
        if (projectPath == null || projectPath.isEmpty()) {
            throw new ExecutionException("Project path is not specified");
        }
        return projectPath;
    }
}
