package com.qin.debug.lsp;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

public final class QinObjectMethodReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        QinReferenceElements.registerMemberReferenceProvider(
                registrar,
                QinReferenceElements.referenceProvider(
                        referenceElement -> !QinJavaReference.isJavaReferenceCandidate(referenceElement)
                                && QinObjectMethodReference.isObjectMethodReferenceCandidate(referenceElement),
                        QinObjectMethodReference::new));
    }
}
