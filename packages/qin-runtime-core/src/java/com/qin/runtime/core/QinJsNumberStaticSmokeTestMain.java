package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsNumberStaticSmokeTestMain {
    private QinJsNumberStaticSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-number-static-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-number-static\" }\n", StandardCharsets.UTF_8);
        String source = """
                const a = Number.MAX_SAFE_INTEGER;
                const b = Math.floor(2);
                const c = 2 === Math.floor(2);
                const d = 2 <= Number.MAX_SAFE_INTEGER;
                const e = isFinite(2);
                ({ a, b, c, d, e });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_number_static");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertNumber(map.get("a"), 9007199254740991d, "a");
        assertNumber(map.get("b"), 2d, "b");
        assertBoolean(map.get("c"), true, "c");
        assertBoolean(map.get("d"), true, "d");
        assertBoolean(map.get("e"), true, "e");
        System.out.println("QinJsNumberStaticSmokeTestMain OK");
    }

    private static void assertNumber(Object value, double expected, String name) {
        if (!(value instanceof Number number) || number.doubleValue() != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }

    private static void assertBoolean(Object value, boolean expected, String name) {
        if (!Boolean.valueOf(expected).equals(value)) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}
