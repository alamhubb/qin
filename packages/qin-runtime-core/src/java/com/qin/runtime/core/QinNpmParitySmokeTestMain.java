package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Compares simple npm package execution results between Node and Qin JVM output.
 */
public final class QinNpmParitySmokeTestMain {
    private QinNpmParitySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveNpmBareRoot();
        verifyCase(root, "main/main.js", "miniPkgSideEffect");
        verifyCase(root, "main/default-export.js", "miniDefaultPkg");
        verifyCase(root, "main/named-export.js", "miniMathPkg");
        verifyCase(root, "main/flow-export.js", "miniFlowPkg");
        System.out.println("QinNpmParitySmokeTestMain passed.");
    }

    private static void verifyCase(Path root, String relativeSource, String suffix) throws Exception {
        Path source = root.resolve(relativeSource).normalize();
        String nodeOutput = normalizeRuntimeOutput(runNode(root, source));
        QinRunCapture qinCapture = runQin(root, source, suffix);
        String qinOutput = normalizeRuntimeOutput(qinCapture.consoleOutput());
        if (!nodeOutput.equals(qinOutput)) {
            throw new IllegalStateException(
                    "Node/Qin output mismatch for " + source.toAbsolutePath()
                            + "\nNODE:\n" + nodeOutput
                            + "\nQIN:\n" + qinOutput);
        }
        validateReturnShape(relativeSource, qinCapture.returnValue());
    }

    private static String runNode(Path root, Path source) throws Exception {
        Process process = new ProcessBuilder("node", source.toAbsolutePath().toString())
                .directory(root.toFile())
                .redirectErrorStream(true)
                .start();
        byte[] bytes = process.getInputStream().readAllBytes();
        int exit = process.waitFor();
        String output = new String(bytes, StandardCharsets.UTF_8).replace("\r\n", "\n").trim();
        if (exit != 0) {
            throw new IllegalStateException(
                    "Node execution failed for " + source.toAbsolutePath() + ":\n" + output);
        }
        return output;
    }

    private static QinRunCapture runQin(Path root, Path source, String suffix) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Object result;
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            result = new QinInMemoryJvmRunner().compileAndRun(
                    source,
                    root,
                    "com.qin.runtime.generated.npm." + suffix);
        } finally {
            System.setOut(originalOut);
        }
        return new QinRunCapture(
                buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n").trim(),
                result);
    }

    private static String renderValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> normalized = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                normalized.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return QinObjectJsonEncoder.toJson(normalized);
        }
        if (value instanceof List<?> list) {
            return QinObjectJsonEncoder.toJson(list);
        }
        return String.valueOf(value);
    }

    private static void validateReturnShape(String relativeSource, Object value) {
        if (relativeSource.endsWith("default-export.js")) {
            if (!(value instanceof Number number) || number.intValue() != 42) {
                throw new IllegalStateException("Expected default export result == 42, got: " + renderValue(value));
            }
            return;
        }
        if (relativeSource.endsWith("named-export.js")) {
            if (!(value instanceof Map<?, ?> map)) {
                throw new IllegalStateException("Expected named export result map, got: " + value);
            }
            Object sum = map.get("sum");
            Object product = map.get("product");
            if (!(sum instanceof Number sumNumber) || sumNumber.intValue() != 42) {
                throw new IllegalStateException("Expected sum == 42, got: " + renderValue(value));
            }
            if (!(product instanceof Number productNumber) || productNumber.intValue() != 42) {
                throw new IllegalStateException("Expected product == 42, got: " + renderValue(value));
            }
            return;
        }
        if (relativeSource.endsWith("flow-export.js")) {
            if (!(value instanceof Map<?, ?> map)) {
                throw new IllegalStateException("Expected flow export result map, got: " + value);
            }
            Object summary = map.get("summary");
            Object ok = map.get("ok");
            Object fail = map.get("fail");
            if (!"total=12; positives=2; negatives=1".equals(summary)) {
                throw new IllegalStateException("Unexpected summary: " + renderValue(value));
            }
            if (!(ok instanceof Number okNumber) || okNumber.intValue() != 42) {
                throw new IllegalStateException("Expected ok == 42, got: " + renderValue(value));
            }
            if (!"division by zero".equals(fail)) {
                throw new IllegalStateException("Unexpected fail message: " + renderValue(value));
            }
        }
    }

    private static String normalizeRuntimeOutput(String output) {
        StringBuilder normalized = new StringBuilder();
        String[] lines = output.replace("\r\n", "\n").split("\n");
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (normalized.length() > 0) {
                normalized.append('\n');
            }
            normalized.append(line);
        }
        return normalized.toString();
    }

    private record QinRunCapture(String consoleOutput, Object returnValue) {
    }
}
