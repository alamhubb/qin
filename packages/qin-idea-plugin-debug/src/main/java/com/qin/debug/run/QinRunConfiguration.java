package com.qin.debug.run;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 杩愯閰嶇疆
 * 瀛樺偍鍜岀鐞?Qin 椤圭洰鐨勮繍琛岄厤缃俊鎭?
 */
public class QinRunConfiguration extends RunConfigurationBase<QinRunConfigurationOptions> {

    protected QinRunConfiguration(@NotNull Project project,
                                   @NotNull ConfigurationFactory factory,
                                   @Nullable String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected QinRunConfigurationOptions getOptions() {
        return (QinRunConfigurationOptions) super.getOptions();
    }

    // ========== Getters and Setters ==========

    public String getProjectPath() {
        return getOptions().getProjectPath();
    }

    public void setProjectPath(String path) {
        getOptions().setProjectPath(path);
    }

    public String getMainClass() {
        return getOptions().getMainClass();
    }

    public void setMainClass(String className) {
        getOptions().setMainClass(className);
    }

    public String getProgramArguments() {
        return getOptions().getProgramArguments();
    }

    public void setProgramArguments(String args) {
        getOptions().setProgramArguments(args);
    }

    public String getJvmArguments() {
        return getOptions().getJvmArguments();
    }

    public void setJvmArguments(String args) {
        getOptions().setJvmArguments(args);
    }

    public int getDebugPort() {
        return getOptions().getDebugPort();
    }

    public void setDebugPort(int port) {
        getOptions().setDebugPort(port);
    }

    // ========== RunConfiguration Implementation ==========

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new QinRunConfigurationEditor(getProject());
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                     @NotNull ExecutionEnvironment environment) {
        QinLogger.info("[RUN] Building RunProfileState: executor=" + executor.getId()
                + ", projectPath=" + getProjectPath()
                + ", mainClass=" + getMainClass());
        return new QinRunProfileState(this, environment);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        String projectPath = getProjectPath();
        QinLogger.info("[RUN] Checking configuration: projectPath=" + projectPath + ", mainClass=" + getMainClass());
        if (projectPath == null || projectPath.isEmpty()) {
            QinLogger.error("[RUN] Configuration check failed: project path is empty");
            throw new RuntimeConfigurationError("Project path is not specified");
        }

        // 妫€鏌?qin.config.json 鏄惁瀛樺湪
        java.nio.file.Path configPath = java.nio.file.Paths.get(projectPath, "qin.config.json");
        if (!java.nio.file.Files.exists(configPath)) {
            QinLogger.error("[RUN] Configuration check failed: qin.config.json missing at " + configPath);
            throw new RuntimeConfigurationError("qin.config.json not found in: " + projectPath);
        }
    }
}
