package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verifies imports emitted from plugin-vue can resolve/load Vite plugin virtual modules.
 */
public final class QinVitePluginVirtualModuleSmokeTestMain {
    private static final Pattern VIRTUAL_IMPORT = Pattern.compile(
            "from\\s+[\"']([^\"']*/__vite_virtual/[^\"']+)[\"']");

    private QinVitePluginVirtualModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-vite-virtual-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:plugin-vite-virtual",
                  "version": "0.1.0",
                  "frontend": {
                    "srcDir": "src",
                    "outDir": "dist",
                    "devPort": 19097
                  },
                  "dependencies": {
                    "@vitejs/plugin-vue": "^6.0.7",
                    "vite": "^8.0.13",
                    "@vue/compiler-sfc": "^3.5.34",
                    "vue": "^3.5.34"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("vite.config.js"), """
                import vue from '@vitejs/plugin-vue'

                function qinVirtualPlugin() {
                  return {
                    name: 'qin-virtual-module',
                    resolveId(id) {
                      if (id === 'virtual:qin-message') return id
                    },
                    load(id) {
                      if (id === 'virtual:qin-message') {
                        return 'export const message = "hello from plugin virtual module"'
                      }
                    }
                  }
                }

                export default {
                  plugins: [qinVirtualPlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <script setup>
                import { message } from 'virtual:qin-message'
                </script>

                <template>
                  <section>{{ message }}</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        Matcher matcher = VIRTUAL_IMPORT.matcher(mainModule);
        if (!matcher.find()) {
            throw new IllegalStateException("Expected virtual module import rewrite, got:\n" + mainModule);
        }
        String virtualRequest = matcher.group(1);
        String virtualModule = service.transpileByRequestPath(virtualRequest);
        if (virtualModule == null || !virtualModule.contains("hello from plugin virtual module")) {
            throw new IllegalStateException("Expected plugin load virtual module, got:\n" + virtualModule
                    + "\nrequest=" + virtualRequest);
        }

        System.out.println("QinVitePluginVirtualModuleSmokeTestMain OK");
    }
}
