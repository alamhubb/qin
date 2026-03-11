package com.qin.debug.gutter;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.qin.debug.QinLogger;
import com.qin.debug.run.QinConfigurationFactory;
import com.qin.debug.run.QinRunConfiguration;
import com.qin.debug.run.QinRunConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * Qin 运行图标提供者
 * 在 main() 方法和 @Test 方法旁边显示运行图标
 */
public class QinRunLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        // 只处理标识符（方法名）
        if (!(element instanceof PsiIdentifier)) {
            return null;
        }

        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiMethod)) {
            return null;
        }

        PsiMethod method = (PsiMethod) parent;

        // 检查是否在 Qin 项目中
        if (!isInQinProject(method)) {
            return null;
        }

        // 检查是否是 main 方法
        if (isMainMethod(method)) {
            return createLineMarkerInfo(element, "Run", AllIcons.RunConfigurations.TestState.Run);
        }

        // 检查是否是 @Test 方法
        if (isTestMethod(method)) {
            return createLineMarkerInfo(element, "Run Test", AllIcons.RunConfigurations.TestState.Run);
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements,
                                        @NotNull Collection<? super LineMarkerInfo<?>> result) {
        // 不需要慢速处理
    }

    /**
     * 检查是否在 Qin 项目中
     */
    private boolean isInQinProject(PsiMethod method) {
        PsiFile file = method.getContainingFile();
        if (file == null) return false;

        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return false;

        // 向上查找 qin.config.json
        VirtualFile current = virtualFile.getParent();
        int maxDepth = 10;
        while (current != null && maxDepth-- > 0) {
            if (current.findChild("qin.config.json") != null) {
                return true;
            }
            current = current.getParent();
        }

        return false;
    }

    /**
     * 检查是否是 main 方法
     */
    private boolean isMainMethod(PsiMethod method) {
        if (!"main".equals(method.getName())) return false;
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) return false;
        if (!method.hasModifierProperty(PsiModifier.STATIC)) return false;

        PsiParameter[] params = method.getParameterList().getParameters();
        if (params.length != 1) return false;

        PsiType type = params[0].getType();
        return type.equalsToText("java.lang.String[]") ||
               type.equalsToText("String[]") ||
               type.getCanonicalText().equals("java.lang.String[]");
    }

    /**
     * 检查是否是 @Test 方法
     */
    private boolean isTestMethod(PsiMethod method) {
        for (PsiAnnotation annotation : method.getAnnotations()) {
            String name = annotation.getQualifiedName();
            if (name != null && (
                name.equals("org.junit.Test") ||
                name.equals("org.junit.jupiter.api.Test") ||
                name.endsWith(".Test"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建行标记信息
     */
    private LineMarkerInfo<PsiElement> createLineMarkerInfo(PsiElement element,
                                                             String text,
                                                             Icon icon) {
        return new LineMarkerInfo<>(
            element,
            element.getTextRange(),
            icon,
            psi -> text + " with Qin",
            (e, elt) -> {
                // 点击后显示运行菜单或直接运行
                runWithQin(elt, false);
            },
            GutterIconRenderer.Alignment.CENTER,
            () -> text + " with Qin"
        );
    }

    /**
     * 使用 Qin 运行
     */
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

        QinLogger.info("[RUN] Gutter run requested: mainClass=" + qualifiedName
                + ", projectPath=" + projectPath
                + ", debug=" + debug);

        // 创建运行配置
        RunManager runManager = RunManager.getInstance(project);
        QinRunConfigurationType configType = QinRunConfigurationType.getInstance();

        RunnerAndConfigurationSettings settings = runManager.createConfiguration(
            "Qin: " + containingClass.getName(),
            configType.getConfigurationFactories()[0]
        );

        QinRunConfiguration config = (QinRunConfiguration) settings.getConfiguration();
        config.setMainClass(qualifiedName);
        config.setProjectPath(projectPath);

        runManager.addConfiguration(settings);
        runManager.setSelectedConfiguration(settings);

        // 执行
        Executor executor = debug ?
            DefaultDebugExecutor.getDebugExecutorInstance() :
            DefaultRunExecutor.getRunExecutorInstance();

        ExecutionUtil.runConfiguration(settings, executor);
    }

    private PsiMethod findMethod(PsiElement element) {
        if (element instanceof PsiMethod) {
            return (PsiMethod) element;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod) {
            return (PsiMethod) parent;
        }
        return null;
    }

    private String findQinProjectPath(PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) return null;

        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return null;

        VirtualFile current = virtualFile.getParent();
        while (current != null) {
            if (current.findChild("qin.config.json") != null) {
                return current.getPath();
            }
            current = current.getParent();
        }

        return null;
    }
}
