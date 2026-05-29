package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSetSpreadSmokeTestMain {
    private QinJsSetSpreadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const set = new Set();
                set.add(20);
                set.add(22);
                const values = [...set];
                values[0] + values[1] + values.length;
                """;
        Path root = Files.createTempDirectory("qin-js-set-spread-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSetSpreadSmoke");
        if (!Double.valueOf(44.0d).equals(result)) {
            throw new IllegalStateException("Expected Set spread sum 44, got: " + result);
        }
        System.out.println("QinJsSetSpreadSmokeTestMain OK");
    }
}
