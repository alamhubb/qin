package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinOvsCompilerStableBatchWrapperHotSessionSmokeTestMain {
    private QinOvsCompilerStableBatchWrapperHotSessionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-stable-batch-wrapper-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-ovs-stable-batch-wrapper\" }\n",
                StandardCharsets.UTF_8);
        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path firstFile = appDir.resolve("First.ovs");
        Path secondFile = appDir.resolve("Second.ovs");

        QinOvsCompiler compiler = new QinOvsCompiler();
        String firstMarkerA = "stable-batch-first-a-" + System.nanoTime();
        String firstMarkerB = "stable-batch-first-b-" + System.nanoTime();
        Map<Path, String> firstBatch = batch(firstFile, firstMarkerA, secondFile, firstMarkerB);
        writeBatch(firstBatch);
        Map<Path, QinOvsCompiler.QinOvsCompileResult> firstResults = compiler.compileAll(root, firstBatch);
        requireBatch(firstResults, firstFile, firstMarkerA, secondFile, firstMarkerB, "first");

        String secondMarkerA = "stable-batch-second-a-" + System.nanoTime();
        String secondMarkerB = "stable-batch-second-b-" + System.nanoTime();
        Map<Path, String> secondBatch = batch(firstFile, secondMarkerA, secondFile, secondMarkerB);
        writeBatch(secondBatch);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Map<Path, QinOvsCompiler.QinOvsCompileResult> secondResults;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            secondResults = compiler.compileAll(root, secondBatch);
        } finally {
            System.setOut(originalOut);
        }
        requireBatch(secondResults, firstFile, secondMarkerA, secondFile, secondMarkerB, "second");
        rejectStale(secondResults.get(firstFile), firstMarkerA, "first file");
        rejectStale(secondResults.get(secondFile), firstMarkerB, "second file");

        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class compile cache hit")) {
            throw new IllegalStateException("Expected stable batch wrapper module-class compile cache hit, got:\n" + log);
        }
        if (!log.contains("module-class dependency session hit")) {
            throw new IllegalStateException("Expected stable batch wrapper dependency session hit, got:\n" + log);
        }
        if (log.contains("transform disk cache hit")) {
            throw new IllegalStateException("Second distinct OVS batch should exercise hot wrapper, got:\n" + log);
        }

        System.out.println("QinOvsCompilerStableBatchWrapperHotSessionSmokeTestMain OK");
    }

    private static Map<Path, String> batch(Path firstFile, String firstMarker, Path secondFile, String secondMarker) {
        Map<Path, String> batch = new LinkedHashMap<>();
        batch.put(firstFile, source("stable-batch-one", firstMarker));
        batch.put(secondFile, source("stable-batch-two", secondMarker));
        return batch;
    }

    private static void writeBatch(Map<Path, String> batch) throws Exception {
        for (Map.Entry<Path, String> entry : batch.entrySet()) {
            Files.writeString(entry.getKey(), entry.getValue(), StandardCharsets.UTF_8);
        }
    }

    private static String source(String className, String marker) {
        return """
                div(class = "%s") {
                  "%s"
                }
                """.formatted(className, marker);
    }

    private static void requireBatch(
            Map<Path, QinOvsCompiler.QinOvsCompileResult> results,
            Path firstFile,
            String firstMarker,
            Path secondFile,
            String secondMarker,
            String label) {
        requireCode(results.get(firstFile), firstMarker, label + " first");
        requireCode(results.get(secondFile), secondMarker, label + " second");
    }

    private static void requireCode(QinOvsCompiler.QinOvsCompileResult result, String marker, String label) {
        if (result == null || !result.code().contains(marker) || !result.code().contains("defineOvsComponent")) {
            throw new IllegalStateException("Unexpected " + label + " OVS output:\n"
                    + (result == null ? "<null>" : result.code()));
        }
    }

    private static void rejectStale(QinOvsCompiler.QinOvsCompileResult result, String staleMarker, String label) {
        if (result != null && result.code().contains(staleMarker)) {
            throw new IllegalStateException("Stable OVS batch wrapper reused stale " + label + " output:\n"
                    + result.code());
        }
    }
}
