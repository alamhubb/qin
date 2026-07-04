package com.qin.debug.lsp;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinObjectFieldReference extends PsiPolyVariantReferenceBase<PsiElement> {
    QinObjectFieldReference(@NotNull PsiElement element) {
        super(element, QinReferenceElements.referenceRange(element));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(
                this,
                QinObjectFieldReference::resolveInner,
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
        return QinPsiRenames.replaceLeafText(myElement, newElementName, "Qin object field reference");
    }

    static boolean isObjectFieldReferenceCandidate(@NotNull PsiElement element) {
        return !QinReferenceElements.isFollowedByCallParenthesis(element) && hasObjectFieldQualifier(element);
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinObjectFieldReference reference,
            boolean incompleteCode) {
        PsiElement fieldName = resolveFieldName(reference.getElement());
        return fieldName == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(fieldName)};
    }

    private static @Nullable PsiElement resolveFieldName(@NotNull PsiElement element) {
        String qualifier = QinReferenceElements.previousQualifierName(element);
        String fieldName = QinReferenceElements.referenceName(element);
        if (QinReferenceElements.isThisQualifier(qualifier)) {
            return QinObjectSymbols.findFieldNameForThis(element, fieldName);
        }
        if (qualifier == null) {
            return null;
        }
        return QinObjectSymbols.findFieldName(element, qualifier, fieldName);
    }

    private static boolean hasObjectFieldQualifier(@NotNull PsiElement element) {
        String qualifier = QinReferenceElements.previousQualifierName(element);
        if (QinReferenceElements.isThisQualifier(qualifier)) {
            return true;
        }
        return qualifier != null && QinObjectSymbols.findObjectName(element, qualifier) != null;
    }
}
