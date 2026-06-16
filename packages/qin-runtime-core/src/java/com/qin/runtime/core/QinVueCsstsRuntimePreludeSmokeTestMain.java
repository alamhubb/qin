package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinVueCsstsRuntimePreludeSmokeTestMain {
    private QinVueCsstsRuntimePreludeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-vue-cssts-prelude-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-vue-cssts-prelude",
                  dependencies: {
                    "@vue/compiler-sfc": "latest",
                    "vue": "latest",
                    "cssts-compiler": "0.2.87",
                    "cssts-ts": "0.2.87"
                  }
                }
                """, StandardCharsets.UTF_8);
        Path app = Files.createDirectories(root.resolve("app"));
        Path vue = app.resolve("Panel.vue");
        Files.writeString(vue, """
                <script setup lang="cssts">
                const panelStyle = css { displayFlex, colorBlue }
                const buttonStyle$$hover = css { backgroundColorNavy }
                const buttonStyle = css { colorBlue, buttonStyle$$hover }
                </script>

                <template>
                  <section :class="panelStyle">hello</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, vue);
        String module = service.transpileByRequestPath("/@qin-mod/app/Panel.vue.js");
        if (module == null
                || !module.contains("?qin-vue-cssts=runtime")
                || !module.contains("?qin-vue-cssts=atom")
                || !module.contains("import { h as __qinVueH }")
                || !module.contains("render: __qinRenderVueComponent")
                || !module.contains("const { displayFlex, colorBlue, backgroundColorNavy } = __qinCsstsAtom")
                || !module.contains("cssts.merge(")) {
            throw new IllegalStateException("Vue lang=cssts module missing runtime/atom prelude:\n" + module);
        }
        if (module.contains("const __qin_vue_component = { ...__qin_vue_descriptor")) {
            throw new IllegalStateException("Vue lang=cssts default export must not expose descriptor template object:\n"
                    + module);
        }
        if (module.contains("import * as cssts")) {
            throw new IllegalStateException("Vue lang=cssts prelude must not shadow compiled cssts binding:\n" + module);
        }
        if (module.contains("const { displayFlex, colorBlue, buttonStyle$$hover } = __qinCsstsAtom")
                || module.contains("const { buttonStyle$$hover")) {
            throw new IllegalStateException("Vue lang=cssts prelude must not shadow generated local atoms:\n" + module);
        }

        String atom = service.transpileByRequestPath("/@qin-mod/app/Panel.vue.js?qin-vue-cssts=atom");
        if (atom == null || !atom.contains("displayFlex") || !atom.contains("colorBlue")) {
            throw new IllegalStateException("Vue lang=cssts atom module missing used atoms:\n" + atom);
        }

        String runtime = service.transpileByRequestPath("/@qin-mod/app/Panel.vue.js?qin-vue-cssts=runtime");
        if (runtime == null || !runtime.contains("function merge")) {
            throw new IllegalStateException("Vue lang=cssts runtime module missing cssts merge:\n" + runtime);
        }

        System.out.println("QinVueCsstsRuntimePreludeSmokeTestMain passed.");
    }
}
