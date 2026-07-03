package com.qin.debug.lsp;

import com.intellij.lang.PsiBuilder;
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
        return isDeclarationIdentifierToken(token.type());
    }

    static boolean isDeclarationIdentifierToken(@Nullable IElementType tokenType) {
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

    static boolean isKeyword(
            @NotNull CharSequence content,
            @NotNull QinLexicalToken token,
            @NotNull String expected) {
        return token.type() == QinTokenTypes.KEYWORD
                && expected.contentEquals(slice(content, token));
    }

    static boolean isKeyword(@NotNull PsiBuilder builder, @NotNull String expected) {
        return builder.getTokenType() == QinTokenTypes.KEYWORD
                && expected.equals(builder.getTokenText());
    }

    static boolean isOpenBrace(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.BRACE && tokenStartsWith(content, token, '{');
    }

    static boolean isOpenBrace(@NotNull PsiBuilder builder) {
        return builder.getTokenType() == QinTokenTypes.BRACE && expectedTokenText(builder, "{");
    }

    static boolean isCloseBrace(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.BRACE && tokenStartsWith(content, token, '}');
    }

    static boolean isCloseBrace(@NotNull PsiBuilder builder) {
        return builder.getTokenType() == QinTokenTypes.BRACE && expectedTokenText(builder, "}");
    }

    static boolean isOpenParen(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.PAREN && tokenStartsWith(content, token, '(');
    }

    static boolean isOpenParen(@NotNull PsiBuilder builder) {
        return builder.getTokenType() == QinTokenTypes.PAREN && expectedTokenText(builder, "(");
    }

    static boolean isCloseParen(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.PAREN && tokenStartsWith(content, token, ')');
    }

    static boolean isCloseParen(@NotNull PsiBuilder builder) {
        return builder.getTokenType() == QinTokenTypes.PAREN && expectedTokenText(builder, ")");
    }

    static boolean isAssignmentOperator(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.OPERATOR && tokenStartsWith(content, token, '=');
    }

    static boolean isFieldDeclarationStart(@NotNull PsiBuilder builder) {
        if (!isDeclarationIdentifierToken(builder.getTokenType())) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        return builder.rawLookup(offset) == QinTokenTypes.OPERATOR
                && rawTokenStartsWith(builder, offset, '=');
    }

    static boolean isMethodDeclarationStart(@NotNull PsiBuilder builder) {
        if (!isDeclarationIdentifierToken(builder.getTokenType())) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        if (builder.rawLookup(offset) != QinTokenTypes.PAREN || !rawTokenStartsWith(builder, offset, '(')) {
            return false;
        }

        int parenDepth = 0;
        while (offset != 0 && builder.rawLookup(offset) != null) {
            if (builder.rawLookup(offset) == QinTokenTypes.PAREN) {
                if (rawTokenStartsWith(builder, offset, '(')) {
                    parenDepth++;
                } else if (rawTokenStartsWith(builder, offset, ')')) {
                    parenDepth--;
                    if (parenDepth == 0) {
                        int afterParams = nextMeaningfulRawOffset(builder, offset + 1);
                        return builder.rawLookup(afterParams) == QinTokenTypes.BRACE
                                && rawTokenStartsWith(builder, afterParams, '{');
                    }
                }
            }
            offset = nextMeaningfulRawOffset(builder, offset + 1);
        }
        return false;
    }

    static boolean isThisMemberAccessStart(@NotNull PsiBuilder builder) {
        if (!isKeyword(builder, "this")) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        return builder.rawLookup(offset) == QinTokenTypes.DOT;
    }

    static int nextMeaningfulRawOffset(@NotNull PsiBuilder builder, int offset) {
        int current = offset;
        while (builder.rawLookup(current) != null && isTrivia(builder.rawLookup(current))) {
            current++;
        }
        return current;
    }

    static boolean rawTokenStartsWith(@NotNull PsiBuilder builder, int offset, char expected) {
        int start = builder.rawTokenTypeStart(offset);
        CharSequence text = builder.getOriginalText();
        return start >= 0 && start < text.length() && text.charAt(start) == expected;
    }

    private static boolean expectedTokenText(@NotNull PsiBuilder builder, @NotNull String expected) {
        return expected.equals(builder.getTokenText());
    }

    static boolean isObjectDeclarationKeyword(
            @NotNull CharSequence content,
            @NotNull QinLexicalToken token) {
        return isKeyword(content, token, "object");
    }

    static boolean isFieldDeclarationName(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int tokenIndex) {
        QinLexicalToken token = tokens.get(tokenIndex);
        if (!isDeclarationIdentifierToken(token)) {
            return false;
        }
        QinLexicalToken next = nextMeaningfulToken(tokens, tokenIndex);
        return next != null && isAssignmentOperator(content, next);
    }

    static boolean isMethodDeclarationName(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int tokenIndex) {
        QinLexicalToken token = tokens.get(tokenIndex);
        if (!isDeclarationIdentifierToken(token)) {
            return false;
        }
        int current = nextMeaningfulTokenIndex(tokens, tokenIndex + 1);
        if (current < 0 || !isOpenParen(content, tokens.get(current))) {
            return false;
        }

        int parenDepth = 0;
        while (current >= 0 && current < tokens.size()) {
            QinLexicalToken currentToken = tokens.get(current);
            if (currentToken.type() == QinTokenTypes.PAREN) {
                if (isOpenParen(content, currentToken)) {
                    parenDepth++;
                } else if (isCloseParen(content, currentToken)) {
                    parenDepth--;
                    if (parenDepth == 0) {
                        int afterParams = nextMeaningfulTokenIndex(tokens, current + 1);
                        return afterParams >= 0 && isOpenBrace(content, tokens.get(afterParams));
                    }
                }
            }
            current = nextMeaningfulTokenIndex(tokens, current + 1);
        }
        return false;
    }
}
