package com.qin.debug.lsp;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public final class QinJavaReferenceContributor extends PsiReferenceContributor {
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
                IElementType elementType = element.getNode() == null ? null : element.getNode().getElementType();
                if (elementType != QinTokenTypes.REFERENCE_IDENTIFIER) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{new QinJavaReference(element)};
            }
        };

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(),
                provider);
    }
}
