package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

final class QinMethodNamePsiElement extends QinNamedPsiElement {
    QinMethodNamePsiElement(@NotNull ASTNode node) {
        super(node);
    }
}
