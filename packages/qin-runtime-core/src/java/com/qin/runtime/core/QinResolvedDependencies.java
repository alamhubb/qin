package com.qin.runtime.core;

import java.util.List;
import java.util.Objects;

/**
 * Resolved dependency view for runtime build pipeline.
 */
public record QinResolvedDependencies(List<String> classpathEntries) {
    public QinResolvedDependencies {
        Objects.requireNonNull(classpathEntries, "classpathEntries cannot be null");
        classpathEntries = List.copyOf(classpathEntries);
    }
}
