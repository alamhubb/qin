package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsStructuralAlternativeObjectSmokeTestMain {
    private QinJsStructuralAlternativeObjectSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const alternative = {
                  alt(prefix = "plain") {
                    return prefix + "-ok";
                  }
                };

                ({
                  executeValue: alternative.execute(),
                  runValue: alternative.run(),
                  getValue: alternative.get(),
                  applyValue: alternative.apply("qin")
                });
                """;
        Path root = Files.createTempDirectory("qin-js-structural-alternative-object-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStructuralAlternativeObjectSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        assertValue(map, "executeValue", "plain-ok");
        assertValue(map, "runValue", "plain-ok");
        assertValue(map, "getValue", "plain-ok");
        assertValue(map, "applyValue", "qin-ok");
        System.out.println("QinJsStructuralAlternativeObjectSmokeTestMain OK");
    }

    private static void assertValue(Map<?, ?> map, String key, String expected) {
        Object actual = map.get(key);
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected " + key + " " + expected + ", got: " + actual);
        }
    }
}
