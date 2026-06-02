package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verifies that Qin routes @vitejs/plugin-vue ?vue&type=* requests through
 * plugin load/transform hooks instead of only compiling the main .vue module.
 */
public final class QinVitePluginVueQueryModuleSmokeTestMain {
    private static final Pattern STYLE_IMPORT = Pattern.compile(
            "import(?:\\s+[^\"'\\n]+\\s+from)?\\s+[\"']([^\"']*\\?vue&type=style[^\"']*)[\"']");

    private QinVitePluginVueQueryModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-vue-query-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:plugin-vue-query",
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

                function qinMarkerPlugin() {
                  return {
                    name: 'qin-marker',
                    config() {
                      return { __qinConfigMarker: 'config-hook-ran' }
                    },
                    transform(code, id) {
                      if (String(id).includes('Comp.vue')) {
                        const resolved = this.resolve('./dep.js', id)
                        if (!resolved || !String(resolved.id).endsWith('/src/dep.js')) {
                          this.error('context.resolve did not resolve relative to importer: ' + JSON.stringify(resolved))
                        }
                        this.addWatchFile(resolved.id)
                        const refId = this.emitFile({ type: 'asset', name: 'marker.txt', source: 'marker' })
                        if (refId !== 'qin-file-0') {
                          this.error('context.emitFile returned unexpected ref id: ' + refId)
                        }
                        this.warn('qin marker warning')
                        if (!this.getWatchFiles().some(file => String(file).endsWith('/src/dep.js'))) {
                          this.error('context.addWatchFile did not record dep.js')
                        }
                        return String(code).replace('Query Works', 'Config Works')
                      }
                    }
                  }
                }

                export default {
                  plugins: [qinMarkerPlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section class="box">Query Works</section>
                </template>

                <style>
                .box { color: red; }
                </style>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (!mainModule.contains("Config Works")) {
            throw new IllegalStateException("Expected vite.config.js marker plugin to run before plugin-vue, got:\n"
                    + mainModule);
        }
        Matcher matcher = STYLE_IMPORT.matcher(mainModule);
        if (!matcher.find()) {
            throw new IllegalStateException("Expected plugin-vue main module to import style query, got:\n"
                    + mainModule);
        }
        String styleRequest = matcher.group(1);
        if (!styleRequest.startsWith("/@qin-mod/src/Comp.vue.js?vue&type=style")) {
            throw new IllegalStateException("Expected style query import to be rewritten to @qin-mod, got: "
                    + styleRequest + "\nModule:\n" + mainModule);
        }

        String styleModule = service.transpileByRequestPath(styleRequest);
        if (styleModule == null
                || !styleModule.contains("data-qin-vue-plugin")
                || !styleModule.contains(".box { color: red; }")
                || !styleModule.contains("export default css")) {
            throw new IllegalStateException("Expected style query module to be transformed through plugin-vue, got:\n"
                    + styleModule);
        }

        String templateModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js?vue&type=template");
        if (templateModule == null
                || !templateModule.contains("export function render")
                || !templateModule.contains("Config Works")) {
            throw new IllegalStateException("Expected template query module from plugin-vue, got:\n"
                    + templateModule);
        }

        System.out.println("QinVitePluginVueQueryModuleSmokeTestMain OK");
    }
}
