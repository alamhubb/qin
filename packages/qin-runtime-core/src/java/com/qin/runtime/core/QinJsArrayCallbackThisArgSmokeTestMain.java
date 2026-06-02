package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayCallbackThisArgSmokeTestMain {
    private QinJsArrayCallbackThisArgSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const ctx = { suffix: "!" };
                const result = ["a"].map(function(value) {
                  return value + this.suffix;
                }, ctx);
                result[0] === "a!" ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-array-callback-thisarg-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-array-callback-thisarg\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "array_callback_thisarg");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsArrayCallbackThisArgSmokeTestMain OK");
    }
}

