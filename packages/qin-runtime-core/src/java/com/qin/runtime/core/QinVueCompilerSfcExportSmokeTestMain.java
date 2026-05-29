package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinVueCompilerSfcExportSmokeTestMain {
    private QinVueCompilerSfcExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().normalize();
        Path entry = root.resolve(".qin").resolve("tmp-vue-sfc-export-smoke.js");
        Files.createDirectories(entry.getParent());
        Files.writeString(entry, """
                import { parse } from "@vue/compiler-sfc";
                export const ok = parse != null;
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        var model = new QinEsmSemanticAnalyzer().analyze(graph);
        var vueSfc = model.modules().values().stream()
                .filter(module -> module.sourceFile().toString().replace('\\', '/')
                        .endsWith("/node_modules/@vue/compiler-sfc/dist/compiler-sfc.esm-browser.js"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("compiler-sfc module was not in graph."));
        boolean hasParse = vueSfc.exports().stream()
                .anyMatch(exportBinding -> "parse".equals(exportBinding.exportName()));
        if (!hasParse) {
            String sample = vueSfc.exports().stream()
                    .limit(20)
                    .map(exportBinding -> exportBinding.exportName() + "<-" + exportBinding.localName())
                    .reduce("", (left, right) -> left.isBlank() ? right : left + ", " + right);
            throw new IllegalStateException(
                    "Expected @vue/compiler-sfc parse export. exportCount=" + vueSfc.exports().size()
                            + " sample=" + sample
                            + " module=" + vueSfc.sourceFile());
        }

        System.out.println("QinVueCompilerSfcExportSmokeTestMain passed.");
    }
}
