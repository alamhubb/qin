package com.qin.debug.lsp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class QinLspFileType extends LanguageFileType {
    public static final QinLspFileType INSTANCE = new QinLspFileType();

    private QinLspFileType() {
        super(QinLanguage.INSTANCE);
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
}
