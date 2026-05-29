package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsCircularLiveBindingSmokeTestMain {
    private QinJsCircularLiveBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-circular-live-binding-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-circular-live-binding\" }\n",
                StandardCharsets.UTF_8);
        Path packageDir = root.resolve("node_modules").resolve("local-cycle");
        Files.createDirectories(packageDir.resolve("src"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "local-cycle",
                  "type": "module",
                  "main": "./src/a.ts",
                  "exports": {
                    ".": {
                      "import": "./src/a.ts",
                      "default": "./src/a.ts"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("src").resolve("a.ts"), """
                import { read } from "./b.ts";
                export const value = { answer: 42 };
                export const result = read();
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("src").resolve("b.ts"), """
                import { value } from "./a.ts";
                export function read() {
                  return value.answer;
                }
                """, StandardCharsets.UTF_8);

        String wrapper = """
                import { result } from "local-cycle";
                ({ result });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_circular_live_binding");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Double.valueOf(42.0d).equals(map.get("result"))) {
            throw new IllegalStateException("Expected circular live binding result 42, got: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsCircularLiveBindingSmokeTestMain OK");
    }
}
