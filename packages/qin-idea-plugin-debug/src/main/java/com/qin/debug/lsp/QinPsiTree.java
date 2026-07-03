package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinPsiTree {
    private QinPsiTree() {
    }

    static boolean isType(@NotNull PsiElement element, @NotNull IElementType type) {
        return element.getNode() != null && element.getNode().getElementType() == type;
    }

    static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull IElementType type) {
        PsiElement current = element.getParent();
        while (current != null) {
            if (isType(current, type)) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    static @Nullable PsiElement firstDescendantOfType(@NotNull PsiElement root, @NotNull IElementType type) {
        FirstElementVisitor visitor = new FirstElementVisitor(type);
        root.accept(visitor);
        return visitor.element;
    }

    static @NotNull List<PsiElement> descendantsOfType(@NotNull PsiElement root, @NotNull IElementType type) {
        List<PsiElement> elements = new ArrayList<>();
        root.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (isType(element, type)) {
                    elements.add(element);
                    return;
                }
                super.visitElement(element);
            }
        });
        return elements;
    }

    private static final class FirstElementVisitor extends PsiRecursiveElementWalkingVisitor {
        private final IElementType type;
        private PsiElement element;

        private FirstElementVisitor(@NotNull IElementType type) {
            this.type = type;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (this.element != null) {
                return;
            }
            if (isType(element, type)) {
                this.element = element;
                return;
            }
            super.visitElement(element);
        }
    }
}
