package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinVitePluginVueCompilerSfcImportIsolationSmokeTestMain {
    private QinVitePluginVueCompilerSfcImportIsolationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-vite-vue-sfc-import-isolation-");
        Files.writeString(root.resolve("package.json"), """
                {
                  "type": "module",
                  "dependencies": {
                    "@vitejs/plugin-vue": "^6.0.1",
                    "@vue/compiler-sfc": "^3.5.34",
                    "vue": "^3.5.0"
                  }
                }
                """);

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import vue from "@vitejs/plugin-vue";
                import { parse } from "@vue/compiler-sfc";

                const plugins = [vue()];
                const parsed = parse("<script setup>\\nconst count = 1\\n</script><template><div>{{ count }}</div></template>", {
                  filename: "Smoke.vue"
                });

                ({
                  pluginName: plugins[0].name,
                  scriptSetup: parsed.descriptor.scriptSetup != null,
                  template: parsed.descriptor.template != null
                });
                """, "vite_vue_compiler_sfc_import_isolation");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result map, got: " + result);
        }
        if (!"vite:vue".equals(map.get("pluginName"))
                || !Boolean.TRUE.equals(map.get("scriptSetup"))
                || !Boolean.TRUE.equals(map.get("template"))) {
            throw new IllegalStateException("Unexpected import isolation result: " + map);
        }
        System.out.println("QinVitePluginVueCompilerSfcImportIsolationSmokeTestMain passed.");
    }
}
