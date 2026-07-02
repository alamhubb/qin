package com.qin.debug.lsp;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public final class QinPsiFile extends PsiFileBase {
    QinPsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, QinLanguage.INSTANCE);
    }

    @Override
    public @NotNull QinLspFileType getFileType() {
        return QinLspFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Qin source file";
    }
}
