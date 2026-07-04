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

    static @NotNull String objectMemberKey(@NotNull String objectName, @NotNull String memberName) {
        return objectName + "." + memberName;
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

    @NotNull List<ImportSpecifierMatch> importSpecifierMatches() {
        List<ImportSpecifierMatch> matches = new ArrayList<>();
        for (ImportDeclaration declaration : importDeclarations) {
            for (ImportSpecifier specifier : declaration.specifiers()) {
                matches.add(new ImportSpecifierMatch(declaration, specifier));
            }
        }
        return matches;
    }

    ImportDeclaration importDeclarationAtKeywordOffset(int offset) {
        for (ImportDeclaration declaration : importDeclarations) {
            if (declaration.keywordRange().startsAt(offset)) {
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

    ImportSpecifierMatch importSpecifierAtNameOffset(int offset) {
        for (ImportDeclaration declaration : importDeclarations) {
            ImportSpecifier specifier = declaration.specifierAtNameOffset(offset);
            if (specifier != null) {
                return new ImportSpecifierMatch(declaration, specifier);
            }
        }
        return null;
    }

    ImportSpecifier importAliasSpecifierNamed(@NotNull String localName) {
        for (ImportDeclaration declaration : importDeclarations) {
            ImportSpecifier specifier = declaration.aliasSpecifierNamed(localName);
            if (specifier != null) {
                return specifier;
            }
        }
        return null;
    }

    ObjectDeclaration objectDeclarationAtKeywordOffset(int offset) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            if (declaration.keywordRange().startsAt(offset)) {
                return declaration;
            }
        }
        return null;
    }

    ObjectDeclaration objectDeclarationNamed(@NotNull String name) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            if (declaration.name().equals(name)) {
                return declaration;
            }
        }
        return null;
    }

    MemberDeclaration memberDeclarationAtNameOffset(int offset, @NotNull ObjectMemberKind kind) {
        for (ObjectDeclaration declaration : objectDeclarations) {
            MemberDeclaration member = declaration.memberDeclarationAtNameOffset(offset, kind);
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
        SourceRange keywordRange = range(tokens.get(importKeywordIndex));
        SourceRange lastImportRange = keywordRange;
        SourceRange previousMeaningfulRange = keywordRange;
        int braceDepth = 0;
        List<ImportSpecifier> specifiers = new ArrayList<>();
        SourceRange moduleRange = SourceRange.missing();
        String moduleSpecifier = "";
        int current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, importKeywordIndex + 1);
        while (current >= 0 && current < tokens.size()) {
            QinLexicalToken token = tokens.get(current);
            if (QinTokenFacts.isNewStatementAfterImport(
                    content,
                    previousMeaningfulRange.endOffset(),
                    token,
                    braceDepth)) {
                break;
            }
            if (token.type() == QinTokenTypes.SEMICOLON) {
                return new ImportDeclaration(
                        keywordRange,
                        rangeBetween(keywordRange, range(token)),
                        moduleRange,
                        moduleSpecifier,
                        specifiers);
            }
            if (QinTokenFacts.isOpenBrace(content, token)) {
                braceDepth++;
                NamedImportSpecifiers namedImports = readNamedImportSpecifiers(content, tokens, current);
                specifiers.addAll(namedImports.specifiers());
                lastImportRange = namedImports.sourceRange();
                previousMeaningfulRange = namedImports.sourceRange();
                braceDepth--;
                current = namedImports.nextTokenIndex();
                continue;
            }
            if (QinTokenFacts.isContextualKeyword(content, token, "from")) {
                SourceRange fromRange = range(token);
                int moduleIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
                if (moduleIndex >= 0
                        && moduleIndex < tokens.size()
                        && tokens.get(moduleIndex).type() == QinTokenTypes.STRING) {
                    moduleRange = range(tokens.get(moduleIndex));
                    moduleSpecifier = unquote(QinTokenFacts.slice(content, tokens.get(moduleIndex)).toString());
                    SourceRange declarationEndRange = moduleRange;
                    int afterModuleIndex = QinTokenFacts.nextMeaningfulTokenIndex(tokens, moduleIndex + 1);
                    if (afterModuleIndex >= 0
                            && afterModuleIndex < tokens.size()
                            && tokens.get(afterModuleIndex).type() == QinTokenTypes.SEMICOLON) {
                        declarationEndRange = range(tokens.get(afterModuleIndex));
                    }
                    return new ImportDeclaration(
                            keywordRange,
                            rangeBetween(keywordRange, declarationEndRange),
                            moduleRange,
                            moduleSpecifier,
                            specifiers);
                }
                return new ImportDeclaration(
                        keywordRange,
                        rangeBetween(keywordRange, fromRange),
                        moduleRange,
                        moduleSpecifier,
                        specifiers);
            }
            lastImportRange = range(token);
            previousMeaningfulRange = lastImportRange;
            current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
        }
        return specifiers.isEmpty() && moduleSpecifier.isEmpty()
                ? null
                : new ImportDeclaration(
                        keywordRange,
                        rangeBetween(keywordRange, lastImportRange),
                        moduleRange,
                        moduleSpecifier,
                        specifiers);
    }
    private static @NotNull NamedImportSpecifiers readNamedImportSpecifiers(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int openBraceIndex) {
        List<ImportSpecifier> specifiers = new ArrayList<>();
        int current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, openBraceIndex + 1);
        while (current >= 0 && current < tokens.size()) {
            QinLexicalToken token = tokens.get(current);
            if (QinTokenFacts.isCloseBrace(content, token)) {
                return new NamedImportSpecifiers(
                        specifiers,
                        QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1),
                        new SourceRange(tokens.get(openBraceIndex).startOffset(), token.endOffset()));
            }
            if (token.type() == QinTokenTypes.SEMICOLON) {
                return new NamedImportSpecifiers(
                        specifiers,
                        current,
                        new SourceRange(tokens.get(openBraceIndex).startOffset(), token.startOffset()));
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
        return new NamedImportSpecifiers(
                specifiers,
                current,
                new SourceRange(tokens.get(openBraceIndex).startOffset(), content.length()));
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
        if (next >= 0 && next < tokens.size() && QinTokenFacts.isContextualKeyword(content, tokens.get(next), "as")) {
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

    private static @NotNull SourceRange readBalancedBodyRange(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int openBraceIndex) {
        int braceDepth = 0;
        for (int index = openBraceIndex; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() != QinTokenTypes.BRACE) {
                continue;
            }
            if (QinTokenFacts.isOpenBrace(content, token)) {
                braceDepth++;
            } else if (QinTokenFacts.isCloseBrace(content, token)) {
                braceDepth--;
                if (braceDepth <= 0) {
                    return new SourceRange(tokens.get(openBraceIndex).startOffset(), token.endOffset());
                }
            }
        }
        return new SourceRange(tokens.get(openBraceIndex).startOffset(), content.length());
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
                List.of());
    }

    private static @NotNull ObjectDeclaration readObjectBody(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int openBraceIndex,
            @NotNull String objectName,
            @NotNull SourceRange objectKeywordRange,
            @NotNull SourceRange objectNameRange) {
        List<ObjectMemberDeclaration> members = new ArrayList<>();
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
                                readBalancedBodyRange(content, tokens, openBraceIndex),
                                members);
                    }
                }
                continue;
            }
            if (braceDepth != 1 || !QinTokenFacts.isDeclarationIdentifierToken(token)) {
                continue;
            }
            String memberName = QinTokenFacts.slice(content, token).toString();
            if (QinTokenFacts.isMethodDeclarationName(content, tokens, index)) {
                MemberDeclaration method = new MemberDeclaration(
                        memberName,
                        range(token),
                        readMethodBodyRange(content, tokens, index));
                addUniqueMember(members, new ObjectMemberDeclaration(ObjectMemberKind.METHOD, method));
            } else if (QinTokenFacts.isFieldDeclarationName(content, tokens, index)) {
                MemberDeclaration field = new MemberDeclaration(memberName, range(token));
                addUniqueMember(members, new ObjectMemberDeclaration(ObjectMemberKind.FIELD, field));
            }
        }
        return new ObjectDeclaration(
                objectName,
                objectKeywordRange,
                objectNameRange,
                readBalancedBodyRange(content, tokens, openBraceIndex),
                members);
    }

    private static @NotNull SourceRange readMethodBodyRange(
            @NotNull CharSequence content,
            @NotNull List<QinLexicalToken> tokens,
            int methodNameIndex) {
        int current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, methodNameIndex + 1);
        while (current >= 0 && current < tokens.size()) {
            QinLexicalToken token = tokens.get(current);
            if (QinTokenFacts.isOpenBrace(content, token)) {
                return readBalancedBodyRange(content, tokens, current);
            }
            if (QinTokenFacts.isCloseBrace(content, token)) {
                break;
            }
            current = QinTokenFacts.nextMeaningfulTokenIndex(tokens, current + 1);
        }
        return SourceRange.missing();
    }

    private static void addUniqueMember(
            @NotNull List<ObjectMemberDeclaration> values,
            @NotNull ObjectMemberDeclaration value) {
        if (values.stream().noneMatch(existing ->
                existing.kind() == value.kind()
                        && existing.declaration().name().equals(value.declaration().name()))) {
            values.add(value);
        }
    }

    private static @NotNull SourceRange range(@NotNull QinLexicalToken token) {
        return new SourceRange(token.startOffset(), token.endOffset());
    }

    private static @NotNull SourceRange rangeBetween(
            @NotNull SourceRange startRange,
            @NotNull SourceRange endRange) {
        return new SourceRange(startRange.startOffset(), endRange.endOffset());
    }

    record SourceRange(int startOffset, int endOffset) {
        static @NotNull SourceRange missing() {
            return new SourceRange(-1, -1);
        }

        boolean isPresent() {
            return startOffset >= 0 && endOffset >= startOffset;
        }

        boolean startsAt(int offset) {
            return isPresent() && startOffset == offset;
        }

        boolean containsOffset(int offset) {
            return isPresent() && startOffset <= offset && offset < endOffset;
        }

        boolean startsAfter(int offset) {
            return isPresent() && offset < startOffset;
        }
    }

    record MemberDeclaration(
            @NotNull String name,
            @NotNull SourceRange nameRange,
            @NotNull SourceRange bodyRange) {
        MemberDeclaration(@NotNull String name, @NotNull SourceRange nameRange) {
            this(name, nameRange, SourceRange.missing());
        }
    }

    enum ObjectMemberKind {
        FIELD,
        METHOD
    }

    record ObjectMemberDeclaration(
            @NotNull ObjectMemberKind kind,
            @NotNull MemberDeclaration declaration) {
    }

    record ObjectMemberIndexEntry(
            @NotNull ObjectMemberKind kind,
            @NotNull String key) {
    }

    record ImportSpecifier(
            @NotNull String exportedName,
            @NotNull SourceRange exportedNameRange,
            @NotNull String localName,
            @NotNull SourceRange localNameRange,
            int nextTokenIndex) {
    }

    record ImportSpecifierMatch(
            @NotNull ImportDeclaration declaration,
            @NotNull ImportSpecifier specifier) {
    }

    private record NamedImportSpecifiers(
            @NotNull List<ImportSpecifier> specifiers,
            int nextTokenIndex,
            @NotNull SourceRange sourceRange) {
        private NamedImportSpecifiers {
            specifiers = List.copyOf(specifiers);
        }
    }

    record ImportDeclaration(
            @NotNull SourceRange keywordRange,
            @NotNull SourceRange declarationRange,
            @NotNull SourceRange moduleSpecifierRange,
            @NotNull String moduleSpecifier,
            @NotNull List<ImportSpecifier> specifiers) {
        ImportDeclaration {
            specifiers = List.copyOf(specifiers);
        }

        ImportSpecifier specifierAtExportedNameOffset(int offset) {
            for (ImportSpecifier specifier : specifiers) {
                if (specifier.exportedNameRange().startsAt(offset)) {
                    return specifier;
                }
            }
            return null;
        }

        ImportSpecifier specifierAtNameOffset(int offset) {
            for (ImportSpecifier specifier : specifiers) {
                if (specifier.exportedNameRange().startsAt(offset)
                        || specifier.localNameRange().startsAt(offset)) {
                    return specifier;
                }
            }
            return null;
        }

        ImportSpecifier aliasSpecifierNamed(@NotNull String localName) {
            for (ImportSpecifier specifier : specifiers) {
                if (specifier.localNameRange().isPresent()
                        && specifier.localName().equals(localName)) {
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
            @NotNull List<ObjectMemberDeclaration> members) {
        ObjectDeclaration(@NotNull String name, @NotNull List<String> fields, @NotNull List<String> methods) {
            this(
                    name,
                    SourceRange.missing(),
                    SourceRange.missing(),
                    SourceRange.missing(),
                    memberDeclarationsFromNames(fields, methods));
        }

        ObjectDeclaration {
            members = List.copyOf(members);
        }

        @NotNull List<String> memberNames(@NotNull ObjectMemberKind kind) {
            return memberDeclarations(kind).stream()
                    .map(MemberDeclaration::name)
                    .toList();
        }

        @NotNull List<ObjectMemberDeclaration> memberDeclarations() {
            return members;
        }

        @NotNull List<MemberDeclaration> memberDeclarations(@NotNull ObjectMemberKind kind) {
            return memberDeclarations().stream()
                    .filter(member -> member.kind() == kind)
                    .map(ObjectMemberDeclaration::declaration)
                    .toList();
        }

        @NotNull List<ObjectMemberIndexEntry> memberIndexEntries() {
            return memberDeclarations().stream()
                    .map(member -> new ObjectMemberIndexEntry(
                            member.kind(),
                            objectMemberKey(name, member.declaration().name())))
                    .toList();
        }

        MemberDeclaration memberDeclarationAtNameOffset(int offset, @NotNull ObjectMemberKind kind) {
            return memberDeclarationAtNameOffset(memberDeclarations(kind), offset);
        }

        MemberDeclaration memberDeclarationNamed(@NotNull String name, @NotNull ObjectMemberKind kind) {
            return memberDeclarationNamed(memberDeclarations(kind), name);
        }

        private static MemberDeclaration memberDeclarationAtNameOffset(
                @NotNull List<MemberDeclaration> members,
                int offset) {
            for (MemberDeclaration member : members) {
                if (member.nameRange().startsAt(offset)) {
                    return member;
                }
            }
            return null;
        }

        private static MemberDeclaration memberDeclarationNamed(
                @NotNull List<MemberDeclaration> members,
                @NotNull String name) {
            for (MemberDeclaration member : members) {
                if (member.name().equals(name)) {
                    return member;
                }
            }
            return null;
        }

        private static @NotNull List<ObjectMemberDeclaration> memberDeclarationsFromNames(
                @NotNull List<String> fields,
                @NotNull List<String> methods) {
            List<ObjectMemberDeclaration> members = new ArrayList<>();
            fields.forEach(field -> addUniqueMember(
                    members,
                    new ObjectMemberDeclaration(
                            ObjectMemberKind.FIELD,
                            new MemberDeclaration(field, SourceRange.missing()))));
            methods.forEach(method -> addUniqueMember(
                    members,
                    new ObjectMemberDeclaration(
                            ObjectMemberKind.METHOD,
                            new MemberDeclaration(method, SourceRange.missing()))));
            return members;
        }
    }
}
