package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Immutable build request for runtime orchestration.
 */
public record QinBuildRequest(
        Path rootDir,
        Path sourceFile,
        QinBuildTarget target,
        String className,
        Path classOutputDir,
        Path jsOutputFile,
        boolean printIr) {
    public QinBuildRequest {
        Objects.requireNonNull(rootDir, "rootDir cannot be null");
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(className, "className cannot be null");
        Objects.requireNonNull(classOutputDir, "classOutputDir cannot be null");
        Objects.requireNonNull(jsOutputFile, "jsOutputFile cannot be null");
    }
}
