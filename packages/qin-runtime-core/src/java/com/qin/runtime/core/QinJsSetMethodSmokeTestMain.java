package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSetMethodSmokeTestMain {
    private QinJsSetMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const set = new Set();
                set.add(20);
                set.add(22);
                const result = set.size;
                result;
                """;
        Path root = Files.createTempDirectory("qin-js-set-method-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSetMethodSmoke");
        if (!Double.valueOf(2.0d).equals(result) && !Integer.valueOf(2).equals(result)) {
            throw new IllegalStateException("Expected Set size 2, got: " + result);
        }
        System.out.println("QinJsSetMethodSmokeTestMain OK");
    }
}
