package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsObjectGetOwnPropertySymbolsSmokeTestMain {
    private QinJsObjectGetOwnPropertySymbolsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const key = Symbol('answer');
                const obj = {};
                obj[key] = 42;
                const symbols = Object.getOwnPropertySymbols(obj);
                symbols.length;
                """;
        Path root = Files.createTempDirectory("qin-js-object-symbols-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsObjectSymbolsSmoke");
        if (!Double.valueOf(1.0d).equals(result) && !Integer.valueOf(1).equals(result)) {
            throw new IllegalStateException("Expected one symbol, got: " + result);
        }
        System.out.println("QinJsObjectGetOwnPropertySymbolsSmokeTestMain OK");
    }
}
