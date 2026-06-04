package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsConditionalLazySmokeTestMain {
    private QinJsConditionalLazySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                let touched = "clean";
                const first = true ? "yes" : (() => {
                  touched = "bad-true";
                  return "no";
                })();
                const second = false ? (() => {
                  touched = "bad-false";
                  return "no";
                })() : "ok";
                first + ";" + second + ";" + touched;
                """;
        Path root = Files.createTempDirectory("qin-conditional-lazy-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                "com.qin.runtime.generated.JsConditionalLazySmoke");
        if (!"yes;ok;clean".equals(String.valueOf(result))) {
            throw new IllegalStateException("Unexpected conditional lazy result: " + result);
        }
        System.out.println("QinJsConditionalLazySmokeTestMain OK");
    }
}
