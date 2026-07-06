package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerSmokeTestMain {
    private QinOvsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-compiler-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-ovs-compiler-smoke\" }\n", StandardCharsets.UTF_8);
        Path sourceFile = root.resolve("app").resolve("AdminDashboard.ovs");
        Files.createDirectories(sourceFile.getParent());

        String source = """
                div(class = css { colorBlue, fontWeight700, padding12px }) {
                  "Hello OVS + CSSTS"
                }
                """;
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, sourceFile, source);
        if (!result.code().contains("defineOvsComponent")
                || !result.code().contains("$OvsHtmlTag")
                || !result.code().contains("cssts.merge")) {
            throw new IllegalStateException("OVS compiler output missed expected Vue/OVS/CSSTS code:\n" + result.code());
        }
        if (!result.css().contains("color: blue")
                && !result.atomModule().contains("fontWeight700")
                && !result.code().contains("fontWeight700")) {
            throw new IllegalStateException("OVS compiler did not emit expected CSSTS assets:\ncss:\n"
                    + result.css()
                    + "\natom:\n"
                    + result.atomModule());
        }
        String importedSource = """
                import { ref } from "vue"

                export const ImportedGrid = () => {
                  const count = ref(1)
                  return section({ class: "summary-grid" }) {
                    span { String(count.value) }
                  }
                }
                """;
        Path importedSourceFile = root.resolve("app").resolve("ImportedGrid.ovs");
        Files.writeString(importedSourceFile, importedSource, StandardCharsets.UTF_8);
        QinOvsCompiler.QinOvsCompileResult importedResult =
                new QinOvsCompiler().compile(root, importedSourceFile, importedSource);
        if (!importedResult.code().contains("import")
                || !importedResult.code().contains("ImportedGrid")
                || !importedResult.code().contains("$OvsHtmlTag")) {
            throw new IllegalStateException("OVS compiler failed import/export render transform:\n" + importedResult.code());
        }
        System.out.println("QinOvsCompilerSmokeTestMain passed.");
    }
}

