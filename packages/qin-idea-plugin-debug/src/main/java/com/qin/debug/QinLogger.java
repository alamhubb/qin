package com.qin.debug;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Qin 插件日志工具（静态单例模式）
 * 日志路径: {project}/.qin/logs/{yyyy-MM-dd-HH}.log
 * 
 * 功能：
 * - 日志级别控制（DEBUG 模式可开关）
 * - IDEA 通知弹窗
 */
public class QinLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private static Path logFile;
    private static boolean initialized = false;
    private static Project currentProject;

    // 日志级别开关
    private static boolean debugEnabled = true; // 可配置

    private QinLogger() {
    }

    /**
     * 初始化日志器
     */
    public static synchronized void init(String projectPath, Project project) {
        if (initialized)
            return;

        currentProject = project;
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(projectPath, ".qin", "logs");
        logFile = logDir.resolve(timestamp + ".log");

        try {
            Files.createDirectories(logDir);
            initialized = true;
            info("[LOGGER] 日志器初始化完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化日志器（兼容旧接口）
     */
    public static synchronized void init(String projectPath) {
        init(projectPath, null);
    }

    /**
     * 设置 DEBUG 日志开关
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * DEBUG 级别日志（可通过开关关闭）
     */
    public static void debug(String msg) {
        if (debugEnabled) {
            log("DEBUG", msg);
        }
    }

    public static void info(String msg) {
        log("INFO", msg);
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
     * 显示 IDEA 通知弹窗（成功）
     */
    public static void notifySuccess(String title, String content) {
        notify(title, content, NotificationType.INFORMATION);
    }

    /**
     * 显示 IDEA 通知弹窗（错误）
     */
    public static void notifyError(String title, String content) {
        notify(title, content, NotificationType.ERROR);
    }

    /**
     * 显示 IDEA 通知弹窗
     */
    private static void notify(String title, String content, NotificationType type) {
        try {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Qin Notifications")
                    .createNotification(title, content, type)
                    .notify(currentProject);
        } catch (Exception e) {
            // 如果通知组不存在，fallback 到日志
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
            Files.writeString(logFile, line,
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
