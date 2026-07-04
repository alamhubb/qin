package com.qin.debug.lsp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public final class QinSymbolHighlightAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element.getContainingFile() instanceof QinPsiFile) || element.getNode() == null) {
            return;
        }

        IElementType elementType = element.getNode().getElementType();
        if (elementType == QinTokenTypes.OBJECT_NAME) {
            highlight(element, holder, DefaultLanguageHighlighterColors.CLASS_NAME, "Qin object symbol");
            return;
        }
        if (elementType == QinTokenTypes.METHOD_NAME) {
            highlight(element, holder, DefaultLanguageHighlighterColors.INSTANCE_METHOD, "Qin method symbol");
            return;
        }
        if (elementType == QinTokenTypes.FIELD_NAME) {
            highlight(element, holder, DefaultLanguageHighlighterColors.INSTANCE_FIELD, "Qin field symbol");
            return;
        }
        if (elementType == QinTokenTypes.IMPORT_ALIAS_NAME) {
            highlight(element, holder, DefaultLanguageHighlighterColors.LOCAL_VARIABLE, "Qin import alias symbol");
            return;
        }
        if (elementType != QinTokenTypes.REFERENCE_IDENTIFIER) {
            return;
        }

        for (PsiReference reference : QinPsiReferences.references(element)) {
            if (reference instanceof QinImportAliasReference) {
                highlight(element, holder, DefaultLanguageHighlighterColors.LOCAL_VARIABLE, "Qin import alias reference");
                return;
            }
            if (reference instanceof QinObjectReference) {
                highlight(element, holder, DefaultLanguageHighlighterColors.CLASS_NAME, "Qin object reference");
                return;
            }
            if (reference instanceof QinObjectMethodReference) {
                highlight(element, holder, DefaultLanguageHighlighterColors.INSTANCE_METHOD, "Qin method reference");
                return;
            }
            if (reference instanceof QinObjectFieldReference) {
                highlight(element, holder, DefaultLanguageHighlighterColors.INSTANCE_FIELD, "Qin field reference");
                return;
            }
            if (reference instanceof QinJavaReference) {
                highlightJavaReference(element, holder, reference.resolve());
                return;
            }
        }
    }

    private static void highlightJavaReference(
            @NotNull PsiElement element,
            @NotNull AnnotationHolder holder,
            PsiElement target) {
        if (target instanceof PsiClass) {
            highlight(element, holder, DefaultLanguageHighlighterColors.CLASS_REFERENCE, "Java class reference");
            return;
        }
        if (target instanceof PsiMethod) {
            highlight(element, holder, DefaultLanguageHighlighterColors.STATIC_METHOD, "Java static method reference");
            return;
        }
        if (target instanceof PsiField) {
            highlight(element, holder, DefaultLanguageHighlighterColors.STATIC_FIELD, "Java static field reference");
        }
    }

    private static void highlight(
            @NotNull PsiElement element,
            @NotNull AnnotationHolder holder,
            @NotNull TextAttributesKey textAttributes,
            @NotNull String description) {
        holder.newAnnotation(HighlightSeverity.INFORMATION, description)
                .range(element.getTextRange())
                .textAttributes(textAttributes)
                .create();
    }
}
