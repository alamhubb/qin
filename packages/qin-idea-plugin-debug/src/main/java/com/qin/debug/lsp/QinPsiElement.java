package com.qin.debug.lsp;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

class QinPsiElement extends ASTWrapperPsiElement {
    QinPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return QinPsiReferences.references(this);
    }
}
