package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
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

    static @Nullable PsiElement elementAtOrParentOfType(
            @NotNull PsiFile file,
            int offset,
            @NotNull IElementType type) {
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return null;
        }
        return isType(element, type) ? element : parentOfType(element, type);
    }

    static @Nullable PsiElement firstDescendantOfType(@NotNull PsiElement root, @NotNull IElementType type) {
        FirstElementVisitor visitor = new FirstElementVisitor(type);
        root.accept(visitor);
        return visitor.element;
    }

    static @Nullable PsiElement firstDescendantOfType(
            @NotNull PsiElement root,
            @NotNull IElementType type,
            @NotNull String text) {
        FirstElementWithTextVisitor visitor = new FirstElementWithTextVisitor(type, text);
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

    static @NotNull List<PsiElement> descendantsOfAnyType(
            @NotNull PsiElement root,
            @NotNull IElementType firstType,
            @NotNull IElementType secondType) {
        List<PsiElement> elements = new ArrayList<>();
        root.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (isType(element, firstType) || isType(element, secondType)) {
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

    private static final class FirstElementWithTextVisitor extends PsiRecursiveElementWalkingVisitor {
        private final IElementType type;
        private final String text;
        private PsiElement element;

        private FirstElementWithTextVisitor(@NotNull IElementType type, @NotNull String text) {
            this.type = type;
            this.text = text;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (this.element != null) {
                return;
            }
            if (isType(element, type) && text.equals(element.getText())) {
                this.element = element;
                return;
            }
            super.visitElement(element);
        }
    }
}
