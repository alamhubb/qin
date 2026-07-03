package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class QinObjectMethodAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element.getContainingFile() instanceof QinPsiFile)
                || element.getNode() == null
                || element.getNode().getElementType() != QinTokenTypes.REFERENCE_IDENTIFIER) {
            return;
        }
        if (QinJavaReference.isJavaReferenceCandidate(element)) {
            return;
        }

        if (QinPsiReferences.unresolvedReferenceOfType(element, QinObjectMethodReference.class) == null) {
            return;
        }

        String objectName = QinJavaReference.previousQualifierName(element);
        holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "Unresolved Qin object method " + objectName + "." + element.getText())
                .range(element.getTextRange())
                .create();
    }
}
