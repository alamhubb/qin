package com.qin.debug.lsp;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

record QinLexicalToken(@NotNull IElementType type, int startOffset, int endOffset) {
}
