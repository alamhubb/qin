package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerWorkspacePackageIndexCacheSmokeTestMain {
    private QinJsPackageRunnerWorkspacePackageIndexCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-workspace-index-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "workspace-index-cache-smoke"
                }
                """, StandardCharsets.UTF_8);
        String source = """
                import * as glog from "glogjs"
                42
                """;

        QinJsPackageRunner runner = new QinJsPackageRunner();
        Object first = runner.runModuleSource(root, source, "workspace_package_index_cache_smoke");
        requireResult(first, "first");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Object second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = runner.runModuleSource(root, source, "workspace_package_index_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }
        requireResult(second, "second");

        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("workspace package index cache hit")) {
            throw new IllegalStateException("Expected workspace package index cache hit on same runner, got:\n" + log);
        }
        if (!log.contains("module-class compile cache hit")) {
            throw new IllegalStateException("Expected same-runner module-class cache hit, got:\n" + log);
        }
        System.out.println("QinJsPackageRunnerWorkspacePackageIndexCacheSmokeTestMain OK");
    }

    private static void requireResult(Object value, String label) {
        if (!(value instanceof Number number) || number.intValue() != 42) {
            throw new IllegalStateException("Unexpected " + label + " result: " + value);
        }
    }
}
