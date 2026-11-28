package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Proves generated npm shims with template literals still expose later ESM exports.
 */
public final class QinEsmGeneratedShimBindingSmokeTestMain {
    private QinEsmGeneratedShimBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-generated-shim-");
        Path shim = root.resolve("plugin.mjs");
        Files.writeString(shim, """
                function parse(source) {
                  const quote = "`";
                  const marker = `source:${source}`;
                  if (quote === "`") return marker;
                  return "";
                }
                const __qinExportDefaultPrefix = String.fromCharCode(101, 120, 112, 111, 114, 116)
                  + " default ";
                function vuePlugin() {
                  return parse("ok");
                }
                export default vuePlugin;
                """, StandardCharsets.UTF_8);
        Path entry = root.resolve("main.mjs");
        Files.writeString(entry, """
                import vuePlugin from "./plugin.mjs";
                export default vuePlugin;
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        QinEsmModuleSemantic shimSemantic = model.modules().get(shim.toAbsolutePath().normalize());
        if (shimSemantic == null
                || shimSemantic.exports().stream()
                .noneMatch(binding -> !binding.typeOnly()
                        && QinEsmExportKind.LOCAL_DEFAULT.equals(binding.kind())
                        && "default".equals(binding.exportName()))) {
            throw new IllegalStateException("Generated shim default export was not collected");
        }
        System.out.println("QinEsmGeneratedShimBindingSmokeTestMain passed.");
    }
}
