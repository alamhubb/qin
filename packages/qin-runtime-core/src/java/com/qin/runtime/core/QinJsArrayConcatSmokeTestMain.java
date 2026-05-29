package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayConcatSmokeTestMain {
    private QinJsArrayConcatSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const value = [1].concat([2], 3).join(",");
                value;
                """;
        Path root = Files.createTempDirectory("qin-js-array-concat-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrayConcatSmoke");
        if (!"1.0,2.0,3.0".equals(result)) {
            throw new IllegalStateException("Expected 1.0,2.0,3.0, got: " + result);
        }
        System.out.println("QinJsArrayConcatSmokeTestMain OK");
    }
}
