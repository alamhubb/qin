package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;

final class QinPsiResolveResult implements ResolveResult {
    private final PsiElement element;

    QinPsiResolveResult(@NotNull PsiElement element) {
        this.element = element;
    }

    @Override
    public PsiElement getElement() {
        return element;
    }

    @Override
    public boolean isValidResult() {
        return element.isValid();
    }
}
