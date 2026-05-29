package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTypedClassMethodSmokeTestMain {
    private QinJsTypedClassMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Tool {
                  generator(node: unknown, tokens: unknown[]): number {
                    return 40 + tokens.length;
                  }
                }
                const tool = new Tool();
                tool.generator({}, [1, 2]);
                """;
        Path root = Files.createTempDirectory("qin-js-typed-class-method-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsTypedClassMethodSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected typed class method result 42, got: " + result);
        }
        System.out.println("QinJsTypedClassMethodSmokeTestMain OK");
    }
}
