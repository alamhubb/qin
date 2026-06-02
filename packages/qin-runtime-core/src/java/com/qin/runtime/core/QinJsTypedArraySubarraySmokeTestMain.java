package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsTypedArraySubarraySmokeTestMain {
    private QinJsTypedArraySubarraySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-typed-array-subarray-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-typed-array-subarray\" }\n", StandardCharsets.UTF_8);
        String source = """
                const data = new Uint8Array(4);
                data[0] = 260;
                data[1] = 7;
                data[2] = 8;
                data[3] = 9;
                const head = data.subarray(0, 2);
                const tail = data.subarray(-2);
                ({
                  headLength: head.length,
                  headFirst: head[0],
                  headSecond: head[1],
                  tailLength: tail.length,
                  tailFirst: tail[0],
                  tailSecond: tail[1]
                });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_typed_array_subarray");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertNumber(map.get("headLength"), 2d, "headLength");
        assertNumber(map.get("headFirst"), 4d, "headFirst");
        assertNumber(map.get("headSecond"), 7d, "headSecond");
        assertNumber(map.get("tailLength"), 2d, "tailLength");
        assertNumber(map.get("tailFirst"), 8d, "tailFirst");
        assertNumber(map.get("tailSecond"), 9d, "tailSecond");
        System.out.println("QinJsTypedArraySubarraySmokeTestMain OK");
    }

    private static void assertNumber(Object value, double expected, String name) {
        if (!(value instanceof Number number) || number.doubleValue() != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}

