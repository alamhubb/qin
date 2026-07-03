package com.qin.debug.lsp;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

public final class QinObjectMethodNameStubIndex extends StringStubIndexExtension<QinPsiFile> {
    static final StubIndexKey<String, QinPsiFile> KEY =
            StubIndexKey.createIndexKey("com.qin.debug.lsp.stub.objectMethodName");

    @Override
    public @NotNull StubIndexKey<String, QinPsiFile> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
