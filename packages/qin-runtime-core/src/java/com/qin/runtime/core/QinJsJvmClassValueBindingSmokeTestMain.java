package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsJvmClassValueBindingSmokeTestMain {
    private QinJsJvmClassValueBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-jvm-class-value-binding-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class BaseValue {
                  value() {
                    return "base";
                  }
                }
                class RuntimeValue extends BaseValue {
                  value() {
                    return "ok";
                  }
                }
                const Exported = RuntimeValue;
                new Exported().value();
                """, "jvm_class_value_binding");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected JVM-compatible class value binding, got: " + result);
        }
        System.out.println("QinJsJvmClassValueBindingSmokeTestMain OK");
    }
}
