package com.qin.debug.lsp;

import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

final class QinObjectMemberStubIndexes {
    private QinObjectMemberStubIndexes() {
    }

    static @NotNull StubIndexKey<String, QinPsiFile> keyFor(
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return kind == QinSourceStructure.ObjectMemberKind.FIELD
                ? QinObjectFieldNameStubIndex.KEY
                : QinObjectMethodNameStubIndex.KEY;
    }
}
