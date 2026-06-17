package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionalAdapterMethodsSmokeTestMain {
    private QinJsFunctionalAdapterMethodsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function value(prefix) {
                  return prefix ? prefix + "-ok" : "ok";
                }

                ({
                  getValue: value.get(),
                  runValue: value.run(),
                  executeValue: value.execute(),
                  applyValue: value.apply("qin")
                });
                """;
        Path root = Files.createTempDirectory("qin-js-functional-adapter-methods-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsFunctionalAdapterMethodsSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        assertValue(map, "getValue", "ok");
        assertValue(map, "runValue", "ok");
        assertValue(map, "executeValue", "ok");
        assertValue(map, "applyValue", "qin-ok");
        System.out.println("QinJsFunctionalAdapterMethodsSmokeTestMain OK");
    }

    private static void assertValue(Map<?, ?> map, String key, String expected) {
        Object actual = map.get(key);
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected " + key + " " + expected + ", got: " + actual);
        }
    }
}
