package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsStringValueOfSmokeTestMain {
    private QinJsStringValueOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const text = "const";
                ({
                  value: text.valueOf(),
                  string: text.toString()
                });
                """;
        Path root = Files.createTempDirectory("qin-js-string-value-of-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStringValueOfSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!"const".equals(map.get("value")) || !"const".equals(map.get("string"))) {
            throw new IllegalStateException("Unexpected String valueOf/toString result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsStringValueOfSmokeTestMain OK");
    }
}
