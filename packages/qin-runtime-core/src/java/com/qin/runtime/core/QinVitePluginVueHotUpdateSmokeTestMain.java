package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Verifies Qin runs Vite plugin handleHotUpdate and captures server.ws.send payloads.
 */
public final class QinVitePluginVueHotUpdateSmokeTestMain {
    private QinVitePluginVueHotUpdateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-vue-hot-update-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:plugin-vue-hot-update",
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

                function qinHotUpdatePlugin() {
                  return {
                    name: 'qin-hot-update-marker',
                    handleHotUpdate(ctx) {
                      if (!ctx.file.includes('Comp.vue')) {
                        this.error('unexpected hot update file: ' + ctx.file)
                      }
                      if (!ctx.server || !ctx.server.ws || !ctx.server.moduleGraph) {
                        this.error('missing server hot update API')
                      }
                      const modules = ctx.server.moduleGraph.getModulesByFile(ctx.file)
                      if (!modules || modules.size !== 1) {
                        this.error('moduleGraph.getModulesByFile did not return module set')
                      }
                      const moduleByUrl = ctx.server.moduleGraph.getModuleByUrl(ctx.file + '?vue&type=script')
                      if (!moduleByUrl || moduleByUrl.file !== ctx.file) {
                        this.error('moduleGraph.getModuleByUrl did not strip query to file')
                      }
                      ctx.server.moduleGraph.invalidateModule(moduleByUrl)
                      if (!moduleByUrl.__qinInvalidated) {
                        this.error('moduleGraph.invalidateModule did not mark module')
                      }
                      const content = ctx.read()
                      if (!String(content).includes('Hot Update Read Marker')) {
                        this.error('ctx.read did not return changed file content: ' + content)
                      }
                      ctx.server.ws.send({
                        type: 'custom',
                        event: 'qin:hot-update-smoke',
                        data: { file: ctx.file, modules: ctx.modules.length }
                      })
                    }
                  }
                }

                export default {
                  plugins: [qinHotUpdatePlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Path component = src.resolve("Comp.vue");
        Files.writeString(component, """
                <template>
                  <section>Hot Update Read Marker</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        List<String> messages = service.collectViteHotUpdateMessages(List.of(component));
        String joined = String.join("\n", messages);
        if (!joined.contains("\"type\":\"custom\"")
                || !joined.contains("\"event\":\"qin:hot-update-smoke\"")
                || !joined.contains("Comp.vue")
                || !joined.contains("\"modules\":1")) {
            throw new IllegalStateException("Expected handleHotUpdate ws message, got:\n" + joined);
        }

        System.out.println("QinVitePluginVueHotUpdateSmokeTestMain OK");
    }
}
