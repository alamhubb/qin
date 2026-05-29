package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSuperDispatchOverrideSmokeTestMain {
    private QinJsSuperDispatchOverrideSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  entry() { return this.read(); }
                  read() { return "base"; }
                }
                class Child extends Base {
                  entry() { return super.entry(); }
                  read() { return "child"; }
                }
                const result = new Child().entry();
                """;
        Path root = Files.createTempDirectory("qin-js-super-dispatch-override-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsSuperDispatchOverrideSmoke");
        if (!"child".equals(result)) {
            throw new IllegalStateException("Expected child dispatch through super.entry, got: " + result);
        }
        System.out.println("QinJsSuperDispatchOverrideSmokeTestMain OK");
    }
}
