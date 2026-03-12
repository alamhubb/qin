package com.qin.debug;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.qin.constants.QinConstants.QIN_CMD;

/**
 * Resolves the most appropriate Qin launcher for the current workspace.
 */
public final class QinCommandResolver {

    private QinCommandResolver() {
    }

    public static ProcessBuilder createProcessBuilder(String workingDirectory, String... args) {
        List<String> command = buildCommand(workingDirectory, args);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(workingDirectory));
        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    public static GeneralCommandLine createGeneralCommandLine(String workingDirectory, String... args) {
        GeneralCommandLine commandLine = new GeneralCommandLine(buildCommand(workingDirectory, args));
        commandLine.setWorkDirectory(new File(workingDirectory));
        commandLine.setCharset(StandardCharsets.UTF_8);
        return commandLine;
    }

    private static List<String> buildCommand(String workingDirectory, String... args) {
        String executable = resolveExecutable(workingDirectory);
        List<String> command = new ArrayList<>();
        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");

        if (windows) {
            command.add("cmd");
            command.add("/c");
            command.add(executable);
        } else {
            command.add(executable);
        }

        command.addAll(Arrays.asList(args));
        return command;
    }

    private static String resolveExecutable(String workingDirectory) {
        Path start = Paths.get(workingDirectory).toAbsolutePath().normalize();
        Path current = Files.isDirectory(start) ? start : start.getParent();

        while (current != null) {
            Path direct = current.resolve("qin.bat");
            if (Files.exists(direct)) {
                return direct.toString();
            }

            Path nested = current.resolve("qin").resolve("qin.bat");
            if (Files.exists(nested)) {
                return nested.toString();
            }

            current = current.getParent();
        }

        return QIN_CMD;
    }
}
