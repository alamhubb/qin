package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerStableModuleClassDiskCacheSmokeTestMain {
    private QinJsPackageRunnerStableModuleClassDiskCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const value = 41
                value + 1
                """;
        Object first = new QinJsPackageRunner().runModuleSource(
                createTempProjectRoot("first"),
                source,
                "stable_module_class_disk_cache_smoke");
        if (!(first instanceof Number firstNumber) || firstNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected first result: " + first);
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Object second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = new QinJsPackageRunner().runModuleSource(
                    createTempProjectRoot("second"),
                    source,
                    "stable_module_class_disk_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }

        if (!(second instanceof Number secondNumber) || secondNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected second result: " + second);
        }
        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class disk cache hit")) {
            throw new IllegalStateException("Expected stable module-class disk cache hit across temp roots, got:\n" + log);
        }
        System.out.println("QinJsPackageRunnerStableModuleClassDiskCacheSmokeTestMain OK");
    }

    private static Path createTempProjectRoot(String label) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-stable-cache-" + label + "-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "stable-module-class-cache-smoke"
                }
                """, StandardCharsets.UTF_8);
        return root;
    }
}
