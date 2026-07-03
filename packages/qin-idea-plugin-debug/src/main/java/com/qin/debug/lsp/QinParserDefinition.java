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
    private static final IFileElementType FILE = new IFileElementType(QinLanguage.INSTANCE);
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
                if (isReferenceToken(builder.getTokenType())) {
                    PsiBuilder.Marker referenceMarker = builder.mark();
                    builder.advanceLexer();
                    referenceMarker.done(QinTokenTypes.REFERENCE_IDENTIFIER);
                } else {
                    builder.advanceLexer();
                }
            }
            rootMarker.done(root);
            return builder.getTreeBuilt();
        };
    }

    private static boolean isReferenceToken(IElementType tokenType) {
        return tokenType == QinTokenTypes.IDENTIFIER
                || tokenType == QinTokenTypes.CLASS_NAME
                || tokenType == QinTokenTypes.MEMBER_IDENTIFIER;
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
        return new QinPsiElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new QinPsiFile(viewProvider);
    }
}
