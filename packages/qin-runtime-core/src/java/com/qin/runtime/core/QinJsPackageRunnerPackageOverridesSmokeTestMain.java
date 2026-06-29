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
        Files.createDirectories(packageRoot.resolve("dist"));
        Files.writeString(packageRoot.resolve("package.json"), """
                {
                  "name": "mini-pkg",
                  "version": "0.0.0-local",
                  "type": "module",
                  "local": "./src/index.ts",
                  "main": "./dist/index.cjs",
                  "module": "./dist/index.mjs",
                  "exports": {
                    ".": {
                      "import": "./dist/index.mjs",
                      "default": "./dist/index.mjs"
                    }
                  },
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
        Files.writeString(packageRoot.resolve("dist").resolve("index.mjs"), """
                export const value = "override-dist-package"
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
        if (!manifest.contains("\"module\": \"./dist/index.mjs\"")
                || manifest.contains("\"main\": \"./src/index.ts\"")
                || !Files.isRegularFile(wrapperDir.resolve("node_modules").resolve("mini-pkg").resolve("dist").resolve("index.mjs"))) {
            throw new IllegalStateException("Package override with built dist did not preserve the manifest entry: " + manifest);
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
        verifyQinJvmHostPackagePatches(runner, root);

        System.out.println("QinJsPackageRunnerPackageOverridesSmokeTestMain OK");
    }

    private static void verifyQinJvmHostPackagePatches(QinJsPackageRunner runner, Path root) throws Exception {
        Path ovsPackageRoot = root.resolve("ovs-compiler-host-patch");
        Path ovsDist = ovsPackageRoot.resolve("dist");
        Files.createDirectories(ovsDist);
        Files.writeString(ovsDist.resolve("index.mjs"), """
                function __decorateMetadata(k, v) {
                \tif (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
                }
                function __decorate(decorators, target, key, desc) {
                \tvar c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
                \tif (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
                \telse for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
                \treturn c > 3 && r && Object.defineProperty(target, key, r), r;
                }
                let _ovsCstToSlimeAstUtil;
                const OvsCstToSlimeAstUtils = new Proxy({}, { get(_, prop) {
                \tconst val = _ovsCstToSlimeAstUtil[prop];
                \treturn typeof val === "function" ? val.bind(_ovsCstToSlimeAstUtil) : val;
                } });
                """, StandardCharsets.UTF_8);
        Method patchOvs = QinJsPackageRunner.class.getDeclaredMethod("patchOvsCompilerForQinJvmHost", Path.class);
        patchOvs.setAccessible(true);
        patchOvs.invoke(runner, ovsPackageRoot);
        String patchedOvs = Files.readString(ovsDist.resolve("index.mjs"), StandardCharsets.UTF_8);
        if (patchedOvs.contains("new Proxy")
                || patchedOvs.contains("Reflect")
                || patchedOvs.contains("arguments.length")
                || patchedOvs.contains("desc === void 0")
                || patchedOvs.contains("Object.getOwnPropertyDescriptor")
                || patchedOvs.contains("Object.defineProperty")
                || !patchedOvs.contains("var c = key === void 0 ? 2 : 4;")
                || !patchedOvs.contains("{ value: target[key], writable: true, enumerable: false, configurable: true }")
                || !patchedOvs.contains("const OvsCstToSlimeAstUtils = {};")) {
            throw new IllegalStateException("OVS compiler Qin JVM host patch did not remove unsupported syntax:\n"
                    + patchedOvs);
        }

        Path lruPackageRoot = root.resolve("lru-cache-host-patch");
        Path lruDist = lruPackageRoot.resolve("dist").resolve("esm");
        Files.createDirectories(lruDist);
        Files.writeString(lruDist.resolve("index.min.js"), """
                var L={hasSubscribers:!1},S=L,A=L;import("node:diagnostics_channel").then(()=>{}).catch(()=>{});class Z{[Symbol.iterator](){return this.entries()}[Symbol.toStringTag]="LRUCache";}
                """, StandardCharsets.UTF_8);
        Method patchLru = QinJsPackageRunner.class.getDeclaredMethod("patchLruCacheForQinJvmHost", Path.class);
        patchLru.setAccessible(true);
        patchLru.invoke(runner, lruPackageRoot);
        String patchedLru = Files.readString(lruDist.resolve("index.min.js"), StandardCharsets.UTF_8);
        if (patchedLru.contains("import(\"node:diagnostics_channel\")")
                || patchedLru.contains("Symbol.toStringTag")
                || !patchedLru.contains("Symbol.iterator")) {
            throw new IllegalStateException("lru-cache Qin JVM host patch did not preserve the supported iterator path:\n"
                    + patchedLru);
        }
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
        Path sourceExportsPackageRoot = root.resolve("workspace-source-exports");
        Files.createDirectories(sourceExportsPackageRoot.resolve("src"));
        Files.writeString(sourceExportsPackageRoot.resolve("package.json"), """
                {
                  "name": "source-exports-package",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.ts",
                  "module": "./index.ts",
                  "exports": {
                    ".": {
                      "import": "./index.ts",
                      "default": "./index.ts"
                    },
                    "./Extra": {
                      "import": "./src/extra.ts",
                      "default": "./src/extra.ts"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(sourceExportsPackageRoot.resolve("index.ts"), """
                export default "source";
                """, StandardCharsets.UTF_8);
        Files.writeString(sourceExportsPackageRoot.resolve("src").resolve("extra.ts"), """
                export const extra = "extra";
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
                "vite-plugin-built-source", builtPackageRoot,
                "source-exports-package", sourceExportsPackageRoot);
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
        materializeDependency.invoke(
                runner,
                "source-exports-package",
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
        String sourceExportsManifest = Files.readString(
                runtimeNodeModules.resolve("source-exports-package").resolve("package.json"),
                StandardCharsets.UTF_8);
        if (!sourceExportsManifest.contains("\"./Extra\"")
                || !sourceExportsManifest.contains("\"import\": \"./src/extra.ts\"")) {
            throw new IllegalStateException("Workspace package with source exports subpaths was rewritten:\n"
                    + sourceExportsManifest);
        }
    }

    private static QinJsPackageRunner allocateRunnerWithoutCompiler() throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return (QinJsPackageRunner) unsafe.allocateInstance(QinJsPackageRunner.class);
    }
}
