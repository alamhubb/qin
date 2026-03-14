package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            if (QinEsmExportKind.RE_EXPORT_ALL.equals(exportBinding.kind())) {
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
            if (!resolution.exists()) {
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2003",
                        "Imported binding does not exist: " + importBinding.importedName()
                                + " from " + importBinding.moduleSpecifier(),
                        importBinding.sourceFile(),
                        importBinding.line(),
                        importBinding.column()));
            } else if (resolution.isAmbiguous()) {
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM2004",
                        "Ambiguous import binding: " + importBinding.importedName()
                                + " from " + importBinding.moduleSpecifier(),
                        importBinding.sourceFile(),
                        importBinding.line(),
                        importBinding.column()));
            }
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

        QinEsmModuleSemantic module = model.modules().get(moduleFile);
        if (module == null) {
            return ExportResolution.notResolved();
        }

        List<QinEsmExportBinding> directMatches = new ArrayList<>();
        List<QinEsmExportBinding> exportAll = new ArrayList<>();
        for (QinEsmExportBinding exportBinding : module.exports()) {
            if (QinEsmExportKind.RE_EXPORT_ALL.equals(exportBinding.kind())) {
                exportAll.add(exportBinding);
                continue;
            }
            if (exportName.equals(exportBinding.exportName())) {
                directMatches.add(exportBinding);
            }
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
            return ExportResolution.foundResult();
        }

        boolean found = false;
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
                if (found) {
                    return ExportResolution.ambiguousResult();
                }
                found = true;
            }
        }
        return found ? ExportResolution.foundResult() : ExportResolution.notResolved();
    }

    private record ExportResolution(boolean exists, boolean isAmbiguous) {
        private static ExportResolution foundResult() {
            return new ExportResolution(true, false);
        }

        private static ExportResolution ambiguousResult() {
            return new ExportResolution(false, true);
        }

        private static ExportResolution notResolved() {
            return new ExportResolution(false, false);
        }
    }
}
