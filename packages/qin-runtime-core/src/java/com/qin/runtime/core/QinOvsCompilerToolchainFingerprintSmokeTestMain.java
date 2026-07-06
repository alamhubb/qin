package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Method fingerprintMethod = QinOvsCompiler.class.getDeclaredMethod(
                "transformToolchainFingerprint",
                Path.class,
                String.class);
        fingerprintMethod.setAccessible(true);
        String configSource = Files.readString(root.resolve("qin.config.js"), StandardCharsets.UTF_8);
        String first = (String) fingerprintMethod.invoke(compiler, root, configSource);
        Files.writeString(source, "export const value = \"new\";\n", StandardCharsets.UTF_8);
        String afterToolchainChange = (String) fingerprintMethod.invoke(compiler, root, configSource);
        if (first.equals(afterToolchainChange)) {
            throw new IllegalStateException("OVS transform toolchain fingerprint must include local package content");
        }

        System.out.println("QinOvsCompilerToolchainFingerprintSmokeTestMain OK");
    }
}
