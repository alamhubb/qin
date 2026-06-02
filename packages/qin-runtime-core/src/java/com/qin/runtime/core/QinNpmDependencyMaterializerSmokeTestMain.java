package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies Qin can materialize npm dependencies declared in qin.config.js
 * without using npm, Node, or Vite.
 */
public final class QinNpmDependencyMaterializerSmokeTestMain {
    private QinNpmDependencyMaterializerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-npm-materializer-smoke-");
        Files.writeString(root.resolve("qin.config.js"), """
                {
                  "name": "qin-npm-materializer-smoke",
                  "dependencies": {
                    "cssts-ts": "0.2.87"
                  }
                }
                """);
        Path nodeModules = root.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules");
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);

        Path packageJson = nodeModules.resolve("cssts-ts").resolve("package.json");
        Path packageDir = nodeModules.resolve("cssts-ts");
        boolean hasRuntimeEntry = Files.isRegularFile(packageDir.resolve("dist").resolve("index.mjs"))
                || Files.isRegularFile(packageDir.resolve("src").resolve("index.ts"));
        if (!Files.isRegularFile(packageJson) || !hasRuntimeEntry) {
            throw new IllegalStateException("Expected cssts-ts to be materialized under " + nodeModules);
        }
        System.out.println("QinNpmDependencyMaterializerSmokeTestMain passed.");
    }
}

