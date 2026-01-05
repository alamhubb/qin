package com.qin.debug;

// 使用 qin-cli 的统一常量
import static com.qin.constants.QinConstants.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Qin 插件日志工具
 * 日志路径: {project}/.qin/logs/{yyyy-MM-dd-HH}.log
 */
public class QinLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private final Path logFile;

    /**
     * 创建日志器
     * 
     * @param projectPath 项目根目录路径
     */
    public QinLogger(String projectPath) {
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        Path logDir = Paths.get(projectPath, QIN_DIR, LOG_SUBDIR);
        this.logFile = logDir.resolve(timestamp + LOG_FILE_EXT);

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

    /**
     * 获取日志文件路径
     */
    public Path getLogFile() {
        return logFile;
    }
}
