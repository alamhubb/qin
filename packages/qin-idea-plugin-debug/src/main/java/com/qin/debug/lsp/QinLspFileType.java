package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class QinLspFileType implements FileType {
    public static final QinLspFileType INSTANCE = new QinLspFileType();

    private QinLspFileType() {
    }

    @Override
    public @NotNull String getName() {
        return "Qin";
    }

    @Override
    public @NotNull String getDescription() {
        return "Qin source";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "qin";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
