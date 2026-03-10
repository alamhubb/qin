package com.qin.debug.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Qin 配置工厂
 * 负责创建 QinRunConfiguration 实例
 */
public class QinConfigurationFactory extends ConfigurationFactory {

    public QinConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public String getId() {
        return QinRunConfigurationType.ID;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new QinRunConfiguration(project, this, "Qin Application");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return QinRunConfigurationOptions.class;
    }
}
