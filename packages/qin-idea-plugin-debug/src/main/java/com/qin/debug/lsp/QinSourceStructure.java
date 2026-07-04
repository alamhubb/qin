package com.qin.debug.lsp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class QinSourceStructure {
    private final List<ObjectDeclaration> objectDeclarations;

    private QinSourceStructure(@NotNull List<ObjectDeclaration> objectDeclarations) {
        this.objectDeclarations = List.copyOf(objectDeclarations);
    }

    static @NotNull QinSourceStructure parse(@NotNull CharSequence content) {
        List<QinLexicalToken> tokens = QinLexicalScanner.scan(content, 0, content.length());
        List<ObjectDeclaration> declarations = new ArrayList<>();
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
            }
        }
        return new QinSourceStructure(declarations);
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
