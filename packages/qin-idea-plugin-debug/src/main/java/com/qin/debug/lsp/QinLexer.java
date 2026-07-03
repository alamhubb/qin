package com.qin.debug.lsp;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class QinLexer extends LexerBase {
    private CharSequence buffer = "";
    private int endOffset;
    private List<QinLexicalToken> tokens = List.of();
    private int tokenIndex;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.endOffset = endOffset;
        this.tokens = QinLexicalScanner.scan(buffer, startOffset, endOffset);
        this.tokenIndex = 0;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public @Nullable IElementType getTokenType() {
        if (tokenIndex >= tokens.size()) {
            return null;
        }
        return tokens.get(tokenIndex).type();
    }

    @Override
    public int getTokenStart() {
        if (tokenIndex >= tokens.size()) {
            return endOffset;
        }
        return tokens.get(tokenIndex).startOffset();
    }

    @Override
    public int getTokenEnd() {
        if (tokenIndex >= tokens.size()) {
            return endOffset;
        }
        return tokens.get(tokenIndex).endOffset();
    }

    @Override
    public void advance() {
        tokenIndex++;
    }

    @Override
    public @NotNull CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}
