package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Semantic view of one Qin module's import/export bindings.
 */
public record QinEsmModuleSemantic(
        Path sourceFile,
        List<QinEsmImportBinding> imports,
        List<QinEsmExportBinding> exports) {
    public QinEsmModuleSemantic {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(imports, "imports cannot be null");
        Objects.requireNonNull(exports, "exports cannot be null");
        imports = List.copyOf(imports);
        exports = List.copyOf(exports);
    }
}
