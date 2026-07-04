package com.qin.debug.lsp;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

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
                        for (QinObjectMemberCompletions.CompletionMember member
                                : QinObjectMemberCompletions.members(parameters.getPosition())) {
                            result.addElement(LookupElementBuilder.create(member.name())
                                    .withPsiElement(member.element()));
                        }
                    }
                });
    }
}
