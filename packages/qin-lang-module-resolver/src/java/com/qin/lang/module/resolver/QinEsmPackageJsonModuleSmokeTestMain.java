package com.qin.lang.module.resolver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmPackageJsonModuleSmokeTestMain {
    private QinEsmPackageJsonModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-json-module-");
        Path entry = root.resolve("main.ts");
        Path packageJson = root.resolve("package.json");
        Files.writeString(entry, """
                import { version } from "./package.json"
                export const currentVersion = version
                """, StandardCharsets.UTF_8);
        Files.writeString(packageJson, """
                {
                  "name": "json-module-smoke",
                  "version": "0.45.4"
                }
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinModuleSource jsonModule = graph.modules().stream()
                .filter(module -> module.file().equals(packageJson.toAbsolutePath().normalize()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JSON module is missing from graph"));
        require(jsonModule.source().contains("export default __qin_json;"), "JSON default export");
        require(jsonModule.source().contains("export const version = __qin_json[\"version\"]"),
                "package.json named version export");

        Files.writeString(packageJson, "{\"version\":1,}", StandardCharsets.UTF_8);
        expectGraphFailure(entry);

        System.out.println("QinEsmPackageJsonModuleSmokeTestMain OK");
    }

    private static void expectGraphFailure(Path entry) throws Exception {
        try {
            new QinModuleGraphBuilder().build(entry);
            throw new IllegalStateException("Malformed JSON module should fail graph construction");
        } catch (IllegalArgumentException expected) {
            // Invalid JSON must remain a visible module-graph error.
        }
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
