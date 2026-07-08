package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinInMemoryJvmRunnerTimeoutSmokeTestMain {
    private QinInMemoryJvmRunnerTimeoutSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String previous = System.getProperty("qin.runtime.jsRunTimeoutMs");
        System.setProperty("qin.runtime.jsRunTimeoutMs", "1000");
        try {
            Path root = Files.createTempDirectory("qin-runner-timeout-");
            Path source = root.resolve("timeout.js");
            Files.writeString(source, """
                    while (true) {
                    }
                    """, StandardCharsets.UTF_8);
            try {
                new QinInMemoryJvmRunner().compileAndRun(source, root, "com.qin.runtime.generated.TimeoutSmoke");
            } catch (IllegalStateException expected) {
                if (!expected.getMessage().contains("timed out")) {
                    throw expected;
                }
                System.out.println("QinInMemoryJvmRunnerTimeoutSmokeTestMain passed.");
                return;
            }
            throw new IllegalStateException("Expected Qin runtime timeout");
        } finally {
            if (previous == null) {
                System.clearProperty("qin.runtime.jsRunTimeoutMs");
            } else {
                System.setProperty("qin.runtime.jsRunTimeoutMs", previous);
            }
        }
    }
}
