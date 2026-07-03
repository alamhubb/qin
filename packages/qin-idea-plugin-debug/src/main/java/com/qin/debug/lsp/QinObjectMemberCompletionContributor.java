package com.qin.debug.lsp;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class QinObjectMemberCompletionContributor extends CompletionContributor {
    public QinObjectMemberCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement referenceElement = QinReferenceElements.referenceElement(parameters.getPosition());
                        if (referenceElement == null || QinJavaReference.isJavaReferenceCandidate(referenceElement)) {
                            return;
                        }

                        String qualifier = QinJavaReference.previousQualifierName(referenceElement);
                        if (qualifier == null) {
                            return;
                        }

                        List<PsiElement> members = "this".equals(qualifier)
                                ? QinObjectSymbols.memberElementsForThis(referenceElement)
                                : QinObjectSymbols.memberElementsForObject(referenceElement, qualifier);
                        for (PsiElement member : members) {
                            if (member instanceof PsiNamedElement namedMember) {
                                result.addElement(LookupElementBuilder.create(namedMember));
                            } else {
                                result.addElement(LookupElementBuilder.create(member.getText()).withPsiElement(member));
                            }
                        }
                    }
                });
    }
}
