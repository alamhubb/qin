package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsTypedArraySmokeTestMain {
    private QinJsTypedArraySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-typed-array-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-typed-array\" }\n", StandardCharsets.UTF_8);
        String source = """
                const heap = new Uint8Array(2);
                heap[0] = 7;
                heap[1] = 260;
                const fromLength = Array.from({ length: 2 }).fill(void 0);
                ({ length: heap.length, first: heap[0], second: heap[1], filled: fromLength.length });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_typed_array");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertNumber(map.get("length"), 2d, "length");
        assertNumber(map.get("first"), 7d, "first");
        assertNumber(map.get("second"), 4d, "second");
        assertNumber(map.get("filled"), 2d, "filled");
        System.out.println("QinJsTypedArraySmokeTestMain OK");
    }

    private static void assertNumber(Object value, double expected, String name) {
        if (!(value instanceof Number number) || number.doubleValue() != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}
