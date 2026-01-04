package com.qin.debug;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 最小化调试启动器
 */
public class DebugStartup implements ProjectActivity {

    private static final Path LOG_FILE;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        String home = System.getProperty("user.home");
        LOG_FILE = Paths.get(home, ".qin-idea", "logs", "debug.log");
        try {
            Files.createDirectories(LOG_FILE.getParent());
            log("DebugStartup 类加载成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {
        log("项目打开: " + project.getName());
        log("路径: " + project.getBasePath());
        return Unit.INSTANCE;
    }

    private static void log(String msg) {
        String time = LocalDateTime.now().format(FMT);
        String line = "[" + time + "] " + msg + "\n";
        try {
            Files.writeString(LOG_FILE, line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
