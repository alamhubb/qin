package com.qin.debug.lsp;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unchecked")
final class QinFileStub extends PsiFileStubImpl<QinPsiFile> {
    private final List<String> objectNames;

    QinFileStub(@Nullable QinPsiFile file, @NotNull List<String> objectNames) {
        super(file);
        this.objectNames = List.copyOf(objectNames);
    }

    @Override
    public QinFileElementType getType() {
        return QinFileElementType.INSTANCE;
    }

    @NotNull List<String> objectNames() {
        return objectNames;
    }
}
