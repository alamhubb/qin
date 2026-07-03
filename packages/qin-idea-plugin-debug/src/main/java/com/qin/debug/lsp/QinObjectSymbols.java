package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinObjectSymbols {
    private QinObjectSymbols() {
    }

    static @Nullable PsiElement findObjectName(@NotNull PsiElement element, @NotNull String name) {
        PsiElement file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        ObjectNameVisitor visitor = new ObjectNameVisitor(name);
        file.accept(visitor);
        return visitor.objectName;
    }

    static @Nullable PsiElement findMethodName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String methodName) {
        PsiElement objectNameElement = findObjectName(element, objectName);
        if (objectNameElement == null) {
            return null;
        }
        PsiElement objectDeclaration = parentOfType(objectNameElement, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        MethodNameVisitor visitor = new MethodNameVisitor(methodName);
        objectDeclaration.accept(visitor);
        return visitor.methodName;
    }

    private static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull IElementType type) {
        PsiElement current = element.getParent();
        while (current != null) {
            if (current.getNode() != null && current.getNode().getElementType() == type) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static final class ObjectNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement objectName;

        private ObjectNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (objectName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.OBJECT_NAME
                    && name.equals(element.getText())) {
                objectName = element;
                return;
            }
            super.visitElement(element);
        }
    }

    private static final class MethodNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement methodName;

        private MethodNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (methodName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.METHOD_NAME
                    && name.equals(element.getText())) {
                methodName = element;
                return;
            }
            super.visitElement(element);
        }
    }
}
