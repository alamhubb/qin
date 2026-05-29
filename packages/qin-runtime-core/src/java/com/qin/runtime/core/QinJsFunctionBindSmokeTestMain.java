package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsFunctionBindSmokeTestMain {
    private QinJsFunctionBindSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const receiver = { value: 41 };
                const sourceObject = {
                  read(extra) { return this.value + extra; }
                };
                const bound = sourceObject.read.bind(receiver, 1);
                const result = bound();
                """;
        Path root = Files.createTempDirectory("qin-js-function-bind-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsFunctionBindSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected bind result 42, got: " + result);
        }
        System.out.println("QinJsFunctionBindSmokeTestMain OK");
    }
}
