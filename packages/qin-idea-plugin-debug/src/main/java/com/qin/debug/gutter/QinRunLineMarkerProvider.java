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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.qin.debug.QinLogger;
import com.qin.debug.QinProjectLocator;
import com.qin.debug.run.QinRunConfiguration;
import com.qin.debug.run.QinRunConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * Provides run gutter icon for Qin main/test methods.
 */
public class QinRunLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof PsiIdentifier)) {
            return null;
        }

        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiMethod method)) {
            return null;
        }

        if (!isInQinProject(method)) {
            return null;
        }

        if (isMainMethod(method)) {
            return createLineMarkerInfo(element, "Run", AllIcons.RunConfigurations.TestState.Run);
        }

        if (isTestMethod(method)) {
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

    private boolean isInQinProject(PsiMethod method) {
        return findQinProjectPath(method) != null;
    }

    private boolean isMainMethod(PsiMethod method) {
        if (!"main".equals(method.getName())) return false;
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) return false;
        if (!method.hasModifierProperty(PsiModifier.STATIC)) return false;

        PsiParameter[] params = method.getParameterList().getParameters();
        if (params.length != 1) return false;

        PsiType type = params[0].getType();
        return type.equalsToText("java.lang.String[]")
                || type.equalsToText("String[]")
                || "java.lang.String[]".equals(type.getCanonicalText());
    }

    private boolean isTestMethod(PsiMethod method) {
        for (PsiAnnotation annotation : method.getAnnotations()) {
            String name = annotation.getQualifiedName();
            if (name != null && (
                    name.equals("org.junit.Test")
                            || name.equals("org.junit.jupiter.api.Test")
                            || name.endsWith(".Test"))) {
                return true;
            }
        }
        return false;
    }

    private LineMarkerInfo<PsiElement> createLineMarkerInfo(PsiElement element, String text, Icon icon) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                icon,
                psi -> text + " with Qin",
                (e, elt) -> runWithQin(elt, false),
                GutterIconRenderer.Alignment.CENTER,
                () -> text + " with Qin");
    }

    private void runWithQin(PsiElement element, boolean debug) {
        PsiMethod method = findMethod(element);
        if (method == null) {
            QinLogger.error("[RUN] Gutter run aborted: method not found");
            return;
        }

        PsiClass containingClass = method.getContainingClass();
        if (containingClass == null) {
            QinLogger.error("[RUN] Gutter run aborted: containing class not found");
            return;
        }

        Project project = element.getProject();
        String qualifiedName = containingClass.getQualifiedName();
        String projectPath = findQinProjectPath(element);

        if (qualifiedName == null || projectPath == null) {
            QinLogger.error("[RUN] Gutter run aborted: qualifiedName=" + qualifiedName + ", projectPath=" + projectPath);
            return;
        }

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

    private PsiMethod findMethod(PsiElement element) {
        if (element instanceof PsiMethod method) {
            return method;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod method) {
            return method;
        }
        return null;
    }

    private String findQinProjectPath(PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) return null;

        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return null;

        Path nearest = QinProjectLocator.findNearestQinProject(Path.of(virtualFile.getPath()));
        return nearest != null ? nearest.toString().replace('\\', '/') : null;
    }
}
