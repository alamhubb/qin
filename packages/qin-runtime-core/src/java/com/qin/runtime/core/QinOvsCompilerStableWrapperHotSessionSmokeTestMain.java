package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerStableWrapperHotSessionSmokeTestMain {
    private QinOvsCompilerStableWrapperHotSessionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-stable-wrapper-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-ovs-stable-wrapper\" }\n",
                StandardCharsets.UTF_8);
        Path sourceFile = root.resolve("app").resolve("StableWrapper.ovs");
        Files.createDirectories(sourceFile.getParent());

        QinOvsCompiler compiler = new QinOvsCompiler();
        String firstMarker = "stable-wrapper-first-" + System.nanoTime();
        String firstSource = source(firstMarker);
        Files.writeString(sourceFile, firstSource, StandardCharsets.UTF_8);
        QinOvsCompiler.QinOvsCompileResult first = compiler.compile(root, sourceFile, firstSource);
        requireCode(first, firstMarker, "first");

        String secondMarker = "stable-wrapper-second-" + System.nanoTime();
        String secondSource = source(secondMarker);
        Files.writeString(sourceFile, secondSource, StandardCharsets.UTF_8);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        QinOvsCompiler.QinOvsCompileResult second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = compiler.compile(root, sourceFile, secondSource);
        } finally {
            System.setOut(originalOut);
        }
        requireCode(second, secondMarker, "second");
        if (second.code().contains(firstMarker)) {
            throw new IllegalStateException("Stable OVS wrapper reused stale source output:\n" + second.code());
        }

        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class compile cache hit")) {
            throw new IllegalStateException("Expected stable wrapper module-class compile cache hit, got:\n" + log);
        }
        if (!log.contains("module-class dependency session hit")) {
            throw new IllegalStateException("Expected stable wrapper dependency session hit, got:\n" + log);
        }
        if (log.contains("transform disk cache hit")) {
            throw new IllegalStateException("Second distinct OVS source should exercise hot wrapper, got:\n" + log);
        }

        System.out.println("QinOvsCompilerStableWrapperHotSessionSmokeTestMain OK");
    }

    private static String source(String marker) {
        return """
                div(class = "stable-wrapper") {
                  "%s"
                }
                """.formatted(marker);
    }

    private static void requireCode(QinOvsCompiler.QinOvsCompileResult result, String marker, String label) {
        if (result == null || !result.code().contains(marker) || !result.code().contains("defineOvsComponent")) {
            throw new IllegalStateException("Unexpected " + label + " OVS output:\n"
                    + (result == null ? "<null>" : result.code()));
        }
    }
}
