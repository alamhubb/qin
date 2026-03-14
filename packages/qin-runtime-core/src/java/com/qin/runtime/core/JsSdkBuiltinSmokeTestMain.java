package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for JS-style built-ins compiled through Qin JVM pipeline.
 */
public final class JsSdkBuiltinSmokeTestMain {
    private JsSdkBuiltinSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveJsSdkRoot();
        Path source = root.resolve("main/main.js").normalize();
        Path classOut = root.resolve("build/js-sdk-smoke/classes");
        Path jsOut = root.resolve("build/js-sdk-smoke/app.js");
        Files.createDirectories(classOut);

        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.JsSdkSmoke",
                classOut,
                jsOut,
                false);
        QinBuildResult result = new QinBuildCoordinator().build(request);
        if (result.classFile() == null || !Files.exists(result.classFile())) {
            throw new IllegalStateException("Expected JVM class output file.");
        }

        String output = invokeGenerated(request.classOutputDir(), request.className());
        String[] lines = output.lines().map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
        if (lines.length != 3) {
            throw new IllegalStateException("Expected 3 output lines, got " + lines.length + ": " + output);
        }
        if (!"1".equals(lines[0])) {
            throw new IllegalStateException("First line mismatch, expected 1, got: " + lines[0]);
        }
        double randomValue = Double.parseDouble(lines[1]);
        if (randomValue < 0.0 || randomValue >= 1.0) {
            throw new IllegalStateException("Math.random out of range: " + randomValue);
        }
        if (!"{\"age\":1}".equals(lines[2])) {
            throw new IllegalStateException("JSON.stringify output mismatch: " + lines[2]);
        }

        System.out.println("JsSdkBuiltinSmokeTestMain passed.");
        System.out.println("class file: " + result.classFile().toAbsolutePath());
    }

    private static String invokeGenerated(Path classOutputDir, String className) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8);
                URLClassLoader loader = new URLClassLoader(new URL[] {classOutputDir.toUri().toURL()},
                        JsSdkBuiltinSmokeTestMain.class.getClassLoader())) {
            System.setOut(capture);
            Class<?> generated = Class.forName(className, true, loader);
            generated.getMethod("run").invoke(null);
        } finally {
            System.setOut(originalOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
