package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRuntimeClassDeclarationSmokeTestMain {
    private QinJsRuntimeClassDeclarationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-class-declaration-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-runtime-class-declaration\" }\n", StandardCharsets.UTF_8);
        String source = """
                function makeValue() {
                  class Box {
                    constructor(value) {
                      this.value = value;
                    }
                    read() {
                      return this.value;
                    }
                  }
                  return new Box(42).read();
                }
                ({ value: makeValue() });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_runtime_class_declaration");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        Object value = map.get("value");
        if (!(value instanceof Number number) || number.doubleValue() != 42d) {
            throw new IllegalStateException("Expected value == 42, got: " + value);
        }
        System.out.println("QinJsRuntimeClassDeclarationSmokeTestMain OK");
    }
}
