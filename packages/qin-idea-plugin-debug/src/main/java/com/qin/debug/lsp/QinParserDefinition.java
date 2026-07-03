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
            PsiBuilder.Marker rootMarker = builder.mark();
            while (!builder.eof()) {
                if (isKeyword(builder, "import")) {
                    parseImportDeclaration(builder);
                } else if (isKeyword(builder, "object")) {
                    parseObjectDeclaration(builder);
                } else if (isThisMemberAccessStart(builder)) {
                    parseThisMemberAccess(builder);
                } else if (isReferenceToken(builder.getTokenType())) {
                    parseReferenceOrMemberAccess(builder);
                } else {
                    builder.advanceLexer();
                }
            }
            rootMarker.done(root);
            return builder.getTreeBuilt();
        };
    }

    private static void parseImportDeclaration(PsiBuilder builder) {
        PsiBuilder.Marker importMarker = builder.mark();
        builder.advanceLexer();
        while (!builder.eof()) {
            if (isReferenceToken(builder.getTokenType())) {
                parseJavaImportSpecifier(builder);
                continue;
            }
            if (isKeyword(builder, "from")) {
                builder.advanceLexer();
                if (builder.getTokenType() == QinTokenTypes.STRING) {
                    builder.advanceLexer();
                }
                if (builder.getTokenType() == QinTokenTypes.SEMICOLON) {
                    builder.advanceLexer();
                }
                break;
            }
            if (builder.getTokenType() == QinTokenTypes.SEMICOLON) {
                builder.advanceLexer();
                break;
            }
            builder.advanceLexer();
        }
        importMarker.done(QinTokenTypes.IMPORT_DECLARATION);
    }

    private static void parseObjectDeclaration(PsiBuilder builder) {
        PsiBuilder.Marker objectMarker = builder.mark();
        builder.advanceLexer();
        if (isReferenceToken(builder.getTokenType())) {
            wrapObjectName(builder);
        }
        if (!isBrace(builder, "{")) {
            objectMarker.done(QinTokenTypes.OBJECT_DECLARATION);
            return;
        }

        int braceDepth = 0;
        while (!builder.eof()) {
            if (isBrace(builder, "{")) {
                braceDepth++;
                builder.advanceLexer();
                continue;
            }
            if (isBrace(builder, "}")) {
                braceDepth--;
                builder.advanceLexer();
                if (braceDepth <= 0) {
                    break;
                }
                continue;
            }
            if (isMethodDeclarationStart(builder)) {
                parseMethodDeclaration(builder);
            } else if (isFieldDeclarationStart(builder)) {
                parseFieldDeclaration(builder);
            } else if (isThisMemberAccessStart(builder)) {
                parseThisMemberAccess(builder);
            } else if (isReferenceToken(builder.getTokenType())) {
                parseReferenceOrMemberAccess(builder);
            } else {
                builder.advanceLexer();
            }
        }
        objectMarker.done(QinTokenTypes.OBJECT_DECLARATION);
    }

    private static void parseMethodDeclaration(PsiBuilder builder) {
        PsiBuilder.Marker methodMarker = builder.mark();
        wrapMethodName(builder);
        consumeParenthesizedTokens(builder);
        if (!isBrace(builder, "{")) {
            methodMarker.done(QinTokenTypes.METHOD_DECLARATION);
            return;
        }

        int braceDepth = 0;
        while (!builder.eof()) {
            if (isBrace(builder, "{")) {
                braceDepth++;
                builder.advanceLexer();
                continue;
            }
            if (isBrace(builder, "}")) {
                braceDepth--;
                builder.advanceLexer();
                if (braceDepth <= 0) {
                    break;
                }
                continue;
            }
            if (isThisMemberAccessStart(builder)) {
                parseThisMemberAccess(builder);
            } else if (isReferenceToken(builder.getTokenType())) {
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

    private static void parseJavaImportSpecifier(PsiBuilder builder) {
        PsiBuilder.Marker specifierMarker = builder.mark();
        wrapReferenceIdentifier(builder);
        if (isKeyword(builder, "as")) {
            builder.advanceLexer();
            if (isReferenceToken(builder.getTokenType())) {
                wrapReferenceIdentifier(builder);
            }
        }
        specifierMarker.done(QinTokenTypes.JAVA_IMPORT_SPECIFIER);
    }

    private static void parseReferenceOrMemberAccess(PsiBuilder builder) {
        PsiBuilder.Marker memberMarker = builder.mark();
        wrapReferenceIdentifier(builder);
        boolean hasMemberAccess = false;
        while (builder.getTokenType() == QinTokenTypes.DOT) {
            hasMemberAccess = true;
            builder.advanceLexer();
            if (isReferenceToken(builder.getTokenType())) {
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
            if (isReferenceToken(builder.getTokenType())) {
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

    private static boolean isKeyword(PsiBuilder builder, String text) {
        return builder.getTokenType() == QinTokenTypes.KEYWORD
                && text.equals(builder.getTokenText());
    }

    private static boolean isMethodDeclarationStart(PsiBuilder builder) {
        if (builder.getTokenType() != QinTokenTypes.FUNCTION_IDENTIFIER) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        if (builder.rawLookup(offset) != QinTokenTypes.PAREN || !rawTokenStartsWith(builder, offset, '(')) {
            return false;
        }

        int parenDepth = 0;
        while (offset != 0 && builder.rawLookup(offset) != null) {
            if (builder.rawLookup(offset) == QinTokenTypes.PAREN) {
                if (rawTokenStartsWith(builder, offset, '(')) {
                    parenDepth++;
                } else if (rawTokenStartsWith(builder, offset, ')')) {
                    parenDepth--;
                    if (parenDepth == 0) {
                        int afterParams = nextMeaningfulRawOffset(builder, offset + 1);
                        return builder.rawLookup(afterParams) == QinTokenTypes.BRACE
                                && rawTokenStartsWith(builder, afterParams, '{');
                    }
                }
            }
            offset = nextMeaningfulRawOffset(builder, offset + 1);
        }
        return false;
    }

    private static boolean isFieldDeclarationStart(PsiBuilder builder) {
        if (!isReferenceToken(builder.getTokenType())) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        return builder.rawLookup(offset) == QinTokenTypes.OPERATOR
                && rawTokenStartsWith(builder, offset, '=');
    }

    private static boolean isThisMemberAccessStart(PsiBuilder builder) {
        if (!isKeyword(builder, "this")) {
            return false;
        }
        int offset = nextMeaningfulRawOffset(builder, 1);
        return builder.rawLookup(offset) == QinTokenTypes.DOT;
    }

    private static void consumeParenthesizedTokens(PsiBuilder builder) {
        if (!isParen(builder, "(")) {
            return;
        }
        int parenDepth = 0;
        while (!builder.eof()) {
            if (isParen(builder, "(")) {
                parenDepth++;
            } else if (isParen(builder, ")")) {
                parenDepth--;
            }
            builder.advanceLexer();
            if (parenDepth <= 0) {
                return;
            }
        }
    }

    private static boolean isBrace(PsiBuilder builder, String text) {
        return builder.getTokenType() == QinTokenTypes.BRACE
                && text.equals(builder.getTokenText());
    }

    private static boolean isParen(PsiBuilder builder, String text) {
        return builder.getTokenType() == QinTokenTypes.PAREN
                && text.equals(builder.getTokenText());
    }

    private static boolean isReferenceToken(IElementType tokenType) {
        return tokenType == QinTokenTypes.IDENTIFIER
                || tokenType == QinTokenTypes.CLASS_NAME
                || tokenType == QinTokenTypes.MEMBER_IDENTIFIER;
    }

    private static int nextMeaningfulRawOffset(PsiBuilder builder, int offset) {
        int current = offset;
        while (builder.rawLookup(current) != null && isTrivia(builder.rawLookup(current))) {
            current++;
        }
        return current;
    }

    private static boolean isTrivia(IElementType tokenType) {
        return tokenType == TokenType.WHITE_SPACE
                || tokenType == QinTokenTypes.LINE_COMMENT
                || tokenType == QinTokenTypes.BLOCK_COMMENT;
    }

    private static boolean rawTokenStartsWith(PsiBuilder builder, int offset, char expected) {
        int start = builder.rawTokenTypeStart(offset);
        CharSequence text = builder.getOriginalText();
        return start >= 0 && start < text.length() && text.charAt(start) == expected;
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
        return new QinPsiElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new QinPsiFile(viewProvider);
    }
}
