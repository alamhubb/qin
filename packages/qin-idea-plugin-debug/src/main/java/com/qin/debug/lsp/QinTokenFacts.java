package com.qin.debug.lsp;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class QinTokenFacts {
    private QinTokenFacts() {
    }

    static boolean isTrivia(@Nullable IElementType tokenType) {
        return tokenType == TokenType.WHITE_SPACE
                || tokenType == QinTokenTypes.LINE_COMMENT
                || tokenType == QinTokenTypes.BLOCK_COMMENT;
    }

    static boolean isReferenceLeafToken(@Nullable IElementType tokenType) {
        return tokenType == QinTokenTypes.IDENTIFIER
                || tokenType == QinTokenTypes.CLASS_NAME
                || tokenType == QinTokenTypes.MEMBER_IDENTIFIER;
    }

    static boolean isDeclarationIdentifierToken(@NotNull QinLexicalToken token) {
        IElementType tokenType = token.type();
        return isReferenceLeafToken(tokenType)
                || tokenType == QinTokenTypes.FUNCTION_IDENTIFIER;
    }

    static @Nullable QinLexicalToken nextMeaningfulToken(
            @NotNull List<QinLexicalToken> tokens,
            int tokenIndex) {
        int index = nextMeaningfulTokenIndex(tokens, tokenIndex + 1);
        return index < 0 ? null : tokens.get(index);
    }

    static int nextMeaningfulTokenIndex(@NotNull List<QinLexicalToken> tokens, int startIndex) {
        for (int index = startIndex; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (!isTrivia(token.type())) {
                return index;
            }
        }
        return -1;
    }

    static @Nullable QinLexicalToken previousMeaningfulToken(@NotNull List<QinLexicalToken> tokens) {
        for (int index = tokens.size() - 1; index >= 0; index--) {
            QinLexicalToken token = tokens.get(index);
            if (!isTrivia(token.type())) {
                return token;
            }
        }
        return null;
    }

    static @NotNull CharSequence slice(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return content.subSequence(token.startOffset(), token.endOffset());
    }

    static boolean tokenStartsWith(
            @NotNull CharSequence content,
            @NotNull QinLexicalToken token,
            char expected) {
        return token.startOffset() < content.length() && content.charAt(token.startOffset()) == expected;
    }
}
