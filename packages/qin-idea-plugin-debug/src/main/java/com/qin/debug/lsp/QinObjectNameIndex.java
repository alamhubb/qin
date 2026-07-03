package com.qin.debug.lsp;

import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.DefaultFileTypeSpecificInputFilter;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.ID;
import com.intellij.util.indexing.ScalarIndexExtension;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public final class QinObjectNameIndex extends ScalarIndexExtension<String> {
    static final ID<String, Void> NAME = ID.create("com.qin.debug.lsp.objectName");
    private static final int VERSION = 1;
    private static final DataIndexer<String, Void, FileContent> INDEXER = inputData -> {
        Map<String, Void> names = new LinkedHashMap<>();
        for (String name : QinDeclarationScanner.objectNames(inputData.getContentAsText())) {
            names.put(name, null);
        }
        return names;
    };

    @Override
    public @NotNull ID<String, Void> getName() {
        return NAME;
    }

    @Override
    public @NotNull DataIndexer<String, Void, FileContent> getIndexer() {
        return INDEXER;
    }

    @Override
    public @NotNull KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public @NotNull FileBasedIndex.InputFilter getInputFilter() {
        return new DefaultFileTypeSpecificInputFilter(QinLspFileType.INSTANCE);
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }
}
