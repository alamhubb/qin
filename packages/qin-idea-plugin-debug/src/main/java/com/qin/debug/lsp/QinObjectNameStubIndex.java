package com.qin.debug.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class QinObjectNameStubIndex extends StringStubIndexExtension<QinPsiFile> {
    static final StubIndexKey<String, QinPsiFile> KEY =
            StubIndexKey.createIndexKey("com.qin.debug.lsp.stub.objectName");

    @Override
    public @NotNull StubIndexKey<String, QinPsiFile> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    static boolean contains(
            @NotNull Project project,
            @NotNull VirtualFile indexedFile,
            @NotNull String objectName) {
        GlobalSearchScope indexedFileScope = GlobalSearchScope.fileScope(project, indexedFile);
        Collection<QinPsiFile> indexedFiles = StubIndex.getElements(
                KEY,
                objectName,
                project,
                indexedFileScope,
                QinPsiFile.class);
        return indexedFiles.stream().anyMatch(file -> indexedFile.equals(QinPsiTree.virtualFile(file)));
    }
}
