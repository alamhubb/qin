package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Minimal launcher: compile and run examples/testqin.js through Qin JVM pipeline.
 */
public final class TestQinJsRunMain {
    private static final String GENERATED_CLASS_NAME = "com.qin.runtime.generated.TestQinJs";

    private TestQinJsRunMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveTestQinFile();
        Object result = new QinInMemoryJvmRunner().compileAndRun(sourceFile, GENERATED_CLASS_NAME);
        System.out.println("run() return: " + result);
    }

    private static Path resolveTestQinFile() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/testqin.js"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/testqin.js"),
                cwd.resolve("examples/testqin.js")
        };
        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/testqin.js");
    }
}
