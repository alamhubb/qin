package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class QinNamedPsiElement extends QinPsiElement implements PsiNameIdentifierOwner {
    QinNamedPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    static @Nullable QinNamedPsiElement create(@NotNull ASTNode node) {
        IElementType elementType = node.getElementType();
        if (elementType == QinTokenTypes.OBJECT_NAME) {
            return new QinObjectNamePsiElement(node);
        }
        if (elementType == QinTokenTypes.METHOD_NAME) {
            return new QinMethodNamePsiElement(node);
        }
        if (elementType == QinTokenTypes.FIELD_NAME) {
            return new QinFieldNamePsiElement(node);
        }
        if (elementType == QinTokenTypes.IMPORT_ALIAS_NAME) {
            return new QinImportAliasNamePsiElement(node);
        }
        return null;
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getFirstChild();
    }

    @Override
    public @Nullable String getName() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : QinPsiTree.elementText(identifier);
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
