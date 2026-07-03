package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

record QinPsiToken(@NotNull PsiElement element, @NotNull IElementType type, @NotNull String text) {
    boolean isKeyword(String expectedText) {
        return type == QinTokenTypes.KEYWORD && expectedText.equals(text);
    }

    boolean isIdentifier() {
        return type == QinTokenTypes.IDENTIFIER
                || type == QinTokenTypes.CLASS_NAME
                || type == QinTokenTypes.FUNCTION_IDENTIFIER
                || type == QinTokenTypes.MEMBER_IDENTIFIER
                || type == QinTokenTypes.IMPORT_ALIAS_NAME;
    }

    boolean is(IElementType expectedType, String expectedText) {
        return type == expectedType && expectedText.equals(text);
    }
}
