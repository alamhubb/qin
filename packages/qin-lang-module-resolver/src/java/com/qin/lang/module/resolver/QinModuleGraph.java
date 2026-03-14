package com.qin.lang.module.resolver;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Resolved Qin module graph in dependency-first order.
 */
public record QinModuleGraph(
        Path entryFile,
        List<QinModuleSource> modules) {
    public QinModuleGraph {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(modules, "modules cannot be null");
        modules = List.copyOf(modules);
    }
}
