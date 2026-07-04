package com.qin.debug.gutter;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.qin.debug.QinLogger;
import com.qin.debug.lsp.QinPsiTree;
import com.qin.debug.run.QinJavaRunPsi;
import com.qin.debug.run.QinRunConfiguration;
import com.qin.debug.run.QinRunConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Collection;
import java.util.List;

/**
 * Provides run gutter icon for Qin main/test methods.
 */
public class QinRunLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        QinJavaRunPsi.RunTarget target = QinJavaRunPsi.gutterRunTargetAt(element);
        if (target == null) {
            return null;
        }

        if (QinJavaRunPsi.isMainMethod(target.method())) {
            return createLineMarkerInfo(element, "Run", AllIcons.RunConfigurations.TestState.Run);
        }

        if (QinJavaRunPsi.isTestMethod(target.method())) {
            return createLineMarkerInfo(element, "Run Test", AllIcons.RunConfigurations.TestState.Run);
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(
            @NotNull List<? extends PsiElement> elements,
            @NotNull Collection<? super LineMarkerInfo<?>> result) {
        // no-op
    }

    private LineMarkerInfo<PsiElement> createLineMarkerInfo(PsiElement element, String text, Icon icon) {
        return new LineMarkerInfo<>(
                element,
                QinPsiTree.elementRange(element),
                icon,
                psi -> text + " with Qin",
                (e, elt) -> runWithQin(elt, false),
                GutterIconRenderer.Alignment.CENTER,
                () -> text + " with Qin");
    }

    private void runWithQin(PsiElement element, boolean debug) {
        QinJavaRunPsi.RunTarget target = QinJavaRunPsi.runTargetForMethodElement(element);
        if (target == null) {
            QinLogger.error("[RUN] Gutter run aborted: method not found");
            return;
        }

        Project project = QinPsiTree.project(element);
        PsiClass containingClass = target.containingClass();
        String qualifiedName = target.qualifiedName();
        String projectPath = target.projectPath();

        QinLogger.ensureInitialized(project, projectPath);
        QinLogger.info("[RUN] Gutter run requested: mainClass=" + qualifiedName
                + ", projectPath=" + projectPath
                + ", debug=" + debug);

        RunManager runManager = RunManager.getInstance(project);
        QinRunConfigurationType configType = QinRunConfigurationType.getInstance();

        RunnerAndConfigurationSettings settings = runManager.createConfiguration(
                "Qin: " + containingClass.getName(),
                configType.getConfigurationFactories()[0]);

        QinRunConfiguration config = (QinRunConfiguration) settings.getConfiguration();
        config.setMainClass(qualifiedName);
        config.setProjectPath(projectPath);

        runManager.addConfiguration(settings);
        runManager.setSelectedConfiguration(settings);

        Executor executor = debug
                ? DefaultDebugExecutor.getDebugExecutorInstance()
                : DefaultRunExecutor.getRunExecutorInstance();

        ExecutionUtil.runConfiguration(settings, executor);
    }
}
