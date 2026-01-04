package com.qin.debug;

import com.intellij.openapi.project.Project;
import java.nio.file.*;

/**
 * BSP 连接器
 */
public class QinBspConnector {
    private final Project project;

    public QinBspConnector(Project project) {
        this.project = project;
    }

    public boolean isQinProject() {
        String basePath = project.getBasePath();
        if (basePath == null)
            return false;
        return Files.exists(Paths.get(basePath, "qin.config.json"));
    }
}
