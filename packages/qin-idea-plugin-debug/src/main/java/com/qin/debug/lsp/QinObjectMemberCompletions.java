package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class QinObjectMemberCompletions {
    private QinObjectMemberCompletions() {
    }

    static @NotNull List<PsiElement> memberElements(@NotNull PsiElement position) {
        PsiElement referenceElement = QinReferenceElements.referenceElement(position);
        if (referenceElement == null || QinJavaReference.isJavaReferenceCandidate(referenceElement)) {
            return List.of();
        }

        String qualifier = QinReferenceElements.previousQualifierName(referenceElement);
        if (qualifier == null) {
            return List.of();
        }
        if (QinReferenceElements.isThisQualifier(qualifier)) {
            return QinObjectSymbols.memberElementsForThis(referenceElement);
        }
        return QinObjectSymbols.memberElementsForObject(referenceElement, qualifier);
    }
}
