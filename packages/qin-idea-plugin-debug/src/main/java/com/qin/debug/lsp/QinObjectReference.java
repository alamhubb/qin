package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinObjectReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String identifier;

    QinObjectReference(@NotNull PsiElement element) {
        super(element, TextRange.from(0, element.getTextLength()));
        this.identifier = element.getText();
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
        if (isImportedAliasLocalReference(myElement)) {
            return myElement;
        }
        ASTNode leaf = myElement.getNode().getFirstChildNode();
        if (leaf instanceof LeafElement leafElement) {
            leafElement.replaceWithText(newElementName);
            return myElement;
        }
        throw new IncorrectOperationException("Cannot rename Qin object reference without a leaf token: " + myElement);
    }

    static boolean isObjectReferenceCandidate(@NotNull PsiElement element) {
        return QinJavaReference.previousQualifierName(element) == null
                && QinJavaImportTable.fromFile(element.getContainingFile()).find(element.getText()) == null
                && QinObjectSymbols.findObjectName(element, element.getText()) != null;
    }

    private static boolean isImportedAliasLocalReference(@NotNull PsiElement element) {
        QinModuleImportTable.QinImport qinImport = QinModuleImportTable.fromFile(element.getContainingFile())
                .find(element.getText());
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
