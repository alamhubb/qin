package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmNamedAliasExportSmokeTestMain {
    private QinEsmNamedAliasExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-named-alias-export-");
        Path dependency = root.resolve("dep.js");
        Files.writeString(dependency, """
                const MagicString = {};
                function parse$2() {
                  return "ok";
                }
                const version = "0.0.0";
                export { MagicString, parse$2 as parse, version };
                """, StandardCharsets.UTF_8);
        Path entry = root.resolve("main.js");
        Files.writeString(entry, """
                import { parse } from "./dep.js";
                export const result = parse();
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        System.out.println("QinEsmNamedAliasExportSmokeTestMain passed.");
    }
}
