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
                import { __qin_java_functional } from "@qin/java-sdk-js"
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
                import { __qin_java_functional } from "@qin/java-sdk-js"

                class BaseParser {
                          __qin_field_lastRuleName = ""

                          executeRuleWrapper(...args) {
                            const ruleName = args[1]
                            this.__qin_field_lastRuleName = ruleName
                            return "ok"
                          }

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

                        const descriptorReceiver = {
                          ruleName: "",
                          executeRuleWrapper(targetFun, ruleName) {
                            this.ruleName = ruleName
                            return targetFun()
                          }
                        }
                        const descriptorWrapped = SubhutiRule({ method: function DescriptorRule() {
                          return "descriptor"
                        } }, { name: "DescriptorRule" })
                        const shiftedWrapped = SubhutiRule("ShiftedRule", { method: function ShiftedRule() {
                          return "shifted"
                        } })
                        const functionalDescriptor = __qin_java_functional(descriptorWrapped)
                        const demo = new Demo()
                        descriptorReceiver.descriptor = descriptorWrapped
                        descriptorReceiver.shifted = shiftedWrapped
                        const token = createRegToken("Hash", /#/)
                        const cache = new SubhutiPackratCache(10)
                        ;({
                          tokenName: token.getName(),
                          methodResult: demo.Rule(),
                          directMethodResult: demo.Statement(),
                          lastRuleName: demo.__qin_field_lastRuleName,
                          descriptorMarked: descriptorWrapped.__isSubhutiRule__ === true,
                          descriptorRuleName: descriptorWrapped.__qinSubhutiRuleName,
                          descriptorResult: descriptorReceiver.descriptor(),
                          descriptorReceiverRuleName: descriptorReceiver.ruleName,
                          functionalDescriptorMarked: functionalDescriptor.__isSubhutiRule__ === true,
                          functionalDescriptorRuleName: functionalDescriptor.__qinSubhutiRuleName,
                          functionalDescriptorMethodRuleName: functionalDescriptor.method && functionalDescriptor.method.__qinSubhutiRuleName,
                          functionalDescriptorResult: functionalDescriptor(),
                          shiftedMarked: shiftedWrapped.__isSubhutiRule__ === true,
                          shiftedRuleName: shiftedWrapped.__qinSubhutiRuleName,
                          shiftedResult: descriptorReceiver.shifted(),
                          cacheReady: cache.getMaxSize() === 10
                        })
                        """,
                "js_subhuti_generated_shim");
        require(String.valueOf(result).contains("tokenName=Hash"), "subhuti shim createRegToken did not execute: " + result);
        require(String.valueOf(result).contains("methodResult=ok"), "subhuti shim SubhutiRule broke method execution: " + result);
        require(String.valueOf(result).contains("directMethodResult=ok"),
                "subhuti shim direct decorated method execution failed: " + result);
        require(String.valueOf(result).contains("lastRuleName=Statement"),
                "subhuti shim did not route class decorators through executeRuleWrapper: " + result);
        require(String.valueOf(result).contains("descriptorMarked=true"),
                "subhuti shim did not mark Qin-lowered method descriptor: " + result);
        require(String.valueOf(result).contains("descriptorRuleName=DescriptorRule"),
                "subhuti shim did not preserve method descriptor rule name: " + result);
        require(String.valueOf(result).contains("descriptorResult=descriptor"),
                "subhuti shim method descriptor execution failed: " + result);
        require(String.valueOf(result).contains("descriptorReceiverRuleName=DescriptorRule"),
                "subhuti shim method descriptor did not route through wrapper: " + result);
        require(String.valueOf(result).contains("functionalDescriptorMarked=true"),
                "subhuti shim functional wrapper did not preserve rule marker: " + result);
        require(String.valueOf(result).contains("functionalDescriptorRuleName=DescriptorRule"),
                "subhuti shim functional wrapper did not preserve rule name: " + result);
        require(String.valueOf(result).contains("functionalDescriptorMethodRuleName=DescriptorRule"),
                "subhuti shim functional wrapper did not preserve nested rule name: " + result);
        require(String.valueOf(result).contains("functionalDescriptorResult=descriptor"),
                "subhuti shim functional wrapper did not preserve execution: " + result);
        require(String.valueOf(result).contains("shiftedMarked=true"),
                "subhuti shim did not mark shifted method descriptor: " + result);
        require(String.valueOf(result).contains("shiftedRuleName=ShiftedRule"),
                "subhuti shim did not preserve shifted descriptor rule name: " + result);
        require(String.valueOf(result).contains("shiftedResult=shifted"),
                "subhuti shim shifted descriptor execution failed: " + result);
        require(String.valueOf(result).contains("cacheReady=true"), "subhuti shim SubhutiPackratCache did not execute: " + result);
        System.out.println("QinJsPackageRunnerSubhutiShimSmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
