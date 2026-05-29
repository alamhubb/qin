package com.qin.lang.module.policy;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Lightweight import descriptor extracted from source text.
 */
public record QinImportDescriptor(
        Path sourceFile,
        String moduleSpecifier,
        QinImportKind kind,
        boolean typeOnly,
        int line,
        int column) {
    public QinImportDescriptor {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(moduleSpecifier, "moduleSpecifier cannot be null");
        Objects.requireNonNull(kind, "kind cannot be null");
    }
}
