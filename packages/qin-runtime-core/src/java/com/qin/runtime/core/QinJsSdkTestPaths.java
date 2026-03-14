package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

final class QinJsSdkTestPaths {
    private QinJsSdkTestPaths() {
    }

    static Path resolveJsSdkRoot() {
        return resolveExample("js-sdk");
    }

    static Path resolveNpmBareRoot() {
        return resolveExample("npm-bare");
    }

    private static Path resolveExample(String name) {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/" + name),
                cwd.resolve("qin/packages/qin-runtime-core/examples/" + name),
                cwd.resolve("examples/" + name)
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/" + name + " directory.");
    }
}
