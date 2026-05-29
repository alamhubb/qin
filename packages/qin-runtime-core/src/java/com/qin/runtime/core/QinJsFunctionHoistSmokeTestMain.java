package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionHoistSmokeTestMain {
    private QinJsFunctionHoistSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-function-hoist-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-function-hoist\" }\n", StandardCharsets.UTF_8);
        String source = """
                function outer() {
                  return before(2);
                  function before(value) {
                    return value + 3;
                  }
                }
                ({ value: outer() });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_function_hoist");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        Object value = map.get("value");
        if (!(value instanceof Number number) || number.doubleValue() != 5d) {
            throw new IllegalStateException("Expected value == 5, got: " + value);
        }
        System.out.println("QinJsFunctionHoistSmokeTestMain OK");
    }
}
