package com.qin.debug.lsp;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

public final class QinImportAliasReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        QinReferenceElements.registerReferenceProvider(
                registrar,
                QinReferenceElements.referenceProvider(
                        QinImportAliasReference::isImportAliasReferenceCandidate,
                        QinImportAliasReference::new));
    }
}
