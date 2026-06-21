package com.qin.lang.sema.esm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validates ESM linkage constraints for Qin module graph.
 */
public final class QinEsmLinkValidator {
    private static final int MAX_EXPORT_RESOLUTION_DEPTH = 128;

    public void validate(QinEsmSemanticModel model) {
        List<QinEsmDiagnostic> diagnostics = new ArrayList<>();
        for (QinEsmModuleSemantic module : model.modules().values()) {
            validateDuplicateExports(module, diagnostics);
            validateImports(module, model, diagnostics);
        }
        if (!diagnostics.isEmpty()) {
            throw new QinEsmSemanticException(diagnostics);
        }
    }

    private void validateDuplicateExports(
            QinEsmModuleSemantic module,
            List<QinEsmDiagnostic> diagnostics) {
        Set<String> seen = new HashSet<>();
        for (QinEsmExportBinding exportBinding : module.exports()) {
            if (QinEsmExportKind.RE_EXPORT_ALL.equals(exportBinding.kind()) || exportBinding.typeOnly()) {
                continue;
            }
            if (!seen.add(exportBinding.exportName())) {
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2001",
                        "Duplicate export name: " + exportBinding.exportName(),
                        exportBinding.sourceFile(),
                        exportBinding.line(),
                        exportBinding.column()));
            }
        }
    }

    private void validateImports(
            QinEsmModuleSemantic module,
            QinEsmSemanticModel model,
            List<QinEsmDiagnostic> diagnostics) {
        Map<String, Boolean> typeDeclarationCache = new HashMap<>();
        for (QinEsmImportBinding importBinding : module.imports()) {
            if (QinEsmImportKind.SIDE_EFFECT.equals(importBinding.kind())) {
                continue;
            }
            Path resolvedModule = importBinding.resolvedModule();
            if (resolvedModule == null) {
                continue;
            }

            QinEsmModuleSemantic targetModule = model.modules().get(resolvedModule);
            if (targetModule == null) {
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2002",
                        "Imported local module is not linked in graph: " + importBinding.moduleSpecifier(),
                        importBinding.sourceFile(),
                        importBinding.line(),
                        importBinding.column()));
                continue;
            }

            String importName = importBinding.importedName();
            if ("*".equals(importName)) {
                continue;
            }

            ExportResolution resolution = resolveExportName(model, resolvedModule, importName, new HashSet<>(), 0);
            if (resolution.isAmbiguous()) {
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2004",
                        "Ambiguous import binding: " + importBinding.importedName()
                                + " from " + importBinding.moduleSpecifier(),
                        importBinding.sourceFile(),
                        importBinding.line(),
                        importBinding.column()));
            } else if (!resolution.exists()) {
                if (isDeclaredTypeOnlyImport(importBinding, typeDeclarationCache)) {
                    continue;
                }
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2003",
                        "Imported binding does not exist: " + importBinding.importedName()
                                + " from " + importBinding.moduleSpecifier(),
                        importBinding.sourceFile(),
                        importBinding.line(),
                        importBinding.column()));
            }
        }
    }

    private boolean isDeclaredTypeOnlyImport(
            QinEsmImportBinding importBinding,
            Map<String, Boolean> typeDeclarationCache) {
        Path sourceFile = importBinding.sourceFile();
        if (sourceFile == null || !isTypescriptSource(sourceFile) || importBinding.resolvedModule() == null) {
            return false;
        }
        Path declarationFile = findSiblingDeclarationFile(importBinding.resolvedModule());
        if (declarationFile == null) {
            return false;
        }
        String cacheKey = declarationFile + "::" + importBinding.importedName();
        return typeDeclarationCache.computeIfAbsent(cacheKey,
                ignored -> declarationFileDeclaresType(declarationFile, importBinding.importedName()));
    }

    private boolean isTypescriptSource(Path sourceFile) {
        String name = sourceFile.getFileName() == null ? "" : sourceFile.getFileName().toString();
        return name.endsWith(".ts") || name.endsWith(".tsx") || name.endsWith(".mts") || name.endsWith(".cts");
    }

    private Path findSiblingDeclarationFile(Path moduleFile) {
        Path parent = moduleFile.getParent();
        String fileName = moduleFile.getFileName() == null ? "" : moduleFile.getFileName().toString();
        if (parent == null || fileName.isBlank()) {
            return null;
        }
        List<String> candidates = new ArrayList<>();
        if (fileName.endsWith(".mjs")) {
            candidates.add(fileName.substring(0, fileName.length() - 4) + ".d.mts");
            candidates.add(fileName.substring(0, fileName.length() - 4) + ".d.ts");
        } else if (fileName.endsWith(".cjs")) {
            candidates.add(fileName.substring(0, fileName.length() - 4) + ".d.cts");
            candidates.add(fileName.substring(0, fileName.length() - 4) + ".d.ts");
        } else if (fileName.endsWith(".js")) {
            candidates.add(fileName.substring(0, fileName.length() - 3) + ".d.ts");
        } else if (fileName.endsWith(".ts")) {
            candidates.add(fileName);
        }
        for (String candidate : candidates) {
            Path path = parent.resolve(candidate);
            if (Files.isRegularFile(path)) {
                return path;
            }
        }
        return null;
    }

    private boolean declarationFileDeclaresType(Path declarationFile, String importedName) {
        if (importedName == null || importedName.isBlank()) {
            return false;
        }
        try {
            String source = Files.readString(declarationFile, StandardCharsets.UTF_8);
            String quotedName = Pattern.quote(importedName);
            return Pattern.compile("(?m)\\b(?:export\\s+)?(?:declare\\s+)?(?:interface|type)\\s+"
                            + quotedName + "\\b")
                    .matcher(source)
                    .find();
        } catch (IOException ignored) {
            return false;
        }
    }

    private ExportResolution resolveExportName(
            QinEsmSemanticModel model,
            Path moduleFile,
            String exportName,
            Set<String> visiting,
            int depth) {
        if (moduleFile == null || exportName == null || exportName.isBlank()) {
            return ExportResolution.notResolved();
        }
        if (depth > MAX_EXPORT_RESOLUTION_DEPTH) {
            return ExportResolution.notResolved();
        }

        String visitKey = moduleFile.toString() + "::" + exportName;
        if (!visiting.add(visitKey)) {
            // Legal cycle in ESM graph; do not treat as immediate error here.
            return ExportResolution.notResolved();
        }
        try {
            QinEsmModuleSemantic module = model.modules().get(moduleFile);
            if (module == null) {
                return ExportResolution.notResolved();
            }

            List<QinEsmExportBinding> directMatches = new ArrayList<>();
            List<QinEsmExportBinding> runtimeDirectMatches = new ArrayList<>();
            List<QinEsmExportBinding> exportAll = new ArrayList<>();
            for (QinEsmExportBinding exportBinding : module.exports()) {
                if (QinEsmExportKind.RE_EXPORT_ALL.equals(exportBinding.kind())) {
                    exportAll.add(exportBinding);
                    continue;
                }
                if (exportName.equals(exportBinding.exportName())) {
                    directMatches.add(exportBinding);
                    if (!exportBinding.typeOnly()) {
                        runtimeDirectMatches.add(exportBinding);
                    }
                }
            }

            if (runtimeDirectMatches.size() > 1) {
                return ExportResolution.ambiguousResult();
            }
            if (runtimeDirectMatches.size() == 1) {
                QinEsmExportBinding binding = runtimeDirectMatches.get(0);
                if (QinEsmExportKind.RE_EXPORT_NAMED.equals(binding.kind())) {
                    return resolveExportName(
                            model,
                            binding.resolvedModule(),
                            binding.localName(),
                            visiting,
                            depth + 1);
                }
                return ExportResolution.foundResult(moduleFile, exportName);
            }
            if (directMatches.size() > 1) {
                return ExportResolution.ambiguousResult();
            }
            if (directMatches.size() == 1) {
                QinEsmExportBinding binding = directMatches.get(0);
                if (QinEsmExportKind.RE_EXPORT_NAMED.equals(binding.kind())) {
                    return resolveExportName(
                            model,
                            binding.resolvedModule(),
                            binding.localName(),
                            visiting,
                            depth + 1);
                }
                return ExportResolution.foundResult(moduleFile, exportName);
            }

            ExportResolution found = ExportResolution.notResolved();
            for (QinEsmExportBinding star : exportAll) {
                ExportResolution sub = resolveExportName(
                        model,
                        star.resolvedModule(),
                        exportName,
                        visiting,
                        depth + 1);
                if (sub.isAmbiguous()) {
                    return ExportResolution.ambiguousResult();
                }
                if (sub.exists()) {
                    if (found.exists() && !sameResolvedBinding(found, sub)) {
                        return ExportResolution.ambiguousResult();
                    }
                    found = sub;
                }
            }
            return found;
        } finally {
            visiting.remove(visitKey);
        }
    }

    private boolean sameResolvedBinding(ExportResolution left, ExportResolution right) {
        if (!left.exists() || !right.exists()) {
            return false;
        }
        return left.owner() != null
                && right.owner() != null
                && left.owner().equals(right.owner())
                && left.exportName().equals(right.exportName());
    }

    private record ExportResolution(boolean exists, boolean isAmbiguous, Path owner, String exportName) {
        private static ExportResolution foundResult(Path owner, String exportName) {
            return new ExportResolution(true, false, owner, exportName);
        }

        private static ExportResolution ambiguousResult() {
            return new ExportResolution(false, true, null, "");
        }

        private static ExportResolution notResolved() {
            return new ExportResolution(false, false, null, "");
        }
    }
}
