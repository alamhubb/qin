package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class OvsLspFileType extends LanguageFileType {
    public static final OvsLspFileType INSTANCE = new OvsLspFileType();

    private OvsLspFileType() {
        super(OvsLanguage.INSTANCE);
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

}
