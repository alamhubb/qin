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

    static boolean isContextualKeyword(
            @NotNull CharSequence content,
            @NotNull QinLexicalToken token,
            @NotNull String expected) {
        return (token.type() == QinTokenTypes.KEYWORD || isReferenceLeafToken(token.type()))
                && expected.contentEquals(slice(content, token));
    }

    static boolean isKeyword(@NotNull PsiBuilder builder, @NotNull String expected) {
        return builder.getTokenType() == QinTokenTypes.KEYWORD
                && expected.equals(builder.getTokenText());
    }

    static boolean isContextualKeyword(@NotNull PsiBuilder builder, @NotNull String expected) {
        return (builder.getTokenType() == QinTokenTypes.KEYWORD || isReferenceLeafToken(builder.getTokenType()))
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
    static boolean isNewStatementAfterImport(
            @NotNull CharSequence content,
            int previousMeaningfulEndOffset,
            @NotNull QinLexicalToken currentToken,
            int braceDepth) {
        return braceDepth <= 0
                && !isContextualKeyword(content, currentToken, "from")
                && hasLineTerminatorBetween(content, previousMeaningfulEndOffset, currentToken.startOffset());
    }

    static boolean hasLineTerminatorBetween(
            @NotNull CharSequence content,
            int startOffset,
            int endOffset) {
        for (int index = Math.max(0, startOffset); index < Math.min(content.length(), endOffset); index++) {
            char value = content.charAt(index);
            if (value == '\n' || value == '\r') {
                return true;
            }
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
        return isFieldDeclarationName(new LexicalTokenSequence(content, tokens), tokenIndex);
    }

    static boolean isMethodDeclarationName(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int tokenIndex) {
        return isMethodDeclarationName(new LexicalTokenSequence(content, tokens), tokenIndex);
    }

    private static boolean isFieldDeclarationName(@NotNull TokenSequence sequence, int tokenIndex) {
        if (!isDeclarationIdentifierToken(sequence.typeAt(tokenIndex))) {
            return false;
        }
        int next = sequence.nextMeaningfulIndex(tokenIndex + 1);
        return sequence.typeAt(next) == QinTokenTypes.OPERATOR && sequence.startsWith(next, '=');
    }

    private static boolean isMethodDeclarationName(@NotNull TokenSequence sequence, int tokenIndex) {
        if (!isDeclarationIdentifierToken(sequence.typeAt(tokenIndex))) {
            return false;
        }
        int current = sequence.nextMeaningfulIndex(tokenIndex + 1);
        if (sequence.typeAt(current) != QinTokenTypes.PAREN || !sequence.startsWith(current, '(')) {
            return false;
        }

        int parenDepth = 0;
        while (sequence.typeAt(current) != null) {
            if (sequence.typeAt(current) == QinTokenTypes.PAREN) {
                if (sequence.startsWith(current, '(')) {
                    parenDepth++;
                } else if (sequence.startsWith(current, ')')) {
                    parenDepth--;
                    if (parenDepth == 0) {
                        return hasMethodBodyAfterParameters(sequence, current + 1);
                    }
                }
            }
            current = sequence.nextMeaningfulIndex(current + 1);
        }
        return false;
    }

    private static boolean hasMethodBodyAfterParameters(@NotNull TokenSequence sequence, int startIndex) {
        int previous = startIndex - 1;
        int current = sequence.nextMeaningfulIndex(startIndex);
        while (sequence.typeAt(current) != null) {
            if (sequence.hasLineTerminatorBetween(previous, current)) {
                return sequence.typeAt(current) == QinTokenTypes.BRACE && sequence.startsWith(current, '{');
            }
            if (sequence.typeAt(current) == QinTokenTypes.BRACE) {
                return sequence.startsWith(current, '{');
            }
            if (sequence.typeAt(current) == QinTokenTypes.SEMICOLON
                    || (sequence.typeAt(current) == QinTokenTypes.OPERATOR && sequence.startsWith(current, '='))) {
                return false;
            }
            previous = current;
            current = sequence.nextMeaningfulIndex(current + 1);
        }
        return false;
    }

    private interface TokenSequence {
        @Nullable IElementType typeAt(int index);

        int nextMeaningfulIndex(int startIndex);

        boolean startsWith(int index, char expected);

        boolean hasLineTerminatorBetween(int previousIndex, int currentIndex);
    }

    private record LexicalTokenSequence(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens) implements TokenSequence {
        @Override
        public @Nullable IElementType typeAt(int index) {
            return index >= 0 && index < tokens.size() ? tokens.get(index).type() : null;
        }

        @Override
        public int nextMeaningfulIndex(int startIndex) {
            return QinTokenFacts.nextMeaningfulTokenIndex(tokens, startIndex);
        }

        @Override
        public boolean hasLineTerminatorBetween(int previousIndex, int currentIndex) {
            if (previousIndex < 0 || previousIndex >= tokens.size() || currentIndex < 0 || currentIndex >= tokens.size()) {
                return false;
            }
            return QinTokenFacts.hasLineTerminatorBetween(
                    content,
                    tokens.get(previousIndex).endOffset(),
                    tokens.get(currentIndex).startOffset());
        }

        @Override
        public boolean startsWith(int index, char expected) {
            return index >= 0
                    && index < tokens.size()
                    && QinTokenFacts.tokenStartsWith(content, tokens.get(index), expected);
        }
    }
}
