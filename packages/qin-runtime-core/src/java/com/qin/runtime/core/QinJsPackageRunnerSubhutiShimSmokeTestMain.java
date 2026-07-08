package com.qin.runtime.core;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies the Qin JVM npm host resolves subhuti to the generated Java-to-TS
 * shim instead of the deprecated handwritten TypeScript source package.
 */
public final class QinJsPackageRunnerSubhutiShimSmokeTestMain {
    private QinJsPackageRunnerSubhutiShimSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-subhuti-shim-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "com.qin.smoke:subhuti-shim",
                  version: "0.1.0",
                  packageOverrides: {
                    "subhuti": "./legacy-subhuti"
                  }
                }
                """, StandardCharsets.UTF_8);

        Path legacySubhuti = root.resolve("legacy-subhuti");
        Files.createDirectories(legacySubhuti.resolve("src"));
        Files.writeString(legacySubhuti.resolve("package.json"), """
                {
                  "name": "subhuti",
                  "version": "0.0.0-legacy",
                  "type": "module",
                  "main": "./src/index.ts",
                  "module": "./src/index.ts"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(legacySubhuti.resolve("src").resolve("index.ts"), """
                export const legacy = import.meta.url
                export const proxy = new Proxy({}, {})
                export const reflect = Reflect.get(proxy, "x")
                """, StandardCharsets.UTF_8);

        Path wrapperDir = root.resolve(".qin").resolve("runtime").resolve("npm-host");
        Files.createDirectories(wrapperDir);
        Method materialize = QinJsPackageRunner.class.getDeclaredMethod(
                "materializeWorkspaceDependencies",
                Path.class,
                Path.class,
                String.class);
        materialize.setAccessible(true);
        materialize.invoke(
                new QinJsPackageRunner(),
                root,
                wrapperDir,
                """
                        import { createRegToken, SubhutiRule, SubhutiPackratCache } from "subhuti"
                        ;({ createRegToken, SubhutiRule, SubhutiPackratCache })
                        """);

        Path subhutiDir = wrapperDir.resolve("node_modules").resolve("subhuti");
        Path shimIndex = subhutiDir.resolve("index.ts");
        String shimSource = Files.readString(shimIndex, StandardCharsets.UTF_8);
        String shimManifest = Files.readString(subhutiDir.resolve("package.json"), StandardCharsets.UTF_8);
        require(shimManifest.contains("0.0.0-qin-generated-shim"), "subhuti shim manifest was not materialized");
        require(shimSource.contains("GeneratedSubhutiCreateToken"), "subhuti shim does not use generated parser exports");
        require(!shimSource.contains("import.meta"), "subhuti shim leaked legacy import.meta");
        require(!shimSource.contains("new Proxy"), "subhuti shim leaked legacy Proxy");
        require(!shimSource.contains("Reflect."), "subhuti shim leaked legacy Reflect");
        require(Files.isRegularFile(wrapperDir
                        .resolve("node_modules")
                        .resolve("@qin")
                        .resolve("generated-qin-parser-ts")
                        .resolve("package.json")),
                "generated Qin parser package was not materialized for subhuti shim");

        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                """
                        import { createRegToken, SubhutiRule, SubhutiPackratCache } from "subhuti"

                        class BaseParser {
                          @SubhutiRule
                          Rule() {
                            return this.Statement()
                          }

                          @SubhutiRule
                          Statement() {
                            return "base"
                          }
                        }

                        class Demo extends BaseParser {
                          @SubhutiRule
                          Statement() {
                            return "ok"
                          }
                        }

                        const token = createRegToken("Hash", /#/)
                        const cache = new SubhutiPackratCache(10)
                        ;({
                          tokenName: token.getName(),
                          methodResult: new Demo().Rule(),
                          directMethodResult: new Demo().Statement(),
                          cacheReady: cache.getMaxSize() === 10
                        })
                        """,
                "js_subhuti_generated_shim");
        require(String.valueOf(result).contains("tokenName=Hash"), "subhuti shim createRegToken did not execute: " + result);
        require(String.valueOf(result).contains("methodResult=ok"), "subhuti shim SubhutiRule broke method execution: " + result);
        require(String.valueOf(result).contains("directMethodResult=ok"),
                "subhuti shim direct decorated method execution failed: " + result);
        require(String.valueOf(result).contains("cacheReady=true"), "subhuti shim SubhutiPackratCache did not execute: " + result);
        System.out.println("QinJsPackageRunnerSubhutiShimSmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
