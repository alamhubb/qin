package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
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
                    if (!QinTokenFacts.isTrivia(type)) {
                        tokens.add(new QinPsiToken(tokenOwner(element), type, element.getText()));
                    }
                }
                super.visitElement(element);
            }
        });
        return tokens;
    }

    private static PsiElement tokenOwner(PsiElement element) {
        PsiElement referenceElement = QinReferenceElements.referenceElement(element);
        return referenceElement == null ? element : referenceElement;
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement element) {
        return previousQualifierName(element.getContainingFile(), element);
    }

    static @Nullable PsiElement previousQualifierElement(@NotNull PsiElement root, @NotNull PsiElement element) {
        List<QinPsiToken> tokens = collect(root);
        int tokenIndex = indexOfElement(tokens, element);
        String qualifierName = tokenIndex < 0 ? null : QinTokenFacts.previousQualifierName(tokens, tokenIndex);
        if (qualifierName == null) {
            return null;
        }
        QinPsiToken qualifier = tokens.get(tokenIndex - 2);
        return qualifierName.equals(qualifier.text()) ? qualifier.element() : null;
    }

    static boolean isFollowedByCallParenthesis(@NotNull PsiElement element) {
        return isFollowedByCallParenthesis(element.getContainingFile(), element);
    }

    static boolean isFollowedByCallParenthesis(@NotNull PsiElement root, @NotNull PsiElement element) {
        List<QinPsiToken> tokens = collect(root);
        int tokenIndex = indexOfElement(tokens, element);
        return tokenIndex >= 0 && QinTokenFacts.isFollowedByCallParenthesis(tokens, tokenIndex);
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement root, @NotNull PsiElement element) {
        List<QinPsiToken> tokens = collect(root);
        int tokenIndex = indexOfElement(tokens, element);
        return tokenIndex < 0 ? null : QinTokenFacts.previousQualifierName(tokens, tokenIndex);
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
