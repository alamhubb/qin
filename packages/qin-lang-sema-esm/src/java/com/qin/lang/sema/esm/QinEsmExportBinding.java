package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.Objects;

/**
 * One export binding extracted from source.
 */
public record QinEsmExportBinding(
        Path sourceFile,
        QinEsmExportKind kind,
        String exportName,
        String localName,
        boolean typeOnly,
        String moduleSpecifier,
        Path resolvedModule,
        int line,
        int column) {
    public QinEsmExportBinding {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(kind, "kind cannot be null");
        Objects.requireNonNull(exportName, "exportName cannot be null");
        Objects.requireNonNull(localName, "localName cannot be null");
    }
}
