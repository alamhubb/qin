package com.qin.lang.lowering.jvm;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Context object for JVM lowering stage.
 */
public record QinEsmJvmLoweringContext(
        Path entryFile,
        List<Path> orderedModules) {
    public QinEsmJvmLoweringContext {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(orderedModules, "orderedModules cannot be null");
        orderedModules = List.copyOf(orderedModules);
    }
}
