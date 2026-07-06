package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerLightweightDependencyFingerprintSmokeTestMain {
    private QinJsPackageRunnerLightweightDependencyFingerprintSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-lightweight-fingerprint-");
        Path packageDir = root.resolve("node_modules").resolve("large-package");
        Files.createDirectories(packageDir.resolve("dist").resolve("nested"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "large-package",
                  "version": "1.0.0",
                  "type": "module",
                  "main": "./dist/index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve(".qin-package-sync.json"), """
                {"files":10000,"bytes":640000000,"modifiedMillis":1}
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("dist").resolve("nested").resolve("huge.js"), """
                export const value = "old";
                """, StandardCharsets.UTF_8);

        QinJsPackageRunner runner = new QinJsPackageRunner();
        String first = runner.moduleDependencyFingerprint(root.resolve("node_modules"));
        Files.writeString(packageDir.resolve("dist").resolve("nested").resolve("huge.js"), """
                export const value = "new";
                """, StandardCharsets.UTF_8);
        String afterDeepFileChange = runner.moduleDependencyFingerprint(root.resolve("node_modules"));
        if (!first.equals(afterDeepFileChange)) {
            throw new IllegalStateException(
                    "Dependency fingerprint must stay package-metadata scoped, not read every dependency file");
        }

        Files.writeString(packageDir.resolve(".qin-package-sync.json"), """
                {"files":10000,"bytes":640000001,"modifiedMillis":2}
                """, StandardCharsets.UTF_8);
        String afterStampChange = runner.moduleDependencyFingerprint(root.resolve("node_modules"));
        if (first.equals(afterStampChange)) {
            throw new IllegalStateException("Dependency fingerprint must include Qin package sync stamps");
        }

        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "large-package",
                  "version": "1.0.1",
                  "type": "module",
                  "main": "./dist/index.js"
                }
                """, StandardCharsets.UTF_8);
        String afterManifestChange = runner.moduleDependencyFingerprint(root.resolve("node_modules"));
        if (afterStampChange.equals(afterManifestChange)) {
            throw new IllegalStateException("Dependency fingerprint must include package manifests");
        }

        System.out.println("QinJsPackageRunnerLightweightDependencyFingerprintSmokeTestMain OK");
    }
}
