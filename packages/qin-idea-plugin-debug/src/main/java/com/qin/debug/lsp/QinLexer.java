package com.qin.debug.lsp;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class QinLexer extends LexerBase {
    private static final Set<String> KEYWORDS = Set.of(
            "as", "async", "await", "break", "case", "catch", "class", "const",
            "continue", "default", "do", "else", "export", "extends", "false",
            "finally", "for", "from", "function", "if", "import", "in", "interface",
            "let", "new", "null", "object", "of", "return", "super", "switch",
            "this", "throw", "true", "try", "type", "var", "while");

    private CharSequence buffer = "";
    private int startOffset;
    private int endOffset;
    private int tokenStart;
    private int tokenEnd;
    private IElementType tokenType;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.tokenStart = startOffset;
        this.tokenEnd = startOffset;
        advance();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public @Nullable IElementType getTokenType() {
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenEnd;
    }

    @Override
    public void advance() {
        tokenStart = tokenEnd;
        if (tokenStart >= endOffset) {
            tokenType = null;
            return;
        }

        char first = buffer.charAt(tokenStart);
        if (Character.isWhitespace(first)) {
            tokenEnd = scanWhile(tokenStart, value -> Character.isWhitespace(value));
            tokenType = TokenType.WHITE_SPACE;
            return;
        }
        if (first == '/' && tokenStart + 1 < endOffset) {
            char second = buffer.charAt(tokenStart + 1);
            if (second == '/') {
                tokenEnd = scanLineComment(tokenStart + 2);
                tokenType = QinTokenTypes.LINE_COMMENT;
                return;
            }
            if (second == '*') {
                tokenEnd = scanBlockComment(tokenStart + 2);
                tokenType = QinTokenTypes.BLOCK_COMMENT;
                return;
            }
        }
        if (first == '\'' || first == '"' || first == '`') {
            tokenEnd = scanString(tokenStart, first);
            tokenType = QinTokenTypes.STRING;
            return;
        }
        if (Character.isDigit(first)) {
            tokenEnd = scanNumber(tokenStart);
            tokenType = QinTokenTypes.NUMBER;
            return;
        }
        if (isIdentifierStart(first)) {
            tokenEnd = scanWhile(tokenStart, QinLexer::isIdentifierPart);
            String text = buffer.subSequence(tokenStart, tokenEnd).toString();
            tokenType = KEYWORDS.contains(text) ? QinTokenTypes.KEYWORD : classifyIdentifier(text, tokenStart, tokenEnd);
            return;
        }

        tokenEnd = tokenStart + 1;
        tokenType = switch (first) {
            case '{', '}' -> QinTokenTypes.BRACE;
            case '(', ')' -> QinTokenTypes.PAREN;
            case '[', ']' -> QinTokenTypes.BRACKET;
            case ',' -> QinTokenTypes.COMMA;
            case ';' -> QinTokenTypes.SEMICOLON;
            case '.' -> QinTokenTypes.DOT;
            default -> isOperator(first) ? QinTokenTypes.OPERATOR : TokenType.BAD_CHARACTER;
        };
    }

    @Override
    public @NotNull CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }

    private int scanLineComment(int offset) {
        int current = offset;
        while (current < endOffset) {
            char value = buffer.charAt(current);
            if (value == '\n' || value == '\r') {
                break;
            }
            current++;
        }
        return current;
    }

    private int scanBlockComment(int offset) {
        int current = offset;
        while (current + 1 < endOffset) {
            if (buffer.charAt(current) == '*' && buffer.charAt(current + 1) == '/') {
                return current + 2;
            }
            current++;
        }
        return endOffset;
    }

    private int scanString(int offset, char quote) {
        int current = offset + 1;
        boolean escaping = false;
        while (current < endOffset) {
            char value = buffer.charAt(current);
            if (escaping) {
                escaping = false;
            } else if (value == '\\') {
                escaping = true;
            } else if (value == quote) {
                return current + 1;
            }
            current++;
        }
        return endOffset;
    }

    private int scanNumber(int offset) {
        int current = offset;
        boolean seenDot = false;
        while (current < endOffset) {
            char value = buffer.charAt(current);
            if (Character.isDigit(value) || value == '_') {
                current++;
            } else if (value == '.' && !seenDot) {
                seenDot = true;
                current++;
            } else {
                break;
            }
        }
        return current;
    }

    private int scanWhile(int offset, CharacterPredicate predicate) {
        int current = offset;
        while (current < endOffset && predicate.test(buffer.charAt(current))) {
            current++;
        }
        return current;
    }

    private static boolean isIdentifierStart(char value) {
        return Character.isLetter(value) || value == '_' || value == '$';
    }

    private static boolean isIdentifierPart(char value) {
        return isIdentifierStart(value) || Character.isDigit(value);
    }

    private static boolean isOperator(char value) {
        return "+-*/%=!<>?:&|^~@#".indexOf(value) >= 0;
    }

    private IElementType classifyIdentifier(String text, int start, int end) {
        int previous = previousNonWhitespace(start - 1);
        if (previous >= startOffset && buffer.charAt(previous) == '.') {
            return QinTokenTypes.MEMBER_IDENTIFIER;
        }
        int next = nextNonWhitespace(end);
        if (next < endOffset && buffer.charAt(next) == '(') {
            return QinTokenTypes.FUNCTION_IDENTIFIER;
        }
        if (!text.isEmpty() && Character.isUpperCase(text.charAt(0))) {
            return QinTokenTypes.CLASS_NAME;
        }
        return QinTokenTypes.IDENTIFIER;
    }

    private int previousNonWhitespace(int offset) {
        int current = offset;
        while (current >= startOffset && Character.isWhitespace(buffer.charAt(current))) {
            current--;
        }
        return current;
    }

    private int nextNonWhitespace(int offset) {
        int current = offset;
        while (current < endOffset && Character.isWhitespace(buffer.charAt(current))) {
            current++;
        }
        return current;
    }

    private interface CharacterPredicate {
        boolean test(char value);
    }
}
