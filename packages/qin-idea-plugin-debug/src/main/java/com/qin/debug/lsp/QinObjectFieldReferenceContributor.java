package com.qin.debug.lsp;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public final class QinObjectFieldReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        PsiReferenceProvider provider = new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(
                    @NotNull PsiElement element,
                    @NotNull ProcessingContext context) {
                if (element.getContainingFile() == null || !(element.getContainingFile() instanceof QinPsiFile)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                PsiElement referenceElement = QinReferenceElements.referenceElement(element);
                if (referenceElement == null
                        || QinJavaReference.isJavaReferenceCandidate(referenceElement)
                        || !QinObjectFieldReference.isObjectFieldReferenceCandidate(referenceElement)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{new QinObjectFieldReference(referenceElement)};
            }
        };

        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.REFERENCE_IDENTIFIER), provider);
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.MEMBER_IDENTIFIER), provider);
    }
}
