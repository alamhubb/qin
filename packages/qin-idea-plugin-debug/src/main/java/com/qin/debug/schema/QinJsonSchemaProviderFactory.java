package com.qin.debug.schema;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider;
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory;
import com.jetbrains.jsonSchema.extension.SchemaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Qin JSON Schema 提供者工厂
 * 为 qin.config.json 文件提供 JSON Schema 支持
 */
public class QinJsonSchemaProviderFactory implements JsonSchemaProviderFactory {

    @NotNull
    @Override
    public List<JsonSchemaFileProvider> getProviders(@NotNull Project project) {
        return Collections.singletonList(new QinConfigSchemaProvider());
    }

    /**
     * qin.config.json 的 Schema 提供者
     */
    private static class QinConfigSchemaProvider implements JsonSchemaFileProvider {

        @Override
        public boolean isAvailable(@NotNull VirtualFile file) {
            return "qin.config.json".equals(file.getName());
        }

        @NotNull
        @Override
        public String getName() {
            return "Qin Configuration";
        }

        @Nullable
        @Override
        public VirtualFile getSchemaFile() {
            // 从插件资源中加载 schema
            try {
                java.net.URL url = getClass().getClassLoader()
                    .getResource("schemas/qin.config.schema.json");
                if (url != null) {
                    return com.intellij.openapi.vfs.VfsUtil.findFileByURL(url);
                }
            } catch (Exception e) {
                // 忽略
            }
            return null;
        }

        @NotNull
        @Override
        public SchemaType getSchemaType() {
            return SchemaType.embeddedSchema;
        }
    }
}
