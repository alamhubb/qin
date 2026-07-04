package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class QinObjectMemberCompletions {
    private QinObjectMemberCompletions() {
    }

    static @NotNull List<CompletionMember> members(@NotNull PsiElement position) {
        PsiElement referenceElement = QinReferenceElements.referenceElement(position);
        if (referenceElement == null || QinJavaReference.isJavaReferenceCandidate(referenceElement)) {
            return List.of();
        }

        String qualifier = QinReferenceElements.previousQualifierName(referenceElement);
        if (qualifier == null) {
            return List.of();
        }
        if (QinReferenceElements.isThisQualifier(qualifier)) {
            return completionMembers(QinObjectSymbols.memberElementsForThis(referenceElement));
        }
        return completionMembers(QinObjectSymbols.memberElementsForObject(referenceElement, qualifier));
    }

    private static @NotNull List<CompletionMember> completionMembers(
            @NotNull List<QinObjectSymbols.ObjectMemberElement> members) {
        return members.stream()
                .map(member -> new CompletionMember(member.name(), member.element()))
                .toList();
    }

    record CompletionMember(@NotNull String name, @NotNull PsiElement element) {
    }
}
