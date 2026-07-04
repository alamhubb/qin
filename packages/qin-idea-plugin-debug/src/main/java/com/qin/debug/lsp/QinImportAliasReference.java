package com.qin.debug.lsp;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinImportAliasReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String localName;

    QinImportAliasReference(@NotNull PsiElement element) {
        super(element, TextRange.from(0, element.getTextLength()));
        this.localName = QinReferenceElements.referenceName(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(
                this,
                QinImportAliasReference::resolveInner,
                false,
                incompleteCode);
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 0 ? null : results[0].getElement();
    }

    @Override
    public @NotNull PsiElement handleElementRename(@NotNull @NlsSafe String newElementName)
            throws IncorrectOperationException {
        return QinPsiRenames.replaceLeafText(myElement, newElementName, "Qin import alias reference");
    }

    static boolean isImportAliasReferenceCandidate(@NotNull PsiElement element) {
        return !QinReferenceElements.isImportAliasDeclaration(element)
                && QinReferenceElements.previousQualifierName(element) == null
                && QinImportBindings.findAliasName(element, QinReferenceElements.referenceName(element)) != null;
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinImportAliasReference reference,
            boolean incompleteCode) {
        PsiElement aliasName = QinImportBindings.findAliasName(reference.getElement(), reference.localName);
        return aliasName == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(aliasName)};
    }
}
