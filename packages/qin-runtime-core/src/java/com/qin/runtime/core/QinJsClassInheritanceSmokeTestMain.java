package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public final class QinJsClassInheritanceSmokeTestMain {
    private QinJsClassInheritanceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class B { Program() { return 123; } }
                class A extends B {}
                const a = new A();
                const result = a.Program();
                """;
        Path root = Files.createTempDirectory("qin-js-class-inheritance-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsClassInheritanceSmoke");
        if (!Double.valueOf(123.0).equals(result)) {
            throw new IllegalStateException("Expected inherited method result 123, got: " + result);
        }
        System.out.println("QinJsClassInheritanceSmokeTestMain OK");
    }
}
