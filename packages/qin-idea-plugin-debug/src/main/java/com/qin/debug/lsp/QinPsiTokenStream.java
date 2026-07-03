package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinPsiTokenStream {
    private QinPsiTokenStream() {
    }

    static List<QinPsiToken> collect(@NotNull PsiFile file) {
        return collect((PsiElement) file);
    }

    static List<QinPsiToken> collect(@NotNull PsiElement root) {
        List<QinPsiToken> tokens = new ArrayList<>();
        root.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getFirstChild() == null && element.getNode() != null) {
                    IElementType type = element.getNode().getElementType();
                    if (type != TokenType.WHITE_SPACE
                            && type != QinTokenTypes.LINE_COMMENT
                            && type != QinTokenTypes.BLOCK_COMMENT) {
                        tokens.add(new QinPsiToken(tokenOwner(element), type, element.getText()));
                    }
                }
                super.visitElement(element);
            }
        });
        return tokens;
    }

    private static PsiElement tokenOwner(PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent != null
                && parent.getNode() != null
                && parent.getNode().getElementType() == QinTokenTypes.REFERENCE_IDENTIFIER) {
            return parent;
        }
        return element;
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement element) {
        return previousQualifierName(element.getContainingFile(), element);
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement root, @NotNull PsiElement element) {
        List<QinPsiToken> tokens = collect(root);
        int tokenIndex = indexOfElement(tokens, element);
        if (tokenIndex < 2) {
            return null;
        }
        QinPsiToken dot = tokens.get(tokenIndex - 1);
        QinPsiToken qualifier = tokens.get(tokenIndex - 2);
        if (!dot.is(QinTokenTypes.DOT, ".") || (!qualifier.isIdentifier() && !qualifier.isKeyword("this"))) {
            return null;
        }
        return qualifier.text();
    }

    private static int indexOfElement(List<QinPsiToken> tokens, PsiElement element) {
        for (int index = 0; index < tokens.size(); index++) {
            if (tokens.get(index).element().equals(element)) {
                return index;
            }
        }
        return -1;
    }
}
