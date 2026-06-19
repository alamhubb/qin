package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsMultiLevelSuperSameNameSmokeTestMain {
    private QinJsMultiLevelSuperSameNameSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  rule() { return "base"; }
                }
                class Mid extends Base {
                  rule() { return "mid>" + super.rule(); }
                }
                class Child extends Mid {
                  rule() { return "child>" + super.rule(); }
                }
                new Child().rule();
                """;
        Path root = Files.createTempDirectory("qin-js-multi-super-same-name-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsMultiLevelSuperSameNameSmoke");
        if (!"child>mid>base".equals(result)) {
            throw new IllegalStateException("Expected child>mid>base, got: " + result);
        }
        System.out.println("QinJsMultiLevelSuperSameNameSmokeTestMain OK");
    }
}
