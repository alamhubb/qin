package com.qin.lang.module.resolver;

import com.qin.lang.module.policy.QinImportDescriptor;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Linked source result for downstream frontend lowering.
 */
public record QinLinkedModuleSource(
        Path entryFile,
        String source,
        List<Path> modules,
        List<QinImportDescriptor> imports,
        QinModuleGraph moduleGraph) {
    public QinLinkedModuleSource {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(modules, "modules cannot be null");
        Objects.requireNonNull(imports, "imports cannot be null");
        Objects.requireNonNull(moduleGraph, "moduleGraph cannot be null");
        modules = List.copyOf(modules);
        imports = List.copyOf(imports);
    }
}
