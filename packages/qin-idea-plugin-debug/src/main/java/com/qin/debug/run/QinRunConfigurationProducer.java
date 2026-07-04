package com.qin.debug.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class QinRunConfigurationProducer extends LazyRunConfigurationProducer<QinRunConfiguration> {

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return QinRunConfigurationType.getInstance().getConfigurationFactories()[0];
    }

    @Override
    protected boolean setupConfigurationFromContext(
            @NotNull QinRunConfiguration configuration,
            @NotNull ConfigurationContext context,
            @NotNull Ref<PsiElement> sourceElement) {
        PsiElement element = context.getPsiLocation();
        if (element == null) return false;

        QinJavaRunPsi.RunTarget target = QinJavaRunPsi.runTargetAt(element);
        if (target == null) return false;

        configuration.setName("Qin: " + target.containingClass().getName());
        configuration.setMainClass(target.qualifiedName());
        configuration.setProjectPath(target.projectPath());

        sourceElement.set(target.method());
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(
            @NotNull QinRunConfiguration configuration,
            @NotNull ConfigurationContext context) {
        PsiElement element = context.getPsiLocation();
        if (element == null) return false;

        QinJavaRunPsi.RunTarget target = QinJavaRunPsi.runTargetAt(element);
        return target != null && target.qualifiedName().equals(configuration.getMainClass());
    }
}
