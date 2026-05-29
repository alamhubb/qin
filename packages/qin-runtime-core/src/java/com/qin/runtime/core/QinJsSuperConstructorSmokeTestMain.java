package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSuperConstructorSmokeTestMain {
    private QinJsSuperConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  constructor(value) { this.value = value; }
                }
                class Child extends Base {
                  constructor(value) { super(value); }
                }
                const child = new Child(42);
                const result = child.value;
                """;
        Path root = Files.createTempDirectory("qin-js-super-constructor-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSuperConstructorSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected super constructor field value 42, got: " + result);
        }
        System.out.println("QinJsSuperConstructorSmokeTestMain OK");
    }
}
