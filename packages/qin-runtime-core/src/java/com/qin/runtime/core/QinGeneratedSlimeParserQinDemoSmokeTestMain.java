package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

public final class QinGeneratedSlimeParserQinDemoSmokeTestMain {
    private QinGeneratedSlimeParserQinDemoSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().getParent().resolve("qin-ovs-cssts-qinjs-demo");
        require(Files.isRegularFile(root.resolve("qin.config.js")), "qin.config.js");
        require(!Files.exists(root.resolve("vite.config.js")), "no vite.config.js in Qin comparison demo");
        require(!Files.exists(root.resolve("package.json")), "no package.json in Qin comparison demo");

        Path entry = root.resolve("src").resolve("main.ts");
        QinFrontendEsmService service = QinFrontendEsmService.create(root, entry);
        QinGeneratedSlimeParserValidator validator = QinGeneratedSlimeParserValidator.usingDefaultBundle(root);

        SequencedMap<String, String> sources = new LinkedHashMap<>();
        sources.put("bootstrap", service.bootstrapJs());
        sources.put("representative qin vue ovs module", """
                import OvsWidget from "/@qin-mod/src/OvsWidget.ovs.js";
                import CsstsPanel from "/@qin-mod/src/CsstsPanel.vue.js";
                import tokenStyle from "/@qin-mod/src/tokens.cssts.js";
                const pageStyle = "cmp-page";
                export default {
                  setup() {
                    return { OvsWidget, CsstsPanel, tokenStyle, pageStyle };
                  }
                };
                """);
        validator.assertAllParse(root, sources);

        Path marker = root.resolve(".qin").resolve("generated-slime-parser-qin-smoke.txt");
        Files.createDirectories(marker.getParent());
        Files.writeString(
                marker,
                "Qin used Java-generated SlimeParser JS bundle to parse Vue/OVS/CSSTS output.\n",
                StandardCharsets.UTF_8);

        System.out.println("QinGeneratedSlimeParserQinDemoSmokeTestMain OK");
    }
    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
