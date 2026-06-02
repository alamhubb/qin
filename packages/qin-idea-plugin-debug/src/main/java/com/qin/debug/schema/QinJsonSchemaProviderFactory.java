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
 * Qin JSON Schema 鎻愪緵鑰呭伐鍘? * 涓?qin.config.js 鏂囦欢鎻愪緵 JSON Schema 鏀寔
 */
public class QinJsonSchemaProviderFactory implements JsonSchemaProviderFactory {

    @NotNull
    @Override
    public List<JsonSchemaFileProvider> getProviders(@NotNull Project project) {
        return Collections.singletonList(new QinConfigSchemaProvider());
    }

    /**
     * qin.config.js 鐨?Schema 鎻愪緵鑰?     */
    private static class QinConfigSchemaProvider implements JsonSchemaFileProvider {

        @Override
        public boolean isAvailable(@NotNull VirtualFile file) {
            return "qin.config.js".equals(file.getName());
        }

        @NotNull
        @Override
        public String getName() {
            return "Qin Configuration";
        }

        @Nullable
        @Override
        public VirtualFile getSchemaFile() {
            // 浠庢彃浠惰祫婧愪腑鍔犺浇 schema
            try {
                java.net.URL url = getClass().getClassLoader()
                    .getResource("schemas/qin.config.schema.json");
                if (url != null) {
                    return com.intellij.openapi.vfs.VfsUtil.findFileByURL(url);
                }
            } catch (Exception e) {
                // 蹇界暐
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

