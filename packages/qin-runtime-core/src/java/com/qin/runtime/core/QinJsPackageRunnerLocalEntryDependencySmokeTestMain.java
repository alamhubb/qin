package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class QinJsPackageRunnerLocalEntryDependencySmokeTestMain {
    private QinJsPackageRunnerLocalEntryDependencySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runner-local-entry-deps-");
        Path parent = root.resolve("parent-package");
        Path child = root.resolve("child-runtime");
        Files.createDirectories(parent.resolve("src"));
        Files.createDirectories(parent.resolve("src").resolve("feature"));
        Files.createDirectories(child.resolve("src"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-js-runner-local-entry-deps",
                  packageOverrides: {
                    "parent-package": "./parent-package",
                    "child-runtime": "./child-runtime"
                  },
                  dependencies: {
                    "parent-package": "0.0.0-local"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(parent.resolve("package.json"), """
                {
                  "name": "parent-package",
                  "version": "0.0.0-local",
                  "type": "module",
                  "local": "./src/index.ts",
                  "main": "./dist/index.mjs"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(parent.resolve("src").resolve("index.ts"), """
                import { feature } from "./feature"
                export const value = feature
                """, StandardCharsets.UTF_8);
        Files.writeString(parent.resolve("src").resolve("feature").resolve("index.ts"), """
                import { childValue } from "child-runtime"
                export const feature = childValue
                """, StandardCharsets.UTF_8);
        Files.writeString(child.resolve("package.json"), """
                {
                  "name": "child-runtime",
                  "version": "0.0.0-local",
                  "type": "module",
                  "local": "./src/index.ts"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(child.resolve("src").resolve("index.ts"), """
                export const childValue = "ok"
                """, StandardCharsets.UTF_8);

        Path wrapperDir = root.resolve(".qin").resolve("runtime").resolve("npm-host");
        Method method = QinJsPackageRunner.class.getDeclaredMethod(
                "materializeWorkspaceDependencies",
                Path.class,
                Path.class,
                String.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> materialized = (Set<String>) method.invoke(
                new QinJsPackageRunner(),
                root,
                wrapperDir,
                """
                        import { value } from "parent-package"
                        value
                        """);

        Path childPackage = wrapperDir.resolve("node_modules").resolve("child-runtime");
        if (!materialized.contains("child-runtime") || !Files.isRegularFile(childPackage.resolve("src").resolve("index.ts"))) {
            throw new IllegalStateException("Expected local-entry child dependency to be materialized under " + childPackage);
        }

        System.out.println("QinJsPackageRunnerLocalEntryDependencySmokeTestMain OK");
    }
}
