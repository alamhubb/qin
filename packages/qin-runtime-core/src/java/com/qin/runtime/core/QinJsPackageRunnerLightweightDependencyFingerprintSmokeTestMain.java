package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

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
        Path stalePackageDir = root.resolve("node_modules").resolve("stale-large-package");
        Files.createDirectories(stalePackageDir.resolve("huge"));
        Files.writeString(stalePackageDir.resolve("package.json"), """
                {
                  "name": "stale-large-package",
                  "version": "1.0.0",
                  "type": "module",
                  "main": "./huge/index.js"
                }
                """, StandardCharsets.UTF_8);
        for (int i = 0; i < 128; i++) {
            Files.writeString(
                    stalePackageDir.resolve("huge").resolve("file-" + i + ".js"),
                    "export const value" + i + " = " + i + ";\n",
                    StandardCharsets.UTF_8);
        }

        QinJsPackageRunner runner = new QinJsPackageRunner();
        String first = runner.moduleDependencyFingerprint(root.resolve("node_modules"), Set.of("large-package"));
        Files.writeString(packageDir.resolve("dist").resolve("nested").resolve("huge.js"), """
                export const value = "new";
                """, StandardCharsets.UTF_8);
        String afterDeepFileChange = runner.moduleDependencyFingerprint(
                root.resolve("node_modules"),
                Set.of("large-package"));
        if (!first.equals(afterDeepFileChange)) {
            throw new IllegalStateException(
                    "Dependency fingerprint must stay package-metadata scoped, not read every dependency file");
        }
        Files.writeString(stalePackageDir.resolve("huge").resolve("file-0.js"), """
                export const value0 = "changed but inactive";
                """, StandardCharsets.UTF_8);
        String afterInactivePackageChange = runner.moduleDependencyFingerprint(
                root.resolve("node_modules"),
                Set.of("large-package"));
        if (!first.equals(afterInactivePackageChange)) {
            throw new IllegalStateException("Inactive stale packages must not affect active dependency fingerprints");
        }

        Files.writeString(packageDir.resolve(".qin-package-sync.json"), """
                {"files":10000,"bytes":640000001,"modifiedMillis":2}
                """, StandardCharsets.UTF_8);
        String afterStampChange = runner.moduleDependencyFingerprint(root.resolve("node_modules"), Set.of("large-package"));
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
        String afterManifestChange = runner.moduleDependencyFingerprint(root.resolve("node_modules"), Set.of("large-package"));
        if (afterStampChange.equals(afterManifestChange)) {
            throw new IllegalStateException("Dependency fingerprint must include package manifests");
        }

        try {
            runner.moduleDependencyFingerprint(root.resolve("node_modules"), Set.of("stale-large-package"));
            throw new IllegalStateException("Active runtime packages without stamps must fail visibly");
        } catch (java.io.IOException expected) {
            if (!expected.getMessage().contains(".qin-package-sync.json")) {
                throw expected;
            }
        }

        Path shimDir = root.resolve("node_modules").resolve("qin-shim-package");
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "qin-shim-package",
                  "version": "0.0.0-qin-shim",
                  "type": "module",
                  "main": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.js"), """
                export const value = "old";
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve(".qin-package-sync.json"), """
                {"files":2,"bytes":120,"sha256":"old"}
                """, StandardCharsets.UTF_8);
        String shimFirst = runner.moduleDependencyFingerprint(
                root.resolve("node_modules"),
                Set.of("qin-shim-package"));
        Files.writeString(shimDir.resolve("index.js"), """
                export const value = "new";
                """, StandardCharsets.UTF_8);
        String afterShimSourceChangeWithoutStamp = runner.moduleDependencyFingerprint(
                root.resolve("node_modules"),
                Set.of("qin-shim-package"));
        if (!shimFirst.equals(afterShimSourceChangeWithoutStamp)) {
            throw new IllegalStateException("Runtime package fingerprints must use the canonical stamp, not tree scans");
        }
        Files.writeString(shimDir.resolve(".qin-package-sync.json"), """
                {"files":2,"bytes":120,"sha256":"new"}
                """, StandardCharsets.UTF_8);
        String afterShimStampChange = runner.moduleDependencyFingerprint(
                root.resolve("node_modules"),
                Set.of("qin-shim-package"));
        if (shimFirst.equals(afterShimStampChange)) {
            throw new IllegalStateException(
                    "Dependency fingerprint must include Qin shim package stamps");
        }

        System.out.println("QinJsPackageRunnerLightweightDependencyFingerprintSmokeTestMain OK");
    }
}
