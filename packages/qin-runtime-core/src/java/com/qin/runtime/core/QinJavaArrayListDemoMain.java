package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Demo runner:
 * import { ArrayList } from "java:java.util";
 * const list = new ArrayList();
 * list.add("hello");
 * console.log(list.size());
 */
public final class QinJavaArrayListDemoMain {
    private QinJavaArrayListDemoMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveSourceFile(args);
        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        Object result = runner.compileAndRun(sourceFile, "com.qin.runtime.generated.ArrayListDemo");
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
                cwd.resolve("qin/packages/qin-runtime-core/examples/java-array-list.qin"),
                cwd.resolve("packages/qin-runtime-core/examples/java-array-list.qin")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find java-array-list.qin. Pass file path as arg[0].");
    }
}
