package com.qin.debug.test;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 测试运行配置
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
        return new QinTestRunProfileState(this, environment);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        String projectPath = getProjectPath();
        if (projectPath == null || projectPath.isEmpty()) {
            throw new RuntimeConfigurationError("Project path is not specified");
        }
    }
}
