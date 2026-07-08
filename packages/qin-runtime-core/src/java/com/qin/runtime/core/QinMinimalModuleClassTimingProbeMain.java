package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinMinimalModuleClassTimingProbeMain {
    private QinMinimalModuleClassTimingProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-minimal-module-class-");
        Path entry = root.resolve("entry.ts");
        Files.writeString(entry, "export const result = 1\n", StandardCharsets.UTF_8);

        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        runRound(runner, entry, root, "cold", "probe.QinMinimalModuleClassTimingProbeCold");
        runRound(runner, entry, root, "warm-no-cache", "probe.QinMinimalModuleClassTimingProbeWarm");
        runRound(runner, entry, root, "cache-hit", "probe.QinMinimalModuleClassTimingProbeWarm");
        System.out.println("QinMinimalModuleClassTimingProbeMain OK");
    }

    private static void runRound(
            QinInMemoryJvmRunner runner,
            Path entry,
            Path root,
            String label,
            String className) throws Exception {
        long started = System.nanoTime();
        Object result = runner.compileAndRunModuleClasses(entry, root, className);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        if (!(result instanceof Number number) || number.doubleValue() != 1.0d) {
            throw new IllegalStateException("Expected minimal module result 1, got " + result);
        }
        System.out.println("QinMinimalModuleClassTimingProbeMain " + label + " elapsedMs=" + elapsedMs);
    }
}
