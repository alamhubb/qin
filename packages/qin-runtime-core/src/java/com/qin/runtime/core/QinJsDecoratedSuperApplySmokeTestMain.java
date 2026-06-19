package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsDecoratedSuperApplySmokeTestMain {
    private QinJsDecoratedSuperApplySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function Wrap(target, key, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function (...args) {
                    return "wrapped:" + original.apply(this, args);
                  };
                  return descriptor;
                }

                class Base {
                  value() {
                    return "base";
                  }
                }

                class Child extends Base {
                  @Wrap
                  value() {
                    return super.value();
                  }
                }

                new Child().value();
                """;
        Path root = Files.createTempDirectory("qin-js-decorated-super-apply-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsDecoratedSuperApplySmoke");
        if (!"wrapped:base".equals(result)) {
            throw new IllegalStateException("Expected wrapped:base, got: " + result);
        }
        System.out.println("QinJsDecoratedSuperApplySmokeTestMain OK");
    }
}
