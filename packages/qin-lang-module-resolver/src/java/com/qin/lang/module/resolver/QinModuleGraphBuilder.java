package com.qin.lang.module.resolver;

import com.qin.lang.module.policy.QinImportDescriptor;
import com.qin.lang.module.policy.QinImportKind;
import com.qin.lang.module.policy.QinImportParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds Qin ESM module graph from an entry source file.
 */
public final class QinModuleGraphBuilder {
    private final QinImportParser importParser = new QinImportParser();
    private final QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();

    public QinModuleGraph build(Path entryFile) throws IOException {
        Path entry = requireFile(entryFile);
        Map<Path, QinModuleSource> ordered = new LinkedHashMap<>();
        Set<Path> visiting = new HashSet<>();
        visit(entry, ordered, visiting);
        return new QinModuleGraph(entry, new ArrayList<>(ordered.values()));
    }

    private void visit(
            Path currentFile,
            Map<Path, QinModuleSource> ordered,
            Set<Path> visiting) throws IOException {
        if (ordered.containsKey(currentFile)) {
            return;
        }
        if (!visiting.add(currentFile)) {
            // ESM allows cyclic dependencies. Keep graph construction moving and
            // leave invalid cycle behavior to semantic/runtime phases.
            return;
        }

        String source = Files.readString(currentFile, StandardCharsets.UTF_8);
        List<QinImportDescriptor> imports = importParser.parse(currentFile, source);
        List<QinResolvedImport> resolvedImports = new ArrayList<>();
        for (QinImportDescriptor descriptor : imports) {
            Path resolvedModule = null;
            if (descriptor.kind() == QinImportKind.LOCAL || descriptor.kind() == QinImportKind.JS) {
                resolvedModule = specifierResolver.resolveModule(
                        currentFile,
                        descriptor.moduleSpecifier());
                if (resolvedModule != null) {
                    visit(resolvedModule, ordered, visiting);
                }
            }
            resolvedImports.add(new QinResolvedImport(descriptor, resolvedModule));
        }

        ordered.put(currentFile, new QinModuleSource(currentFile, source, resolvedImports));
        visiting.remove(currentFile);
    }

    private Path requireFile(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("entry file cannot be null");
        }
        Path normalized = file.toAbsolutePath().normalize();
        if (!Files.exists(normalized) || !Files.isRegularFile(normalized)) {
            throw new IllegalArgumentException("Entry file does not exist: " + normalized);
        }
        return normalized;
    }
}
