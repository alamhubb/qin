package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

/**
 * Verifies that Qin can host the real @vitejs/plugin-vue package lifecycle
 * without starting Vite or delegating to a Node/Vite process.
 */
public final class QinVitePluginVueLifecycleSmokeTestMain {
    private QinVitePluginVueLifecycleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import vuePlugin from "@vitejs/plugin-vue";

                const compiler = {
                  parse(source, options = {}) {
                    return {
                      descriptor: {
                        filename: options.filename || "App.vue",
                        source,
                        id: "qin-plugin-vue-smoke",
                        template: {
                          type: "template",
                          content: "<div>Hello</div>",
                          loc: { start: { line: 1, column: 1 } }
                        },
                        script: null,
                        scriptSetup: null,
                        styles: [],
                        customBlocks: []
                      },
                      errors: []
                    };
                  }
                };

                const plugin = vuePlugin({ compiler, sourceMap: false });
                const configPatch = plugin.config({ define: {}, build: {}, legacy: {} });
                plugin.configResolved({
                  root: ".",
                  command: "serve",
                  isProduction: false,
                  build: { sourcemap: false },
                  css: { devSourcemap: false },
                  define: {},
                  logger: { warn(message) {} }
                });
                plugin.options();
                plugin.buildStart();

                ({
                  name: plugin.name,
                  version: plugin.api.version,
                  hasTransform: typeof plugin.transform.handler === "function",
                  hasLoad: typeof plugin.load.handler === "function",
                  vueOptionsApi: configPatch.define.__VUE_OPTIONS_API__
                });
                """, "vite_plugin_vue_lifecycle");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected plugin-vue lifecycle result object, got: " + result);
        }
        requireEquals("vite:vue", map.get("name"), "plugin name");
        requireTruthy(map.get("version"), "plugin version");
        requireEquals(Boolean.TRUE, map.get("hasTransform"), "transform hook");
        requireEquals(Boolean.TRUE, map.get("hasLoad"), "load hook");
        requireEquals(Boolean.TRUE, map.get("vueOptionsApi"), "config define");
        System.out.println("QinVitePluginVueLifecycleSmokeTestMain OK");
    }

    private static void requireEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected " + label + " = " + expected + ", got: " + actual);
        }
    }

    private static void requireTruthy(Object value, String label) {
        if (value == null || String.valueOf(value).isBlank()) {
            throw new IllegalStateException("Expected non-empty " + label + ", got: " + value);
        }
    }
}
