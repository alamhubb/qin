package com.qin.debug.lsp;

import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class QinDeclarationScanner {
    private QinDeclarationScanner() {
    }

    static @NotNull List<String> objectNames(@NotNull CharSequence content) {
        return objectDeclarations(content).stream()
                .map(ObjectDeclaration::name)
                .toList();
    }

    static @NotNull List<ObjectDeclaration> objectDeclarations(@NotNull CharSequence content) {
        List<QinLexicalToken> tokens = QinLexicalScanner.scan(content, 0, content.length());
        List<ObjectDeclaration> declarations = new ArrayList<>();
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.KEYWORD
                    && "object".contentEquals(slice(content, token))) {
                QinLexicalToken nameToken = nextMeaningfulToken(tokens, index);
                if (nameToken != null && isIdentifierToken(nameToken)) {
                    String name = slice(content, nameToken).toString();
                    if (declarations.stream().noneMatch(declaration -> declaration.name().equals(name))) {
                        declarations.add(readObjectDeclaration(content, tokens, index, name));
                    }
                }
            }
        }
        return declarations;
    }

    private static @NotNull ObjectDeclaration readObjectDeclaration(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int objectKeywordIndex,
            @NotNull String objectName) {
        int braceIndex = nextMeaningfulTokenIndex(tokens, objectKeywordIndex + 1);
        while (braceIndex >= 0 && braceIndex < tokens.size()) {
            QinLexicalToken token = tokens.get(braceIndex);
            if (token.type() == QinTokenTypes.BRACE && tokenStartsWith(content, token, '{')) {
                return readObjectBody(content, tokens, braceIndex, objectName);
            }
            if (token.type() == QinTokenTypes.BRACE && tokenStartsWith(content, token, '}')) {
                break;
            }
            braceIndex = nextMeaningfulTokenIndex(tokens, braceIndex + 1);
        }
        return new ObjectDeclaration(objectName, List.of(), List.of());
    }

    private static @NotNull ObjectDeclaration readObjectBody(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int openBraceIndex,
            @NotNull String objectName) {
        List<String> fields = new ArrayList<>();
        List<String> methods = new ArrayList<>();
        int braceDepth = 0;
        for (int index = openBraceIndex; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.BRACE) {
                if (tokenStartsWith(content, token, '{')) {
                    braceDepth++;
                } else if (tokenStartsWith(content, token, '}')) {
                    braceDepth--;
                    if (braceDepth <= 0) {
                        break;
                    }
                }
                continue;
            }
            if (braceDepth != 1 || !isIdentifierToken(token)) {
                continue;
            }
            String memberName = slice(content, token).toString();
            QinLexicalToken next = nextMeaningfulToken(tokens, index);
            if (next == null) {
                continue;
            }
            if (next.type() == QinTokenTypes.PAREN && tokenStartsWith(content, next, '(')) {
                addUnique(methods, memberName);
            } else if (next.type() == QinTokenTypes.OPERATOR && tokenStartsWith(content, next, '=')) {
                addUnique(fields, memberName);
            }
        }
        return new ObjectDeclaration(objectName, fields, methods);
    }

    private static void addUnique(@NotNull List<String> values, @NotNull String value) {
        if (!values.contains(value)) {
            values.add(value);
        }
    }

    private static boolean isIdentifierToken(@NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.IDENTIFIER
                || token.type() == QinTokenTypes.CLASS_NAME
                || token.type() == QinTokenTypes.FUNCTION_IDENTIFIER;
    }

    private static QinLexicalToken nextMeaningfulToken(@NotNull List<QinLexicalToken> tokens, int tokenIndex) {
        int index = nextMeaningfulTokenIndex(tokens, tokenIndex + 1);
        return index < 0 ? null : tokens.get(index);
    }

    private static int nextMeaningfulTokenIndex(@NotNull List<QinLexicalToken> tokens, int startIndex) {
        for (int index = startIndex; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() != TokenType.WHITE_SPACE
                    && token.type() != QinTokenTypes.LINE_COMMENT
                    && token.type() != QinTokenTypes.BLOCK_COMMENT) {
                return index;
            }
        }
        return -1;
    }

    private static @NotNull CharSequence slice(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return content.subSequence(token.startOffset(), token.endOffset());
    }

    private static boolean tokenStartsWith(
            @NotNull CharSequence content,
            @NotNull QinLexicalToken token,
            char expected) {
        return token.startOffset() < content.length() && content.charAt(token.startOffset()) == expected;
    }

    record ObjectDeclaration(@NotNull String name, @NotNull List<String> fields, @NotNull List<String> methods) {
        ObjectDeclaration {
            fields = List.copyOf(fields);
            methods = List.copyOf(methods);
        }
    }
}
