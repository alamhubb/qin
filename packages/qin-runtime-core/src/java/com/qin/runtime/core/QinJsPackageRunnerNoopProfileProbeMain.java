package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerNoopProfileProbeMain {
    private QinJsPackageRunnerNoopProfileProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }

        long started = System.nanoTime();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const started = Date.now();
                ({
                  ok: true,
                  elapsed: Date.now() - started
                });
                """, "noop_profile_probe");
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        System.out.println("QinJsPackageRunnerNoopProfileProbeMain result=" + result + ", elapsedMs=" + elapsedMs);
    }
}
