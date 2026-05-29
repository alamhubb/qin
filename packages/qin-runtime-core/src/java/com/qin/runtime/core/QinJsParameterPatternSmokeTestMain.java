package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsParameterPatternSmokeTestMain {
    private QinJsParameterPatternSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function read({ value }, fallback = 1, ...rest) {
                  return value + fallback + rest[0];
                }
                const result = read({ value: 40 }, undefined, 1);
                """;
        Path root = Files.createTempDirectory("qin-js-parameter-pattern-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsParameterPatternSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected parameter pattern result 42, got: " + result);
        }
        System.out.println("QinJsParameterPatternSmokeTestMain OK");
    }
}
