package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsLegacyMethodDecoratorSmokeTestMain {
    private QinJsLegacyMethodDecoratorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function wrap(target, propertyKey, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function () {
                    return original.call(this) + 1;
                  };
                  return descriptor;
                }

                class Tool {
                  @wrap
                  value() {
                    return 41;
                  }
                }

                const tool = new Tool();
                tool.value();
                """;
        Path root = Files.createTempDirectory("qin-js-legacy-method-decorator-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        Object result = runner.compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsLegacyMethodDecoratorSmoke");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected decorated method result 42, got: " + result);
        }
        String descriptorFallbackSource = """
                function wrap(target, propertyKey, descriptor) {
                  if (!descriptor) {
                    descriptor = {
                      value: target[propertyKey],
                      writable: true,
                      enumerable: false,
                      configurable: true
                    };
                  }
                  const original = descriptor.value;
                  descriptor.value = function () {
                    return original.call(this) + 1;
                  };
                  return descriptor;
                }

                class Tool {
                  value() {
                    return 41;
                  }
                }

                const descriptor = wrap(Tool.prototype, "value");
                const tool = new Tool();
                descriptor.value.call(tool);
                """;
        Path fallbackSourceFile = root.resolve("fallback.ts");
        Files.writeString(fallbackSourceFile, descriptorFallbackSource, StandardCharsets.UTF_8);
        Object fallbackResult = runner.compileAndRun(
                fallbackSourceFile,
                root,
                "com.qin.runtime.generated.JsLegacyMethodDecoratorFallbackSmoke");
        if (!Double.valueOf(42.0d).equals(fallbackResult)) {
            throw new IllegalStateException("Expected fallback decorated method result 42, got: " + fallbackResult);
        }
        System.out.println("QinJsLegacyMethodDecoratorSmokeTestMain OK");
    }
}
