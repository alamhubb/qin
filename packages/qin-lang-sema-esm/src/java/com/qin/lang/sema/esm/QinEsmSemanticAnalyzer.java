package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.lang.module.resolver.QinResolvedImport;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts ESM semantic model from resolved module graph.
 */
public final class QinEsmSemanticAnalyzer {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+(.+?)\\s+from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern EXPORT_CONST_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+const\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_DEFAULT_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+default\\b");
    private static final Pattern EXPORT_NAMED_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\{([^}]*)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?\\s*$");
    private static final Pattern EXPORT_ALL_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");

    public QinEsmSemanticModel analyze(QinModuleGraph graph) {
        Map<Path, QinEsmModuleSemantic> modules = new LinkedHashMap<>();
        for (QinModuleSource module : graph.modules()) {
            List<QinEsmImportBinding> imports = parseImports(module);
            List<QinEsmExportBinding> exports = parseExports(module);
            modules.put(
                    module.file(),
                    new QinEsmModuleSemantic(module.file(), imports, exports));
        }
        return new QinEsmSemanticModel(graph.entryFile(), modules);
    }

    private List<QinEsmImportBinding> parseImports(QinModuleSource module) {
        List<QinEsmImportBinding> bindings = new ArrayList<>();
        Matcher matcher = IMPORT_FROM_PATTERN.matcher(module.source());
        while (matcher.find()) {
            String clause = matcher.group(1).trim();
            String moduleSpecifier = matcher.group(2).trim();
            Path resolvedModule = resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(module.source(), matcher.start(2));
            parseImportClause(
                    module.file(),
                    moduleSpecifier,
                    resolvedModule,
                    lineCol[0],
                    lineCol[1],
                    clause,
                    bindings);
        }

        Matcher sideEffectMatcher = IMPORT_SIDE_EFFECT_PATTERN.matcher(module.source());
        while (sideEffectMatcher.find()) {
            String moduleSpecifier = sideEffectMatcher.group(1).trim();
            Path resolvedModule = resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(module.source(), sideEffectMatcher.start(1));
            bindings.add(new QinEsmImportBinding(
                    module.file(),
                    moduleSpecifier,
                    QinEsmImportKind.SIDE_EFFECT,
                    "",
                    "",
                    lineCol[0],
                    lineCol[1],
                    resolvedModule));
        }
        return bindings;
    }

    private List<QinEsmExportBinding> parseExports(QinModuleSource module) {
        List<QinEsmExportBinding> exports = new ArrayList<>();
        Matcher matcher = EXPORT_CONST_PATTERN.matcher(module.source());
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            int[] lineCol = lineCol(module.source(), matcher.start(1));
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_NAMED,
                    name,
                    name,
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
        }

        Matcher defaultMatcher = EXPORT_DEFAULT_PATTERN.matcher(module.source());
        while (defaultMatcher.find()) {
            int[] lineCol = lineCol(module.source(), defaultMatcher.start());
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_DEFAULT,
                    "default",
                    "default",
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
        }

        Matcher namedMatcher = EXPORT_NAMED_PATTERN.matcher(module.source());
        while (namedMatcher.find()) {
            String block = namedMatcher.group(1);
            String moduleSpecifier = namedMatcher.group(2) == null ? null : namedMatcher.group(2).trim();
            Path resolvedModule = moduleSpecifier == null ? null : resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(module.source(), namedMatcher.start());
            parseNamedExportBlock(module.file(), block, moduleSpecifier, resolvedModule, lineCol[0], lineCol[1], exports);
        }

