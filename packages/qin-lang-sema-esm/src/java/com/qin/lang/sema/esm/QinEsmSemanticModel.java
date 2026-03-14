package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * Full semantic model for one Qin module graph.
 */
public record QinEsmSemanticModel(
        Path entryFile,
        Map<Path, QinEsmModuleSemantic> modules) {
    public QinEsmSemanticModel {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(modules, "modules cannot be null");
        modules = Map.copyOf(modules);
    }
}
