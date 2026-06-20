package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerModuleClassDiskCacheSmokeTestMain {
    private QinJsPackageRunnerModuleClassDiskCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-module-cache-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"module-class-cache-smoke\" }\n",
                StandardCharsets.UTF_8);
        String source = """
                const value = 40
                value + 2
                """;

        Object first = new QinJsPackageRunner().runModuleSource(root, source, "module_class_disk_cache_smoke");
        if (!(first instanceof Number firstNumber) || firstNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected first result: " + first);
        }
        Path cacheDir = root.resolve(".qin/cache/jvm-module-classes");
        if (!Files.isDirectory(cacheDir) || countFiles(cacheDir) == 0) {
            throw new IllegalStateException("Expected module-class disk cache files under " + cacheDir);
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Object second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = new QinJsPackageRunner().runModuleSource(root, source, "module_class_disk_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }

        if (!(second instanceof Number secondNumber) || secondNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected second result: " + second);
        }
        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class disk cache hit")) {
            throw new IllegalStateException("Expected module-class disk cache hit, got:\n" + log);
        }
        System.out.println("QinJsPackageRunnerModuleClassDiskCacheSmokeTestMain OK");
    }

    private static long countFiles(Path root) throws Exception {
        try (var stream = Files.walk(root)) {
            return stream.filter(Files::isRegularFile).count();
        }
    }
}
