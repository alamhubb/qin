package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsInheritedAccessorFieldMethodSmokeTestMain {
    private QinJsInheritedAccessorFieldMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  _indent = 0;
                  get indent() {
                    return this._indent;
                  }
                  set indent(value) {
                    this._indent = value;
                  }
                  readIndent() {
                    return this.indent;
                  }
                }
                class Child extends Base {
                }
                const child = new Child();
                const before = child.readIndent();
                child.indent = child.indent + 2;
                ({
                  before,
                  after: child.readIndent(),
                  raw: child._indent
                });
                """;
        Path root = Files.createTempDirectory("qin-js-inherited-accessor-field-method-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsInheritedAccessorFieldMethodSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Double.valueOf(0.0d).equals(map.get("before"))
                || !Double.valueOf(2.0d).equals(map.get("after"))
                || !Double.valueOf(2.0d).equals(map.get("raw"))) {
            throw new IllegalStateException("Unexpected inherited accessor field method result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsInheritedAccessorFieldMethodSmokeTestMain OK");
    }
}
