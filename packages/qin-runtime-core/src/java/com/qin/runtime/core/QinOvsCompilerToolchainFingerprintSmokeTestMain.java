package com.qin.runtime.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinOvsCompilerToolchainFingerprintSmokeTestMain {
    private QinOvsCompilerToolchainFingerprintSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-toolchain-fingerprint-");
        Path packageDir = root.resolve("local-ovs-compiler");
        Files.createDirectories(packageDir.resolve("src"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "ovs-compiler",
                  "version": "0.0.0",
                  "type": "module"
                }
                """, StandardCharsets.UTF_8);
        Path source = packageDir.resolve("src").resolve("index.ts");
        Files.writeString(source, "export const value = \"old\";\n", StandardCharsets.UTF_8);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  packageOverrides: {
                    "ovs-compiler": "./local-ovs-compiler"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinOvsCompiler compiler = new QinOvsCompiler();
        Method workspacePackagesMethod = QinOvsCompiler.class.getDeclaredMethod("indexWorkspaceToolchainPackages");
        workspacePackagesMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Path> workspacePackages = (Map<String, Path>) workspacePackagesMethod.invoke(compiler);
        require(workspacePackages.containsKey("@qin/generated-qin-parser-ts"),
                "OVS transform fingerprint must include the generated Qin parser TS package");
        require(workspacePackages.containsKey("slime-generator"),
                "OVS transform fingerprint must include slime-generator");
        require(workspacePackages.containsKey("slime-ast"),
                "OVS transform fingerprint must include slime-ast");

        Method fingerprintMethod = QinOvsCompiler.class.getDeclaredMethod(
                "transformToolchainFingerprint",
                Path.class,
                String.class);
        fingerprintMethod.setAccessible(true);
        String configSource = Files.readString(root.resolve("qin.config.js"), StandardCharsets.UTF_8);
        String first = (String) fingerprintMethod.invoke(compiler, root, configSource);
        int contentHashesAfterFirst = intField(compiler, "directoryDigestContentHashes");
        String second = (String) fingerprintMethod.invoke(compiler, root, configSource);
        int contentHashesAfterSecond = intField(compiler, "directoryDigestContentHashes");
        int cacheHitsAfterSecond = intField(compiler, "directoryDigestCacheHits");
        if (!first.equals(second)) {
            throw new IllegalStateException("OVS transform toolchain fingerprint must stay stable without source changes");
        }
        if (contentHashesAfterSecond != contentHashesAfterFirst) {
            throw new IllegalStateException("Unchanged toolchain fingerprint should reuse directory content hashes");
        }
        if (cacheHitsAfterSecond <= 0) {
            throw new IllegalStateException("Unchanged toolchain fingerprint should hit the in-process directory digest cache");
        }

        Files.writeString(source, "export const value = \"new\";\n", StandardCharsets.UTF_8);
        String afterToolchainChange = (String) fingerprintMethod.invoke(compiler, root, configSource);
        if (first.equals(afterToolchainChange)) {
            throw new IllegalStateException("OVS transform toolchain fingerprint must include local package content");
        }
        if (intField(compiler, "directoryDigestContentHashes") <= contentHashesAfterSecond) {
            throw new IllegalStateException("Changed toolchain package must invalidate the directory digest cache");
        }

        System.out.println("QinOvsCompilerToolchainFingerprintSmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static int intField(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.getInt(target);
    }
}
