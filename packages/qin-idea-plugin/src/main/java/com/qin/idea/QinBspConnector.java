package com.qin.idea;

import com.intellij.openapi.project.Project;
import java.io.*;
import java.nio.file.*;

/**
 * BSP 连接器
 * 负责启动 Qin BSP Server 并管理通信
 */
public class QinBspConnector {
    private final Project project;
    private Process bspProcess;

    public QinBspConnector(Project project) {
        this.project = project;
    }

    public void start() throws IOException {
        String basePath = project.getBasePath();
        if (basePath == null)
            return;

        // 检查是否为 Qin 项目
        Path configPath = Paths.get(basePath, "qin.config.json");
        if (!Files.exists(configPath))
            return;

        // TODO: 启动 BSP Server 进程
        // ProcessBuilder pb = new ProcessBuilder(
        // "java", "-cp", "...", "com.qin.bsp.BspServer");
        // pb.directory(new File(basePath));
        // bspProcess = pb.start();
    }

    public void stop() {
        if (bspProcess != null && bspProcess.isAlive()) {
            bspProcess.destroy();
        }
    }

    public boolean isQinProject() {
        String basePath = project.getBasePath();
        if (basePath == null)
            return false;
        return Files.exists(Paths.get(basePath, "qin.config.json"));
    }
}
