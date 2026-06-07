package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsInterpretedObjectFunctionPropertyCallSmokeTestMain {
    private QinJsInterpretedObjectFunctionPropertyCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-interpreted-object-function-property-call-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                function runOr() {
                  for (const alt of [{ alt: () => 7 }]) {
                    return alt.alt();
                  }
                }
                runOr();
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.InterpretedObjectFunctionPropertyCallSmoke");
        if (!(result instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected interpreted object function property call to return 7, got: " + result);
        }
        System.out.println("QinJsInterpretedObjectFunctionPropertyCallSmokeTestMain OK");
    }
}
