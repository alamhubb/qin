package com.qin.debug.run;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 运行配置
 * 存储和管理 Qin 项目的运行配置信息
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
        return new QinRunProfileState(this, environment);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        String projectPath = getProjectPath();
        if (projectPath == null || projectPath.isEmpty()) {
            throw new RuntimeConfigurationError("Project path is not specified");
        }

        // 检查 qin.config.json 是否存在
        java.nio.file.Path configPath = java.nio.file.Paths.get(projectPath, "qin.config.json");
        if (!java.nio.file.Files.exists(configPath)) {
            throw new RuntimeConfigurationError("qin.config.json not found in: " + projectPath);
        }
    }
}
