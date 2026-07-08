package com.qin.lang.module.resolver;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinLinkedModuleClassSlotScopeSmokeTestMain {
    private QinLinkedModuleClassSlotScopeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-module-class-slot-scope-");
        for (int i = 0; i < 260; i++) {
            Files.writeString(root.resolve("dep" + i + ".ts"), "export const value" + i + " = " + i + ";\n");
        }
        Path classModule = root.resolve("class-module.ts");
        Files.writeString(classModule, """
                class GeneratedClass {
                  value(): any {
                    return 42;
                  }
                  declare(): any {
                    return this.value();
                  }
                }
                const GeneratedAlias = GeneratedClass;
                export { GeneratedAlias };
                """);
        Path entry = root.resolve("entry.ts");
        StringBuilder entrySource = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            entrySource.append("export { value")
                    .append(i)
                    .append(" } from './dep")
                    .append(i)
                    .append(".ts';\n");
        }
        entrySource.append("export { GeneratedAlias } from './class-module.ts';\n");
        Files.writeString(entry, entrySource.toString());

        QinLinkedModuleSource linked = new QinLinkedModuleSourceEmitter()
                .emit(new QinModuleGraphBuilder().build(entry));
        QinLinkedModuleSection classSection = linked.moduleSections().stream()
                .filter(section -> section.file().equals(classModule.toAbsolutePath().normalize()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class module section"));
        String classSource = classSection.classSource();
        int generatedClassIndex = classSource.indexOf("class GeneratedClass");
        if (generatedClassIndex < 0) {
            throw new IllegalStateException("Class module source lost class declaration:\n" + classSource);
        }
        int slotPrefixCount = countOccurrences(classSource.substring(0, generatedClassIndex), "__qin_global__(");
        if (slotPrefixCount > 2) {
            throw new IllegalStateException("Class module imported unrelated export slots: " + slotPrefixCount);
        }
        if (!classSource.contains("declare(): any")) {
            throw new IllegalStateException("Class member named declare was stripped as top-level declare:\n"
                    + classSource);
        }
        System.out.println("QinLinkedModuleClassSlotScopeSmokeTestMain OK");
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count++;
            index += needle.length();
        }
        return count;
    }
}
