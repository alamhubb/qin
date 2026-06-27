package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class CsstsLspFileType implements FileType {
    public static final CsstsLspFileType INSTANCE = new CsstsLspFileType();

    private CsstsLspFileType() {
    }

    @Override
    public @NotNull String getName() {
        return "CSSTS";
    }

    @Override
    public @NotNull String getDescription() {
        return "CSSTS source";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "cssts";
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
