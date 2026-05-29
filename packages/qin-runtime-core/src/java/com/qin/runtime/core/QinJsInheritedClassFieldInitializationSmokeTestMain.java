package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsInheritedClassFieldInitializationSmokeTestMain {
    private QinJsInheritedClassFieldInitializationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  _parseSuccess = true;
                  baseValue = 40;
                  get parserFail() {
                    return !this._parseSuccess;
                  }
                }
                class Child extends Base {
                  childValue = 2;
                }
                const child = new Child();
                ({
                  parserFail: child.parserFail,
                  parseSuccess: child._parseSuccess,
                  total: child.baseValue + child.childValue
                });
                """;
        Path root = Files.createTempDirectory("qin-js-inherited-class-field-initialization-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsInheritedClassFieldInitializationSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.FALSE.equals(map.get("parserFail"))
                || !Boolean.TRUE.equals(map.get("parseSuccess"))
                || !Double.valueOf(42.0d).equals(map.get("total"))) {
            throw new IllegalStateException("Unexpected inherited class field result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsInheritedClassFieldInitializationSmokeTestMain OK");
    }
}
