package com.qin.lang.module.resolver;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinLinkedModuleSectionsSmokeTestMain {
    private QinLinkedModuleSectionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-linked-module-sections-");
        Path dep = root.resolve("dep.ts");
        Path entry = root.resolve("entry.ts");
        Files.writeString(dep, """
                export abstract class Base {
                    protected abstract pending: boolean
                    protected abstract makeValue(
                        input: number
                    ): number
                    public value() {
                        return 41;
                    }
                }
                export const value = 41;
                """);
        Files.writeString(entry, "import { value } from './dep.ts';\nconst result = value + 1;\n");

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinLinkedModuleSource linked = new QinLinkedModuleSourceEmitter().emit(graph);

        if (linked.moduleSections().size() != 2) {
            throw new IllegalStateException("Expected two module sections, got "
                    + linked.moduleSections().size());
        }
        QinLinkedModuleSection depSection = linked.moduleSections().get(0);
        QinLinkedModuleSection entrySection = linked.moduleSections().get(1);
        if (!depSection.file().equals(dep.toAbsolutePath().normalize())) {
            throw new IllegalStateException("Expected dep section first, got " + depSection.file());
        }
        if (!entrySection.file().equals(entry.toAbsolutePath().normalize())) {
            throw new IllegalStateException("Expected entry section second, got " + entrySection.file());
        }
        if (!depSection.source().contains("__qesm_m0_e_value")) {
            throw new IllegalStateException("Dep section did not initialize its export slot:\n"
                    + depSection.source());
        }
        if (depSection.classSource().contains("abstract pending")
                || depSection.classSource().contains("abstract makeValue")) {
            throw new IllegalStateException("Dep section still contains type-only abstract class members:\n"
                    + depSection.classSource());
        }
        if (!depSection.classSource().contains("public value()")) {
            throw new IllegalStateException("Dep section removed a concrete class member:\n"
                    + depSection.classSource());
        }
        if (!entrySection.source().contains("const value = __qin_export_get__(__qesm_m0_e_value)")) {
            throw new IllegalStateException("Entry section did not include import alias:\n"
                    + entrySection.source());
        }
        if (!linked.moduleInitializerSource().contains("__qin_bind_global__(\"__qesm_m0_e_value\"")) {
            throw new IllegalStateException("Module initializer did not bind export slot globally:\n"
                    + linked.moduleInitializerSource());
        }
        if (!entrySection.classSource().contains(
                "const __qesm_m0_e_value = __qin_global__(\"__qesm_m0_e_value\")")) {
            throw new IllegalStateException("Entry class source did not declare global export slot access:\n"
                    + entrySection.classSource());
        }
        if (!linked.source().contains(depSection.source()) || !linked.source().contains(entrySection.source())) {
            throw new IllegalStateException("Linked source no longer contains all module sections");
        }

        Path defaultRoot = Files.createTempDirectory("qin-linked-default-function-");
        Path plugin = defaultRoot.resolve("plugin.ts");
        Path defaultEntry = defaultRoot.resolve("entry.ts");
        Files.writeString(plugin, """
                type Plugin = { name: string }
                export default function cssTsPlugin(options: Plugin = {}): Plugin {
                    return options;
                }
                """);
        Files.writeString(defaultEntry, "import cssTsPlugin from './plugin.ts';\nconst plugin = cssTsPlugin;\n");

        QinLinkedModuleSource defaultLinked = new QinLinkedModuleSourceEmitter()
                .emit(new QinModuleGraphBuilder().build(defaultEntry));
        QinLinkedModuleSection pluginSection = defaultLinked.moduleSections().get(0);
        String pluginClassSource = pluginSection.classSource();
        if (!pluginClassSource.contains("function cssTsPlugin(options: Plugin = {}): Plugin {")) {
            throw new IllegalStateException("Default function signature was corrupted:\n" + pluginClassSource);
        }
        int returnIndex = pluginClassSource.indexOf("return options;");
        int defaultAssignIndex = pluginClassSource.indexOf("const __qesm_m0_default_local = cssTsPlugin;");
        if (returnIndex < 0 || defaultAssignIndex < returnIndex) {
            throw new IllegalStateException("Default function export initializer was inserted inside the body/header:\n"
                    + pluginClassSource);
        }

        System.out.println("QinLinkedModuleSectionsSmokeTestMain OK");
    }
}
