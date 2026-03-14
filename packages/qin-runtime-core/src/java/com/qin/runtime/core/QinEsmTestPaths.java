package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

final class QinEsmTestPaths {
    private QinEsmTestPaths() {
    }

    static Path resolveStage1Root() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/esm-stage1"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/esm-stage1"),
                cwd.resolve("examples/esm-stage1")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/esm-stage1 directory.");
    }
}

