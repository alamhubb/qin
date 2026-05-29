package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsStringSubstrSmokeTestMain {
    private QinJsStringSubstrSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const value = "ab".substr(-1);
                value;
                """;
        Path root = Files.createTempDirectory("qin-js-string-substr-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStringSubstrSmoke");
        if (!"b".equals(result)) {
            throw new IllegalStateException("Expected b, got: " + result);
        }
        System.out.println("QinJsStringSubstrSmokeTestMain OK");
    }
}
