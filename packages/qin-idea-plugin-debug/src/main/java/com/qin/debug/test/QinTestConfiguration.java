package com.qin.debug.test;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.qin.debug.run.QinRunConfigurationDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin test run configuration.
 */
public class QinTestConfiguration extends RunConfigurationBase<QinTestConfigurationOptions> {

    protected QinTestConfiguration(@NotNull Project project,
                                   @NotNull ConfigurationFactory factory,
                                   @Nullable String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected QinTestConfigurationOptions getOptions() {
        return (QinTestConfigurationOptions) super.getOptions();
    }

    public String getProjectPath() {
        return getOptions().getProjectPath();
    }

    public void setProjectPath(String path) {
        getOptions().setProjectPath(path);
    }

    public String getResolvedProjectPath() {
        return QinRunConfigurationDefaults.projectPath(getProject(), getProjectPath());
    }

    public String getTestClass() {
        return getOptions().getTestClass();
    }

    public void setTestClass(String className) {
        getOptions().setTestClass(className);
    }

    public String getTestMethod() {
        return getOptions().getTestMethod();
    }

    public void setTestMethod(String method) {
        getOptions().setTestMethod(method);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new QinTestConfigurationEditor(getProject());
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {
        applyResolvedDefaults();
        return new QinTestRunProfileState(this, environment);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        applyResolvedDefaults();
        if (getResolvedProjectPath() == null || getResolvedProjectPath().isBlank()) {
            throw new RuntimeConfigurationError(
                    "Project path is not specified and could not be inferred from qin.config.js");
        }
    }

    private void applyResolvedDefaults() {
        String resolvedProjectPath = getResolvedProjectPath();
        if (resolvedProjectPath != null && !resolvedProjectPath.equals(getProjectPath())) {
            setProjectPath(resolvedProjectPath);
        }
    }
}

