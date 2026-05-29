package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinCsstsCompilerAtomRegistrySmokeTestMain {
    private QinCsstsCompilerAtomRegistrySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-atom-registry-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-cssts-atom-registry\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { CsstsInit, generateStats } from "cssts-compiler";
                CsstsInit.init({ dts: false });
                const stats = generateStats();
                ({
                  padding: stats.byProperty.padding,
                  borderRadius: stats.byProperty["border-radius"],
                  padding32: CsstsInit.isValidAtomName("padding32px"),
                  borderRadius16: CsstsInit.isValidAtomName("borderRadius16px"),
                  paddingSamples: CsstsInit.getAllAtomNames("atom").filter(name => String(name).includes("padding32")).slice(0, 10).join(","),
                  borderRadiusSamples: CsstsInit.getAllAtomNames("atom").filter(name => String(name).includes("borderRadius16")).slice(0, 10).join(",")
                });
                """, "cssts_atom_registry");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected registry result object, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("padding32"))
                || !Boolean.TRUE.equals(map.get("borderRadius16"))) {
            throw new IllegalStateException("Expected numeric atoms in registry, got: " + map);
        }
        System.out.println("QinCsstsCompilerAtomRegistrySmokeTestMain passed.");
    }
}
