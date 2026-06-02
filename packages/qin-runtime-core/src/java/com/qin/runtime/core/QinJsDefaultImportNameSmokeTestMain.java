package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsDefaultImportNameSmokeTestMain {
    private QinJsDefaultImportNameSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function appendDefaultNamesFromImports(lines, names) {
                  for (const line of lines) {
                    const text = String(line || "").trim();
                    if (!text.startsWith("import ")) continue;
                    const fromIndex = text.indexOf(" from ");
                    if (fromIndex < 0) continue;
                    let clause = text.slice(7, fromIndex).trim();
                    const comma = clause.indexOf(",");
                    if (comma >= 0) clause = clause.slice(0, comma).trim();
                    if (!clause || clause.startsWith("{") || clause.startsWith("*")) continue;
                    names.push(clause);
                  }
                }
                const names = ["count"];
                appendDefaultNamesFromImports(["import { ref } from 'vue'", "import logo from './logo.svg'"], names);
                names;
                """;
        Path root = Files.createTempDirectory("qin-js-default-import-name-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsDefaultImportNameSmoke");
        if (!(result instanceof List<?> list) || list.size() != 2
                || !"count".equals(list.get(0))
                || !"logo".equals(list.get(1))) {
            throw new IllegalStateException("Unexpected default import names: " + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsDefaultImportNameSmokeTestMain OK");
    }
}
