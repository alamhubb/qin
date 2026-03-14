package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.Objects;

/**
 * One import binding extracted from source.
 */
public record QinEsmImportBinding(
        Path sourceFile,
        String moduleSpecifier,
        QinEsmImportKind kind,
        String importedName,
        String localName,
        int line,
        int column,
        Path resolvedModule) {
    public QinEsmImportBinding {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(moduleSpecifier, "moduleSpecifier cannot be null");
        Objects.requireNonNull(kind, "kind cannot be null");
        Objects.requireNonNull(importedName, "importedName cannot be null");
        Objects.requireNonNull(localName, "localName cannot be null");
    }
}
