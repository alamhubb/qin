package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsObjectFunctionPropertyCallSmokeTestMain {
    private QinJsObjectFunctionPropertyCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-object-function-property-call-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                const alt = { alt: () => 7 };
                alt.alt();
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.ObjectFunctionPropertyCallSmoke");
        if (!(result instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected object function property call to return 7, got: " + result);
        }
        System.out.println("QinJsObjectFunctionPropertyCallSmokeTestMain OK");
    }
}
