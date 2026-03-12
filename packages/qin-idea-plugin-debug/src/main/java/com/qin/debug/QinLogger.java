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

import static com.qin.constants.QinConstants.LOG_SUBDIR;
import static com.qin.constants.QinConstants.QIN_DIR;

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
    private static String currentProjectPath;
    private static boolean debugEnabled = true;

    private QinLogger() {
    }

    public static synchronized void init(String projectPath, Project project) {
        if (projectPath == null || projectPath.isBlank()) {
            return;
        }

        if (initialized && projectPath.equals(currentProjectPath)) {
            if (project != null) {
                currentProject = project;
            }
            return;
        }

        currentProject = project;
        currentProjectPath = projectPath;
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(projectPath, QIN_DIR, LOG_SUBDIR);
        logFile = logDir.resolve(timestamp + ".log");

        try {
            Files.createDirectories(logDir);
            initialized = true;
            info("[LOGGER] Logger initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void init(String projectPath) {
        init(projectPath, null);
    }

    public static synchronized void ensureInitialized(Project project, String projectPath) {
        if (!initialized || logFile == null || (projectPath != null && !projectPath.isBlank() && !projectPath.equals(currentProjectPath))) {
            init(projectPath, project);
            return;
        }
        if (project != null && currentProject == null) {
            currentProject = project;
        }
    }

    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

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

    public static void notifySuccess(String title, String content) {
        notify(title, content, NotificationType.INFORMATION);
    }

    public static void notifyError(String title, String content) {
        notify(title, content, NotificationType.ERROR);
    }

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
            System.out.println("[QinLogger] " + normalizeForLog(msg));
            return;
        }

        String time = LocalDateTime.now().format(TIME_FMT);
        String cleanMsg = normalizeForLog(msg);
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

    private static String normalizeForLog(String msg) {
        if (msg == null || msg.isEmpty()) {
            return msg;
        }

        return msg
                .replaceAll("\\u001B\\[[;\\d]*m", "")
                .replace("鈫?", "-> ")
                .replace("→", "-> ")
                .replace("鉁?", "[OK] ")
                .replace("✓", "[OK] ")
                .replace("鈿?", "[WARN] ")
                .replace("⚠", "[WARN] ")
                .replace("鈼?", "[SKIP] ")
                .replace("•", "- ")
                .replace("鉂?", "[ERROR] ")
                .replace("✗", "[ERROR] ")
                .replace("锟斤拷", "")
                .trim();
    }
}
