package com.qin.debug.lsp;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

public final class QinObjectNameStubIndex extends StringStubIndexExtension<QinObjectNamePsiElement> {
    static final StubIndexKey<String, QinObjectNamePsiElement> KEY =
            StubIndexKey.createIndexKey("com.qin.debug.lsp.stub.objectName");

    @Override
    public @NotNull StubIndexKey<String, QinObjectNamePsiElement> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
