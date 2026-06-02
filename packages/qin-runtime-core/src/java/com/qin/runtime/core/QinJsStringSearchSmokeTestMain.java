package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsStringSearchSmokeTestMain {
    private QinJsStringSearchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const text = "abc def";
                ({
                  whitespace: text.search(/\\s/),
                  stringPattern: text.search("d"),
                  missing: text.search(/z/)
                });
                """;
        Path root = Files.createTempDirectory("qin-js-string-search-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStringSearchSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!numberEquals(map.get("whitespace"), 3)
                || !numberEquals(map.get("stringPattern"), 4)
                || !numberEquals(map.get("missing"), -1)) {
            throw new IllegalStateException("Unexpected String.search result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsStringSearchSmokeTestMain OK");
    }

    private static boolean numberEquals(Object value, int expected) {
        return value instanceof Number number && number.intValue() == expected;
    }
}
