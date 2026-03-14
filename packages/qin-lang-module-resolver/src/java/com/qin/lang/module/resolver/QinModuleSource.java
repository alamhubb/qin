package com.qin.lang.module.resolver;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * One module source unit inside a resolved graph.
 */
public record QinModuleSource(
        Path file,
        String source,
        List<QinResolvedImport> imports) {
    public QinModuleSource {
        Objects.requireNonNull(file, "file cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(imports, "imports cannot be null");
        imports = List.copyOf(imports);
    }
}
