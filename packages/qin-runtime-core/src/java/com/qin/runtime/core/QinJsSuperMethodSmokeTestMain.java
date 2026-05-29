package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSuperMethodSmokeTestMain {
    private QinJsSuperMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base extends Object {
                  read(value) { return value + this.offset; }
                }
                class Child extends Base {
                  constructor() { this.offset = 1; }
                  read(value) { return super.read(value) + 1; }
                }
                const result = new Child().read(40);
                """;
        Path root = Files.createTempDirectory("qin-js-super-method-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSuperMethodSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected super method result 42, got: " + result);
        }
        System.out.println("QinJsSuperMethodSmokeTestMain OK");
    }
}
