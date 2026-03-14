package com.qin.runtime.core;

import com.qin.lang.module.policy.QinImportDescriptor;
import com.qin.lang.module.resolver.QinModuleGraph;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Linked Qin source after local module imports are resolved.
 */
public record QinLinkedSource(
        Path entryFile,
        String source,
        List<Path> modules,
        List<QinImportDescriptor> imports,
        QinModuleGraph moduleGraph) {
    public QinLinkedSource {
        Objects.requireNonNull(entryFile, "entryFile cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(modules, "modules cannot be null");
        Objects.requireNonNull(imports, "imports cannot be null");
        Objects.requireNonNull(moduleGraph, "moduleGraph cannot be null");
        modules = List.copyOf(modules);
        imports = List.copyOf(imports);
    }

    public QinLinkedSource(
            Path entryFile,
            String source,
            List<Path> modules,
            List<QinImportDescriptor> imports) {
        this(entryFile, source, modules, imports, new QinModuleGraph(entryFile, List.of()));
    }
}
