package com.qin.debug;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple UTF-8 project logger for the IDEA plugin.
 * Log path: {project}/.qin/logs/{yyyy-MM-dd-HH}.log
 */
public class QinLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private static Path logFile;
    private static boolean initialized = false;
    private static Project currentProject;
    private static boolean debugEnabled = true;

    private QinLogger() {
    }

    /**
     * Initializes the logger once per project.
     */
    public static synchronized void init(String projectPath, Project project) {
        if (initialized) {
            return;
        }

        currentProject = project;
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(projectPath, ".qin", "logs");
        logFile = logDir.resolve(timestamp + ".log");

        try {
            Files.createDirectories(logDir);
            initialized = true;
            info("[LOGGER] Logger initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Backward-compatible init entry point.
     */
    public static synchronized void init(String projectPath) {
        init(projectPath, null);
    }

    /**
     * Enables or disables debug logging.
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * Writes a debug line when debug logging is enabled.
     */
    public static void debug(String msg) {
        if (debugEnabled) {
            log("DEBUG", msg);
        }
    }

    public static void info(String msg) {
        log("INFO", msg);
    }

    public static void warn(String msg) {
        log("WARN", msg);
    }

    public static void error(String msg) {
        log("ERROR", msg);
    }

    public static void error(String msg, Throwable t) {
        error(msg + " - " + t.getMessage());
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        error(sw.toString());
    }

    /**
     * Shows a success notification in IDEA.
     */
    public static void notifySuccess(String title, String content) {
        notify(title, content, NotificationType.INFORMATION);
    }

    /**
     * Shows an error notification in IDEA.
     */
    public static void notifyError(String title, String content) {
        notify(title, content, NotificationType.ERROR);
    }

    /**
     * Shows an IDEA notification and falls back to the log file.
     */
    private static void notify(String title, String content, NotificationType type) {
        try {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Qin Notifications")
                    .createNotification(title, content, type)
                    .notify(currentProject);
        } catch (Exception e) {
            info("[Notify] " + title + ": " + content);
        }
    }

    private static synchronized void log(String level, String msg) {
        if (!initialized || logFile == null) {
            System.out.println("[QinLogger] " + msg);
            return;
        }

        String time = LocalDateTime.now().format(TIME_FMT);
        String cleanMsg = msg.replaceAll("\\u001B\\[[;\\d]*m", "");
        String line = String.format("[%s] [%s] %s%n", time, level, cleanMsg);
        try {
            Files.writeString(
                    logFile,
                    line,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getLogFile() {
        return logFile;
    }
}
