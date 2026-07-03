package com.qin.debug.lsp;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unchecked")
final class QinFileStub extends PsiFileStubImpl<QinPsiFile> {
    private final List<QinSourceStructure.ObjectDeclaration> objectDeclarations;

    QinFileStub(
            @Nullable QinPsiFile file,
            @NotNull List<QinSourceStructure.ObjectDeclaration> objectDeclarations) {
        super(file);
        this.objectDeclarations = List.copyOf(objectDeclarations);
    }

    @Override
    public QinFileElementType getType() {
        return QinFileElementType.INSTANCE;
    }

    @NotNull List<String> objectNames() {
        return objectDeclarations.stream()
                .map(QinSourceStructure.ObjectDeclaration::name)
                .toList();
    }

    @NotNull List<QinSourceStructure.ObjectDeclaration> objectDeclarations() {
        return objectDeclarations;
    }
}
