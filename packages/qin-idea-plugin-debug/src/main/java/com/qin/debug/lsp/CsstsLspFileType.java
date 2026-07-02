package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class CsstsLspFileType extends LanguageFileType {
    public static final CsstsLspFileType INSTANCE = new CsstsLspFileType();

    private CsstsLspFileType() {
        super(CsstsLanguage.INSTANCE);
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

}
