package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRegexLiteralSmokeTestMain {
    private QinJsRegexLiteralSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const pattern = /^[A-Z0-9]/;
                ({
                  yes: pattern.test("Red"),
                  no: pattern.test("red")
                });
                """;
        Path root = Files.createTempDirectory("qin-js-regex-literal-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRegexLiteralSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("yes")) || !Boolean.FALSE.equals(map.get("no"))) {
            throw new IllegalStateException("Unexpected regex literal result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsRegexLiteralSmokeTestMain OK");
    }
}
