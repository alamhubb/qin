package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public final class QinJavaInteropAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element.getContainingFile() instanceof QinPsiFile) || element.getNode() == null) {
            return;
        }
        IElementType elementType = element.getNode().getElementType();
        if (elementType != QinTokenTypes.REFERENCE_IDENTIFIER) {
            return;
        }

        QinJavaImportTable importTable = QinJavaImportTable.fromFile(element.getContainingFile());
        String qualifier = QinJavaReference.previousQualifierName(element);
        if (qualifier != null) {
            QinJavaImportTable.JavaImport importedClass = importTable.find(qualifier);
            if (importedClass == null) {
                return;
            }
            annotateUnresolvedReference(
                    element,
                    holder,
                    "Unresolved static Java member " + importedClass.qualifiedClassName() + "." + element.getText());
            return;
        }

        QinJavaImportTable.JavaImport importedClass = importTable.find(element.getText());
        if (importedClass != null) {
            annotateUnresolvedReference(
                    element,
                    holder,
                    "Unresolved Java class " + importedClass.qualifiedClassName());
        }
    }

    private static void annotateUnresolvedReference(
            @NotNull PsiElement element,
            @NotNull AnnotationHolder holder,
            @NotNull String message) {
        for (PsiReference reference : element.getReferences()) {
            if (reference instanceof QinJavaReference && reference.resolve() == null) {
                holder.newAnnotation(HighlightSeverity.ERROR, message)
                        .range(element.getTextRange())
                        .create();
                return;
            }
        }
    }
}
