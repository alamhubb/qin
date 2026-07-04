package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public final class QinParserDefinition implements ParserDefinition {
    private static final IFileElementType FILE = QinFileElementType.INSTANCE;
    private static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    private static final TokenSet COMMENTS = TokenSet.create(
            QinTokenTypes.LINE_COMMENT,
            QinTokenTypes.BLOCK_COMMENT);
    private static final TokenSet STRINGS = TokenSet.create(QinTokenTypes.STRING);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new QinLexer();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return (root, builder) -> {
            QinSourceStructure sourceStructure = QinSourceStructure.parse(builder.getOriginalText());
            PsiBuilder.Marker rootMarker = builder.mark();
            while (!builder.eof()) {
                if (QinTokenFacts.isKeyword(builder, "import")) {
                    parseImportDeclaration(builder, sourceStructure);
                } else if (QinTokenFacts.isKeyword(builder, "object")) {
                    parseObjectDeclaration(builder, sourceStructure);
                } else if (QinTokenFacts.isThisMemberAccessStart(builder)) {
                    parseThisMemberAccess(builder);
                } else if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                    parseReferenceOrMemberAccess(builder);
                } else {
                    builder.advanceLexer();
                }
            }
            rootMarker.done(root);
            return builder.getTreeBuilt();
        };
    }

    private static void parseImportDeclaration(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure) {
        QinSourceStructure.ImportDeclaration importDeclaration =
                sourceStructure.importDeclarationAtKeywordOffset(builder.getCurrentOffset());
        PsiBuilder.Marker importMarker = builder.mark();
        builder.advanceLexer();
        while (!builder.eof() && isInsideSourceStructureImportDeclaration(builder, importDeclaration)) {
            if (isSourceStructureImportSpecifier(builder, sourceStructure)) {
                parseImportSpecifier(builder);
                continue;
            }
            builder.advanceLexer();
        }
        importMarker.done(QinTokenTypes.IMPORT_DECLARATION);
    }

    private static boolean isInsideSourceStructureImportDeclaration(
            @NotNull PsiBuilder builder,
            QinSourceStructure.ImportDeclaration importDeclaration) {
        return importDeclaration != null
                && importDeclaration.declarationRange().containsOffset(builder.getCurrentOffset());
    }

    private static boolean isSourceStructureImportSpecifier(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure) {
        return QinTokenFacts.isReferenceLeafToken(builder.getTokenType())
                && sourceStructure.importSpecifierAtExportedNameOffset(builder.getCurrentOffset()) != null;
    }

    private static void parseObjectDeclaration(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure) {
        QinSourceStructure.ObjectDeclaration objectDeclaration =
                sourceStructure.objectDeclarationAtKeywordOffset(builder.getCurrentOffset());
        PsiBuilder.Marker objectMarker = builder.mark();
        builder.advanceLexer();
        if (isSourceStructureObjectName(builder, objectDeclaration)) {
            wrapObjectName(builder);
        } else if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
            wrapObjectName(builder);
        }
        if (objectDeclaration == null || !objectDeclaration.bodyRange().isPresent()) {
            objectMarker.done(QinTokenTypes.OBJECT_DECLARATION);
            return;
        }
        while (!builder.eof() && objectDeclaration.bodyRange().startsAfter(builder.getCurrentOffset())) {
            builder.advanceLexer();
        }
        while (!builder.eof() && objectDeclaration.bodyRange().containsOffset(builder.getCurrentOffset())) {
            if (isSourceStructureMemberName(builder, sourceStructure, QinSourceStructure.ObjectMemberKind.METHOD)) {
                parseMethodDeclaration(builder, sourceStructure);
            } else if (isSourceStructureMemberName(builder, sourceStructure, QinSourceStructure.ObjectMemberKind.FIELD)) {
                parseFieldDeclaration(builder);
            } else if (QinTokenFacts.isThisMemberAccessStart(builder)) {
                parseThisMemberAccess(builder);
            } else if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                parseReferenceOrMemberAccess(builder);
            } else {
                builder.advanceLexer();
            }
        }
        objectMarker.done(QinTokenTypes.OBJECT_DECLARATION);
    }

    private static boolean isSourceStructureObjectName(
            @NotNull PsiBuilder builder,
            QinSourceStructure.ObjectDeclaration objectDeclaration) {
        return objectDeclaration != null
                && objectDeclaration.nameRange().startsAt(builder.getCurrentOffset())
                && QinTokenFacts.isReferenceLeafToken(builder.getTokenType());
    }

    private static QinSourceStructure.MemberDeclaration sourceStructureMemberDeclaration(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return QinTokenFacts.isDeclarationIdentifierToken(builder.getTokenType())
                ? sourceStructure.memberDeclarationAtNameOffset(builder.getCurrentOffset(), kind)
                : null;
    }

    private static boolean isSourceStructureMemberName(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return QinTokenFacts.isDeclarationIdentifierToken(builder.getTokenType())
                && sourceStructure.memberDeclarationAtNameOffset(builder.getCurrentOffset(), kind) != null;
    }

    private static void parseMethodDeclaration(
            @NotNull PsiBuilder builder,
            @NotNull QinSourceStructure sourceStructure) {
        QinSourceStructure.MemberDeclaration methodDeclaration =
                sourceStructureMemberDeclaration(
                        builder,
                        sourceStructure,
                        QinSourceStructure.ObjectMemberKind.METHOD);
        PsiBuilder.Marker methodMarker = builder.mark();
        wrapMethodName(builder);
        if (methodDeclaration == null || !methodDeclaration.bodyRange().isPresent()) {
            methodMarker.done(QinTokenTypes.METHOD_DECLARATION);
            return;
        }
        while (!builder.eof() && methodDeclaration.bodyRange().startsAfter(builder.getCurrentOffset())) {
            builder.advanceLexer();
        }
        while (!builder.eof() && methodDeclaration.bodyRange().containsOffset(builder.getCurrentOffset())) {
            if (QinTokenFacts.isThisMemberAccessStart(builder)) {
                parseThisMemberAccess(builder);
            } else if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                parseReferenceOrMemberAccess(builder);
            } else {
                builder.advanceLexer();
            }
        }
        methodMarker.done(QinTokenTypes.METHOD_DECLARATION);
    }

    private static void parseFieldDeclaration(PsiBuilder builder) {
        PsiBuilder.Marker fieldMarker = builder.mark();
        wrapFieldName(builder);
        fieldMarker.done(QinTokenTypes.FIELD_DECLARATION);
    }

    private static void parseImportSpecifier(PsiBuilder builder) {
        PsiBuilder.Marker specifierMarker = builder.mark();
        wrapReferenceIdentifier(builder);
        if (QinTokenFacts.isContextualKeyword(builder, "as")) {
            builder.advanceLexer();
            if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                wrapImportAliasName(builder);
            }
        }
        specifierMarker.done(QinTokenTypes.IMPORT_SPECIFIER);
    }

    private static void parseReferenceOrMemberAccess(PsiBuilder builder) {
        PsiBuilder.Marker memberMarker = builder.mark();
        wrapReferenceIdentifier(builder);
        boolean hasMemberAccess = false;
        while (builder.getTokenType() == QinTokenTypes.DOT) {
            hasMemberAccess = true;
            builder.advanceLexer();
            if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                wrapReferenceIdentifier(builder);
            } else {
                break;
            }
        }
        if (hasMemberAccess) {
            memberMarker.done(QinTokenTypes.MEMBER_ACCESS);
        } else {
            memberMarker.drop();
        }
    }

    private static void parseThisMemberAccess(PsiBuilder builder) {
        PsiBuilder.Marker memberMarker = builder.mark();
        builder.advanceLexer();
        if (builder.getTokenType() == QinTokenTypes.DOT) {
            builder.advanceLexer();
            if (QinTokenFacts.isReferenceLeafToken(builder.getTokenType())) {
                wrapReferenceIdentifier(builder);
            }
        }
        memberMarker.done(QinTokenTypes.MEMBER_ACCESS);
    }

    private static void wrapObjectName(PsiBuilder builder) {
        PsiBuilder.Marker objectNameMarker = builder.mark();
        builder.advanceLexer();
        objectNameMarker.done(QinTokenTypes.OBJECT_NAME);
    }

    private static void wrapMethodName(PsiBuilder builder) {
        PsiBuilder.Marker methodNameMarker = builder.mark();
        builder.advanceLexer();
        methodNameMarker.done(QinTokenTypes.METHOD_NAME);
    }

    private static void wrapFieldName(PsiBuilder builder) {
        PsiBuilder.Marker fieldNameMarker = builder.mark();
        builder.advanceLexer();
        fieldNameMarker.done(QinTokenTypes.FIELD_NAME);
    }

    private static void wrapReferenceIdentifier(PsiBuilder builder) {
        PsiBuilder.Marker referenceMarker = builder.mark();
        builder.advanceLexer();
        referenceMarker.done(QinTokenTypes.REFERENCE_IDENTIFIER);
    }

    private static void wrapImportAliasName(PsiBuilder builder) {
        PsiBuilder.Marker aliasMarker = builder.mark();
        builder.advanceLexer();
        aliasMarker.done(QinTokenTypes.IMPORT_ALIAS_NAME);
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        if (node.getElementType() == QinTokenTypes.OBJECT_NAME) {
            return new QinObjectNamePsiElement(node);
        }
        if (node.getElementType() == QinTokenTypes.METHOD_NAME) {
            return new QinMethodNamePsiElement(node);
        }
        if (node.getElementType() == QinTokenTypes.FIELD_NAME) {
            return new QinFieldNamePsiElement(node);
        }
        if (node.getElementType() == QinTokenTypes.IMPORT_ALIAS_NAME) {
            return new QinImportAliasNamePsiElement(node);
        }
        return new QinPsiElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new QinPsiFile(viewProvider);
    }
}
