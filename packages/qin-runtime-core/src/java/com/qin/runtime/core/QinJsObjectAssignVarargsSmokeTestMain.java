package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsObjectAssignVarargsSmokeTestMain {
    private QinJsObjectAssignVarargsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-object-assign-varargs-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-object-assign-varargs\" }\n", StandardCharsets.UTF_8);
        String source = """
                const target = { a: 1 };
                const result = Object.assign(target, { b: 2 }, { c: 3 });
                ({ same: result === target, a: target.a, b: target.b, c: target.c });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_object_assign_varargs");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertBoolean(map.get("same"), true, "same");
        assertNumber(map.get("a"), 1d, "a");
        assertNumber(map.get("b"), 2d, "b");
        assertNumber(map.get("c"), 3d, "c");
        System.out.println("QinJsObjectAssignVarargsSmokeTestMain OK");
    }

    private static void assertBoolean(Object value, boolean expected, String name) {
        if (!(value instanceof Boolean bool) || bool != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }

    private static void assertNumber(Object value, double expected, String name) {
        if (!(value instanceof Number number) || number.doubleValue() != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}

