package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public final class QinEsmAstBindingCollectorSmokeTestMain {
    private QinEsmAstBindingCollectorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-ast-binding-collector-");
        Files.writeString(root.resolve("dep.js"), """
                export const named = 41;
                export type ValueType = { value: number };
                export default function depDefault() {
                  return named;
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("side.js"), """
                export const touched = true;
                """, StandardCharsets.UTF_8);
        Path entry = root.resolve("main.js");
        Files.writeString(entry, """
                const ignoredImport = "import { bad } from './missing.js'";
                const ignoredExport = `export { alsoBad } from './missing.js'`;
                // import { commentBad } from './missing.js'
                /* export * from './missing.js' */
                import type { ValueType } from "./dep.js";
                import { named as mixedAlias, type ValueType as MixedValueType } from "./dep.js";
                import depDefault, { named as alias } from "./dep.js";
                import * as depNamespace from "./dep.js";
                import "./side.js";
                export type { ValueType } from "./dep.js";
                export const localValue = alias;
                export { alias as aliasOut };
                export { named as forwarded, type ValueType as ForwardedValueType } from "./dep.js";
                export * as depBag from "./dep.js";
                export * from "./dep.js";
                export default function main() {
                  return depDefault() + depNamespace.named + mixedAlias;
                }
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        QinEsmModuleSemantic semantic = model.modules().get(entry.toAbsolutePath().normalize());
        if (semantic == null) {
            throw new IllegalStateException("Entry semantic missing");
        }
        EnumSet<QinEsmImportKind> importKinds = EnumSet.noneOf(QinEsmImportKind.class);
        semantic.imports().forEach(binding -> {
            importKinds.add(binding.kind());
            if (binding.moduleSpecifier().contains("missing")) {
                throw new IllegalStateException("String/comment import leaked into semantic model: " + binding);
            }
            if ("ValueType".equals(binding.localName()) || "ValueType".equals(binding.importedName())) {
                throw new IllegalStateException("Type-only import leaked into runtime imports: " + binding);
            }
            if ("MixedValueType".equals(binding.localName()) || "MixedValueType".equals(binding.importedName())) {
                throw new IllegalStateException("Mixed type-only import leaked into runtime imports: " + binding);
            }
        });
        require(importKinds.contains(QinEsmImportKind.DEFAULT), "default import missing");
        require(importKinds.contains(QinEsmImportKind.NAMED), "named import missing");
        require(importKinds.contains(QinEsmImportKind.NAMESPACE), "namespace import missing");
        require(importKinds.contains(QinEsmImportKind.SIDE_EFFECT), "side-effect import missing");

        EnumSet<QinEsmExportKind> exportKinds = EnumSet.noneOf(QinEsmExportKind.class);
        semantic.exports().forEach(binding -> {
            exportKinds.add(binding.kind());
            if (binding.moduleSpecifier() != null && binding.moduleSpecifier().contains("missing")) {
                throw new IllegalStateException("String/comment export leaked into semantic model: " + binding);
            }
        });
        require(semantic.exports().stream()
                        .anyMatch(binding -> binding.typeOnly() && "ValueType".equals(binding.exportName())),
                "type-only export missing");
        require(semantic.exports().stream()
                        .anyMatch(binding -> binding.typeOnly() && "ForwardedValueType".equals(binding.exportName())),
                "mixed type-only export missing");
        require(semantic.exports().stream()
                        .anyMatch(binding -> !binding.typeOnly() && "forwarded".equals(binding.exportName())),
                "mixed runtime re-export missing");
        require(exportKinds.contains(QinEsmExportKind.LOCAL_NAMED), "local named export missing");
        require(exportKinds.contains(QinEsmExportKind.LOCAL_DEFAULT), "default export missing");
        require(exportKinds.contains(QinEsmExportKind.RE_EXPORT_NAMED), "named re-export missing");
        require(exportKinds.contains(QinEsmExportKind.RE_EXPORT_ALL), "export-all missing");
        require(exportKinds.contains(QinEsmExportKind.RE_EXPORT_NAMESPACE), "namespace re-export missing");

        System.out.println("QinEsmAstBindingCollectorSmokeTestMain passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
