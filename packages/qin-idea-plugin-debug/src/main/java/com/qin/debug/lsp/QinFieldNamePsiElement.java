package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

final class QinFieldNamePsiElement extends QinNamedPsiElement {
    QinFieldNamePsiElement(@NotNull ASTNode node) {
        super(node);
    }
}
