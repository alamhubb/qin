package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Guards the standard OVS entry against reintroducing a JavaScript fallback. */
public final class QinOvsCompilerNoFallbackSmokeTestMain {
    private QinOvsCompilerNoFallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        assertJavaOnlySource("src/java/com/qin/runtime/core/QinOvsCompiler.java");
        assertJavaOnlySource("src/java/com/qin/runtime/core/QinOvsJavaCompiler.java");
        QinOvsCompiler.QinOvsCompileResult result = QinOvsJavaCompiler.compile("div { \"java-only\" }");
        if (!result.code().contains("$OvsHtmlTag.div") || result.code().contains("generated-runtime-adapter")) {
            throw new IllegalStateException("OVS Java entry did not produce a Java-native result");
        }
        System.out.println("QinOvsCompilerNoFallbackSmokeTestMain OK");
    }

    private static void assertJavaOnlySource(String relativePath) throws Exception {
        Path source = locateSource(relativePath);
        if (!Files.isRegularFile(source)) {
            throw new IllegalStateException("OVS compiler source not found: " + source);
        }
        String text = Files.readString(source, StandardCharsets.UTF_8);
        for (String forbidden : List.of(
                "QinJsPackageRunner",
                "QinInMemoryJvmRunner",
                "generated-runtime-adapter",
                "InterpretedFunction",
                "GraalJS",
                "org.graalvm.polyglot",
                "ScriptEngine")) {
            if (text.contains(forbidden)) {
                throw new IllegalStateException(relativePath + " contains a JavaScript fallback reference: " + forbidden);
            }
        }
    }

    private static Path locateSource(String relativePath) {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            Path direct = current.resolve(relativePath).normalize();
            if (Files.isRegularFile(direct)) return direct;
            Path workspacePath = current.resolve("qin/packages/qin-runtime-core").resolve(relativePath).normalize();
            if (Files.isRegularFile(workspacePath)) return workspacePath;
            current = current.getParent();
        }
        return Path.of(relativePath).toAbsolutePath().normalize();
    }
}
