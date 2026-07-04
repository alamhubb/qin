package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinPsiReferences {
    private QinPsiReferences() {
    }

    static PsiReference @NotNull [] references(@NotNull PsiElement element) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(element);
    }

    static <T extends PsiReference> @NotNull List<T> referencesOfType(
            @NotNull PsiElement element,
            @NotNull Class<T> referenceType) {
        List<T> matches = new ArrayList<>();
        for (PsiReference reference : references(element)) {
            if (referenceType.isInstance(reference)) {
                matches.add(referenceType.cast(reference));
            }
        }
        return matches;
    }

    static <T extends PsiReference> @Nullable T unresolvedReferenceOfType(
            @NotNull PsiElement element,
            @NotNull Class<T> referenceType) {
        for (T reference : referencesOfType(element, referenceType)) {
            if (reference.resolve() == null) {
                return reference;
            }
        }
        return null;
    }

    static @Nullable PsiReference findReferenceAt(@NotNull PsiFile file, int offset) {
        PsiElement element = QinPsiTree.elementAt(file, offset);
        while (element != null && element != file) {
            for (PsiReference reference : references(element)) {
                if (containsOffset(reference, offset)) {
                    return reference;
                }
            }
            element = QinPsiTree.parent(element);
        }
        return null;
    }

    private static boolean containsOffset(@NotNull PsiReference reference, int offset) {
        return QinPsiTree.referenceRangeInFile(reference).containsOffset(offset);
    }
}
