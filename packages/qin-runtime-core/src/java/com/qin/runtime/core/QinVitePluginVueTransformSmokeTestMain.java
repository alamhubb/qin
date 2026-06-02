package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

/**
 * Verifies that Qin can execute the real @vitejs/plugin-vue transform hook
 * for a Vue SFC without starting Vite or delegating to a Node/Vite dev server.
 */
public final class QinVitePluginVueTransformSmokeTestMain {
    private QinVitePluginVueTransformSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import vuePlugin from "@vitejs/plugin-vue";

                const source = `
                <script setup>
                import { ref } from 'vue'
                const count = ref(0)
                </script>

                <template>
                  <button type="button" @click="count++">Count is {{ count }}</button>
                </template>
                `;

                console.log("[smoke] create plugin");
                const plugin = vuePlugin({ sourceMap: false });
                const config = {
                  root: ".",
                  command: "serve",
                  isProduction: false,
                  build: { sourcemap: false },
                  css: { devSourcemap: false },
                  define: {},
                  logger: { warn(message) {} }
                };
                const ctx = {
                  parse(code) {
                    return {};
                  },
                  addWatchFile(file) {},
                  emitFile(file) {},
                  warn(message) {},
                  error(message) {
                    throw message;
                  }
                };
                const server = {
                  config,
                  watcher: { on(event, handler) {} },
                  moduleGraph: {
                    getModuleById(id) { return null; },
                    invalidateModule(module) {}
                  }
                };
                console.log("[smoke] configResolved");
                plugin.configResolved(config);
                if (plugin.configureServer) plugin.configureServer(server);
                console.log("[smoke] buildStart");
                plugin.buildStart.call(ctx);
                console.log("[smoke] transform start");
                let transformed = plugin.transform.handler.call(ctx, source, "App.vue");
                if (transformed && transformed.then) {
                  transformed.then(result => { transformed = result; });
                }
                console.log("[smoke] transform done");
                ({
                  code: typeof transformed === "string" ? transformed : transformed.code,
                  map: typeof transformed === "string" ? null : transformed.map,
                  transformedType: typeof transformed,
                  transformedKeys: transformed ? Object.keys(transformed).join(",") : "null",
                  hasThen: !!(transformed && transformed.then)
                });
                """, "vite_plugin_vue_transform");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected plugin-vue transform result object, got: " + result);
        }
        Object code = map.get("code");
        if (!(code instanceof String text) || text.isBlank()) {
            throw new IllegalStateException("Expected non-empty plugin-vue transform code, got: " + code
                    + "; result=" + map);
        }
        if (!text.contains("function _sfc_render") || !text.contains("_export_sfc")) {
            throw new IllegalStateException("Expected plugin-vue SFC entry output, got:\n" + text);
        }
        System.out.println("QinVitePluginVueTransformSmokeTestMain OK");
    }
}
