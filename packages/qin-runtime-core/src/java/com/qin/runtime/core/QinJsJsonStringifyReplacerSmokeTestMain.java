package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsJsonStringifyReplacerSmokeTestMain {
    private QinJsJsonStringifyReplacerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const text = JSON.stringify(
                  { a: 1, nested: { b: 2 } },
                  (_, val) => typeof val === "number" ? val + 1 : val,
                  2
                );
                text === "{\\"a\\":2,\\"nested\\":{\\"b\\":3}}" ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-json-stringify-replacer-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-json-stringify-replacer\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "json_stringify_replacer");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsJsonStringifyReplacerSmokeTestMain OK");
    }
}

