package com.qin.debug.test;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 测试配置工厂
 */
public class QinTestConfigurationFactory extends ConfigurationFactory {

    public QinTestConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public String getId() {
        return QinTestConfigurationType.ID;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new QinTestConfiguration(project, this, "Qin Test");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return QinTestConfigurationOptions.class;
    }
}
