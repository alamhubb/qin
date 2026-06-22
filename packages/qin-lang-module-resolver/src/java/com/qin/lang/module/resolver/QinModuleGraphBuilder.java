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
    @FunctionalInterface
    public interface ImportVirtualizer {
        String virtualSource(
                Path importerFile,
                QinImportDescriptor descriptor,
                Path resolvedModule,
                String resolvedSource);
    }

    private final QinImportParser importParser = new QinImportParser();
    private final QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();
    private static final char UTF8_BOM = '\uFEFF';
    private final ImportVirtualizer importVirtualizer;

    public QinModuleGraphBuilder() {
        this(null);
    }

    public QinModuleGraphBuilder(ImportVirtualizer importVirtualizer) {
        this.importVirtualizer = importVirtualizer;
    }

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

        String source = readModuleSource(currentFile);
        List<QinImportDescriptor> imports = isBinaryAssetModule(currentFile)
                ? List.of()
                : importParser.parse(currentFile, source);
        List<QinResolvedImport> resolvedImports = new ArrayList<>();
        for (QinImportDescriptor descriptor : imports) {
            Path resolvedModule = null;
            if (!descriptor.typeOnly()
                    && (descriptor.kind() == QinImportKind.LOCAL || descriptor.kind() == QinImportKind.JS)) {
                resolvedModule = specifierResolver.resolveModule(
                        currentFile,
                        descriptor.moduleSpecifier());
                if (resolvedModule != null) {
                    String virtualSource = virtualSourceOrNull(
                            currentFile,
                            descriptor,
                            resolvedModule);
                    if (virtualSource != null) {
                        ordered.putIfAbsent(
                                resolvedModule,
                                new QinModuleSource(resolvedModule, virtualSource, List.of()));
                    } else {
                        visit(resolvedModule, ordered, visiting);
                    }
                }
            }
            resolvedImports.add(new QinResolvedImport(descriptor, resolvedModule));
        }

        ordered.put(currentFile, new QinModuleSource(currentFile, source, resolvedImports));
        visiting.remove(currentFile);
    }

    private String virtualSourceOrNull(
            Path importerFile,
            QinImportDescriptor descriptor,
            Path resolvedModule) throws IOException {
        if (importVirtualizer == null || resolvedModule == null) {
            return null;
        }
        String resolvedSource = readModuleSource(resolvedModule);
        String virtualSource = importVirtualizer.virtualSource(
                importerFile,
                descriptor,
                resolvedModule,
                resolvedSource);
        return virtualSource == null || virtualSource.isBlank() ? null : virtualSource;
    }

    private String stripUtf8Bom(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        return source.charAt(0) == UTF8_BOM ? source.substring(1) : source;
    }

    private String readModuleSource(Path file) throws IOException {
        if (isBinaryAssetModule(file)) {
            return "";
        }
        return stripUtf8Bom(Files.readString(file, StandardCharsets.UTF_8));
    }

    private boolean isBinaryAssetModule(Path file) {
        String fileName = file == null || file.getFileName() == null
                ? ""
                : file.getFileName().toString().toLowerCase();
        return fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp")
                || fileName.endsWith(".ico")
                || fileName.endsWith(".avif");
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
