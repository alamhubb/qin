package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

final class QinPsiRenames {
    private QinPsiRenames() {
    }

    static @NotNull PsiElement replaceLeafText(
            @NotNull PsiElement element,
            @NotNull String newText,
            @NotNull String subject) throws IncorrectOperationException {
        ASTNode node = element.getNode();
        if (node instanceof LeafElement leafElement) {
            leafElement.replaceWithText(newText);
            return element;
        }
        ASTNode firstChild = node == null ? null : node.getFirstChildNode();
        if (firstChild instanceof LeafElement leafElement) {
            leafElement.replaceWithText(newText);
            return element;
        }
        throw new IncorrectOperationException("Cannot rename " + subject + " without a leaf token: " + element);
    }
}
