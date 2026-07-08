package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionalReturnPropagationSmokeTestMain {
    private QinJsFunctionalReturnPropagationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { com_subhuti_parser_Alternative as Alternative }
                  from "@qin/generated-qin-parser-ts/com/subhuti/parser/Alternative.ts";
                import { __qin_java_functional } from "@qin/java-sdk-js";

                class Probe {
                  constructor() {
                    this.runs = 0;
                  }

                  value() {
                    this.runs++;
                    return "VALUE";
                  }
                }

                const probe = new Probe();
                const direct = probe.value();
                const functional = __qin_java_functional(() => probe.value());
                const functionalGet = functional.get();
                const alternative = Alternative.of(() => probe.value());
                const alternativeExecute = alternative.execute();

                ({
                  direct,
                  functionalGet,
                  alternativeExecute,
                  runs: probe.runs
                });
                """, "js_functional_return_propagation");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        require(map, "direct", "VALUE");
        require(map, "functionalGet", "VALUE");
        require(map, "alternativeExecute", "VALUE");
        Object runs = map.get("runs");
        if (!(runs instanceof Number number) || number.intValue() != 3) {
            throw new IllegalStateException("Unexpected runs: " + QinObjectJsonEncoder.toJson(map));
        }

        System.out.println("QinJsFunctionalReturnPropagationSmokeTestMain OK "
                + QinObjectJsonEncoder.toJson(map));
    }

    private static void require(Map<?, ?> map, String key, String expected) {
        if (!expected.equals(map.get(key))) {
            throw new IllegalStateException("Unexpected " + key + ": "
                    + QinObjectJsonEncoder.toJson(map));
        }
    }
}
