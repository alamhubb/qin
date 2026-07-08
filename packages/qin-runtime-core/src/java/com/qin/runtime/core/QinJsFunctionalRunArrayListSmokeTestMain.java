package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionalRunArrayListSmokeTestMain {
    private QinJsFunctionalRunArrayListSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { __qin_java_functional, __QinJavaUtilArrayList } from "@qin/java-sdk-js";

                let hits = 0;
                const functional = __qin_java_functional(() => {
                  hits++;
                  return "RUN";
                });
                const directRun = functional.run();

                const list = new __QinJavaUtilArrayList();
                list.add(functional);
                const listRun = list.get(0).run();

                ({
                  directRun,
                  listRun,
                  hits
                });
                """, "js_functional_run_array_list");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        require(map, "directRun", "RUN");
        require(map, "listRun", "RUN");
        Object hits = map.get("hits");
        if (!(hits instanceof Number number) || number.intValue() != 2) {
            throw new IllegalStateException("Unexpected hits: " + QinObjectJsonEncoder.toJson(map));
        }

        System.out.println("QinJsFunctionalRunArrayListSmokeTestMain OK "
                + QinObjectJsonEncoder.toJson(map));
    }

    private static void require(Map<?, ?> map, String key, String expected) {
        if (!expected.equals(map.get(key))) {
            throw new IllegalStateException("Unexpected " + key + ": "
                    + QinObjectJsonEncoder.toJson(map));
        }
    }
}
