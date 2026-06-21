package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmTypescriptDeclarationImportSmokeTestMain {
    private QinEsmTypescriptDeclarationImportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-ts-declaration-import-");
        Files.writeString(root.resolve("dep.mjs"), """
                export const RuntimeValue = 42;
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("dep.d.mts"), """
                export interface DeclaredOnly {
                  value: number;
                }
                export type AliasOnly = string;
                export declare const RuntimeValue: number;
                """, StandardCharsets.UTF_8);
        Path entry = root.resolve("main.ts");
        Files.writeString(entry, """
                import { RuntimeValue, DeclaredOnly, AliasOnly } from "./dep.mjs";

                const value: DeclaredOnly = { value: RuntimeValue };
                const alias: AliasOnly = "ok";
                export const result = value.value + alias.length;
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        System.out.println("QinEsmTypescriptDeclarationImportSmokeTestMain OK");
    }
}
