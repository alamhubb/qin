package com.qin.lang.module.resolver;

import com.qin.lang.module.policy.QinImportDescriptor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Emits one linked source string from module graph for current Qin frontend.
 */
public final class QinLinkedModuleSourceEmitter {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+(.+?)\\s+from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern EXPORT_CONST_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+const\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_DEFAULT_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+default\\s+(.+?)\\s*;?\\s*$");
    private static final Pattern EXPORT_NAMED_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\{([^}]*)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?\\s*$");
    private static final Pattern EXPORT_ALL_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");

    private static final Pattern RESOLVED_IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+[^;\\n]*?\\s+from\\s*[\"'](?!java:)[^\"']+[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern RESOLVED_IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"'](?!java:)[^\"']+[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern RESOLVED_EXPORT_FROM_NAMED_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\{[^}\\n]*}\\s*from\\s*[\"'](?!java:)[^\"']+[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern RESOLVED_EXPORT_FROM_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?from\\s*[\"'](?!java:)[^\"']+[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_NAMED_LOCAL_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\{[^}\\n]*}\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_CONST_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+const\\s+");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z_$][\\w$]*$");
    private static final int MAX_EXPORT_RESOLUTION_DEPTH = 128;

    public QinLinkedModuleSource emit(QinModuleGraph graph) {
        StringBuilder output = new StringBuilder();
        List<Path> moduleFiles = new ArrayList<>();
        List<QinImportDescriptor> allImports = new ArrayList<>();
        Map<Path, ModuleParsed> parsedModules = new LinkedHashMap<>();
        Map<Path, Integer> moduleIndex = new LinkedHashMap<>();

        int index = 0;
        for (QinModuleSource module : graph.modules()) {
            moduleIndex.put(module.file(), index++);
            parsedModules.put(module.file(), parseModule(module));
        }

        for (QinModuleSource module : graph.modules()) {
            moduleFiles.add(module.file());
            for (QinResolvedImport resolvedImport : module.imports()) {
                allImports.add(resolvedImport.descriptor());
            }

            ModuleParsed parsed = parsedModules.get(module.file());
            List<String> importAliases = emitImportAliases(
                    parsed,
                    parsedModules,
                    moduleIndex);
            String rewrittenBody = rewriteExports(stripLocalJsImports(module.source()), module.file(), moduleIndex)
                    .trim();
            List<String> exportAliases = emitExportAliases(
                    parsed,
                    parsedModules,
                    moduleIndex);
            String rewritten = joinModuleSection(importAliases, rewrittenBody, exportAliases).trim();
            if (!rewritten.isEmpty()) {
                if (output.length() > 0) {
                    output.append(System.lineSeparator()).append(System.lineSeparator());
                }
                output.append("// module: ")
                        .append(module.file().toAbsolutePath())
                        .append(System.lineSeparator())
                        .append(rewritten);
            }
        }

        return new QinLinkedModuleSource(
                graph.entryFile(),
                output.toString(),
                moduleFiles,
                allImports,
                graph);
    }

    private String stripLocalJsImports(String source) {
        String stripped = RESOLVED_IMPORT_FROM_PATTERN.matcher(source).replaceAll("");
        stripped = RESOLVED_IMPORT_SIDE_EFFECT_PATTERN.matcher(stripped).replaceAll("");
        stripped = RESOLVED_EXPORT_FROM_NAMED_PATTERN.matcher(stripped).replaceAll("");
        return RESOLVED_EXPORT_FROM_ALL_PATTERN.matcher(stripped).replaceAll("");
    }

    private String rewriteExports(String source, Path moduleFile, Map<Path, Integer> moduleIndex) {
        String rewritten = EXPORT_CONST_REWRITE_PATTERN.matcher(source).replaceAll("const ");
        rewritten = rewriteDefaultExports(rewritten, moduleFile, moduleIndex);
        // Local `export { ... }` has no runtime effect for flattened source.
        return EXPORT_NAMED_LOCAL_PATTERN.matcher(rewritten).replaceAll("");
    }

    private String rewriteDefaultExports(String source, Path moduleFile, Map<Path, Integer> moduleIndex) {
        Matcher matcher = EXPORT_DEFAULT_PATTERN.matcher(source);
        StringBuilder out = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1).trim();
            String replacement = "const " + exportSymbol(moduleFile, "default", moduleIndex) + " = "
                    + expression + ";";
            matcher.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private List<String> emitImportAliases(
            ModuleParsed parsed,
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        List<String> lines = new ArrayList<>();
        for (ParsedImport parsedImport : parsed.imports()) {
            if (parsedImport.kind() == ImportKind.SIDE_EFFECT || parsedImport.resolvedModule() == null) {
                continue;
            }

            if (parsedImport.kind() == ImportKind.NAMESPACE) {
                lines.add("const " + parsedImport.localName() + " = "
                        + namespaceLiteral(parsedImport.resolvedModule(), parsedModules, moduleIndex) + ";");
                continue;
            }

            lines.add("const " + parsedImport.localName() + " = "
                    + exportSymbol(parsedImport.resolvedModule(), parsedImport.importedName(), moduleIndex) + ";");
        }
        return lines;
    }

    private List<String> emitExportAliases(
            ModuleParsed parsed,
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        List<String> lines = new ArrayList<>();
        Set<String> directExports = new LinkedHashSet<>();
        Set<String> emittedExports = new HashSet<>();

        for (ParsedExport parsedExport : parsed.exports()) {
            if (parsedExport.kind() != ExportKind.RE_EXPORT_ALL) {
                directExports.add(parsedExport.exportName());
            }
        }

        for (ParsedExport parsedExport : parsed.exports()) {
            switch (parsedExport.kind()) {
                case LOCAL_NAMED -> emitExportAliasLine(
                        lines,
                        emittedExports,
                        parsed.sourceFile(),
                        parsedExport.exportName(),
                        parsedExport.localName(),
                        moduleIndex);
                case LOCAL_DEFAULT -> {
                    // handled in rewriteDefaultExports for `export default expr`.
                }
                case RE_EXPORT_NAMED -> emitReExportNamed(
                        lines,
                        emittedExports,
                        parsed,
                        parsedExport,
                        moduleIndex);
                case RE_EXPORT_NAMESPACE -> {
                    if (parsedExport.resolvedModule() != null) {
                        String target = exportSymbol(parsed.sourceFile(), parsedExport.exportName(), moduleIndex);
                        if (emittedExports.add(target)) {
                            lines.add("const " + target + " = "
                                    + namespaceLiteral(parsedExport.resolvedModule(), parsedModules, moduleIndex) + ";");
                        }
                    }
                }
                case RE_EXPORT_ALL -> emitReExportAll(
                        lines,
                        emittedExports,
                        directExports,
                        parsed,
                        parsedExport,
                        parsedModules,
                        moduleIndex);
            }
        }
        return lines;
    }

    private void emitExportAliasLine(
            List<String> lines,
            Set<String> emittedExports,
            Path sourceFile,
            String exportName,
            String localName,
            Map<Path, Integer> moduleIndex) {
        String target = exportSymbol(sourceFile, exportName, moduleIndex);
        if (!emittedExports.add(target)) {
            return;
        }
        lines.add("const " + target + " = " + localName + ";");
    }

    private void emitReExportNamed(
            List<String> lines,
            Set<String> emittedExports,
            ModuleParsed parsed,
            ParsedExport parsedExport,
            Map<Path, Integer> moduleIndex) {
        Path resolvedModule = parsedExport.resolvedModule();
        if (resolvedModule == null) {
            return;
        }
        String target = exportSymbol(parsed.sourceFile(), parsedExport.exportName(), moduleIndex);
        if (!emittedExports.add(target)) {
            return;
        }
        lines.add("const " + target + " = " + exportSymbol(resolvedModule, parsedExport.localName(), moduleIndex) + ";");
    }

    private void emitReExportAll(
            List<String> lines,
            Set<String> emittedExports,
            Set<String> directExports,
            ModuleParsed parsed,
            ParsedExport parsedExport,
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        if (parsedExport.resolvedModule() == null) {
            return;
        }

        List<String> inherited = resolveExportedNames(parsedExport.resolvedModule(), parsedModules, new HashSet<>(), 0);
        for (String name : inherited) {
            if ("default".equals(name) || directExports.contains(name)) {
                continue;
            }
            ExportResolution resolution = resolveExportName(parsedExport.resolvedModule(), name, parsedModules,
                    new HashSet<>(), 0);
            if (!resolution.exists() || resolution.isAmbiguous()) {
                continue;
            }

            String target = exportSymbol(parsed.sourceFile(), name, moduleIndex);
            if (!emittedExports.add(target)) {
                continue;
            }
            lines.add("const " + target + " = "
                    + exportSymbol(resolution.owner(), resolution.exportName(), moduleIndex) + ";");
        }
    }

    private String namespaceLiteral(
            Path moduleFile,
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        List<String> names = resolveExportedNames(moduleFile, parsedModules, new HashSet<>(), 0);
        if (names.isEmpty()) {
            return "{}";
        }
        StringBuilder out = new StringBuilder("{ ");
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (i > 0) {
                out.append(", ");
            }
            out.append(objectKey(name))
                    .append(": ")
                    .append(exportSymbol(moduleFile, name, moduleIndex));
        }
        out.append(" }");
        return out.toString();
    }

    private List<String> resolveExportedNames(
            Path moduleFile,
            Map<Path, ModuleParsed> parsedModules,
            Set<Path> visiting,
            int depth) {
        if (moduleFile == null || depth > MAX_EXPORT_RESOLUTION_DEPTH) {
            return List.of();
        }
        if (!visiting.add(moduleFile)) {
            return List.of();
        }

        ModuleParsed module = parsedModules.get(moduleFile);
        if (module == null) {
            return List.of();
        }

        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (ParsedExport parsedExport : module.exports()) {
            if (parsedExport.kind() == ExportKind.RE_EXPORT_ALL) {
                continue;
            }
            names.add(parsedExport.exportName());
        }

        for (ParsedExport parsedExport : module.exports()) {
            if (parsedExport.kind() != ExportKind.RE_EXPORT_ALL || parsedExport.resolvedModule() == null) {
                continue;
            }
            for (String inherited : resolveExportedNames(
                    parsedExport.resolvedModule(),
                    parsedModules,
                    visiting,
                    depth + 1)) {
                if (!"default".equals(inherited)) {
                    names.add(inherited);
                }
            }
        }
        visiting.remove(moduleFile);
        return new ArrayList<>(names);
    }

    private ExportResolution resolveExportName(
            Path moduleFile,
            String exportName,
            Map<Path, ModuleParsed> parsedModules,
            Set<String> visiting,
            int depth) {
        if (moduleFile == null || exportName == null || exportName.isBlank() || depth > MAX_EXPORT_RESOLUTION_DEPTH) {
            return ExportResolution.notResolvedResult();
        }

        String visitKey = moduleFile.toString() + "::" + exportName;
        if (!visiting.add(visitKey)) {
            return ExportResolution.notResolvedResult();
        }

        ModuleParsed module = parsedModules.get(moduleFile);
        if (module == null) {
            return ExportResolution.notResolvedResult();
        }

        List<ParsedExport> direct = new ArrayList<>();
        List<ParsedExport> stars = new ArrayList<>();
        for (ParsedExport parsedExport : module.exports()) {
            if (parsedExport.kind() == ExportKind.RE_EXPORT_ALL) {
                stars.add(parsedExport);
                continue;
            }
            if (exportName.equals(parsedExport.exportName())) {
                direct.add(parsedExport);
            }
        }

        if (direct.size() > 1) {
            return ExportResolution.ambiguousResult();
        }
        if (direct.size() == 1) {
            ParsedExport parsedExport = direct.get(0);
            if (parsedExport.kind() == ExportKind.RE_EXPORT_NAMED && parsedExport.resolvedModule() != null) {
                return resolveExportName(parsedExport.resolvedModule(), parsedExport.localName(), parsedModules, visiting,
                        depth + 1);
            }
            return ExportResolution.found(moduleFile, exportName);
        }

        ExportResolution found = ExportResolution.notResolvedResult();
        for (ParsedExport star : stars) {
            if (star.resolvedModule() == null) {
                continue;
            }
            ExportResolution sub = resolveExportName(star.resolvedModule(), exportName, parsedModules, visiting, depth + 1);
            if (sub.isAmbiguous()) {
                return ExportResolution.ambiguousResult();
            }
            if (sub.exists()) {
                if (found.exists()) {
                    return ExportResolution.ambiguousResult();
                }
                found = sub;
            }
        }
        return found;
    }

    private String joinModuleSection(List<String> importAliases, String body, List<String> exportAliases) {
        StringBuilder out = new StringBuilder();
        appendLines(out, importAliases);
        if (!body.isBlank()) {
            if (out.length() > 0) {
                out.append(System.lineSeparator());
            }
            out.append(body);
        }
        if (!exportAliases.isEmpty()) {
            if (out.length() > 0) {
                out.append(System.lineSeparator());
            }
            appendLines(out, exportAliases);
        }
        return out.toString();
    }

    private void appendLines(StringBuilder out, List<String> lines) {
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            if (out.length() > 0) {
                out.append(System.lineSeparator());
            }
            out.append(line);
        }
    }

    private ModuleParsed parseModule(QinModuleSource module) {
        List<ParsedImport> imports = parseImports(module);
        List<ParsedExport> exports = parseExports(module);
        return new ModuleParsed(module.file(), imports, exports);
    }

    private List<ParsedImport> parseImports(QinModuleSource module) {
        List<ParsedImport> bindings = new ArrayList<>();
        Matcher matcher = IMPORT_FROM_PATTERN.matcher(module.source());
        while (matcher.find()) {
            String clause = matcher.group(1).trim();
            String moduleSpecifier = matcher.group(2).trim();
            Path resolved = resolveTargetModule(module, moduleSpecifier);
            parseImportClause(clause, resolved, bindings);
        }

        Matcher sideEffect = IMPORT_SIDE_EFFECT_PATTERN.matcher(module.source());
        while (sideEffect.find()) {
            String moduleSpecifier = sideEffect.group(1).trim();
            Path resolved = resolveTargetModule(module, moduleSpecifier);
            bindings.add(new ParsedImport(ImportKind.SIDE_EFFECT, "", "", resolved));
        }
        return bindings;
    }

    private void parseImportClause(String clause, Path resolvedModule, List<ParsedImport> out) {
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
            out.add(new ParsedImport(ImportKind.DEFAULT, "default", defaultPart, resolvedModule));
        }

        if (namedOrNamespacePart == null || namedOrNamespacePart.isBlank()) {
            return;
        }

        if (namedOrNamespacePart.startsWith("*")) {
            String ns = namedOrNamespacePart.replaceFirst("^\\*\\s*as\\s*", "").trim();
            out.add(new ParsedImport(ImportKind.NAMESPACE, "*", ns, resolvedModule));
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
                int asIndex = spec.indexOf(" as ");
                if (asIndex > 0) {
                    String importedName = spec.substring(0, asIndex).trim();
                    String localName = spec.substring(asIndex + 4).trim();
                    out.add(new ParsedImport(ImportKind.NAMED, importedName, localName, resolvedModule));
                } else {
                    out.add(new ParsedImport(ImportKind.NAMED, spec, spec, resolvedModule));
                }
            }
        }
    }

    private List<ParsedExport> parseExports(QinModuleSource module) {
        List<ParsedExport> exports = new ArrayList<>();

        Matcher constMatcher = EXPORT_CONST_PATTERN.matcher(module.source());
        while (constMatcher.find()) {
            String name = constMatcher.group(1).trim();
            exports.add(new ParsedExport(ExportKind.LOCAL_NAMED, name, name, null));
        }

        Matcher defaultMatcher = EXPORT_DEFAULT_PATTERN.matcher(module.source());
        while (defaultMatcher.find()) {
            exports.add(new ParsedExport(ExportKind.LOCAL_DEFAULT, "default", "default", null));
        }

        Matcher namedMatcher = EXPORT_NAMED_PATTERN.matcher(module.source());
        while (namedMatcher.find()) {
            String block = namedMatcher.group(1);
            String moduleSpecifier = namedMatcher.group(2) == null ? null : namedMatcher.group(2).trim();
            Path resolvedModule = moduleSpecifier == null ? null : resolveTargetModule(module, moduleSpecifier);
            parseNamedExportBlock(block, resolvedModule, exports);
        }

        Matcher exportAllMatcher = EXPORT_ALL_PATTERN.matcher(module.source());
        while (exportAllMatcher.find()) {
            String namespace = exportAllMatcher.group(1);
            String moduleSpecifier = exportAllMatcher.group(2).trim();
            Path resolvedModule = resolveTargetModule(module, moduleSpecifier);
            if (namespace != null && !namespace.isBlank()) {
                exports.add(new ParsedExport(ExportKind.RE_EXPORT_NAMESPACE, namespace, "*", resolvedModule));
            } else {
                exports.add(new ParsedExport(ExportKind.RE_EXPORT_ALL, "*", "*", resolvedModule));
            }
        }
        return exports;
    }

    private void parseNamedExportBlock(
            String block,
            Path resolvedModule,
            List<ParsedExport> out) {
        String[] items = block.split(",");
        for (String raw : items) {
            String spec = raw.trim();
            if (spec.isEmpty()) {
                continue;
            }
            int asIndex = spec.indexOf(" as ");
            String importedOrLocal = asIndex > 0 ? spec.substring(0, asIndex).trim() : spec;
            String exported = asIndex > 0 ? spec.substring(asIndex + 4).trim() : spec;
            if (resolvedModule == null) {
                out.add(new ParsedExport(ExportKind.LOCAL_NAMED, exported, importedOrLocal, null));
            } else {
                out.add(new ParsedExport(ExportKind.RE_EXPORT_NAMED, exported, importedOrLocal, resolvedModule));
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

    private String exportSymbol(Path moduleFile, String exportName, Map<Path, Integer> moduleIndex) {
        int idx = moduleIndex.getOrDefault(moduleFile, -1);
        return "__qesm_m" + idx + "_e_" + sanitize(exportName);
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) {
            return "_";
        }
        String sanitized = name.replaceAll("[^A-Za-z0-9_$]", "_");
        if (sanitized.isEmpty()) {
            return "_";
        }
        char first = sanitized.charAt(0);
        if (Character.isDigit(first)) {
            return "_" + sanitized;
        }
        return sanitized;
    }

    private String objectKey(String key) {
        if (IDENTIFIER_PATTERN.matcher(key).matches()) {
            return key;
        }
        return "\"" + key.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private enum ImportKind {
        DEFAULT,
        NAMED,
        NAMESPACE,
        SIDE_EFFECT
    }

    private enum ExportKind {
        LOCAL_NAMED,
        LOCAL_DEFAULT,
        RE_EXPORT_NAMED,
        RE_EXPORT_ALL,
        RE_EXPORT_NAMESPACE
    }

    private record ParsedImport(
            ImportKind kind,
            String importedName,
            String localName,
            Path resolvedModule) {
    }

    private record ParsedExport(
            ExportKind kind,
            String exportName,
            String localName,
            Path resolvedModule) {
    }

    private record ModuleParsed(
            Path sourceFile,
            List<ParsedImport> imports,
            List<ParsedExport> exports) {
        private ModuleParsed {
            imports = imports == null ? List.of() : Collections.unmodifiableList(new ArrayList<>(imports));
            exports = exports == null ? List.of() : Collections.unmodifiableList(new ArrayList<>(exports));
        }
    }

    private record ExportResolution(
            boolean exists,
            boolean isAmbiguous,
            Path owner,
            String exportName) {
        private static ExportResolution found(Path owner, String exportName) {
            return new ExportResolution(true, false, owner, exportName);
        }

        private static ExportResolution ambiguousResult() {
            return new ExportResolution(false, true, null, "");
        }

        private static ExportResolution notResolvedResult() {
            return new ExportResolution(false, false, null, "");
        }
    }
}
