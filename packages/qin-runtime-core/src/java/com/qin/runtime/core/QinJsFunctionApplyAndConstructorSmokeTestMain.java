package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionApplyAndConstructorSmokeTestMain {
    private QinJsFunctionApplyAndConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Tool {
                  constructor() {
                    this.seenName = this.constructor.name;
                  }
                  value(a, b) {
                    return a + b;
                  }
                }

                const tool = new Tool();
                const fn = tool.value;
                ({
                  name: tool.seenName,
                  hasOwnValue: tool.hasOwnProperty("value"),
                  applied: fn.apply(tool, [20, 22])
                });
                """;
        Path root = Files.createTempDirectory("qin-js-function-apply-constructor-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsFunctionApplyAndConstructorSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!"Tool".equals(map.get("name"))) {
            throw new IllegalStateException("Expected constructor.name Tool, got: " + map.get("name"));
        }
        if (!Boolean.FALSE.equals(map.get("hasOwnValue"))) {
            throw new IllegalStateException("Expected hasOwnProperty('value') false, got: " + map.get("hasOwnValue"));
        }
        if (!Double.valueOf(42.0d).equals(map.get("applied"))) {
            throw new IllegalStateException("Expected apply result 42, got: " + map.get("applied"));
        }
        System.out.println("QinJsFunctionApplyAndConstructorSmokeTestMain OK");
    }
}
