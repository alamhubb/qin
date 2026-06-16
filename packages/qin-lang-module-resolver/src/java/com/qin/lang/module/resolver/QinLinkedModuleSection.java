package com.qin.lang.module.resolver;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Rewritten source for one module before the final linked-source concatenation.
 */
public record QinLinkedModuleSection(
        Path file,
        int index,
        String source,
        String classSource) {
    public QinLinkedModuleSection {
        Objects.requireNonNull(file, "file cannot be null");
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        source = source == null ? "" : source;
        classSource = classSource == null ? "" : classSource;
    }
}
