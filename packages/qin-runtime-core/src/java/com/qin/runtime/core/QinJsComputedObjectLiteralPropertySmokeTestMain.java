package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsComputedObjectLiteralPropertySmokeTestMain {
    private QinJsComputedObjectLiteralPropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-computed-object-literal-property-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                const key = "property";
                const obj = { [key]: 123 };
                function wrapBlock(wrapperName, normalized) {
                  return { [wrapperName]: normalized };
                }
                const wrapArrow = (wrapperName, normalized) => {
                  return { [wrapperName]: normalized };
                };
                const mapped = [1].map(item => {
                  const normalized = item + 2;
                  return { [key]: normalized };
                })[0];
                ({
                  value: obj.property,
                  wrongLiteralKey: obj.key,
                  blockValue: wrapBlock(key, 456).property,
                  blockWrongLiteralKey: wrapBlock(key, 456).key,
                  arrowValue: wrapArrow(key, 789).property,
                  arrowWrongLiteralKey: wrapArrow(key, 789).key,
                  mappedValue: mapped.property,
                  mappedWrongLiteralKey: mapped.key
                });
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.ComputedObjectLiteralPropertySmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        Object value = map.get("value");
        if (!(value instanceof Number number) || number.intValue() != 123) {
            throw new IllegalStateException("Computed object literal key did not evaluate to property: " + map);
        }
        if (map.get("wrongLiteralKey") != null) {
            throw new IllegalStateException("Computed object literal key was emitted as a literal key: " + map);
        }
        requireNumber(map, "blockValue", 456);
        requireNull(map, "blockWrongLiteralKey");
        requireNumber(map, "arrowValue", 789);
        requireNull(map, "arrowWrongLiteralKey");
        requireNumber(map, "mappedValue", 3);
        requireNull(map, "mappedWrongLiteralKey");
        System.out.println("QinJsComputedObjectLiteralPropertySmokeTestMain OK");
    }

    private static void requireNumber(Map<?, ?> map, String key, int expected) {
        Object value = map.get(key);
        if (!(value instanceof Number number) || number.intValue() != expected) {
            throw new IllegalStateException("Expected " + key + "=" + expected + ", got: " + map);
        }
    }

    private static void requireNull(Map<?, ?> map, String key) {
        if (map.get(key) != null) {
            throw new IllegalStateException("Expected " + key + " to be null, got: " + map);
        }
    }
}
