package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
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
        ASTNode leaf = myElement.getNode().getFirstChildNode();
        if (leaf instanceof LeafElement leafElement) {
            leafElement.replaceWithText(newElementName);
            return myElement;
        }
        throw new IncorrectOperationException("Cannot rename Qin object reference without a leaf token: " + myElement);
    }

    static boolean isObjectReferenceCandidate(@NotNull PsiElement element) {
        return findObjectName(element, element.getText()) != null;
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinObjectReference reference,
            boolean incompleteCode) {
        PsiElement objectName = findObjectName(reference.getElement(), reference.identifier);
        return objectName == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(objectName)};
    }

    private static @Nullable PsiElement findObjectName(@NotNull PsiElement element, @NotNull String name) {
        PsiElement file = element.getContainingFile();
        ObjectNameVisitor visitor = new ObjectNameVisitor(name);
        file.accept(visitor);
        return visitor.objectName;
    }

    private static final class ObjectNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement objectName;

        private ObjectNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (objectName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.OBJECT_NAME
                    && name.equals(element.getText())) {
                objectName = element;
                return;
            }
            super.visitElement(element);
        }
    }
}
