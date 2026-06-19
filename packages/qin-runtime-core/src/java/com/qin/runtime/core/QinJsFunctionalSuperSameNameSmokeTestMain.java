package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsFunctionalSuperSameNameSmokeTestMain {
    private QinJsFunctionalSuperSameNameSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function __qin_java_functional(fn) {
                  fn.get = () => fn();
                  fn.run = () => fn();
                  return fn;
                }
                class Base {
                  rule() { return "base"; }
                }
                class Mid extends Base {
                  rule() {
                    const supplier = __qin_java_functional(() => super.rule());
                    return "mid>" + supplier.get();
                  }
                }
                class Child extends Mid {
                  rule() {
                    const supplier = __qin_java_functional(() => super.rule());
                    return "child>" + supplier.run();
                  }
                }
                new Child().rule();
                """;
        Path root = Files.createTempDirectory("qin-js-functional-super-same-name-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsFunctionalSuperSameNameSmoke");
        if (!"child>mid>base".equals(result)) {
            throw new IllegalStateException("Expected child>mid>base, got: " + result);
        }
        System.out.println("QinJsFunctionalSuperSameNameSmokeTestMain OK");
    }
}
