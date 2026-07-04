package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinReferenceElements {
    private QinReferenceElements() {
    }

    static @Nullable PsiElement referenceElement(@NotNull PsiElement element) {
        IElementType elementType = element.getNode() == null ? null : element.getNode().getElementType();
        if (isReferenceIdentifier(element)) {
            return element;
        }
        if (QinTokenFacts.isReferenceLeafToken(elementType)) {
            PsiElement parent = element.getParent();
            if (parent != null && isReferenceIdentifier(parent)) {
                return parent;
            }
        }
        return null;
    }

    static boolean isReferenceIdentifier(@NotNull PsiElement element) {
        return QinPsiTree.isType(element, QinTokenTypes.REFERENCE_IDENTIFIER);
    }

    static boolean isImportAliasDeclaration(@NotNull PsiElement element) {
        return QinPsiTree.isType(element, QinTokenTypes.IMPORT_ALIAS_NAME);
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent != null && QinPsiTree.isType(parent, QinTokenTypes.MEMBER_ACCESS)) {
            return QinPsiTokenStream.previousQualifierName(parent, element);
        }
        return null;
    }
}
