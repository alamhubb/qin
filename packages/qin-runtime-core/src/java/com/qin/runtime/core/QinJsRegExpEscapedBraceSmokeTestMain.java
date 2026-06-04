package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRegExpEscapedBraceSmokeTestMain {
    private QinJsRegExpEscapedBraceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const pattern = new RegExp("^(?:`(?:[^`\\\\\\\\$]+|\\\\\\\\[\\\\s\\\\S]|\\\\$(?!\\\\{))*`)");
                const result = {
                  miss: pattern.test("const answer = 42;"),
                  hit: pattern.test("`abc`"),
                  javaPatternMiss: __qin_java_pattern_regexp__(
                    "^(?:`(?:[^`\\\\\\\\$]+|\\\\\\\\[\\\\s\\\\S]|\\\\$(?!\\\\{))*`)",
                    ""
                  ).test("const answer = 42;")
                };
                """;
        Path root = Files.createTempDirectory("qin-js-regexp-escaped-brace-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRegExpEscapedBraceSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !Boolean.FALSE.equals(map.get("miss"))
                || !Boolean.TRUE.equals(map.get("hit"))
                || !Boolean.FALSE.equals(map.get("javaPatternMiss"))) {
            throw new IllegalStateException("Unexpected RegExp escaped brace result: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsRegExpEscapedBraceSmokeTestMain OK");
    }
}
