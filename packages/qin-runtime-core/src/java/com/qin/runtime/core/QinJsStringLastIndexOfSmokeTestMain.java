package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsStringLastIndexOfSmokeTestMain {
    private QinJsStringLastIndexOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const text = "a\\nb\\n";
                ({
                  last: text.lastIndexOf("\\n"),
                  bounded: text.lastIndexOf("\\n", 2)
                });
                """;
        Path root = Files.createTempDirectory("qin-js-string-last-index-of-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStringLastIndexOfSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!numberEquals(map.get("last"), 3)
                || !numberEquals(map.get("bounded"), 1)) {
            throw new IllegalStateException("Unexpected String.lastIndexOf result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsStringLastIndexOfSmokeTestMain OK");
    }

    private static boolean numberEquals(Object value, int expected) {
        return value instanceof Number number && number.intValue() == expected;
    }
}
