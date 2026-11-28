package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsCompilerToolchainFingerprintSmokeTestMain {
    private QinCsstsCompilerToolchainFingerprintSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-java-fingerprint-");
        Path config = root.resolve("qin.config.js");
        Files.writeString(config, "export default { name: 'cssts-java' };\n", StandardCharsets.UTF_8);
        String source = "const style = css { colorRed }";
        QinCsstsCompiler compiler = new QinCsstsCompiler();
        QinCsstsCompiler.QinCsstsCompileResult defaultResult = compiler.compile(root, source);
        Files.writeString(config, "export default { name: 'cssts-java', classPrefix: 'unit' };\n", StandardCharsets.UTF_8);
        QinCsstsCompiler.QinCsstsCompileResult configuredResult = compiler.compile(root, source);
        if (!defaultResult.atomModule().contains("cssts_color_red")
                || !configuredResult.atomModule().contains("unit_color_red")
                || defaultResult.atomModule().equals(configuredResult.atomModule())) {
            throw new IllegalStateException("CSSTS cache identity/output must reflect project config changes");
        }
        Path javaSource = Path.of("src/java/com/qin/runtime/core/QinCsstsCompiler.java");
        String implementation = Files.readString(javaSource, StandardCharsets.UTF_8);
        if (implementation.contains("QinJsPackageRunner") || implementation.contains("cssts-compiler\";")) {
            throw new IllegalStateException("Standard CSSTS compiler must execute its Java implementation");
        }
        System.out.println("QinCsstsCompilerToolchainFingerprintSmokeTestMain OK");
    }
}
