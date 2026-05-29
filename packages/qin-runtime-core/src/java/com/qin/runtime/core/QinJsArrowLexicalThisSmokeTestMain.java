package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrowLexicalThisSmokeTestMain {
    private QinJsArrowLexicalThisSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Tool {
                  value = 41;
                  run() {
                    const item = { alt: () => this.value + 1 };
                    return item.alt();
                  }
                }

                new Tool().run();
                """;
        Path root = Files.createTempDirectory("qin-js-arrow-lexical-this-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrowLexicalThisSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected arrow lexical this result 42, got: " + result);
        }
        System.out.println("QinJsArrowLexicalThisSmokeTestMain OK");
    }
}
