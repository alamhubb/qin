package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies plugins returned from a Vite config() hook join Qin's plugin container.
 */
public final class QinVitePluginConfigReturnedPluginsSmokeTestMain {
    private QinVitePluginConfigReturnedPluginsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-config-returned-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.js"), """
                {
                  "name": "com.qin.smoke:plugin-config-returned",
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

                function qinConfigInjector() {
                  return {
                    name: 'qin-config-injector',
                    config() {
                      return {
                        plugins: [{
                          name: 'qin-config-returned-transform',
                          configResolved(config) {
                            config.__qinConfigReturnedPluginResolved = true
                          },
                          buildStart() {
                            this.addWatchFile('virtual:config-returned-watch')
                          },
                          transform(code, id) {
                            if (String(id).includes('Comp.vue') && !String(id).includes('?vue')) {
                              return String(code).replace('Config Plugin Works', 'Config Returned Plugin Works')
                            }
                          }
                        }]
                      }
                    }
                  }
                }

                export default {
                  plugins: [vue(), qinConfigInjector()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section>Config Plugin Works</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (mainModule == null || !mainModule.contains("Config Returned Plugin Works")) {
            throw new IllegalStateException("Expected config-returned plugin transform to run, got:\n" + mainModule);
        }

        System.out.println("QinVitePluginConfigReturnedPluginsSmokeTestMain OK");
    }
}

