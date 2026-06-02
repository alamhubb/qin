package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies Qin runs Vite configureServer return hooks before transforms.
 */
public final class QinVitePluginConfigureServerPostHookSmokeTestMain {
    private QinVitePluginConfigureServerPostHookSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-configure-post-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:configure-server-post-hook",
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

                function qinConfigurePostHookPlugin() {
                  let postHookRan = false
                  return {
                    name: 'qin-configure-post-hook',
                    configureServer(server) {
                      if (!server || !server.config || !server.moduleGraph || !server.ws) {
                        this.error('configureServer did not receive Qin server API')
                      }
                      return () => {
                        postHookRan = true
                        server.__qinConfigurePostHookRan = true
                      }
                    },
                    transform(code, id) {
                      if (String(id).includes('Comp.vue') && !String(id).includes('?vue')) {
                        if (!postHookRan) {
                          this.error('configureServer return hook did not run before transform')
                        }
                        return String(code).replace('Configure Post Works', 'Configure Post Hook Works')
                      }
                    }
                  }
                }

                export default {
                  plugins: [vue(), qinConfigurePostHookPlugin()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section>Configure Post Works</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (mainModule == null || !mainModule.contains("Configure Post Hook Works")) {
            throw new IllegalStateException("Expected configureServer return hook transform, got:\n" + mainModule);
        }

        System.out.println("QinVitePluginConfigureServerPostHookSmokeTestMain OK");
    }
}
