package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinNativeVueFrontendSmokeTestMain {
    private QinNativeVueFrontendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-native-vue-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-native-vue-smoke\" }\n", StandardCharsets.UTF_8);
        Path packageDir = Files.createDirectories(
                root.resolve("node_modules").resolve("@vue").resolve("compiler-sfc").resolve("dist"));
        Files.writeString(packageDir.getParent().resolve("package.json"), """
                {
                  "name": "@vue/compiler-sfc",
                  "exports": {
                    ".": {
                      "import": "./dist/compiler-sfc.esm-browser.js",
                      "default": "./dist/compiler-sfc.esm-browser.js"
                    }
                  },
                  "module": "./dist/compiler-sfc.esm-browser.js",
                  "main": "./dist/compiler-sfc.cjs.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("compiler-sfc.esm-browser.js"), """
                export function parse(source, options) {
                  const filename = options?.filename || "Unknown.vue";
                  const hasCssts = source.includes('lang="cssts"') || source.includes("lang='cssts'")
                    || source.includes('lang="ccsts"') || source.includes("lang='ccsts'");
                  if (hasCssts) {
                    return {
                      descriptor: {
                        filename,
                        source,
                        template: {
                          content: "<button :class=\\"buttonBase, buttonHover\\">Hello Qin Vue</button>",
                          attrs: {}
                        },
                        script: null,
                        scriptSetup: {
                          content: "const buttonBase = css { colorRed, fontBold }\\nconst buttonHover = css { backgroundColorBlue }",
                          lang: "cssts",
                          attrs: { lang: "cssts" }
                        },
                        styles: [],
                        customBlocks: [],
                        errors: []
                      }
                    };
                  }
                  return {
                    descriptor: {
                      filename,
                      source,
                      template: {
                        content: "<button :class=\\"buttonBase, buttonHover\\">Hello Qin Vue</button>",
                        attrs: {}
                      },
                      script: null,
                      scriptSetup: {
                        content: "const buttonBase = \\"button-base\\"\\nconst buttonHover = { hovered: true }",
                        lang: null,
                        attrs: {}
                      },
                      styles: [],
                      customBlocks: [],
                      errors: []
                    }
                  };
                }
                """, StandardCharsets.UTF_8);
        Path appDir = Files.createDirectories(root.resolve("app"));
        Files.writeString(appDir.resolve("index.html"), """
                <!doctype html>
                <html>
                <body><div id="app"></div></body>
                </html>
                """, StandardCharsets.UTF_8);

        Files.writeString(appDir.resolve("App.vue"), """
                <template>
                  <button :class="buttonBase, buttonHover">Hello Qin Vue</button>
                </template>
                <script setup>
                const buttonBase = "button-base"
                const buttonHover = { hovered: true }
                </script>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, appDir.resolve("App.vue"));
        String bootstrap = service.bootstrapJs();
        if (!bootstrap.contains("/@qin-mod/app/App.vue.js")) {
            throw new IllegalStateException("Unexpected bootstrap js: " + bootstrap);
        }

        String transpiled = service.transpileByRequestPath("/@qin-mod/app/App.vue.js");
        if (transpiled == null || !transpiled.contains("__qin_vue_descriptor")) {
            throw new IllegalStateException("Native Vue module did not emit descriptor JS");
        }
        if (!transpiled.contains("export default")) {
            throw new IllegalStateException("Native Vue module did not export default descriptor");
        }
        if (!transpiled.contains("__qinNormalizeClass([buttonBase, buttonHover])")) {
            throw new IllegalStateException("Native Vue module did not compile class binding merge");
        }

        Files.writeString(appDir.resolve("Cssts.vue"), """
                <template>
                  <button :class="buttonBase, buttonHover">Hello Qin Vue</button>
                </template>
                <script setup lang="cssts">
                const buttonBase = css { colorRed, fontBold }
                const buttonHover = css { backgroundColorBlue }
                </script>
                """, StandardCharsets.UTF_8);
        QinFrontendEsmService csstsService = QinFrontendEsmService.create(root, appDir.resolve("Cssts.vue"));
        String csstsBootstrap = csstsService.bootstrapJs();
        String csstsRequestPath = extractBootstrapModulePath(csstsBootstrap);
        String csstsTranspiledOutput = csstsService.transpileByRequestPath(csstsRequestPath);
        if (csstsTranspiledOutput == null) {
            throw new IllegalStateException(
                    "Cssts Vue request path did not resolve. bootstrap="
                            + csstsBootstrap + " requestPath=" + csstsRequestPath);
        }
        if (!csstsTranspiledOutput.contains("__qin_vue_descriptor")) {
            throw new IllegalStateException("Cssts Vue module did not emit descriptor JS:\n" + csstsTranspiledOutput);
        }
        if (!csstsTranspiledOutput.contains("__qinNormalizeClass([buttonBase, buttonHover])")) {
            throw new IllegalStateException("Cssts Vue module did not compile class binding merge:\n" + csstsTranspiledOutput);
        }
        if (!csstsTranspiledOutput.contains("qin-vue-cssts=style")) {
            throw new IllegalStateException("Cssts Vue module did not rewrite css virtual import:\n" + csstsTranspiledOutput);
        }
        if (!csstsTranspiledOutput.contains("qin-vue-cssts=atom")) {
            throw new IllegalStateException("Cssts Vue module did not rewrite atom virtual import:\n" + csstsTranspiledOutput);
        }

        String csstsModuleBase = csstsRequestPath.endsWith(".js")
                ? csstsRequestPath
                : "/@qin-mod/app/Cssts.vue.js";
        String cssModule = csstsService.transpileByRequestPath(csstsModuleBase + "?qin-vue-cssts=style");
        if (cssModule == null || !cssModule.contains("data-qin-cssts") || !cssModule.contains("background-color")) {
            throw new IllegalStateException("Cssts Vue CSS virtual module did not expose injectable CSS:\n" + cssModule);
        }
        String atomModule = csstsService.transpileByRequestPath(csstsModuleBase + "?qin-vue-cssts=atom");
        if (atomModule == null || !atomModule.contains("csstsAtom") || !atomModule.contains("background-color")) {
            throw new IllegalStateException("Cssts Vue atom virtual module did not expose generated atoms:\n" + atomModule);
        }

        System.out.println("QinNativeVueFrontendSmokeTestMain passed.");
    }

    private static String extractBootstrapModulePath(String bootstrap) {
        if (bootstrap == null) {
            throw new IllegalArgumentException("bootstrap cannot be null");
        }
        int start = bootstrap.indexOf('"');
        int end = bootstrap.lastIndexOf('"');
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("Unexpected bootstrap js: " + bootstrap);
        }
        return bootstrap.substring(start + 1, end);
    }
}
