package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSetForOfSmokeTestMain {
    private QinJsSetForOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const set = new Set();
                set.add(20);
                set.add(22);
                let result = 0;
                for (const item of set.values()) {
                  result = result + item;
                }
                result + set.size;
                """;
        Path root = Files.createTempDirectory("qin-js-set-for-of-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSetForOfSmoke");
        if (!Double.valueOf(44.0d).equals(result)) {
            throw new IllegalStateException("Expected Set for-of result+size 44, got: " + result);
        }
        System.out.println("QinJsSetForOfSmokeTestMain OK");
    }
}
