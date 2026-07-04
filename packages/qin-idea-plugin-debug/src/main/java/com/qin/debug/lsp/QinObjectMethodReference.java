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

final class QinObjectMethodReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String methodName;

    QinObjectMethodReference(@NotNull PsiElement element) {
        super(element, TextRange.from(0, element.getTextLength()));
        this.methodName = QinReferenceElements.referenceName(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(
                this,
                QinObjectMethodReference::resolveInner,
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
        return QinPsiRenames.replaceLeafText(myElement, newElementName, "Qin object method reference");
    }

    static boolean isObjectMethodReferenceCandidate(@NotNull PsiElement element) {
        return QinReferenceElements.isFollowedByCallParenthesis(element) && hasObjectMethodQualifier(element);
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinObjectMethodReference reference,
            boolean incompleteCode) {
        PsiElement methodNameElement = resolveMethodName(reference.getElement(), reference.methodName);
        return methodNameElement == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(methodNameElement)};
    }

    private static @Nullable PsiElement resolveMethodName(@NotNull PsiElement element, @NotNull String methodName) {
        String objectName = QinReferenceElements.previousQualifierName(element);
        if (QinReferenceElements.isThisQualifier(objectName)) {
            return QinObjectSymbols.findMethodNameForThis(element, methodName);
        }
        if (objectName == null) {
            return null;
        }
        return QinObjectSymbols.findMethodName(element, objectName, methodName);
    }

    private static boolean hasObjectMethodQualifier(@NotNull PsiElement element) {
        String objectName = QinReferenceElements.previousQualifierName(element);
        String methodName = QinReferenceElements.referenceName(element);
        if (QinReferenceElements.isThisQualifier(objectName)) {
            return QinObjectSymbols.findMethodNameForThis(element, methodName) != null
                    || QinObjectSymbols.findFieldNameForThis(element, methodName) == null;
        }
        return objectName != null && QinObjectSymbols.findObjectName(element, objectName) != null;
    }
}
