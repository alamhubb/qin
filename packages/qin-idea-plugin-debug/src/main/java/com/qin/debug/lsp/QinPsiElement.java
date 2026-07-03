package com.qin.debug.lsp;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

final class QinPsiElement extends ASTWrapperPsiElement {
    QinPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        IElementType elementType = getNode().getElementType();
        if (getContainingFile() instanceof QinPsiFile
                && elementType == QinTokenTypes.REFERENCE_IDENTIFIER) {
            return new PsiReference[]{new QinJavaReference(this)};
        }
        return super.getReferences();
    }
}
