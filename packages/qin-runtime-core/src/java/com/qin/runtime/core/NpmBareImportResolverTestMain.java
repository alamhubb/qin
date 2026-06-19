package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies bare specifier resolution from node_modules.
 */
public final class NpmBareImportResolverTestMain {
    private NpmBareImportResolverTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-npm-bare-");
        Path entry = root.resolve("main").resolve("main.js").normalize();
        Path miniPkg = root.resolve("node_modules").resolve("mini-pkg");
        Files.createDirectories(entry.getParent());
        Files.createDirectories(miniPkg);
        Files.writeString(entry, """
                import { value } from "mini-pkg"
                export const result = value
                """, StandardCharsets.UTF_8);
        Files.writeString(miniPkg.resolve("package.json"), """
                {
                  "name": "mini-pkg",
                  "type": "module",
                  "main": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(miniPkg.resolve("index.js"), """
                export const value = "mini"
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        boolean foundMiniPkg = false;
        for (QinModuleSource module : graph.modules()) {
            String path = module.file().toString().replace('\\', '/');
            if (path.endsWith("/node_modules/mini-pkg/index.js")) {
                foundMiniPkg = true;
                break;
            }
        }

        if (!foundMiniPkg) {
            throw new IllegalStateException("Expected mini-pkg module in graph.");
        }
        verifyImportDefaultConditionWinsOverNodeCondition();

        System.out.println("NpmBareImportResolverTestMain passed.");
        System.out.println("modules: " + graph.modules().size());
    }

    private static void verifyImportDefaultConditionWinsOverNodeCondition() throws Exception {
        Path root = Files.createTempDirectory("qin-npm-exports-condition-");
        Path mainDir = root.resolve("main");
        Path packageDir = root.resolve("node_modules").resolve("conditional-pkg");
        Files.createDirectories(mainDir);
        Files.createDirectories(packageDir.resolve("dist"));
        Files.writeString(mainDir.resolve("main.js"), """
                import value from "conditional-pkg"
                export const result = value
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "conditional-pkg",
                  "type": "module",
                  "exports": {
                    ".": {
                      "import": {
                        "node": {
                          "default": "./dist/node.cjs.js"
                        },
                        "default": "./dist/esm.mjs"
                      },
                      "default": "./dist/default.js"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("dist").resolve("esm.mjs"), """
                export default "esm"
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("dist").resolve("node.cjs.js"), """
                exports.default = "node-cjs"
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("dist").resolve("default.js"), """
                export default "default"
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(mainDir.resolve("main.js"));
        boolean foundEsm = false;
        boolean foundNodeCjs = false;
        for (QinModuleSource module : graph.modules()) {
            String path = module.file().toString().replace('\\', '/');
            foundEsm |= path.endsWith("/node_modules/conditional-pkg/dist/esm.mjs");
            foundNodeCjs |= path.endsWith("/node_modules/conditional-pkg/dist/node.cjs.js");
        }
        if (!foundEsm || foundNodeCjs) {
            throw new IllegalStateException(
                    "Expected conditional-pkg to resolve import.default ESM entry, foundEsm="
                            + foundEsm + ", foundNodeCjs=" + foundNodeCjs);
        }
    }
}
