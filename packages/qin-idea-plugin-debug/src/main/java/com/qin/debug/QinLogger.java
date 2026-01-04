package com.qin.debug;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Qin 插件日志工具
 * 日志路径: ~/.qin-idea/{project-name}/logs/{yyyy-MM-dd-HH}.log
 */
public class QinLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private final Path logFile;

    public QinLogger(String projectName) {
        String userHome = System.getProperty("user.home");
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(userHome, QinConstants.LOG_DIR_NAME, projectName, QinConstants.LOG_SUBDIR);
        this.logFile = logDir.resolve(timestamp + QinConstants.LOG_FILE_EXT);

        try {
            Files.createDirectories(logDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(String msg) {
        log("INFO", msg);
    }

    public void error(String msg) {
        log("ERROR", msg);
    }

    public void error(String msg, Throwable t) {
        log("ERROR", msg + " - " + t.getMessage());
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        log("ERROR", sw.toString());
    }

    private synchronized void log(String level, String msg) {
        String time = LocalDateTime.now().format(TIME_FMT);
        String line = String.format("[%s] [%s] %s%n", time, level, msg);
        try {
            Files.writeString(logFile, line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
