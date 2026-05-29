package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsPreincrementNegativeSmokeTestMain {
    private QinJsPreincrementNegativeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-preincrement-negative-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-preincrement-negative\" }\n", StandardCharsets.UTF_8);
        String source = """
                let value = -1;
                ({ next: ++value, value });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_preincrement_negative");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertNumber(map.get("next"), 0d, "next");
        assertNumber(map.get("value"), 0d, "value");
        System.out.println("QinJsPreincrementNegativeSmokeTestMain OK");
    }

    private static void assertNumber(Object value, double expected, String name) {
        if (!(value instanceof Number number) || number.doubleValue() != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}
