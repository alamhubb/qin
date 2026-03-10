package com.qin.debug.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Qin 运行配置类型
 * 定义 Qin 项目的运行配置类型，显示在 Run/Debug Configurations 对话框中
 */
public class QinRunConfigurationType implements ConfigurationType {

    public static final String ID = "QinRunConfiguration";

    @NotNull
    @Override
    public String getDisplayName() {
        return "Qin Application";
    }

    @Nls
    @Override
    public String getConfigurationTypeDescription() {
        return "Run or debug Qin Java application";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.RunConfigurations.Application;
    }

    @NotNull
    @Override
    public String getId() {
        return ID;
    }

    @NotNull
    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new QinConfigurationFactory(this)};
    }

    /**
     * 获取实例
     */
    public static QinRunConfigurationType getInstance() {
        return ConfigurationType.CONFIGURATION_TYPE_EP.findExtensionOrFail(QinRunConfigurationType.class);
    }
}