        Matcher exportAllMatcher = EXPORT_ALL_PATTERN.matcher(module.source());
        while (exportAllMatcher.find()) {
            String namespace = exportAllMatcher.group(1);
            String moduleSpecifier = exportAllMatcher.group(2).trim();
            Path resolvedModule = resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(module.source(), exportAllMatcher.start(2));
            if (namespace != null && !namespace.isBlank()) {
                exports.add(new QinEsmExportBinding(
                        module.file(),
                        QinEsmExportKind.RE_EXPORT_NAMESPACE,
                        namespace,
                        "*",
                        moduleSpecifier,
                        resolvedModule,
                        lineCol[0],
                        lineCol[1]));
            } else {
                exports.add(new QinEsmExportBinding(
                        module.file(),
                        QinEsmExportKind.RE_EXPORT_ALL,
                        "*",
                        "*",
                        moduleSpecifier,
                        resolvedModule,
                        lineCol[0],
                        lineCol[1]));
            }
        }
        return exports;
    }

    private void parseImportClause(
            Path sourceFile,
            String moduleSpecifier,
            Path resolvedModule,
            int line,
            int column,
            String clause,
            List<QinEsmImportBinding> out) {
        String trimmed = clause == null ? "" : clause.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        String defaultPart = null;
        String namedOrNamespacePart = null;

        int commaIndex = indexOfTopLevelComma(trimmed);
        if (commaIndex >= 0) {
            defaultPart = trimmed.substring(0, commaIndex).trim();
            namedOrNamespacePart = trimmed.substring(commaIndex + 1).trim();
        } else if (trimmed.startsWith("{") || trimmed.startsWith("*")) {
            namedOrNamespacePart = trimmed;
        } else {
            defaultPart = trimmed;
        }

        if (defaultPart != null && !defaultPart.isBlank()) {
            out.add(new QinEsmImportBinding(
                    sourceFile,
                    moduleSpecifier,
                    QinEsmImportKind.DEFAULT,
                    "default",
                    defaultPart,
                    line,
                    column,
                    resolvedModule));
        }

        if (namedOrNamespacePart == null || namedOrNamespacePart.isBlank()) {
            return;
        }

        if (namedOrNamespacePart.startsWith("*")) {
            String ns = namedOrNamespacePart.replaceFirst("^\\*\\s*as\\s*", "").trim();
            out.add(new QinEsmImportBinding(
                    sourceFile,
                    moduleSpecifier,
                    QinEsmImportKind.NAMESPACE,
                    "*",
                    ns,
                    line,
                    column,
                    resolvedModule));
            return;
        }

        if (namedOrNamespacePart.startsWith("{") && namedOrNamespacePart.endsWith("}")) {
            String block = namedOrNamespacePart.substring(1, namedOrNamespacePart.length() - 1);
            String[] items = block.split(",");
            for (String raw : items) {
                String spec = raw.trim();
                if (spec.isEmpty()) {
                    continue;
                }
                String importedName;
                String localName;
                int asIndex = spec.indexOf(" as ");
                if (asIndex > 0) {
                    importedName = spec.substring(0, asIndex).trim();
                    localName = spec.substring(asIndex + 4).trim();
                } else {
                    importedName = spec;
                    localName = spec;
                }
                out.add(new QinEsmImportBinding(
                        sourceFile,
                        moduleSpecifier,
                        QinEsmImportKind.NAMED,
                        importedName,
                        localName,
                        line,
                        column,
                        resolvedModule));
            }
        }
    }

    private void parseNamedExportBlock(
            Path sourceFile,
            String block,
            String moduleSpecifier,
            Path resolvedModule,
            int line,
            int column,
            List<QinEsmExportBinding> out) {
        String[] items = block.split(",");
        for (String raw : items) {
            String spec = raw.trim();
            if (spec.isEmpty()) {
                continue;
            }
            String importedOrLocal;
            String exported;
            int asIndex = spec.indexOf(" as ");
            if (asIndex > 0) {
                importedOrLocal = spec.substring(0, asIndex).trim();
                exported = spec.substring(asIndex + 4).trim();
            } else {
                importedOrLocal = spec;
                exported = spec;
            }

            if (moduleSpecifier == null || moduleSpecifier.isBlank()) {
                out.add(new QinEsmExportBinding(
                        sourceFile,
                        QinEsmExportKind.LOCAL_NAMED,
                        exported,
                        importedOrLocal,
                        null,
                        null,
                        line,
                        column));
            } else {
                out.add(new QinEsmExportBinding(
                        sourceFile,
                        QinEsmExportKind.RE_EXPORT_NAMED,
                        exported,
                        importedOrLocal,
                        moduleSpecifier,
                        resolvedModule,
                        line,
                        column));
            }
        }
    }

    private Path resolveTargetModule(QinModuleSource module, String specifier) {
        for (QinResolvedImport resolvedImport : module.imports()) {
            if (specifier.equals(resolvedImport.descriptor().moduleSpecifier())) {
                return resolvedImport.resolvedModule();
            }
        }
        return null;
    }

    private int[] lineCol(String source, int index) {
        int line = 1;
        int col = 1;
        for (int i = 0; i < index && i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        return new int[] {line, col};
    }

    private int indexOfTopLevelComma(String text) {
        int depth = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '{' || ch == '[' || ch == '(') {
                depth++;
            } else if (ch == '}' || ch == ']' || ch == ')') {
                depth = Math.max(0, depth - 1);
            } else if (ch == ',' && depth == 0) {
                return i;
            }
        }
        return -1;
    }
}
