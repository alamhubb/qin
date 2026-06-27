package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class OvsLspFileType implements FileType {
    public static final OvsLspFileType INSTANCE = new OvsLspFileType();

    private OvsLspFileType() {
    }

    @Override
    public @NotNull String getName() {
        return "OVS";
    }

    @Override
    public @NotNull String getDescription() {
        return "OVS source";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "ovs";
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
