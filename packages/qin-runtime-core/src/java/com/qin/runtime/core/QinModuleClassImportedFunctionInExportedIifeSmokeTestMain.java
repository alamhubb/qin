package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinModuleClassImportedFunctionInExportedIifeSmokeTestMain {
    private QinModuleClassImportedFunctionInExportedIifeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-module-class-imported-function-iife-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"module-class-imported-function-iife\" }\n",
                StandardCharsets.UTF_8);
        Files.writeString(root.resolve("runtime.js"), """
                export const make = ((name) => {
                  return globalThis[name];
                });
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("system.js"), """
                import { make } from "./runtime.js";
                export const value = (() => {
                  const MapCtor = make("Map");
                  const map = new MapCtor();
                  return { ok: map instanceof Map };
                })();
                value;
                """, StandardCharsets.UTF_8);

        Object result = new QinInMemoryJvmRunner().compileAndRunModuleClasses(
                root.resolve("system.js"),
                root,
                "probe.QinModuleClassImportedFunctionInExportedIifeSmoke",
                "",
                root.resolve(".qin").resolve("module-class-cache"),
                "");
        if (!Boolean.TRUE.equals(((java.util.Map<?, ?>) result).get("ok"))) {
            throw new IllegalStateException("Expected imported function inside exported IIFE to construct Map, got: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinModuleClassImportedFunctionInExportedIifeSmokeTestMain OK");
    }
}
