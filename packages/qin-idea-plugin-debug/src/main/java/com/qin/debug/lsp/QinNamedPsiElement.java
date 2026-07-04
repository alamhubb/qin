package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class QinNamedPsiElement extends QinPsiElement implements PsiNameIdentifierOwner {
    QinNamedPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getFirstChild();
    }

    @Override
    public @Nullable String getName() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        PsiElement identifier = getNameIdentifier();
        if (identifier != null) {
            QinPsiRenames.replaceLeafText(identifier, name, "Qin named element");
            return this;
        }
        throw new IncorrectOperationException("Cannot rename Qin named element without a name token: " + this);
    }
}
