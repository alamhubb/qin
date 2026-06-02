package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinJsInterpretedFunctionCallSmokeTestMain {
    private QinJsInterpretedFunctionCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                function Base(value) {
                  this.value = value;
                  return this;
                }
                function Child(value) {
                  const self = Base.call(this, value) || this;
                  return self;
                }
                const instance = {};
                Child.call(instance, "ok");
                instance.value;
                """, "js_interpreted_function_call");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected Function.prototype.call to bind this, got: " + result);
        }
        System.out.println("QinJsInterpretedFunctionCallSmokeTestMain OK");
    }
}
