package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSetConstructorIterableSmokeTestMain {
    private QinJsSetConstructorIterableSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const first = { id: 1 };
                const set = new Set([first, first, { id: 2 }]);
                set.size;
                """;
        Path root = Files.createTempDirectory("qin-js-set-constructor-iterable-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSetConstructorIterableSmoke");
        if (!Double.valueOf(2.0d).equals(result) && !Integer.valueOf(2).equals(result)) {
            throw new IllegalStateException("Expected Set constructor iterable size 2, got: " + result);
        }
        System.out.println("QinJsSetConstructorIterableSmokeTestMain OK");
    }
}
