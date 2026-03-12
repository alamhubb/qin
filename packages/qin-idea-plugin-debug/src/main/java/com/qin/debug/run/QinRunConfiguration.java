package com.qin.debug.run;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.qin.debug.QinProjectLocator;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.qin.constants.QinConstants.CONFIG_FILE;

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

    public String getResolvedProjectPath() {
        return QinProjectLocator.resolveProjectPath(getProject(), getProjectPath());
    }

    public String getResolvedMainClass() {
        return QinProjectLocator.resolveMainClass(getProject(), getResolvedProjectPath(), getMainClass());
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
        applyResolvedDefaults();
        QinLogger.ensureInitialized(getProject(), getResolvedProjectPath());
        QinLogger.info("[RUN] Building RunProfileState: executor=" + executor.getId()
                + ", projectPath=" + getResolvedProjectPath()
                + ", mainClass=" + getResolvedMainClass());
        return new QinRunProfileState(this, environment);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        applyResolvedDefaults();
        String projectPath = getResolvedProjectPath();
        QinLogger.ensureInitialized(getProject(), projectPath);
        QinLogger.info("[RUN] Checking configuration: projectPath=" + projectPath + ", mainClass=" + getResolvedMainClass());
        if (projectPath == null || projectPath.isEmpty()) {
            QinLogger.error("[RUN] Configuration check failed: project path is empty");
            throw new RuntimeConfigurationError("Project path is not specified and could not be inferred from qin.config.json");
        }

        // 妫€鏌?qin.config.json 鏄惁瀛樺湪
        java.nio.file.Path configPath = java.nio.file.Paths.get(projectPath, CONFIG_FILE);
        if (!java.nio.file.Files.exists(configPath)) {
            QinLogger.error("[RUN] Configuration check failed: " + CONFIG_FILE + " missing at " + configPath);
            throw new RuntimeConfigurationError(CONFIG_FILE + " not found in: " + projectPath);
        }
    }

    private void applyResolvedDefaults() {
        String resolvedProjectPath = getResolvedProjectPath();
        if (resolvedProjectPath != null && !resolvedProjectPath.equals(getProjectPath())) {
            setProjectPath(resolvedProjectPath);
        }

        String resolvedMainClass = QinProjectLocator.resolveMainClass(getProject(), resolvedProjectPath, getMainClass());
        if (resolvedMainClass != null && !resolvedMainClass.equals(getMainClass())) {
            setMainClass(resolvedMainClass);
        }
    }
}
