package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies the main .vue request goes through plugin.load before plugin.transform.
 */
public final class QinVitePluginVueMainLoadSmokeTestMain {
    private QinVitePluginVueMainLoadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-vue-main-load-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:plugin-vue-main-load",
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

                function qinMainLoadPlugin() {
                  return {
                    name: 'qin-main-load',
                    load(id) {
                      if (String(id).includes('Comp.vue') && !String(id).includes('?vue')) {
                        return `
                          <template>
                            <section class="box">Loaded By Plugin</section>
                          </template>
                        `
                      }
                    }
                  }
                }

                export default {
                  plugins: [qinMainLoadPlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section class="box">File Source</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String module = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (module == null
                || !module.contains("Loaded By Plugin")
                || module.contains("File Source")) {
            throw new IllegalStateException("Expected main .vue load hook source to feed plugin-vue, got:\n"
                    + module);
        }

        System.out.println("QinVitePluginVueMainLoadSmokeTestMain OK");
    }
}
