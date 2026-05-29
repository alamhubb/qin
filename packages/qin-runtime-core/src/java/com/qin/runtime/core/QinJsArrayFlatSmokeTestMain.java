package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayFlatSmokeTestMain {
    private QinJsArrayFlatSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const result = [[1], [2, [3]]].flat(2).join(",");
                """;
        Path root = Files.createTempDirectory("qin-js-array-flat-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrayFlatSmoke");
        if (!"1.0,2.0,3.0".equals(result)) {
            throw new IllegalStateException("Expected flat result 1.0,2.0,3.0, got: " + result);
        }
        System.out.println("QinJsArrayFlatSmokeTestMain OK");
    }
}
