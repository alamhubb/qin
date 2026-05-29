package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsObjectHasOwnPropertyCallSmokeTestMain {
    private QinJsObjectHasOwnPropertyCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-has-own-property-call-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-has-own-property-call\" }\n", StandardCharsets.UTF_8);
        String source = """
                const hasOwn = {}.hasOwnProperty;
                const source = { a: 1 };
                ({ hasA: hasOwn.call(source, "a"), hasB: hasOwn.call(source, "b") });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_has_own_property_call");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        assertBoolean(map.get("hasA"), true, "hasA");
        assertBoolean(map.get("hasB"), false, "hasB");
        System.out.println("QinJsObjectHasOwnPropertyCallSmokeTestMain OK");
    }

    private static void assertBoolean(Object value, boolean expected, String name) {
        if (!(value instanceof Boolean bool) || bool != expected) {
            throw new IllegalStateException("Expected " + name + " == " + expected + ", got: " + value);
        }
    }
}
