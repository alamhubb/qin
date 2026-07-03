package com.qin.debug.lsp;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public final class QinSyntaxHighlighter extends SyntaxHighlighterBase {
    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new QinLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType == QinTokenTypes.KEYWORD) {
            return pack(DefaultLanguageHighlighterColors.KEYWORD);
        }
        if (tokenType == QinTokenTypes.IDENTIFIER) {
            return pack(DefaultLanguageHighlighterColors.IDENTIFIER);
        }
        if (tokenType == QinTokenTypes.CLASS_NAME) {
            return pack(DefaultLanguageHighlighterColors.CLASS_NAME);
        }
        if (tokenType == QinTokenTypes.FUNCTION_IDENTIFIER) {
            return pack(DefaultLanguageHighlighterColors.FUNCTION_CALL);
        }
        if (tokenType == QinTokenTypes.MEMBER_IDENTIFIER) {
            return pack(DefaultLanguageHighlighterColors.INSTANCE_METHOD);
        }
        if (tokenType == QinTokenTypes.STRING) {
            return pack(DefaultLanguageHighlighterColors.STRING);
        }
        if (tokenType == QinTokenTypes.NUMBER) {
            return pack(DefaultLanguageHighlighterColors.NUMBER);
        }
        if (tokenType == QinTokenTypes.LINE_COMMENT) {
            return pack(DefaultLanguageHighlighterColors.LINE_COMMENT);
        }
        if (tokenType == QinTokenTypes.BLOCK_COMMENT) {
            return pack(DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        }
        if (tokenType == QinTokenTypes.BRACE) {
            return pack(DefaultLanguageHighlighterColors.BRACES);
        }
        if (tokenType == QinTokenTypes.PAREN) {
            return pack(DefaultLanguageHighlighterColors.PARENTHESES);
        }
        if (tokenType == QinTokenTypes.BRACKET) {
            return pack(DefaultLanguageHighlighterColors.BRACKETS);
        }
        if (tokenType == QinTokenTypes.COMMA) {
            return pack(DefaultLanguageHighlighterColors.COMMA);
        }
        if (tokenType == QinTokenTypes.SEMICOLON) {
            return pack(DefaultLanguageHighlighterColors.SEMICOLON);
        }
        if (tokenType == QinTokenTypes.DOT) {
            return pack(DefaultLanguageHighlighterColors.DOT);
        }
        if (tokenType == QinTokenTypes.OPERATOR) {
            return pack(DefaultLanguageHighlighterColors.OPERATION_SIGN);
        }
        if (tokenType == TokenType.BAD_CHARACTER) {
            return pack(DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        }
        return TextAttributesKey.EMPTY_ARRAY;
    }
}
