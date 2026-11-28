package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/** Guards the standard OVS entry against reintroducing a JavaScript fallback. */
public final class QinOvsCompilerNoFallbackSmokeTestMain {
    private QinOvsCompilerNoFallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        assertJavaOnlySource("src/java/com/qin/runtime/core/QinOvsCompiler.java");
        assertJavaOnlySource("src/java/com/qin/runtime/core/QinOvsJavaCompiler.java");
        Path project = Files.createTempDirectory("qin-ovs-java-only-");
        var compiler = new QinOvsCompiler();
        var single = compiler.compile(project, project.resolve("Single.ovs"), "div { \"single\" }");
        assertRender(single, "$OvsHtmlTag.div", "single");

        Path heading = project.resolve("Heading.ovs");
        Path body = project.resolve("Body.ovs");
        var batch = compiler.compileAll(project, Map.of(heading, "h1 { \"heading\" }", body, "p { \"body\" }"));
        assertRender(batch.get(heading), "$OvsHtmlTag.h1", "heading");
        assertRender(batch.get(body), "$OvsHtmlTag.p", "body");
        System.out.println("QinOvsCompilerNoFallbackSmokeTestMain OK");
    }

    private static void assertRender(QinOvsCompiler.QinOvsCompileResult result, String tag, String content) {
        if (result == null || !result.code().contains(tag) || !result.code().contains(content)
                || result.mappings().isEmpty() || result.code().contains("generated-runtime-adapter")) {
            throw new AssertionError("OVS Java compile entry did not generate the expected mapped render: " + tag);
        }
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
