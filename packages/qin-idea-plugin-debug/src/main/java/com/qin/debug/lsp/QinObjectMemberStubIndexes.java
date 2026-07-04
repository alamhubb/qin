package com.qin.debug.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

final class QinObjectMemberStubIndexes {
    private QinObjectMemberStubIndexes() {
    }

    static @NotNull StubIndexKey<String, QinPsiFile> keyFor(
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return kind == QinSourceStructure.ObjectMemberKind.FIELD
                ? QinObjectFieldNameStubIndex.KEY
                : QinObjectMethodNameStubIndex.KEY;
    }

    static boolean contains(
            @NotNull Project project,
            @NotNull VirtualFile indexedFile,
            @NotNull String objectName,
            @NotNull String memberName,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        GlobalSearchScope indexedFileScope = GlobalSearchScope.fileScope(project, indexedFile);
        String key = QinSourceStructure.objectMemberKey(objectName, memberName);
        Collection<QinPsiFile> indexedFiles = StubIndex.getElements(
                keyFor(kind),
                key,
                project,
                indexedFileScope,
                QinPsiFile.class);
        return indexedFiles.stream().anyMatch(file -> indexedFile.equals(file.getVirtualFile()));
    }
}
