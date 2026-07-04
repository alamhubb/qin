package com.qin.debug.lsp;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinObjectReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String identifier;

    QinObjectReference(@NotNull PsiElement element) {
        super(element, QinReferenceElements.referenceRange(element));
        this.identifier = QinReferenceElements.referenceName(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(
                this,
                QinObjectReference::resolveInner,
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
        return QinPsiRenames.replaceLeafText(myElement, newElementName, "Qin object reference");
    }

    static boolean isObjectReferenceCandidate(@NotNull PsiElement element) {
        String referenceName = QinReferenceElements.referenceName(element);
        return QinReferenceElements.previousQualifierName(element) == null
                && !QinImportBindings.isAliasedLocalSpecifierElement(element)
                && !isImportedAliasLocalReference(element)
                && QinJavaImportTable.fromFile(element.getContainingFile()).find(referenceName) == null
                && QinObjectSymbols.findObjectName(element, referenceName) != null;
    }

    private static boolean isImportedAliasLocalReference(@NotNull PsiElement element) {
        QinImportBindings.ImportBinding qinImport = QinModuleImportTable.fromFile(element.getContainingFile())
                .find(QinReferenceElements.referenceName(element));
        return qinImport != null && !qinImport.exportedName().equals(qinImport.localName());
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinObjectReference reference,
            boolean incompleteCode) {
        PsiElement objectName = QinObjectSymbols.findObjectName(reference.getElement(), reference.identifier);
        return objectName == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(objectName)};
    }
}
