package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;

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
        verifyWorkspaceSourcePackageEntrypointPolicy(runner, root, wrapperDir);
        Method patchVueHelper = QinJsPackageRunner.class.getDeclaredMethod(
                "patchVitePluginVueHelperCodeTemplate",
                String.class);
        patchVueHelper.setAccessible(true);
        String patchedVuePlugin = (String) patchVueHelper.invoke(
                runner,
                """
                const helperCode = `
                export default (sfc, props) => {
                  const target = sfc.__vccOpts || sfc;
                  for (const [key, val] of props) {
                    target[key] = val;
                  }
                  return target;
                }
                `;
                """);
        if (patchedVuePlugin.contains("const helperCode = `")
                || patchedVuePlugin.contains("export default")
                || patchedVuePlugin.contains("EXPORT_HELPER_ID")
                || patchedVuePlugin.contains("plugin-vue:export-helper")
                || patchedVuePlugin.contains("_export_sfc")
                || patchedVuePlugin.contains("port default")
                || !patchedVuePlugin.contains("String.fromCharCode(101, 120, 112, 111, 114, 116)")) {
            throw new IllegalStateException("Vite plugin vue helper template was not patched: " + patchedVuePlugin);
        }

        Method vueShimSource = QinJsPackageRunner.class.getDeclaredMethod("qinVitePluginVueShimSource");
        vueShimSource.setAccessible(true);
        String shimSource = (String) vueShimSource.invoke(runner);
        if (shimSource.contains("export default")
                || !shimSource.contains("export { vuePlugin as default };")
                || !shimSource.contains("String.fromCharCode(101, 120, 112, 111, 114, 116) + \" default \"")) {
            throw new IllegalStateException("Vite plugin vue Qin shim exposes parser-hostile default export text:\n"
                    + shimSource);
        }

        System.out.println("QinJsPackageRunnerPackageOverridesSmokeTestMain OK");
    }

    private static void verifyWorkspaceSourcePackageEntrypointPolicy(
            QinJsPackageRunner runner,
            Path root,
            Path wrapperDir) throws Exception {
        Path runtimeNodeModules = wrapperDir.resolve("node_modules");
        Path missingDistPackageRoot = root.resolve("workspace-vite-plugin-source");
        Files.createDirectories(missingDistPackageRoot.resolve("src"));
        Files.writeString(missingDistPackageRoot.resolve("package.json"), """
                {
                  "name": "vite-plugin-local-source",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./dist/index.cjs",
                  "module": "./dist/index.mjs",
                  "exports": {
                    ".": {
                      "import": "./dist/index.mjs",
                      "default": "./dist/index.mjs"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(missingDistPackageRoot.resolve("src").resolve("index.ts"), """
                export default function plugin() {
                  return { name: "vite-plugin-local-source" }
                }
                """, StandardCharsets.UTF_8);

        Path builtPackageRoot = root.resolve("workspace-vite-plugin-built");
        Files.createDirectories(builtPackageRoot.resolve("dist"));
        Files.createDirectories(builtPackageRoot.resolve("src"));
        Files.writeString(builtPackageRoot.resolve("package.json"), """
                {
                  "name": "vite-plugin-built-source",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./dist/index.cjs",
                  "module": "./dist/index.mjs",
                  "exports": {
                    ".": {
                      "import": "./dist/index.mjs",
                      "default": "./dist/index.mjs"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(builtPackageRoot.resolve("dist").resolve("index.mjs"), """
                export default function plugin() {
                  return { name: "vite-plugin-built-source" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(builtPackageRoot.resolve("src").resolve("index.ts"), """
                export default function sourcePlugin() {
                  return { name: "vite-plugin-built-source-src" }
                }
                """, StandardCharsets.UTF_8);

        Method materializeDependency = QinJsPackageRunner.class.getDeclaredMethod(
                "materializeDependency",
                String.class,
                String.class,
                Path.class,
                Path.class,
                Path.class,
                Path.class,
                Map.class,
                Map.class,
                java.util.Set.class);
        materializeDependency.setAccessible(true);
        Map<String, Path> workspacePackages = Map.of(
                "vite-plugin-local-source", missingDistPackageRoot,
                "vite-plugin-built-source", builtPackageRoot);
        materializeDependency.invoke(
                runner,
                "vite-plugin-local-source",
                null,
                null,
                root,
                runtimeNodeModules,
                root,
                workspacePackages,
                Map.of(),
                new LinkedHashSet<String>());
        materializeDependency.invoke(
                runner,
                "vite-plugin-built-source",
                null,
                null,
                root,
                runtimeNodeModules,
                root,
                workspacePackages,
                Map.of(),
                new LinkedHashSet<String>());

        String missingDistManifest = Files.readString(
                runtimeNodeModules.resolve("vite-plugin-local-source").resolve("package.json"),
                StandardCharsets.UTF_8);
        if (!missingDistManifest.contains("\"main\": \"./src/index.ts\"")
                || !missingDistManifest.contains("\"module\": \"./src/index.ts\"")) {
            throw new IllegalStateException("Workspace source package with missing dist was not rewritten:\n"
                    + missingDistManifest);
        }
        String builtManifest = Files.readString(
                runtimeNodeModules.resolve("vite-plugin-built-source").resolve("package.json"),
                StandardCharsets.UTF_8);
        if (!builtManifest.contains("\"module\": \"./dist/index.mjs\"")
                || builtManifest.contains("\"main\": \"./src/index.ts\"")) {
            throw new IllegalStateException("Workspace package with resolvable dist entry was rewritten:\n"
                    + builtManifest);
        }
    }

    private static QinJsPackageRunner allocateRunnerWithoutCompiler() throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return (QinJsPackageRunner) unsafe.allocateInstance(QinJsPackageRunner.class);
    }
}
