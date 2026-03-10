package com.qin.debug.test;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Qin 测试运行配置类型
 */
public class QinTestConfigurationType implements ConfigurationType {

    public static final String ID = "QinTestConfiguration";

    @NotNull
    @Override
    public String getDisplayName() {
        return "Qin Test";
    }

    @Nls
    @Override
    public String getConfigurationTypeDescription() {
        return "Run Qin tests with TeamCity format output";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.RunConfigurations.TestState.Run;
    }

    @NotNull
    @Override
    public String getId() {
        return ID;
    }

    @NotNull
    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new QinTestConfigurationFactory(this)};
    }

    public static QinTestConfigurationType getInstance() {
        return ConfigurationType.CONFIGURATION_TYPE_EP.findExtensionOrFail(QinTestConfigurationType.class);
    }
}
