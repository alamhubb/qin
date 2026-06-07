package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsConstructorAssignedMethodPropertySmokeTestMain {
    private QinJsConstructorAssignedMethodPropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-constructor-assigned-method-property-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                class Alternative {
                  constructor(alt) {
                    this.alt = alt;
                  }
                  static of(supplier) {
                    return new Alternative(supplier);
                  }
                }
                const alt = Alternative.of(() => 7);
                alt.alt();
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.ConstructorAssignedMethodPropertySmoke");
        if (!(result instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected constructor-assigned method property to return 7, got: " + result);
        }
        System.out.println("QinJsConstructorAssignedMethodPropertySmokeTestMain OK");
    }
}
