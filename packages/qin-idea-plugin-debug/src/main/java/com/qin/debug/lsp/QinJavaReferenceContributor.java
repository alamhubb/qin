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
                PsiElement referenceElement = referenceElement(element);
                if (referenceElement == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{new QinJavaReference(referenceElement)};
            }
        };

        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.REFERENCE_IDENTIFIER), provider);
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.IDENTIFIER), provider);
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.CLASS_NAME), provider);
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(QinTokenTypes.MEMBER_IDENTIFIER), provider);
    }

    private static PsiElement referenceElement(@NotNull PsiElement element) {
        IElementType elementType = element.getNode() == null ? null : element.getNode().getElementType();
        if (elementType == QinTokenTypes.REFERENCE_IDENTIFIER) {
            return element;
        }
        if (isReferenceLeafToken(elementType)) {
            PsiElement parent = element.getParent();
            if (parent != null
                    && parent.getNode() != null
                    && parent.getNode().getElementType() == QinTokenTypes.REFERENCE_IDENTIFIER) {
                return parent;
            }
        }
        return null;
    }

    private static boolean isReferenceLeafToken(IElementType elementType) {
        return elementType == QinTokenTypes.IDENTIFIER
                || elementType == QinTokenTypes.CLASS_NAME
                || elementType == QinTokenTypes.MEMBER_IDENTIFIER;
    }
}
