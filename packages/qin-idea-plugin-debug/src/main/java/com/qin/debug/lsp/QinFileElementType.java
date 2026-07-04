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
        List<QinSourceStructure.ObjectDeclaration> declarations = stub.objectDeclarations();
        dataStream.writeVarInt(declarations.size());
        for (QinSourceStructure.ObjectDeclaration declaration : declarations) {
            dataStream.writeName(declaration.name());
            writeNames(dataStream, declaration.memberNames(QinSourceStructure.ObjectMemberKind.FIELD));
            writeNames(dataStream, declaration.memberNames(QinSourceStructure.ObjectMemberKind.METHOD));
        }
    }

    @Override
    public @NotNull QinFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub)
            throws IOException {
        int count = dataStream.readVarInt();
        List<QinSourceStructure.ObjectDeclaration> declarations = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            String objectName = dataStream.readNameString();
            List<String> fields = readNames(dataStream);
            List<String> methods = readNames(dataStream);
            declarations.add(new QinSourceStructure.ObjectDeclaration(objectName, fields, methods));
        }
        return new QinFileStub(null, declarations);
    }

    @Override
    public void indexStub(@NotNull QinFileStub stub, @NotNull IndexSink sink) {
        for (QinSourceStructure.ObjectDeclaration declaration : stub.objectDeclarations()) {
            sink.occurrence(QinObjectNameStubIndex.KEY, declaration.name());
            for (QinSourceStructure.ObjectMemberIndexEntry member : declaration.memberIndexEntries()) {
                sink.occurrence(QinObjectMemberStubIndexes.keyFor(member.kind()), member.key());
            }
        }
    }

    @Override
    public boolean shouldBuildStubFor(@NotNull VirtualFile file) {
        return file.getFileType() == QinLspFileType.INSTANCE;
    }

    private static void writeNames(@NotNull StubOutputStream dataStream, @NotNull List<String> names)
            throws IOException {
        dataStream.writeVarInt(names.size());
        for (String name : names) {
            dataStream.writeName(name);
        }
    }

    private static @NotNull List<String> readNames(@NotNull StubInputStream dataStream) throws IOException {
        int count = dataStream.readVarInt();
        List<String> names = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            names.add(dataStream.readNameString());
        }
        return names;
    }
}
