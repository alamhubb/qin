package com.qin.debug.lsp;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.slime.token.JavaScriptTokens;
import com.slime.token.TokenUtils;
import com.subhuti.lexer.SubhutiLexer;
import com.subhuti.lexer.TokenCacheEntry;
import com.subhuti.struct.LexerMode;
import com.subhuti.struct.SubhutiMatchToken;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class QinLexicalScanner {
    private QinLexicalScanner() {
    }

    static List<QinLexicalToken> scan(@NotNull CharSequence source, int startOffset, int endOffset) {
        String text = source.toString();
        SubhutiLexer lexer = new SubhutiLexer(JavaScriptTokens.getTokens());
        List<QinLexicalToken> tokens = new ArrayList<>();
        int cursor = startOffset;
        int line = 1;
        int column = 1;
        String lastTokenName = null;

        while (cursor < endOffset) {
            try {
                TokenCacheEntry entry = lexer.readTokenAt(
                        text,
                        cursor,
                        line,
                        column,
                        LexerMode.DEFAULT_MODE,
                        lastTokenName);
                if (entry == null || entry.getToken() == null) {
                    appendTrivia(tokens, text, cursor, endOffset);
                    break;
                }

                SubhutiMatchToken token = entry.getToken();
                int tokenStart = clamp(token.index(), startOffset, endOffset);
                int tokenEnd = clamp(entry.getTokenEndCodeIndex(), tokenStart, endOffset);
                if (cursor < tokenStart) {
                    appendTrivia(tokens, text, cursor, tokenStart);
                }
                if (tokenStart < tokenEnd) {
                    tokens.add(new QinLexicalToken(mapToken(token, tokens), tokenStart, tokenEnd));
                }
                cursor = Math.max(tokenEnd, cursor + 1);
                line = entry.getNextLine();
                column = entry.getNextColumn();
                lastTokenName = entry.getLastTokenName();
            } catch (SubhutiLexer.LexerException error) {
                int recoveredEnd = recoverEditorTokenEnd(text, cursor, endOffset);
                tokens.add(new QinLexicalToken(recoverEditorTokenType(text, cursor), cursor, recoveredEnd));
                cursor = recoveredEnd;
            }
        }

        return classifyFunctionIdentifiers(text, classifyImportContextualKeywords(text, tokens));
    }

    private static IElementType mapToken(SubhutiMatchToken token, List<QinLexicalToken> previousTokens) {
        String tokenName = token.getTokenName();
        String value = token.getTokenValue();
        if (TokenUtils.isKeyword(value) && !isImportOnlyContextualKeyword(value)) {
            return QinTokenTypes.KEYWORD;
        }
        if ("IdentifierName".equals(tokenName) || "PrivateIdentifier".equals(tokenName)) {
            return classifyIdentifier(value, previousTokens);
        }
        if ("StringLiteral".equals(tokenName)
                || "NoSubstitutionTemplate".equals(tokenName)
                || "TemplateHead".equals(tokenName)
                || "TemplateMiddle".equals(tokenName)
                || "TemplateTail".equals(tokenName)) {
            return QinTokenTypes.STRING;
        }
        if ("NumericLiteral".equals(tokenName)) {
            return QinTokenTypes.NUMBER;
        }
        return switch (tokenName) {
            case "LBrace", "RBrace" -> QinTokenTypes.BRACE;
            case "LParen", "RParen" -> QinTokenTypes.PAREN;
            case "LBracket", "RBracket" -> QinTokenTypes.BRACKET;
            case "Comma" -> QinTokenTypes.COMMA;
            case "Semicolon" -> QinTokenTypes.SEMICOLON;
            case "Dot" -> QinTokenTypes.DOT;
            default -> QinTokenTypes.OPERATOR;
        };
    }

    private static boolean isImportOnlyContextualKeyword(String value) {
        return "as".equals(value) || "from".equals(value);
    }

    private static IElementType classifyIdentifier(String value, List<QinLexicalToken> previousTokens) {
        QinLexicalToken previous = QinTokenFacts.previousMeaningfulToken(previousTokens);
        if (previous != null && previous.type() == QinTokenTypes.DOT) {
            return QinTokenTypes.MEMBER_IDENTIFIER;
        }
        if (value != null && !value.isEmpty() && Character.isUpperCase(value.charAt(0))) {
            return QinTokenTypes.CLASS_NAME;
        }
        return QinTokenTypes.IDENTIFIER;
    }

    private static List<QinLexicalToken> classifyFunctionIdentifiers(String text, List<QinLexicalToken> tokens) {
        List<QinLexicalToken> classified = new ArrayList<>(tokens.size());
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.IDENTIFIER && isBeforeCallParen(text, tokens, index)) {
                classified.add(new QinLexicalToken(
                        QinTokenTypes.FUNCTION_IDENTIFIER,
                        token.startOffset(),
                        token.endOffset()));
            } else {
                classified.add(token);
            }
        }
        return classified;
    }

    private static List<QinLexicalToken> classifyImportContextualKeywords(String text, List<QinLexicalToken> tokens) {
        List<QinLexicalToken> classified = new ArrayList<>(tokens);
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (!QinTokenFacts.isKeyword(text, token, "import")) {
                continue;
            }
            int braceDepth = 0;
            QinLexicalToken previousMeaningful = token;
            int current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, index + 1);
            while (current >= 0 && current < tokens.size()) {
                QinLexicalToken currentToken = tokens.get(current);
                if (currentToken.type() == QinTokenTypes.SEMICOLON) {
                    break;
                }
                if (QinTokenFacts.isNewStatementAfterImport(text, previousMeaningful.endOffset(), currentToken, braceDepth)) {
                    break;
                }
                if (QinTokenFacts.isOpenBrace(text, currentToken)) {
                    braceDepth++;
                } else if (QinTokenFacts.isCloseBrace(text, currentToken) && braceDepth > 0) {
                    braceDepth--;
                }
                if (QinTokenFacts.isContextualKeyword(text, currentToken, "as")
                        || QinTokenFacts.isContextualKeyword(text, currentToken, "from")) {
                    classified.set(current, new QinLexicalToken(
                            QinTokenTypes.KEYWORD,
                            currentToken.startOffset(),
                            currentToken.endOffset()));
                }
                previousMeaningful = currentToken;
                if (QinTokenFacts.isContextualKeyword(text, currentToken, "from")) {
                    break;
                }
                current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
            }
        }
        return classified;
    }


    private static boolean isBeforeCallParen(String text, List<QinLexicalToken> tokens, int tokenIndex) {
        QinLexicalToken next = QinTokenFacts.nextMeaningfulToken(tokens, tokenIndex);
        return next != null
                && next.type() == QinTokenTypes.PAREN
                && next.startOffset() < text.length()
                && text.charAt(next.startOffset()) == '(';
    }

    private static void appendTrivia(List<QinLexicalToken> tokens, String text, int startOffset, int endOffset) {
        int cursor = startOffset;
        while (cursor < endOffset) {
            char first = text.charAt(cursor);
            if (Character.isWhitespace(first)) {
                int next = cursor + 1;
                while (next < endOffset && Character.isWhitespace(text.charAt(next))) {
                    next++;
                }
                tokens.add(new QinLexicalToken(TokenType.WHITE_SPACE, cursor, next));
                cursor = next;
                continue;
            }
            if (first == '/' && cursor + 1 < endOffset && text.charAt(cursor + 1) == '/') {
                int next = cursor + 2;
                while (next < endOffset && text.charAt(next) != '\n' && text.charAt(next) != '\r') {
                    next++;
                }
                tokens.add(new QinLexicalToken(QinTokenTypes.LINE_COMMENT, cursor, next));
                cursor = next;
                continue;
            }
            if (first == '/' && cursor + 1 < endOffset && text.charAt(cursor + 1) == '*') {
                int next = cursor + 2;
                while (next + 1 < endOffset && !(text.charAt(next) == '*' && text.charAt(next + 1) == '/')) {
                    next++;
                }
                next = next + 1 < endOffset ? next + 2 : endOffset;
                tokens.add(new QinLexicalToken(QinTokenTypes.BLOCK_COMMENT, cursor, next));
                cursor = next;
                continue;
            }
            tokens.add(new QinLexicalToken(TokenType.BAD_CHARACTER, cursor, cursor + 1));
            cursor++;
        }
    }

    private static int recoverEditorTokenEnd(String text, int startOffset, int endOffset) {
        char first = text.charAt(startOffset);
        if (first == '\'' || first == '"' || first == '`') {
            int cursor = startOffset + 1;
            boolean escaping = false;
            while (cursor < endOffset) {
                char value = text.charAt(cursor);
                if (escaping) {
                    escaping = false;
                } else if (value == '\\') {
                    escaping = true;
                } else if (value == first) {
                    return cursor + 1;
                } else if (first != '`' && (value == '\n' || value == '\r')) {
                    return cursor;
                }
                cursor++;
            }
            return endOffset;
        }
        return Math.min(startOffset + 1, endOffset);
    }

    private static IElementType recoverEditorTokenType(String text, int startOffset) {
        char first = text.charAt(startOffset);
        if (first == '\'' || first == '"' || first == '`') {
            return QinTokenTypes.STRING;
        }
        return TokenType.BAD_CHARACTER;
    }

    private static int clamp(Integer value, int min, int max) {
        if (value == null) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }
}
