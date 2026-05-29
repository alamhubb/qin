package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsNativeFunctionPropertySmokeTestMain {
    private QinJsNativeFunctionPropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const fn = Object.keys;
                fn.extra = 42;
                fn.extra;
                """;
        Path root = Files.createTempDirectory("qin-js-native-function-property-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsNativeFunctionPropertySmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsNativeFunctionPropertySmokeTestMain OK");
    }
}
