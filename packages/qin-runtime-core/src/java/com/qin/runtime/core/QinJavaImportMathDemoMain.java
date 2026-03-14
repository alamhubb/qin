package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Demo runner:
 * import { Math } from "java:java.lang";
 * console.log(Math.random());
 */
public final class QinJavaImportMathDemoMain {
    private QinJavaImportMathDemoMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveSourceFile(args);
        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        Object result = runner.compileAndRun(sourceFile, "com.qin.runtime.generated.MathRandomDemo");
        System.out.println("source file: " + sourceFile.toAbsolutePath());
        System.out.println("run() returned: " + result);
    }

    private static Path resolveSourceFile(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) {
            Path file = Path.of(args[0]).toAbsolutePath().normalize();
            return QinInMemoryJvmRunner.requireFile(file);
        }
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[]{
                cwd.resolve("qin/packages/qin-runtime-core/examples/java-import-math.js"),
                cwd.resolve("packages/qin-runtime-core/examples/java-import-math.js")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find java-import-math.js. Pass file path as arg[0].");
    }
}
