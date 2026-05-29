package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsClassPrototypeOverrideSmokeTestMain {
    private QinJsClassPrototypeOverrideSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  read() { return "base"; }
                }
                class Child extends Base {
                  read() { return "child"; }
                }
                const result = Child.prototype.read();
                """;
        Path root = Files.createTempDirectory("qin-js-class-prototype-override-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsClassPrototypeOverrideSmoke");
        if (!"child".equals(result)) {
            throw new IllegalStateException("Expected child override result, got: " + result);
        }
        System.out.println("QinJsClassPrototypeOverrideSmokeTestMain OK");
    }
}
