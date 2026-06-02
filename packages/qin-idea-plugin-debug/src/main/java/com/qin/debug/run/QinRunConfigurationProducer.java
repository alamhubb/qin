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
 * Qin 杩愯閰嶇疆鐢熶骇鑰? * 鍙抽敭鐐瑰嚮 main() 鏂规硶鏃惰嚜鍔ㄥ垱寤鸿繍琛岄厤缃? */
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

        // 鏌ユ壘 main 鏂规硶
        PsiMethod mainMethod = findMainMethod(element);
        if (mainMethod == null) return false;

        PsiClass containingClass = mainMethod.getContainingClass();
        if (containingClass == null) return false;

        String qualifiedName = containingClass.getQualifiedName();
        if (qualifiedName == null) return false;

        // 鏌ユ壘 Qin 椤圭洰璺緞
        String projectPath = findQinProjectPath(element);
        if (projectPath == null) return false;

        // 閰嶇疆 RunConfiguration
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
     * 鏌ユ壘 main 鏂规硶
     */
    private PsiMethod findMainMethod(PsiElement element) {
        // 妫€鏌ュ綋鍓嶅厓绱犳槸鍚﹀湪 main 鏂规硶鍐?        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class, false);
        if (method != null && isMainMethod(method)) {
            return method;
        }

        // 濡傛灉鍦ㄧ被鍐咃紝鏌ユ壘绫荤殑 main 鏂规硶
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
     * 妫€鏌ユ槸鍚︽槸 main 鏂规硶
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
     * 鍚戜笂鏌ユ壘 qin.config.js 鎵€鍦ㄧ洰褰?     */
    private String findQinProjectPath(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        if (psiFile == null) return null;

        VirtualFile file = psiFile.getVirtualFile();
        if (file == null) return null;

        Path nearest = QinProjectLocator.findNearestQinProject(Path.of(file.getPath()));
        return nearest != null ? nearest.toString().replace('\\', '/') : null;
    }
}

