package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
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

        QinJsPackageRunner runner = new QinJsPackageRunner();
        Object first = runner.runModuleSource(root, source, "module_class_disk_cache_smoke");
        if (!(first instanceof Number firstNumber) || firstNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected first result: " + first);
        }
        Path cacheDir = moduleClassCacheRoot(runner, root).resolve(".qin/cache/jvm-module-classes");
        if (!Files.isDirectory(cacheDir) || countFiles(cacheDir) == 0) {
            throw new IllegalStateException("Expected module-class disk cache files under " + cacheDir);
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Object second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = runner.runModuleSource(root, source, "module_class_disk_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }

        if (!(second instanceof Number secondNumber) || secondNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected second result: " + second);
        }
        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class compile cache hit")) {
            throw new IllegalStateException("Expected same-runner module-class memory cache hit, got:\n" + log);
        }
        if (log.contains("module-class run start")) {
            throw new IllegalStateException("Module-class per-file trace should be opt-in, got:\n" + log);
        }

        captured.reset();
        Object third;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            third = new QinJsPackageRunner().runModuleSource(root, source, "module_class_disk_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }

        if (!(third instanceof Number thirdNumber) || thirdNumber.intValue() != 42) {
            throw new IllegalStateException("Unexpected third result: " + third);
        }
        log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class disk cache hit")) {
            throw new IllegalStateException("Expected cross-runner module-class disk cache hit, got:\n" + log);
        }
        System.out.println("QinJsPackageRunnerModuleClassDiskCacheSmokeTestMain OK");
    }

    private static long countFiles(Path root) throws Exception {
        try (var stream = Files.walk(root)) {
            return stream.filter(Files::isRegularFile).count();
        }
    }

    private static Path moduleClassCacheRoot(QinJsPackageRunner runner, Path projectRoot) throws Exception {
        Method method = QinJsPackageRunner.class.getDeclaredMethod("moduleClassCacheRoot", Path.class);
        method.setAccessible(true);
        return (Path) method.invoke(runner, projectRoot);
    }
}
