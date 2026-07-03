package com.qin.debug.lsp;

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
            if (QinTokenFacts.isObjectDeclarationKeyword(content, token)) {
                QinLexicalToken nameToken = QinTokenFacts.nextMeaningfulToken(tokens, index);
                if (nameToken != null && QinTokenFacts.isDeclarationIdentifierToken(nameToken)) {
                    String name = QinTokenFacts.slice(content, nameToken).toString();
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
        int braceIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, objectKeywordIndex + 1);
        while (braceIndex >= 0 && braceIndex < tokens.size()) {
            QinLexicalToken token = tokens.get(braceIndex);
            if (QinTokenFacts.isOpenBrace(content, token)) {
                return readObjectBody(content, tokens, braceIndex, objectName);
            }
            if (QinTokenFacts.isCloseBrace(content, token)) {
                break;
            }
            braceIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, braceIndex + 1);
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
                if (QinTokenFacts.isOpenBrace(content, token)) {
                    braceDepth++;
                } else if (QinTokenFacts.isCloseBrace(content, token)) {
                    braceDepth--;
                    if (braceDepth <= 0) {
                        break;
                    }
                }
                continue;
            }
            if (braceDepth != 1 || !QinTokenFacts.isDeclarationIdentifierToken(token)) {
                continue;
            }
            String memberName = QinTokenFacts.slice(content, token).toString();
            if (QinTokenFacts.isMethodDeclarationName(content, tokens, index)) {
                addUnique(methods, memberName);
            } else if (QinTokenFacts.isFieldDeclarationName(content, tokens, index)) {
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

    record ObjectDeclaration(@NotNull String name, @NotNull List<String> fields, @NotNull List<String> methods) {
        ObjectDeclaration {
            fields = List.copyOf(fields);
            methods = List.copyOf(methods);
        }
    }
}
