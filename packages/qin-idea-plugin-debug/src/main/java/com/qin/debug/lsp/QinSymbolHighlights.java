package com.qin.debug.lsp;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinSymbolHighlights {
    private QinSymbolHighlights() {
    }

    static @Nullable SymbolHighlight declarationHighlight(@NotNull PsiElement element) {
        if (QinPsiTree.isType(element, QinTokenTypes.OBJECT_NAME)) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.CLASS_NAME, "Qin object symbol");
        }
        if (QinPsiTree.isType(element, QinTokenTypes.METHOD_NAME)) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.INSTANCE_METHOD, "Qin method symbol");
        }
        if (QinPsiTree.isType(element, QinTokenTypes.FIELD_NAME)) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.INSTANCE_FIELD, "Qin field symbol");
        }
        if (QinReferenceElements.isImportAliasDeclaration(element)) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.LOCAL_VARIABLE, "Qin import alias symbol");
        }
        return null;
    }

    static @Nullable SymbolHighlight referenceHighlight(@NotNull PsiReference reference) {
        if (reference instanceof QinImportAliasReference) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.LOCAL_VARIABLE, "Qin import alias reference");
        }
        if (reference instanceof QinObjectReference) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.CLASS_NAME, "Qin object reference");
        }
        if (reference instanceof QinObjectMethodReference) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.INSTANCE_METHOD, "Qin method reference");
        }
        if (reference instanceof QinObjectFieldReference) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.INSTANCE_FIELD, "Qin field reference");
        }
        if (reference instanceof QinJavaReference) {
            return javaReferenceHighlight(reference.resolve());
        }
        return null;
    }

    private static @Nullable SymbolHighlight javaReferenceHighlight(@Nullable PsiElement target) {
        if (target instanceof PsiClass) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.CLASS_REFERENCE, "Java class reference");
        }
        if (target instanceof PsiMethod) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.STATIC_METHOD, "Java static method reference");
        }
        if (target instanceof PsiField) {
            return new SymbolHighlight(DefaultLanguageHighlighterColors.STATIC_FIELD, "Java static field reference");
        }
        return null;
    }

    record SymbolHighlight(@NotNull TextAttributesKey textAttributes, @NotNull String description) {
    }
}
