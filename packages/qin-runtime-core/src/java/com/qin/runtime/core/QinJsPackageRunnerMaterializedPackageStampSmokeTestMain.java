package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public final class QinJsPackageRunnerMaterializedPackageStampSmokeTestMain {
    private QinJsPackageRunnerMaterializedPackageStampSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-materialized-stamp-");
        Path packageDir = root.resolve("package");
        Files.createDirectories(packageDir.resolve("src"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "stamp-package",
                  "version": "1.0.0",
                  "type": "module"
                }
                """, StandardCharsets.UTF_8);
        Path source = packageDir.resolve("src").resolve("index.js");
        Files.writeString(source, "export const value = \"old\";\n", StandardCharsets.UTF_8);

        QinJsPackageRunner runner = new QinJsPackageRunner();
        Method stampMethod = QinJsPackageRunner.class.getDeclaredMethod(
                "materializedPackageStamp",
                Path.class,
                boolean.class,
                boolean.class);
        stampMethod.setAccessible(true);

        String first = (String) stampMethod.invoke(runner, packageDir, true, false);
        if (!first.contains("\"sha256\"")) {
            throw new IllegalStateException("Materialized package stamp must include a content sha256");
        }
        if (first.contains("\"modifiedMillis\"")) {
            throw new IllegalStateException("Materialized package stamp must not include volatile mtimes");
        }

        Files.setLastModifiedTime(source, FileTime.fromMillis(System.currentTimeMillis() + 10_000L));
        String afterSameContentTimestampChange = (String) stampMethod.invoke(runner, packageDir, true, false);
        if (!first.equals(afterSameContentTimestampChange)) {
            throw new IllegalStateException("Materialized package stamp must stay stable when only mtimes change");
        }

        Files.writeString(source, "export const value = \"new\";\n", StandardCharsets.UTF_8);
        String afterSameSizeContentChange = (String) stampMethod.invoke(runner, packageDir, true, false);
        if (first.equals(afterSameSizeContentChange)) {
            throw new IllegalStateException(
                    "Materialized package stamp must change when source content changes with the same file count and size");
        }

        Files.createDirectories(packageDir.resolve("node_modules").resolve("ignored"));
        Files.writeString(
                packageDir.resolve("node_modules").resolve("ignored").resolve("index.js"),
                "export const ignored = true;\n",
                StandardCharsets.UTF_8);
        String afterIgnoredNodeModulesChange = (String) stampMethod.invoke(runner, packageDir, true, false);
        if (!afterSameSizeContentChange.equals(afterIgnoredNodeModulesChange)) {
            throw new IllegalStateException("Workspace package stamp should ignore node_modules by default");
        }

        System.out.println("QinJsPackageRunnerMaterializedPackageStampSmokeTestMain OK");
    }
}
