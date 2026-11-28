package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerToolchainFingerprintSmokeTestMain {
    private QinOvsCompilerToolchainFingerprintSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-java-fingerprint-");
        Path config = root.resolve("qin.config.js");
        Files.writeString(config, "export default { name: 'java-ovs' };\n", StandardCharsets.UTF_8);

        QinOvsCompiler compiler = new QinOvsCompiler();
        Path module = root.resolve("app/Panel.ovs");
        String source = "div { \"panel\" }";
        String before = compiler.frontendCacheIdentity(root, module, source);
        Files.writeString(config, "export default { name: 'java-ovs', classPrefix: 'unit' };\n", StandardCharsets.UTF_8);
        String afterConfigChange = compiler.frontendCacheIdentity(root, module, source);
        if (before.equals(afterConfigChange)) {
            throw new IllegalStateException("Java OVS cache identity must change when Qin/CSSTS configuration changes");
        }

        QinOvsCompiler.QinOvsCompileResult result = compiler.compile(root, module, source);
        if (!result.code().contains("$OvsHtmlTag.div") || !result.code().contains("panel")) {
            throw new IllegalStateException("Standard Qin OVS entry did not produce Java OVS output: " + result.code());
        }
        Method fingerprint = QinOvsCompiler.class.getDeclaredMethod("transformToolchainFingerprint", String.class);
        fingerprint.setAccessible(true);
        String digest = (String) fingerprint.invoke(compiler, Files.readString(config, StandardCharsets.UTF_8));
        if (digest == null || digest.isBlank()) {
            throw new IllegalStateException("Java OVS compiler fingerprint is empty");
        }
        System.out.println("QinOvsCompilerToolchainFingerprintSmokeTestMain OK");
    }
}
