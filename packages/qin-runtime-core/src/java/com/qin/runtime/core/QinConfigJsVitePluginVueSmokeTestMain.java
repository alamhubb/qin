package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies qin.config.js can replace root package.json and vite.config.js for plugin-vue.
 */
public final class QinConfigJsVitePluginVueSmokeTestMain {
    private QinConfigJsVitePluginVueSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-config-js-vite-vue-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.js"), """
                import vue from '@vitejs/plugin-vue'

                function qinConfigJsMarkerPlugin() {
                  return {
                    name: 'qin-config-js-marker',
                    transform(code, id) {
                      if (String(id).includes('Comp.vue') && !String(id).includes('?vue')) {
                        return String(code).replace('Qin Config JS Works', 'Qin Config JS Plugin Works')
                      }
                    }
                  }
                }

                export default {
                  name: "com.qin.smoke:config-js-vite-vue",
                  version: "0.1.0",
                  frontend: {
                    srcDir: "src",
                    outDir: "dist",
                    devPort: 19097
                  },
                  dependencies: {
                    "vue": "^3.5.34",
                    "@vue/compiler-sfc": "^3.5.34"
                  },
                  devDependencies: {
                    "@vitejs/plugin-vue": "^6.0.7",
                    "vite": "^8.0.13"
                  },
                  plugins: [qinConfigJsMarkerPlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section>Qin Config JS Works</section>
                </template>
                """, StandardCharsets.UTF_8);

        if (Files.exists(root.resolve("package.json")) || Files.exists(root.resolve("vite.config.js"))) {
            throw new IllegalStateException("Smoke root should not contain package.json or vite.config.js");
        }
        if (!QinViteVuePluginCompiler.isEnabled(root)) {
            throw new IllegalStateException("qin.config.js did not enable Qin Vite plugin-vue compiler");
        }

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (mainModule == null || !mainModule.contains("Qin Config JS Plugin Works")) {
            throw new IllegalStateException("Expected qin.config.js plugin transform, got:\n" + mainModule);
        }
        Path materializedVite = root.resolve(".qin/runtime/npm-host/node_modules/vite/package.json");
        if (!Files.isRegularFile(materializedVite)) {
            throw new IllegalStateException("Expected devDependencies from qin.config.js to materialize vite");
        }

        System.out.println("QinConfigJsVitePluginVueSmokeTestMain OK");
    }
}
