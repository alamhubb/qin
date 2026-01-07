package com.qin.debug;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Qin 插件日志工具（静态单例模式）
 * 日志路径: {project}/.qin/logs/{yyyy-MM-dd-HH}.log
 * 
 * 用法:
 * QinLogger.init(basePath); // 初始化（项目打开时调用一次）
 * QinLogger.info("message"); // 静态调用
 */
public class QinLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private static Path logFile;
    private static boolean initialized = false;

    private QinLogger() {
    } // 禁止实例化

    /**
     * 初始化日志器（项目打开时调用一次）
     */
    public static synchronized void init(String projectPath) {
        if (initialized)
            return;

        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(projectPath, ".qin", "logs");
        logFile = logDir.resolve(timestamp + ".log");

        try {
            Files.createDirectories(logDir);
            initialized = true;
            info("[LOGGER] 日志器初始化完成");
            info("[LOGGER] 日志文件: " + logFile);
        } catch (IOException e) {
            e.printStackTrace();
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

    private static synchronized void log(String level, String msg) {
        if (!initialized || logFile == null) {
            System.out.println("[QinLogger] " + msg);
            return;
        }

        String time = LocalDateTime.now().format(TIME_FMT);
        // 移除 ANSI 颜色控制码（如 \u001b[32m）
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

    /**
     * 获取日志文件路径
     */
    public static Path getLogFile() {
        return logFile;
    }
}
