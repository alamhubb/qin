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
                  "local": "./src/index.ts",
                  "dependencies": {
                    "child-pkg": "file:./child-pkg"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageRoot.resolve("src").resolve("index.ts"), """
                import { childValue } from "child-pkg"
                import { cachedValue } from "cached-pkg"
                export const value = "override-ts-package:" + childValue + ":" + cachedValue
                """, StandardCharsets.UTF_8);

        Path childPackageRoot = packageRoot.resolve("child-pkg");
        Files.createDirectories(childPackageRoot);
        Files.writeString(childPackageRoot.resolve("package.json"), """
                {
                  "name": "child-pkg",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(childPackageRoot.resolve("index.js"), """
                export const childValue = "file-dependency"
                """, StandardCharsets.UTF_8);

        Path wrapperDir = root.resolve(".qin").resolve("runtime").resolve("npm-host");
        Files.createDirectories(wrapperDir);
        Path cachedPackageRoot = wrapperDir.resolve("node_modules").resolve("cached-pkg");
        Files.createDirectories(cachedPackageRoot);
        Files.writeString(cachedPackageRoot.resolve("package.json"), """
                {
                  "name": "cached-pkg",
                  "version": "0.0.0-runtime",
                  "type": "module",
                  "main": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(cachedPackageRoot.resolve("index.js"), """
                export const cachedValue = "runtime-cache"
                """, StandardCharsets.UTF_8);
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
        Path materializedChildManifest = wrapperDir.resolve("node_modules").resolve("child-pkg").resolve("package.json");
        if (!Files.isRegularFile(materializedChildManifest)) {
            throw new IllegalStateException("Package override did not materialize file: dependency: " + materializedChildManifest);
        }
        if (!Files.isRegularFile(cachedPackageRoot.resolve("index.js"))) {
            throw new IllegalStateException("Runtime node_modules package was deleted while materializing: " + cachedPackageRoot);
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
