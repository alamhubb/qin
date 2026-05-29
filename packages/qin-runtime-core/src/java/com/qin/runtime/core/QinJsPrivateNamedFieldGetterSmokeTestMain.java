package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPrivateNamedFieldGetterSmokeTestMain {
    private QinJsPrivateNamedFieldGetterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Tool {
                  private _indent = 0;
                  get indent() {
                    return this._indent;
                  }
                }
                const result = new Tool().indent;
                """;
        Path root = Files.createTempDirectory("qin-js-private-named-field-getter-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsPrivateNamedFieldGetterSmoke");
        if (!Double.valueOf(0).equals(result) && !Integer.valueOf(0).equals(result)) {
            throw new IllegalStateException("Expected private named field getter 0, got: " + result);
        }
        System.out.println("QinJsPrivateNamedFieldGetterSmokeTestMain OK");
    }
}
