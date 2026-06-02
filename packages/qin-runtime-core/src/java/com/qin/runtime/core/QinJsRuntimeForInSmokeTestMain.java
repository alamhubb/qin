package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRuntimeForInSmokeTestMain {
    private QinJsRuntimeForInSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-for-in-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-runtime-for-in\" }\n", StandardCharsets.UTF_8);
        String source = """
                function collect(input) {
                  let result = "";
                  for (const key in input) {
                    result += key + ":" + input[key] + ";";
                  }
                  return result;
                }
                ({ objectKeys: collect({ a: 1, b: 2 }), arrayKeys: collect(["x", "y"]) });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_runtime_for_in");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertValue(map.get("objectKeys"), "a:1;b:2;", "objectKeys");
        assertValue(map.get("arrayKeys"), "0:x;1:y;", "arrayKeys");
        System.out.println("QinJsRuntimeForInSmokeTestMain OK");
    }

    private static void assertValue(Object value, String expected, String name) {
        if (!expected.equals(value)) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}

