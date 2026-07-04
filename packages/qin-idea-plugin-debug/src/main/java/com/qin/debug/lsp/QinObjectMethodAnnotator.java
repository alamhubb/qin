package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class QinObjectMethodAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        String message = QinUnresolvedReferenceMessages.objectMethodMessageFor(element);
        if (message == null) {
            return;
        }
        holder.newAnnotation(HighlightSeverity.ERROR, message)
                .range(element.getTextRange())
                .create();
    }
}
