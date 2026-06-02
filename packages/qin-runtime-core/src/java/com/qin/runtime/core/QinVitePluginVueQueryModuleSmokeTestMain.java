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
    private static final Pattern VIRTUAL_IMPORT = Pattern.compile(
            "from\\s+[\"']([^\"']*/__vite_virtual/[^\"']+)[\"']");

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

                function qinResolveMarkerPlugin() {
                  return {
                    name: 'qin-resolve-marker',
                    resolveId(id, importer) {
                      if (id === './dep.js' && String(importer).includes('Comp.vue')) {
                        return { id: 'virtual:qin-dep', meta: { marker: 'resolve-hook-ran' } }
                      }
                      if (id === 'virtual:qin-query-message') return id
                    },
                    load(id) {
                      if (id === 'virtual:qin-query-message') {
                        return 'export const message = "hello from query virtual module"'
                      }
                    }
                  }
                }

                function qinPreMarkerPlugin() {
                  return {
                    name: 'qin-pre-marker',
                    enforce: 'pre',
                    transform(code, id) {
                      const textId = String(id)
                      if (textId.includes('Comp.vue') && !textId.includes('?vue')) {
                        if (!String(code).includes('<template>')) {
                          this.error('pre plugin ran after plugin-vue transformed the SFC')
                        }
                        return String(code).replace('Query Works', 'Pre Works')
                      }
                    }
                  }
                }

                function qinMarkerPlugin() {
                  return {
                    name: 'qin-marker',
                    config() {
                      return { __qinConfigMarker: 'config-hook-ran' }
                    },
                    transform(code, id) {
                      const textId = String(id)
                      if (textId.includes('Comp.vue') && !textId.includes('?vue')) {
                        const resolved = this.resolve('./dep.js', id)
                        if (!resolved || resolved.id !== 'virtual:qin-dep') {
                          this.error('context.resolve did not use plugin resolveId: ' + JSON.stringify(resolved))
                        }
                        if (!resolved.meta || resolved.meta.marker !== 'resolve-hook-ran') {
                          this.error('context.resolve did not preserve resolveId metadata: ' + JSON.stringify(resolved))
                        }
                        this.addWatchFile(resolved.id)
                        const refId = this.emitFile({ type: 'asset', name: 'marker.txt', source: 'marker' })
                        if (refId !== 'qin-file-0') {
                          this.error('context.emitFile returned unexpected ref id: ' + refId)
                        }
                        this.warn('qin marker warning')
                        if (!this.getWatchFiles().includes('virtual:qin-dep')) {
                          this.error('context.addWatchFile did not record plugin-resolved id')
                        }
                      }
                      if (textId.includes('Comp.vue')) {
                        if (textId.includes('?vue&type=template')) {
                          return String(code)
                            .replace('Pre Works', 'Config Works')
                            + "\\nimport { message as qinQueryMessage } from 'virtual:qin-query-message'"
                            + "\\nexport const qinQueryVirtualMessage = qinQueryMessage"
                        }
                        return String(code).replace('Pre Works', 'Config Works')
                      }
                    }
                  }
                }

                export default {
                  plugins: [vue(), qinResolveMarkerPlugin(), qinPreMarkerPlugin(), qinMarkerPlugin()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section class="box">Query Works</section>
                </template>

                <script>
                export default {
                  name: 'CompSmoke',
                  data() {
                    return { fromScript: 'Script Works' }
                  }
                }
                </script>

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
        Matcher virtualMatcher = VIRTUAL_IMPORT.matcher(templateModule);
        if (!virtualMatcher.find()) {
            throw new IllegalStateException("Expected template query virtual import rewrite, got:\n"
                    + templateModule);
        }
        String queryVirtualModule = service.transpileByRequestPath(virtualMatcher.group(1));
        if (queryVirtualModule == null || !queryVirtualModule.contains("hello from query virtual module")) {
            throw new IllegalStateException("Expected query virtual module content, got:\n"
                    + queryVirtualModule);
        }

        String scriptModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js?vue&type=script&lang.js");
        if (scriptModule == null
                || !scriptModule.contains("CompSmoke")
                || !scriptModule.contains("Script Works")) {
            throw new IllegalStateException("Expected script query module from plugin-vue, got:\n"
                    + scriptModule);
        }

        System.out.println("QinVitePluginVueQueryModuleSmokeTestMain OK");
    }
}
