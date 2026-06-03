package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTypedClassValueBindingSmokeTestMain {
    private QinJsTypedClassValueBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-typed-class-value-binding-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class BaseValue {
                  value(): string {
                    return "base";
                  }
                }
                class RuntimeValue extends BaseValue {
                  value(): string {
                    return "ok";
                  }
                }
                const Exported = RuntimeValue;
                new Exported().value();
                """, "typed_class_value_binding");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected typed class value binding, got: " + result);
        }
        System.out.println("QinJsTypedClassValueBindingSmokeTestMain OK");
    }
}
