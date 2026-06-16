package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsNestedSetMemberCallSmokeTestMain {
    private QinJsNestedSetMemberCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const context = { styles: new Set() };
                const atoms = new Set(["displayFlex", "colorBlue"]);
                for (const atom of atoms) {
                  context.styles.add(atom);
                }
                context.styles.size;
                """;
        Path root = Files.createTempDirectory("qin-js-nested-set-member-call-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsNestedSetMemberCallSmoke");
        if (!Double.valueOf(2.0d).equals(result) && !Integer.valueOf(2).equals(result)) {
            throw new IllegalStateException("Expected nested Set member call size 2, got: " + result);
        }
        System.out.println("QinJsNestedSetMemberCallSmokeTestMain OK");
    }
}
