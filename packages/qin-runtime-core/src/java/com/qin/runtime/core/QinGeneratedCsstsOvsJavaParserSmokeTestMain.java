package com.qin.runtime.core;

import com.qin.parser.QinParserFacade;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinGeneratedCsstsOvsJavaParserSmokeTestMain {
    private QinGeneratedCsstsOvsJavaParserSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-generated-cssts-ovs-parser-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-generated-cssts-ovs-parser-smoke",
                  dependencies: {
                    "vue": "latest",
                    "cssts-compiler": "0.2.87",
                    "cssts-ts": "0.2.87",
                    "vite-plugin-ovs": "0.2.2",
                    "ovs-compiler": "0.2.2",
                    "ovsjs": "0.2.2"
                  }
                };
                """, StandardCharsets.UTF_8);

        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path vueFile = appDir.resolve("main.vue");
        Files.writeString(vueFile, """
                <template>
                  <main :class="pageStyle">
                    <OvsDemo />
                  </main>
                </template>

                <script setup lang="cssts">
                import OvsDemo from "./OvsDemo.ovs";

                const pageStyle = css { displayFlex, colorBlue, padding16px };
                </script>
                """, StandardCharsets.UTF_8);
        Files.writeString(appDir.resolve("OvsDemo.ovs"), """
                div(class = css { colorBlue, fontWeight700, padding16px }) {
                  "Qin parser sees generated OVS"
                }
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, vueFile);
        assertParses("bootstrap", service.bootstrapJs());
        assertParses("vue module", requireModule(service, "/@qin-mod/app/main.vue.js"));
        assertParses("ovs module", requireModule(service, "/@qin-mod/app/OvsDemo.ovs.js"));
        assertParses("global cssts css", requireModule(service, "/@qin-mod/__virtual/cssts.css.js"));
        assertParses("global cssts atom", requireModule(service, "/@qin-mod/__virtual/csstsAtom.js"));
        assertParses("global cssts runtime", requireModule(service, "/@qin-mod/__virtual/cssts-runtime.js"));
        assertParses("ovs css", requireModule(service, "/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=style"));
        assertParses("ovs atom", requireModule(service, "/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=atom"));
        assertParses("ovs runtime", requireModule(service, "/@qin-mod/app/OvsDemo.ovs.js?qin-ovs=runtime"));

        System.out.println("QinGeneratedCsstsOvsJavaParserSmokeTestMain passed.");
    }

    private static String requireModule(QinFrontendEsmService service, String requestPath) throws Exception {
        String module = service.transpileByRequestPath(requestPath);
        if (module == null || module.isBlank()) {
            throw new IllegalStateException("Expected generated module for " + requestPath);
        }
        return module;
    }

    private static void assertParses(String label, String source) {
        try {
            new QinParserFacade().parseSource(source);
        } catch (RuntimeException error) {
            throw new IllegalStateException("Java Qin/Slime parser failed on " + label + ":\n" + source, error);
        }
    }
}
