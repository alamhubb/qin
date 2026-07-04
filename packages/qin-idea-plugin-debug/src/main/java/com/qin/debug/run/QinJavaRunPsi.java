package com.qin.debug.run;

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
import com.intellij.psi.util.PsiTreeUtil;
import com.qin.debug.QinProjectLocator;
import com.qin.debug.lsp.QinPsiTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public final class QinJavaRunPsi {
    private QinJavaRunPsi() {
    }

    public static @Nullable RunTarget runTargetAt(@NotNull PsiElement element) {
        PsiMethod method = findMainMethod(element);
        if (method == null) {
            return null;
        }
        return runTarget(element, method);
    }

    public static @Nullable RunTarget runTargetForMethodElement(@NotNull PsiElement element) {
        PsiMethod method = methodAt(element);
        if (method == null) {
            return null;
        }
        return runTarget(element, method);
    }

    public static @Nullable RunTarget gutterRunTargetAt(@NotNull PsiElement element) {
        if (!(element instanceof PsiIdentifier)) {
            return null;
        }
        PsiElement parent = QinPsiTree.parent(element);
        if (!(parent instanceof PsiMethod method)) {
            return null;
        }
        if (!isMainMethod(method) && !isTestMethod(method)) {
            return null;
        }
        return runTarget(element, method);
    }

    public static @Nullable PsiMethod methodAt(@NotNull PsiElement element) {
        if (element instanceof PsiMethod method) {
            return method;
        }
        PsiElement parent = QinPsiTree.parent(element);
        return parent instanceof PsiMethod method ? method : null;
    }

    public static boolean isMainMethod(@NotNull PsiMethod method) {
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

    public static boolean isTestMethod(@NotNull PsiMethod method) {
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

    private static @Nullable PsiMethod findMainMethod(@NotNull PsiElement element) {
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class, false);
        if (method != null && isMainMethod(method)) {
            return method;
        }

        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class, false);
        if (psiClass != null) {
            for (PsiMethod candidate : psiClass.getMethods()) {
                if (isMainMethod(candidate)) {
                    return candidate;
                }
            }
        }

        return null;
    }

    private static @Nullable RunTarget runTarget(
            @NotNull PsiElement contextElement,
            @NotNull PsiMethod method) {
        PsiClass containingClass = method.getContainingClass();
        if (containingClass == null) {
            return null;
        }

        String qualifiedName = containingClass.getQualifiedName();
        String projectPath = findQinProjectPath(contextElement);
        if (qualifiedName == null || projectPath == null) {
            return null;
        }

        return new RunTarget(method, containingClass, qualifiedName, projectPath);
    }

    private static @Nullable String findQinProjectPath(@NotNull PsiElement element) {
        PsiFile file = QinPsiTree.containingFile(element);
        if (file == null) return null;

        VirtualFile virtualFile = QinPsiTree.virtualFile(file);
        if (virtualFile == null) return null;

        Path nearest = QinProjectLocator.findNearestQinProject(Path.of(virtualFile.getPath()));
        return nearest != null ? nearest.toString().replace('\\', '/') : null;
    }

    public record RunTarget(
            @NotNull PsiMethod method,
            @NotNull PsiClass containingClass,
            @NotNull String qualifiedName,
            @NotNull String projectPath) {
    }
}
