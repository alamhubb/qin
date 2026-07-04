package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public final class QinSymbolHighlightAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!QinPsiTree.isQinFile(element) || element.getNode() == null) {
            return;
        }

        QinSymbolHighlights.SymbolHighlight declarationHighlight =
                QinSymbolHighlights.declarationHighlight(element);
        if (declarationHighlight != null) {
            highlight(element, holder, declarationHighlight);
            return;
        }
        if (!QinReferenceElements.isReferenceIdentifier(element)) {
            return;
        }

        for (PsiReference reference : QinPsiReferences.references(element)) {
            QinSymbolHighlights.SymbolHighlight referenceHighlight =
                    QinSymbolHighlights.referenceHighlight(reference);
            if (referenceHighlight != null) {
                highlight(element, holder, referenceHighlight);
                return;
            }
        }
    }

    private static void highlight(
            @NotNull PsiElement element,
            @NotNull AnnotationHolder holder,
            @NotNull QinSymbolHighlights.SymbolHighlight highlight) {
        holder.newAnnotation(HighlightSeverity.INFORMATION, highlight.description())
                .range(element.getTextRange())
                .textAttributes(highlight.textAttributes())
                .create();
    }
}
