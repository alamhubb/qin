package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Linked Qin source after local module imports are resolved.
 */
public record QinLinkedSource(
        Path entryFile,
        String source,
        List<Path> modules) {
    public QinLinkedSource {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(modules, "modules cannot be null");
        modules = List.copyOf(modules);
    }
}
