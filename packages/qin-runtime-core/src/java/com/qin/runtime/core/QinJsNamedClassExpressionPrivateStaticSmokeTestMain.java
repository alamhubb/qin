package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsNamedClassExpressionPrivateStaticSmokeTestMain {
    private QinJsNamedClassExpressionPrivateStaticSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-named-class-private-static-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const Stack = class c {
                  static #open = false;

                  static create(size) {
                    c.#open = true;
                    const value = new c(size);
                    c.#open = false;
                    return value;
                  }

                  constructor(size) {
                    if (!c.#open) {
                      throw new TypeError("use create");
                    }
                    this.size = size;
                  }
                };

                Stack.create(7).size;
                """, "js_named_class_expression_private_static");

        if (!(result instanceof Number number) || number.doubleValue() != 7.0d) {
            throw new IllegalStateException("Expected Stack.create(7).size = 7, got: " + result);
        }
        System.out.println("QinJsNamedClassExpressionPrivateStaticSmokeTestMain OK");
    }
}
