package com.qin.debug;

import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

/**
 * qin.config.js 鏂囦欢绫诲瀷
 */
public class QinConfigFileType implements FileType {
    public static final QinConfigFileType INSTANCE = new QinConfigFileType();

    @Override
    public @NotNull String getName() {
        return "Qin Config";
    }

    @Override
    public @NotNull String getDescription() {
        return "Qin Build Tool Config";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "json";
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

