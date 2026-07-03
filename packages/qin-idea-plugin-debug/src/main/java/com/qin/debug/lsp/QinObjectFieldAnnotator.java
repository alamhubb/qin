package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class QinObjectFieldAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element.getContainingFile() instanceof QinPsiFile)
                || element.getNode() == null
                || element.getNode().getElementType() != QinTokenTypes.REFERENCE_IDENTIFIER) {
            return;
        }
        if (QinJavaReference.isJavaReferenceCandidate(element)
                || QinPsiTokenStream.isFollowedByCallParenthesis(element)) {
            return;
        }

        String qualifier = QinJavaReference.previousQualifierName(element);
        if (qualifier == null) {
            return;
        }

        if (QinPsiReferences.unresolvedReferenceOfType(element, QinObjectFieldReference.class) != null) {
            holder.newAnnotation(
                            HighlightSeverity.ERROR,
                            "Unresolved Qin object field " + qualifier + "." + element.getText())
                    .range(element.getTextRange())
                    .create();
        }
    }
}
