package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsFunctionPrototypeMethodSmokeTestMain {
    private QinJsFunctionPrototypeMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function Box(value) {
                  this.value = value;
                  this.last = { wrong: true };
                }
                Box.prototype.read = function() {
                  return this.value;
                };
                const box = new Box(42);
                box.read();
                """;
        Path root = Files.createTempDirectory("qin-js-function-prototype-method-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-function-prototype-method\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "function_prototype_method");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsFunctionPrototypeMethodSmokeTestMain OK");
    }
}

