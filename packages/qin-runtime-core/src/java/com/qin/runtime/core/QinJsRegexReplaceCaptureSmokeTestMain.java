package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRegexReplaceCaptureSmokeTestMain {
    private QinJsRegexReplaceCaptureSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                ({
                  kebab: "backgroundColor".replace(/([a-z])([A-Z])/g, "$1-$2"),
                  whole: "abc".replace(/b/, "[$&]"),
                  dollar: "abc".replace(/b/, "$$")
                });
                """;
        Path root = Files.createTempDirectory("qin-js-regex-replace-capture-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRegexReplaceCaptureSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!"background-Color".equals(map.get("kebab"))
                || !"a[b]c".equals(map.get("whole"))
                || !"a$c".equals(map.get("dollar"))) {
            throw new IllegalStateException("Unexpected RegExp replace capture result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsRegexReplaceCaptureSmokeTestMain OK");
    }
}
