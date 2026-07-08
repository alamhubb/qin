package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class QinJsPackageRunnerPruneStaleMaterializedPackagesSmokeTestMain {
    private QinJsPackageRunnerPruneStaleMaterializedPackagesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path nodeModules = Files.createTempDirectory("qin-js-package-runner-prune-");

        Path stale = nodeModules.resolve("slime-parser");
        Files.createDirectories(stale);
        Files.writeString(stale.resolve("package.json"), "{\"name\":\"slime-parser\"}\n", StandardCharsets.UTF_8);
        Files.writeString(stale.resolve(".qin-package-sync.json"), "{}\n", StandardCharsets.UTF_8);

        Path active = nodeModules.resolve("ovs-compiler");
        Files.createDirectories(active);
        Files.writeString(active.resolve("package.json"), "{\"name\":\"ovs-compiler\"}\n", StandardCharsets.UTF_8);
        Files.writeString(active.resolve(".qin-package-sync.json"), "{}\n", StandardCharsets.UTF_8);

        Path remote = nodeModules.resolve("vue");
        Files.createDirectories(remote);
        Files.writeString(remote.resolve("package.json"), "{\"name\":\"vue\"}\n", StandardCharsets.UTF_8);

        Path scoped = nodeModules.resolve("@qin").resolve("generated-qin-parser-ts");
        Files.createDirectories(scoped);
        Files.writeString(scoped.resolve("package.json"), "{\"name\":\"@qin/generated-qin-parser-ts\"}\n", StandardCharsets.UTF_8);
        Files.writeString(scoped.resolve(".qin-package-sync.json"), "{}\n", StandardCharsets.UTF_8);

        QinJsPackageRunner runner = new QinJsPackageRunner();
        Method pruneMethod = QinJsPackageRunner.class.getDeclaredMethod(
                "pruneStaleQinMaterializedPackages",
                Path.class,
                Set.class);
        pruneMethod.setAccessible(true);
        pruneMethod.invoke(runner, nodeModules, Set.of("ovs-compiler", "@qin/generated-qin-parser-ts"));

        if (Files.exists(stale)) {
            throw new IllegalStateException("Stale Qin materialized package should be pruned");
        }
        if (!Files.isDirectory(active)) {
            throw new IllegalStateException("Active Qin materialized package should be kept");
        }
        if (!Files.isDirectory(remote)) {
            throw new IllegalStateException("Unstamped remote package should not be pruned");
        }
        if (!Files.isDirectory(scoped)) {
            throw new IllegalStateException("Active scoped package should be kept");
        }

        System.out.println("QinJsPackageRunnerPruneStaleMaterializedPackagesSmokeTestMain OK");
    }
}
