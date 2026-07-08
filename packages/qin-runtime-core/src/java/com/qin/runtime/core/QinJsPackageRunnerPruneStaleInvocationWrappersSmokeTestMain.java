package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerPruneStaleInvocationWrappersSmokeTestMain {
    private QinJsPackageRunnerPruneStaleInvocationWrappersSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path wrapperDir = Files.createTempDirectory("qin-js-package-runner-wrapper-prune-");
        Path active = wrapperDir.resolve("invoke-current-123.js");
        Path stale = wrapperDir.resolve("invoke-old-456.js");
        Path unrelated = wrapperDir.resolve("project-module.js");
        Files.writeString(active, "export const active = true;\n", StandardCharsets.UTF_8);
        Files.writeString(stale, "import 'slime-parser';\n", StandardCharsets.UTF_8);
        Files.writeString(unrelated, "export const keep = true;\n", StandardCharsets.UTF_8);

        QinJsPackageRunner runner = new QinJsPackageRunner();
        Method pruneMethod = QinJsPackageRunner.class.getDeclaredMethod(
                "pruneStaleInvocationWrappers",
                Path.class,
                Path.class);
        pruneMethod.setAccessible(true);
        pruneMethod.invoke(runner, wrapperDir, active);

        if (!Files.isRegularFile(active)) {
            throw new IllegalStateException("Active invocation wrapper should be kept");
        }
        if (Files.exists(stale)) {
            throw new IllegalStateException("Stale invocation wrapper should be deleted");
        }
        if (!Files.isRegularFile(unrelated)) {
            throw new IllegalStateException("Unrelated wrapper-dir file should not be deleted");
        }

        System.out.println("QinJsPackageRunnerPruneStaleInvocationWrappersSmokeTestMain OK");
    }
}
