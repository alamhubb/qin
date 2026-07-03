package com.qin.debug.lsp;

import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class QinDeclarationScanner {
    private QinDeclarationScanner() {
    }

    static @NotNull List<String> objectNames(@NotNull CharSequence content) {
        List<QinLexicalToken> tokens = QinLexicalScanner.scan(content, 0, content.length());
        List<String> names = new ArrayList<>();
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.KEYWORD
                    && "object".contentEquals(slice(content, token))) {
                QinLexicalToken nameToken = nextMeaningfulToken(tokens, index);
                if (nameToken != null && isIdentifierToken(nameToken)) {
                    String name = slice(content, nameToken).toString();
                    if (!names.contains(name)) {
                        names.add(name);
                    }
                }
            }
        }
        return names;
    }

    private static boolean isIdentifierToken(@NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.IDENTIFIER
                || token.type() == QinTokenTypes.CLASS_NAME
                || token.type() == QinTokenTypes.FUNCTION_IDENTIFIER;
    }

    private static QinLexicalToken nextMeaningfulToken(@NotNull List<QinLexicalToken> tokens, int tokenIndex) {
        for (int index = tokenIndex + 1; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() != TokenType.WHITE_SPACE
                    && token.type() != QinTokenTypes.LINE_COMMENT
                    && token.type() != QinTokenTypes.BLOCK_COMMENT) {
                return token;
            }
        }
        return null;
    }

    private static @NotNull CharSequence slice(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return content.subSequence(token.startOffset(), token.endOffset());
    }
}
