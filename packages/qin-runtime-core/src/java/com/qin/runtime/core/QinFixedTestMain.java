package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Fixed runner for the canonical Qin syntax test file.
 */
public final class QinFixedTestMain {
    private QinFixedTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveFixedTestFile();
        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        Object result = runner.compileAndRun(sourceFile, QinRuntimeTestConstants.GENERATED_CLASS_NAME);
        System.out.println("source file: " + sourceFile.toAbsolutePath());
        System.out.println("run() returned: " + result);
    }

    private static Path resolveFixedTestFile() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[]{
                cwd.resolve(QinRuntimeTestConstants.TEST_SOURCE_PATH),
                cwd.resolve(QinRuntimeTestConstants.ALT_TEST_SOURCE_PATH)
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot find fixed Qin test file. Checked: "
                + QinRuntimeTestConstants.TEST_SOURCE_PATH + ", "
                + QinRuntimeTestConstants.ALT_TEST_SOURCE_PATH);
    }
}
