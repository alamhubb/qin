package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayPatternCallbackSmokeTestMain {
    private QinJsArrayPatternCallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const result = Object.entries({ filter: 'brightness(1.15)' })
                  .map(([prop, val]) => `${prop}: ${val}`)
                  .join('; ');
                (result);
                """;
        Path root = Files.createTempDirectory("qin-js-array-pattern-callback-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrayPatternCallbackSmoke");
        if (!"filter: brightness(1.15)".equals(result)) {
            throw new IllegalStateException("Expected destructured entry string, got: " + result);
        }
        System.out.println("QinJsArrayPatternCallbackSmokeTestMain OK");
    }
}
