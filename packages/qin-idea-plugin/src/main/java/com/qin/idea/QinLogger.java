package com.qin.idea;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Qin 插件日志工具
 * 写入日志到 ~/.qin-idea/logs/
 */
public class QinLogger {
    private static final Path LOG_DIR;
    private static final Path LOG_FILE;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        String userHome = System.getProperty("user.home");
        LOG_DIR = Paths.get(userHome, ".qin-idea", "logs");
        LOG_FILE = LOG_DIR.resolve("qin-plugin.log");
        try {
            Files.createDirectories(LOG_DIR);
            info("QinLogger 初始化完成");
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
        log("ERROR", msg + " - " + t.getMessage());
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        log("ERROR", sw.toString());
    }

    private static synchronized void log(String level, String msg) {
        String time = LocalDateTime.now().format(FMT);
        String line = String.format("[%s] [%s] %s%n", time, level, msg);
        try {
            Files.writeString(LOG_FILE, line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
