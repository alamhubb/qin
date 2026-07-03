package com.qin.debug.lsp;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class QinPsiFile extends PsiFileBase {
    QinPsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, QinLanguage.INSTANCE);
    }

    @Override
    public @NotNull QinLspFileType getFileType() {
        return QinLspFileType.INSTANCE;
    }

    @Override
    public @Nullable PsiReference findReferenceAt(int offset) {
        PsiElement element = findElementAt(offset);
        while (element != null && element != this) {
            for (PsiReference reference : ReferenceProvidersRegistry.getReferencesFromProviders(element)) {
                if (containsOffset(reference, offset)) {
                    return reference;
                }
            }
            element = element.getParent();
        }
        return null;
    }

    private static boolean containsOffset(@NotNull PsiReference reference, int offset) {
        return reference.getRangeInElement()
                .shiftRight(reference.getElement().getTextRange().getStartOffset())
                .containsOffset(offset);
    }

    @Override
    public String toString() {
        return "Qin source file";
    }
}
