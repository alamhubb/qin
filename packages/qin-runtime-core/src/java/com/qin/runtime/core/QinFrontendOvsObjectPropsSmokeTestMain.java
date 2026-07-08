package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendOvsObjectPropsSmokeTestMain {
    private QinFrontendOvsObjectPropsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-ovs-object-props-");
        Path app = root.resolve("app");
        Files.createDirectories(app);
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-frontend-ovs-object-props\" }\n",
                StandardCharsets.UTF_8);
        Path sourceFile = app.resolve("SummaryGrid.ovs");
        String source = """
                export const SummaryGrid = () => {
                  return section(class = "summary-grid") {
                    StatCard({ label: "Root URLs", value: String(rows.value.length), description: "Deduplicated endpoints" })
                  }
                }
                """;
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, sourceFile, source);
        String module = result.code();
        if (module == null
                || !module.contains("export const SummaryGrid")
                || module.contains("type=Const")
                || !module.contains("summary-grid")
                || !module.contains("Root URLs")
                || !module.contains("Deduplicated endpoints")) {
            throw new IllegalStateException("OVS object props were not preserved in browser module:\n" + module);
        }

        System.out.println("QinFrontendOvsObjectPropsSmokeTestMain passed.");
    }
}
