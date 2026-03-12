package com.qin.debug.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.qin.debug.QinProjectLocator;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Qin 运行配置生产者
 * 右键点击 main() 方法时自动创建运行配置
 */
public class QinRunConfigurationProducer extends LazyRunConfigurationProducer<QinRunConfiguration> {

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return QinRunConfigurationType.getInstance().getConfigurationFactories()[0];
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull QinRunConfiguration configuration,
                                                     @NotNull ConfigurationContext context,
                                                     @NotNull Ref<PsiElement> sourceElement) {
        PsiElement element = context.getPsiLocation();
        if (element == null) return false;

        // 查找 main 方法
        PsiMethod mainMethod = findMainMethod(element);
        if (mainMethod == null) return false;

        PsiClass containingClass = mainMethod.getContainingClass();
        if (containingClass == null) return false;

        String qualifiedName = containingClass.getQualifiedName();
        if (qualifiedName == null) return false;

        // 查找 Qin 项目路径
        String projectPath = findQinProjectPath(element);
        if (projectPath == null) return false;

        // 配置 RunConfiguration
        configuration.setName("Qin: " + containingClass.getName());
        configuration.setMainClass(qualifiedName);
        configuration.setProjectPath(projectPath);

        sourceElement.set(mainMethod);
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull QinRunConfiguration configuration,
                                               @NotNull ConfigurationContext context) {
        PsiElement element = context.getPsiLocation();
        if (element == null) return false;

        PsiMethod mainMethod = findMainMethod(element);
        if (mainMethod == null) return false;

        PsiClass containingClass = mainMethod.getContainingClass();
        if (containingClass == null) return false;

        String qualifiedName = containingClass.getQualifiedName();
        return qualifiedName != null && qualifiedName.equals(configuration.getMainClass());
    }

    /**
     * 查找 main 方法
     */
    private PsiMethod findMainMethod(PsiElement element) {
        // 检查当前元素是否在 main 方法内
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class, false);
        if (method != null && isMainMethod(method)) {
            return method;
        }

        // 如果在类内，查找类的 main 方法
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class, false);
        if (psiClass != null) {
            for (PsiMethod m : psiClass.getMethods()) {
                if (isMainMethod(m)) {
                    return m;
                }
            }
        }

        return null;
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
     * 向上查找 qin.config.json 所在目录
     */
    private String findQinProjectPath(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        if (psiFile == null) return null;

        VirtualFile file = psiFile.getVirtualFile();
        if (file == null) return null;

        Path nearest = QinProjectLocator.findNearestQinProject(Path.of(file.getPath()));
        return nearest != null ? nearest.toString().replace('\\', '/') : null;
    }
}
