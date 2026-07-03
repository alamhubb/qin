package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class QinFileElementType extends IStubFileElementType<QinFileStub> {
    static final QinFileElementType INSTANCE = new QinFileElementType();
    private static final int STUB_VERSION = 1;
    private static final String EXTERNAL_ID = "qin.FILE";
    private final StubBuilder builder = new QinFileStubBuilder();

    private QinFileElementType() {
        super("QIN_FILE", QinLanguage.INSTANCE);
    }

    @Override
    public int getStubVersion() {
        return STUB_VERSION;
    }

    @Override
    public @NotNull StubBuilder getBuilder() {
        return builder;
    }

    @Override
    public @NotNull String getExternalId() {
        return EXTERNAL_ID;
    }

    @Override
    public void serialize(@NotNull QinFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        List<String> objectNames = stub.objectNames();
        dataStream.writeVarInt(objectNames.size());
        for (String objectName : objectNames) {
            dataStream.writeName(objectName);
        }
    }

    @Override
    public @NotNull QinFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub)
            throws IOException {
        int count = dataStream.readVarInt();
        List<String> objectNames = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            objectNames.add(dataStream.readNameString());
        }
        return new QinFileStub(null, objectNames);
    }

    @Override
    public void indexStub(@NotNull QinFileStub stub, @NotNull IndexSink sink) {
        for (String objectName : stub.objectNames()) {
            sink.occurrence(QinObjectNameStubIndex.KEY, objectName);
        }
    }

    @Override
    public boolean shouldBuildStubFor(@NotNull VirtualFile file) {
        return file.getFileType() == QinLspFileType.INSTANCE;
    }
}
