package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmIgnoresStringImportSmokeTestMain {
    private QinEsmIgnoresStringImportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-ignore-string-import-");
        Path entry = root.resolve("main.js");
        Files.writeString(entry, """
                const generated = `
                import { ${CSS_VARS_HELPER} as _${CSS_VARS_HELPER} } from 'vue'
                export default ${defaultVar}
                `;
                export const ok = generated.length > 0;
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        if (!graph.modules().getFirst().imports().isEmpty()) {
            throw new IllegalStateException("Template string import was incorrectly added to module graph.");
        }

        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        System.out.println("QinEsmIgnoresStringImportSmokeTestMain passed.");
    }
}
