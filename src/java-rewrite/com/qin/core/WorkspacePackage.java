package com.qin.core;

import com.qin.types.QinConfig;

/**
 * Workspace package information
 */
public class WorkspacePackage {
    private final String name;
    private final String path;
    private final QinConfig config;
    private final String classesDir;

    public WorkspacePackage(String name, String path, QinConfig config, String classesDir) {
        this.name = name;
        this.path = path;
        this.config = config;
        this.classesDir = classesDir;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
    public QinConfig getConfig() { return config; }
    public String getClassesDir() { return classesDir; }
}
