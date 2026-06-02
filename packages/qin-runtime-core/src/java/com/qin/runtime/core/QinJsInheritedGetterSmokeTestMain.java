package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsInheritedGetterSmokeTestMain {
    private QinJsInheritedGetterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-inherited-getter-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-inherited-getter\" }\n", StandardCharsets.UTF_8);
        String source = """
                class Base {
                  constructor() {
                    this.raw = ["A", "B"];
                  }
                  get value() {
                    return this.raw;
                  }
                }
                class Child extends Base {
                  constructor() {
                    super();
                  }
                }
                const child = new Child();
                child.value.length;
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_inherited_getter");
        if (!(result instanceof Number number) || number.intValue() != 2) {
            throw new IllegalStateException("Expected inherited getter length 2, got: " + result);
        }
        System.out.println("QinJsInheritedGetterSmokeTestMain OK");
    }
}

