package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sun.misc.Unsafe;

/**
 * Verifies project-level packageOverrides in qin.config.js win over workspace package discovery.
 */
public final class QinJsPackageRunnerPackageOverridesSmokeTestMain {
    private QinJsPackageRunnerPackageOverridesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-overrides-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "com.qin.smoke:js-package-overrides",
                  version: "0.1.0",
                  packageOverrides: {
                    "mini-pkg": "./local-mini"
                  }
                }
                """, StandardCharsets.UTF_8);

        Path packageRoot = root.resolve("local-mini");
        Files.createDirectories(packageRoot.resolve("src"));
        Files.writeString(packageRoot.resolve("package.json"), """
                {
                  "name": "mini-pkg",
                  "version": "0.0.0-local",
                  "type": "module",
                  "local": "./src/index.ts"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageRoot.resolve("src").resolve("index.ts"), """
                export const value = "override-ts-package"
                """, StandardCharsets.UTF_8);

        Path wrapperDir = root.resolve(".qin").resolve("runtime").resolve("npm-host");
        Files.createDirectories(wrapperDir);
        QinJsPackageRunner runner = allocateRunnerWithoutCompiler();
        Method materialize = QinJsPackageRunner.class.getDeclaredMethod(
                "materializeWorkspaceDependencies",
                Path.class,
                Path.class,
                String.class);
        materialize.setAccessible(true);
        materialize.invoke(
                runner,
                root,
                wrapperDir,
                """
                        import { value } from "mini-pkg"
                        ;({ value })
                        """);

        Path materializedManifest = wrapperDir.resolve("node_modules").resolve("mini-pkg").resolve("package.json");
        String manifest = Files.readString(materializedManifest, StandardCharsets.UTF_8);
        if (!manifest.contains("\"main\": \"./src/index.ts\"")
                || !Files.isRegularFile(wrapperDir.resolve("node_modules").resolve("mini-pkg").resolve("src").resolve("index.ts"))) {
            throw new IllegalStateException("Package override did not materialize the local TS package: " + manifest);
        }

        System.out.println("QinJsPackageRunnerPackageOverridesSmokeTestMain OK");
    }

    private static QinJsPackageRunner allocateRunnerWithoutCompiler() throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return (QinJsPackageRunner) unsafe.allocateInstance(QinJsPackageRunner.class);
    }
}
