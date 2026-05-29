package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for first-phase Array/Map/Set/Object/JSON builtins.
 */
public final class JsBuiltinCollectionsSmokeTestMain {
    private JsBuiltinCollectionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveJsBuiltinsCollectionsRoot();
        Path source = root.resolve("main/main.js").normalize();
        Path classOut = root.resolve("build/js-builtins-collections/classes");
        Path jsOut = root.resolve("build/js-builtins-collections/app.js");
        Files.createDirectories(classOut);

        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.JsBuiltinsCollectionsSmoke",
                classOut,
                jsOut,
                false);
        QinBuildResult result = new QinBuildCoordinator().build(request);
        if (result.classFile() == null || !Files.exists(result.classFile())) {
            throw new IllegalStateException("Expected JVM class output file.");
        }

        String output = invokeGenerated(request.classOutputDir(), request.className());
        String[] lines = output.lines().map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
        String[] expected = {
                "2",
                "2",
                "[1]",
                "qin",
                "true",
                "1",
                "true",
                "1",
                "[\"age\"]",
                "1"
        };
        if (lines.length != expected.length) {
            throw new IllegalStateException(
                    "Expected " + expected.length + " output lines, got " + lines.length + ": " + output);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(lines[i])) {
                throw new IllegalStateException(
                        "Line " + i + " mismatch, expected `" + expected[i] + "`, got `" + lines[i] + "`");
            }
        }

        System.out.println("JsBuiltinCollectionsSmokeTestMain passed.");
        System.out.println("class file: " + result.classFile().toAbsolutePath());
    }

    private static String invokeGenerated(Path classOutputDir, String className) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8);
                URLClassLoader loader = new URLClassLoader(
                        new URL[]{classOutputDir.toUri().toURL()},
                        JsBuiltinCollectionsSmokeTestMain.class.getClassLoader())) {
            System.setOut(capture);
            Class<?> generated = Class.forName(className, true, loader);
            generated.getMethod("run").invoke(null);
        } finally {
            System.setOut(originalOut);
        }
        return buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
    }
}
