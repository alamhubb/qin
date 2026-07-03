package com.qin.debug.lsp;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiReference;
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
        return QinPsiReferences.findReferenceAt(this, offset);
    }

    @Override
    public String toString() {
        return "Qin source file";
    }
}
