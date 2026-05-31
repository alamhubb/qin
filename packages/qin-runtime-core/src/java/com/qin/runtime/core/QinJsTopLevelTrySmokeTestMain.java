package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTopLevelTrySmokeTestMain {
    private QinJsTopLevelTrySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                let message = "unset";
                try {
                  throw "top-level-boom";
                } catch (error) {
                  message = error;
                } finally {
                  message = message + "-finally";
                }
                message;
                """;
        Path root = Files.createTempDirectory("qin-top-level-try-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                "com.qin.runtime.generated.JsTopLevelTrySmoke");
        if (!"top-level-boom-finally".equals(String.valueOf(result))) {
            throw new IllegalStateException("Unexpected top-level try result: " + result);
        }
        System.out.println("QinJsTopLevelTrySmokeTestMain OK");
    }
}
