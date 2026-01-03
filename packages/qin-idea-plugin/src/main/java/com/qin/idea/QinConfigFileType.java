package com.qin.idea;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

/**
 * qin.config.json 文件类型
 */
public class QinConfigFileType implements FileType {
    public static final QinConfigFileType INSTANCE = new QinConfigFileType();

    @Override
    public @NotNull String getName() {
        return "Qin Config";
    }

    @Override
    public @NotNull String getDescription() {
        return "Qin 构建工具配置文件";
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
