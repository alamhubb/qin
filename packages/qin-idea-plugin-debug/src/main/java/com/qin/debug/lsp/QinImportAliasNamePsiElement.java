package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

final class QinImportAliasNamePsiElement extends QinNamedPsiElement {
    QinImportAliasNamePsiElement(@NotNull ASTNode node) {
        super(node);
    }
}
