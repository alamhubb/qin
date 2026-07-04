package com.qin.debug.lsp;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

public final class QinObjectFieldReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        QinReferenceElements.registerMemberReferenceProvider(
                registrar,
                QinReferenceElements.objectReferenceProvider(
                        QinObjectFieldReference::isObjectFieldReferenceCandidate,
                        QinObjectFieldReference::new));
    }
}
