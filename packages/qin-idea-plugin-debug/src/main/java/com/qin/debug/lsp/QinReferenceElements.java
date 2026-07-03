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
        if (elementType == QinTokenTypes.REFERENCE_IDENTIFIER) {
            return element;
        }
        if (QinTokenFacts.isReferenceLeafToken(elementType)) {
            PsiElement parent = element.getParent();
            if (parent != null
                    && parent.getNode() != null
                    && parent.getNode().getElementType() == QinTokenTypes.REFERENCE_IDENTIFIER) {
                return parent;
            }
        }
        return null;
    }
}
