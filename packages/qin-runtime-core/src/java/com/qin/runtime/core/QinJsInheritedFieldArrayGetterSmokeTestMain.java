package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsInheritedFieldArrayGetterSmokeTestMain {
    private QinJsInheritedFieldArrayGetterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-inherited-field-array-getter-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-inherited-field-array-getter\" }\n", StandardCharsets.UTF_8);

        String source = """
                class Base {
                  constructor() {
                    this.items = [];
                  }
                  get size() {
                    return this.items.length;
                  }
                }
                class Child extends Base {
                  constructor() {
                    super();
                  }
                }
                const child = new Child();
                ({ size: child.size, hasItems: child.items !== undefined });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_inherited_field_array_getter");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        Object size = map.get("size");
        Object hasItems = map.get("hasItems");
        if (!(size instanceof Number number) || number.intValue() != 0 || !Boolean.TRUE.equals(hasItems)) {
            throw new IllegalStateException("Unexpected inherited field getter result: " + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsInheritedFieldArrayGetterSmokeTestMain OK");
    }
}
