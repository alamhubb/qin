package com.qin.debug.lsp;

import com.intellij.psi.TokenType;
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
import java.util.List;
import java.util.Map;

public final class QinObjectNameIndex extends ScalarIndexExtension<String> {
    static final ID<String, Void> NAME = ID.create("com.qin.debug.lsp.objectName");
    private static final int VERSION = 1;
    private static final DataIndexer<String, Void, FileContent> INDEXER = inputData -> {
        CharSequence content = inputData.getContentAsText();
        List<QinLexicalToken> tokens = QinLexicalScanner.scan(content, 0, content.length());
        Map<String, Void> names = new LinkedHashMap<>();
        for (int index = 0; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() == QinTokenTypes.KEYWORD
                    && "object".contentEquals(slice(content, token))) {
                QinLexicalToken nameToken = nextMeaningfulToken(tokens, index);
                if (nameToken != null && isIdentifierToken(nameToken)) {
                    names.put(slice(content, nameToken).toString(), null);
                }
            }
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

    private static boolean isIdentifierToken(@NotNull QinLexicalToken token) {
        return token.type() == QinTokenTypes.IDENTIFIER
                || token.type() == QinTokenTypes.CLASS_NAME
                || token.type() == QinTokenTypes.FUNCTION_IDENTIFIER;
    }

    private static QinLexicalToken nextMeaningfulToken(@NotNull List<QinLexicalToken> tokens, int tokenIndex) {
        for (int index = tokenIndex + 1; index < tokens.size(); index++) {
            QinLexicalToken token = tokens.get(index);
            if (token.type() != TokenType.WHITE_SPACE
                    && token.type() != QinTokenTypes.LINE_COMMENT
                    && token.type() != QinTokenTypes.BLOCK_COMMENT) {
                return token;
            }
        }
        return null;
    }

    private static @NotNull CharSequence slice(@NotNull CharSequence content, @NotNull QinLexicalToken token) {
        return content.subSequence(token.startOffset(), token.endOffset());
    }
}
