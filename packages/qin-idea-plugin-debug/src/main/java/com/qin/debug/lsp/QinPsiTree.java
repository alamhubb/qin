package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    static @Nullable PsiElement elementAtRangeOrParentOfType(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.SourceRange range,
            @NotNull IElementType type) {
        return range.isPresent() ? elementAtOrParentOfType(file, range.startOffset(), type) : null;
    }
}
