package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class QinJsObjectGetOwnPropertyNamesSmokeTestMain {
    private QinJsObjectGetOwnPropertyNamesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  base() { return 0; }
                }
                class Example extends Base {
                  alpha() { return 1; }
                  beta() { return 2; }
                }
                const objectNames = Object.getOwnPropertyNames({ a: 1, b: 2 });
                const prototypeNames = Object.getOwnPropertyNames(Example.prototype);
                const parentNames = Object.getOwnPropertyNames(Object.getPrototypeOf(Example.prototype));
                ({
                  objectNames,
                  prototypeNames,
                  parentNames
                });
                """;
        Path root = Files.createTempDirectory("qin-js-object-get-own-property-names-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsObjectGetOwnPropertyNamesSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !containsAll(map.get("objectNames"), "a", "b")
                || !containsAll(map.get("prototypeNames"), "alpha", "beta")
                || !containsAll(map.get("parentNames"), "base")) {
            throw new IllegalStateException("Unexpected Object.getOwnPropertyNames result: " + result);
        }
        System.out.println("QinJsObjectGetOwnPropertyNamesSmokeTestMain OK");
    }

    private static boolean containsAll(Object value, String... names) {
        if (!(value instanceof List<?> list)) {
            return false;
        }
        for (String name : names) {
            if (!list.contains(name)) {
                return false;
            }
        }
        return true;
    }
}
