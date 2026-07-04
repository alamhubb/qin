package com.qin.debug.lsp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class QinSourceStructure {
    private final List<ObjectDeclaration> objectDeclarations;
    private final List<ImportDeclaration> importDeclarations;

    private QinSourceStructure(
            @NotNull List<ObjectDeclaration> objectDeclarations,
            @NotNull List<ImportDeclaration> importDeclarations) {
        this.objectDeclarations = List.copyOf(objectDeclarations);
        this.importDeclarations = List.copyOf(importDeclarations);
    }

    static @NotNull QinSourceStructure parse(@NotNull CharSequence content) {
        List<QinLexicalToken> tokens = QinLexicalScanner.scan(content, 0, content.length());
        List<ObjectDeclaration> declarations = new ArrayList<>();
        List<ImportDeclaration> imports = new ArrayList<>();
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (QinTokenFacts.isObjectDeclarationKeyword(content, token)) {
                QinLexicalToken nameToken = QinTokenFacts.nextMeaningfulToken(tokens, index);
                if (nameToken != null && QinTokenFacts.isDeclarationIdentifierToken(nameToken)) {
                    String name = QinTokenFacts.slice(content, nameToken).toString();
                    if (declarations.stream().noneMatch(declaration -> declaration.name().equals(name))) {
                        declarations.add(readObjectDeclaration(content, tokens, index, name, range(token), range(nameToken)));
                    }
                }
            } else if (QinTokenFacts.isKeyword(content, token, "import")) {
                ImportDeclaration importDeclaration = readImportDeclaration(content, tokens, index);
                if (importDeclaration != null) {
                    imports.add(importDeclaration);
                }
            }
        }
        return new QinSourceStructure(declarations, imports);
    }

    static @NotNull List<String> objectNames(@NotNull CharSequence content) {
        return parse(content).objectNames();
    }

    @NotNull List<String> objectNames() {
        return objectDeclarations.stream()
                .map(ObjectDeclaration::name)
                .toList();
    }

    @NotNull List<ObjectDeclaration> objectDeclarations() {
        return objectDeclarations;
    }

    @NotNull List<ImportDeclaration> importDeclarations() {
        return importDeclarations;
    }

    ImportDeclaration importDeclarationAtKeywordOffset(int offset) {
        for (ImportDeclaration declaration : importDeclarations) {
            if (declaration.keywordRange().startOffset() == offset) {
                return declaration;
            }
        }
        return null;
    }

    ImportSpecifier importSpecifierAtExportedNameOffset(int offset) {
        for (ImportDeclaration declaration : importDeclarations) {
            ImportSpecifier specifier = declaration.specifierAtExportedNameOffset(offset);
            if (specifier != null) {
                return specifier;
            }
        }
        return null;
    }

    ObjectDeclaration objectDeclarationAtKeywordOffset(int offset) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            if (declaration.keywordRange().startOffset() == offset) {
                return declaration;
            }
        }
        return null;
    }

    MemberDeclaration methodDeclarationAtNameOffset(int offset) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            MemberDeclaration member = declaration.methodDeclarationAtNameOffset(offset);
            if (member != null) {
                return member;
            }
        }
        return null;
    }

    MemberDeclaration fieldDeclarationAtNameOffset(int offset) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            MemberDeclaration member = declaration.fieldDeclarationAtNameOffset(offset);
            if (member != null) {
                return member;
            }
        }
        return null;
    }

    private static ImportDeclaration readImportDeclaration(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int importKeywordIndex) {
        List<ImportSpecifier> specifiers = new ArrayList<>();
        SourceRange moduleRange = SourceRange.missing();
        String moduleSpecifier = "";
        int current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, importKeywordIndex + 1);
        while (current >= 0 && current < tokens.size()) {
            QinLexicalToken token = tokens.get(current);
            if (token.type() == QinTokenTypes.SEMICOLON) {
                return new ImportDeclaration(range(tokens.get(importKeywordIndex)), moduleRange, moduleSpecifier, specifiers);
            }
            if (QinTokenFacts.isKeyword(content, token, "from")) {
                int moduleIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
                if (moduleIndex >= 0
                        && moduleIndex < tokens.size()
                        && tokens.get(moduleIndex).type() == QinTokenTypes.STRING) {
                    moduleRange = range(tokens.get(moduleIndex));
                    moduleSpecifier = unquote(QinTokenFacts.slice(content, tokens.get(moduleIndex)).toString());
                    current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, moduleIndex + 1);
                    continue;
                }
                return new ImportDeclaration(range(tokens.get(importKeywordIndex)), moduleRange, moduleSpecifier, specifiers);
            }
            if (QinTokenFacts.isReferenceLeafToken(token.type())) {
                ImportSpecifier specifier = readImportSpecifier(content, tokens, current);
                if (specifier != null) {
                    specifiers.add(specifier);
                    current = specifier.nextTokenIndex();
                    continue;
                }
            }
            current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
        }
        return specifiers.isEmpty() && moduleSpecifier.isEmpty()
                ? null
                : new ImportDeclaration(range(tokens.get(importKeywordIndex)), moduleRange, moduleSpecifier, specifiers);
    }

    private static ImportSpecifier readImportSpecifier(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int exportedNameIndex) {
        QinLexicalToken exportedNameToken = tokens.get(exportedNameIndex);
        if (!QinTokenFacts.isReferenceLeafToken(exportedNameToken.type())) {
            return null;
        }
        String exportedName = QinTokenFacts.slice(content, exportedNameToken).toString();
        String localName = exportedName;
        SourceRange localNameRange = SourceRange.missing();
        int next = QinTokenFacts.nextMeaningfulTokenIndex(tokens, exportedNameIndex + 1);
        if (next >= 0 && next < tokens.size() && QinTokenFacts.isKeyword(content, tokens.get(next), "as")) {
            int aliasIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, next + 1);
            if (aliasIndex >= 0
                    && aliasIndex < tokens.size()
                    && QinTokenFacts.isReferenceLeafToken(tokens.get(aliasIndex).type())) {
                localName = QinTokenFacts.slice(content, tokens.get(aliasIndex)).toString();
                localNameRange = range(tokens.get(aliasIndex));
                next = QinTokenFacts.nextMeaningfulTokenIndex(tokens, aliasIndex + 1);
            }
        }
        return new ImportSpecifier(exportedName, range(exportedNameToken), localName, localNameRange, next);
    }

    private static @NotNull String unquote(@NotNull String text) {
        if (text.length() < 2) {
            return text.trim();
        }
        return text.substring(1, text.length() - 1).trim();
    }

    private static @NotNull ObjectDeclaration readObjectDeclaration(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int objectKeywordIndex,
            @NotNull String objectName,
            @NotNull SourceRange objectKeywordRange,
            @NotNull SourceRange objectNameRange) {
        int braceIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, objectKeywordIndex + 1);
        while (braceIndex >= 0 && braceIndex < tokens.size()) {
            QinLexicalToken token = tokens.get(braceIndex);
            if (QinTokenFacts.isOpenBrace(content, token)) {
                return readObjectBody(content, tokens, braceIndex, objectName, objectKeywordRange, objectNameRange);
            }
            if (QinTokenFacts.isCloseBrace(content, token)) {
                break;
            }
            braceIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, braceIndex + 1);
        }
        return new ObjectDeclaration(
                objectName,
                objectKeywordRange,
                objectNameRange,
                SourceRange.missing(),
                List.of(),
                List.of());
    }

    private static @NotNull ObjectDeclaration readObjectBody(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int openBraceIndex,
            @NotNull String objectName,
            @NotNull SourceRange objectKeywordRange,
            @NotNull SourceRange objectNameRange) {
        List<MemberDeclaration> fields = new ArrayList<>();
        List<MemberDeclaration> methods = new ArrayList<>();
        int braceDepth = 0;
        for (int index = openBraceIndex; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.BRACE) {
                if (QinTokenFacts.isOpenBrace(content, token)) {
                    braceDepth++;
                } else if (QinTokenFacts.isCloseBrace(content, token)) {
                    braceDepth--;
                    if (braceDepth <= 0) {
                        return new ObjectDeclaration(
                                objectName,
                                objectKeywordRange,
                                objectNameRange,
                                new SourceRange(tokens.get(openBraceIndex).startOffset(), token.endOffset()),
                                fields,
                                methods);
                    }
                }
                continue;
            }
            if (braceDepth != 1 || !QinTokenFacts.isDeclarationIdentifierToken(token)) {
                continue;
            }
            String memberName = QinTokenFacts.slice(content, token).toString();
            if (QinTokenFacts.isMethodDeclarationName(content, tokens, index)) {
                addUnique(methods, new MemberDeclaration(memberName, range(token)));
            } else if (QinTokenFacts.isFieldDeclarationName(content, tokens, index)) {
                addUnique(fields, new MemberDeclaration(memberName, range(token)));
            }
        }
        return new ObjectDeclaration(
                objectName,
                objectKeywordRange,
                objectNameRange,
                new SourceRange(tokens.get(openBraceIndex).startOffset(), content.length()),
                fields,
                methods);
    }

    private static void addUnique(@NotNull List<MemberDeclaration> values, @NotNull MemberDeclaration value) {
        if (values.stream().noneMatch(existing -> existing.name().equals(value.name()))) {
            values.add(value);
        }
    }

    private static @NotNull SourceRange range(@NotNull QinLexicalToken token) {
        return new SourceRange(token.startOffset(), token.endOffset());
    }

    private static @NotNull List<MemberDeclaration> membersFromNames(@NotNull List<String> names) {
        return names.stream()
                .map(name -> new MemberDeclaration(name, SourceRange.missing()))
                .toList();
    }

    record SourceRange(int startOffset, int endOffset) {
        static @NotNull SourceRange missing() {
            return new SourceRange(-1, -1);
        }

        boolean isPresent() {
            return startOffset >= 0 && endOffset >= startOffset;
        }
    }

    record MemberDeclaration(@NotNull String name, @NotNull SourceRange nameRange) {
    }

    record ImportSpecifier(
            @NotNull String exportedName,
            @NotNull SourceRange exportedNameRange,
            @NotNull String localName,
            @NotNull SourceRange localNameRange,
            int nextTokenIndex) {
    }

    record ImportDeclaration(
            @NotNull SourceRange keywordRange,
            @NotNull SourceRange moduleSpecifierRange,
            @NotNull String moduleSpecifier,
            @NotNull List<ImportSpecifier> specifiers) {
        ImportDeclaration {
            specifiers = List.copyOf(specifiers);
        }

        ImportSpecifier specifierAtExportedNameOffset(int offset) {
            for (ImportSpecifier specifier : specifiers) {
                if (specifier.exportedNameRange().startOffset() == offset) {
                    return specifier;
                }
            }
            return null;
        }
    }

    record ObjectDeclaration(
            @NotNull String name,
            @NotNull SourceRange keywordRange,
            @NotNull SourceRange nameRange,
            @NotNull SourceRange bodyRange,
            @NotNull List<MemberDeclaration> fields,
            @NotNull List<MemberDeclaration> methods) {
        ObjectDeclaration(@NotNull String name, @NotNull List<String> fields, @NotNull List<String> methods) {
            this(
                    name,
                    SourceRange.missing(),
                    SourceRange.missing(),
                    SourceRange.missing(),
                    membersFromNames(fields),
                    membersFromNames(methods));
        }

        ObjectDeclaration {
            fields = List.copyOf(fields);
            methods = List.copyOf(methods);
        }

        @NotNull List<String> fieldNames() {
            return fields.stream()
                    .map(MemberDeclaration::name)
                    .toList();
        }

        @NotNull List<String> methodNames() {
            return methods.stream()
                    .map(MemberDeclaration::name)
                    .toList();
        }

        MemberDeclaration fieldDeclarationAtNameOffset(int offset) {
            return memberDeclarationAtNameOffset(fields, offset);
        }

        MemberDeclaration methodDeclarationAtNameOffset(int offset) {
            return memberDeclarationAtNameOffset(methods, offset);
        }

        private static MemberDeclaration memberDeclarationAtNameOffset(
                @NotNull List<MemberDeclaration> members,
                int offset) {
            for (MemberDeclaration member : members) {
                if (member.nameRange().startOffset() == offset) {
                    return member;
                }
            }
            return null;
        }
    }
}
