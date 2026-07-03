package com.qin.debug.lsp;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unchecked")
final class QinFileStub extends PsiFileStubImpl<QinPsiFile> {
    private final List<QinDeclarationScanner.ObjectDeclaration> objectDeclarations;

    QinFileStub(
            @Nullable QinPsiFile file,
            @NotNull List<QinDeclarationScanner.ObjectDeclaration> objectDeclarations) {
        super(file);
        this.objectDeclarations = List.copyOf(objectDeclarations);
    }

    @Override
    public QinFileElementType getType() {
        return QinFileElementType.INSTANCE;
    }

    @NotNull List<String> objectNames() {
        return objectDeclarations.stream()
                .map(QinDeclarationScanner.ObjectDeclaration::name)
                .toList();
    }

    @NotNull List<QinDeclarationScanner.ObjectDeclaration> objectDeclarations() {
        return objectDeclarations;
    }
}
