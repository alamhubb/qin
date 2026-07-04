package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;

final class QinFileStubBuilder implements StubBuilder {
    @Override
    public @NotNull StubElement<?> buildStubTree(@NotNull PsiFile file) {
        QinPsiFile qinFile = file instanceof QinPsiFile ? (QinPsiFile) file : null;
        return new QinFileStub(qinFile, QinPsiTree.sourceStructure(file).objectDeclarations());
    }

    @Override
    public boolean skipChildProcessingWhenBuildingStubs(@NotNull ASTNode parent, @NotNull ASTNode node) {
        return true;
    }
}
