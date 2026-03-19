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
            "(?m)^\\s*import\\s+([\\s\\S]*?)\\s+from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern EXPORT_CALLABLE_DECLARATION_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+(function|class)\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_VARIABLE_PREFIX_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+(const|let|var)\\s+");
    private static final Pattern EXPORT_DEFAULT_PREFIX_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+default\\s+");
    private static final Pattern EXPORT_DEFAULT_DECLARATION_PATTERN = Pattern.compile(
            "\\bexport\\s+default\\s+(function|class)\\b");
    private static final Pattern DEFAULT_EXPR_FUNCTION_NAMED_PATTERN = Pattern.compile(
            "^function\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern DEFAULT_EXPR_CLASS_NAMED_PATTERN = Pattern.compile(
            "^class\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_NAMED_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\{([\\s\\S]*?)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?\\s*$");
    private static final Pattern EXPORT_ALL_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s*\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");

    private static final Pattern EXPORT_NAMED_LOCAL_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\{[^}\\n]*}\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_CONST_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+const\\s+");
    private static final Pattern EXPORT_LET_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+let\\s+");
    private static final Pattern EXPORT_VAR_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+var\\s+");
    private static final Pattern EXPORT_FUNCTION_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+function\\s+");
    private static final Pattern EXPORT_CLASS_REWRITE_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+class\\s+");
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

        List<String> instantiateLines = emitExportSlotDeclarations(parsedModules, moduleIndex);
        if (!instantiateLines.isEmpty()) {
            output.append("// instantiate exports");
            appendLines(output, instantiateLines);
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
            String rewrittenBody = rewriteExports(stripModuleLinkageStatements(module.source()), module.file(), moduleIndex)
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

    private String stripModuleLinkageStatements(String source) {
        String stripped = IMPORT_FROM_PATTERN.matcher(source).replaceAll("");
        stripped = IMPORT_SIDE_EFFECT_PATTERN.matcher(stripped).replaceAll("");
        stripped = EXPORT_NAMED_PATTERN.matcher(stripped).replaceAll("");
        return EXPORT_ALL_PATTERN.matcher(stripped).replaceAll("");
    }

    private String rewriteExports(String source, Path moduleFile, Map<Path, Integer> moduleIndex) {
        String rewritten = EXPORT_CONST_REWRITE_PATTERN.matcher(source).replaceAll("const ");
        rewritten = EXPORT_LET_REWRITE_PATTERN.matcher(rewritten).replaceAll("let ");
        rewritten = EXPORT_VAR_REWRITE_PATTERN.matcher(rewritten).replaceAll("var ");
        rewritten = EXPORT_FUNCTION_REWRITE_PATTERN.matcher(rewritten).replaceAll("function ");
        rewritten = EXPORT_CLASS_REWRITE_PATTERN.matcher(rewritten).replaceAll("class ");
        rewritten = rewriteDefaultExports(rewritten, moduleFile, moduleIndex);
        // Local `export { ... }` has no runtime effect for flattened source.
        return EXPORT_NAMED_LOCAL_PATTERN.matcher(rewritten).replaceAll("");
    }

    private String rewriteDefaultExports(String source, Path moduleFile, Map<Path, Integer> moduleIndex) {
        String defaultLocal = defaultLocalSymbol(moduleFile, moduleIndex);
        String rewritten = rewriteDefaultDeclarationExports(source, defaultLocal);
        Matcher matcher = EXPORT_DEFAULT_PREFIX_PATTERN.matcher(rewritten);
        StringBuilder out = new StringBuilder();
        int cursor = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int exprStart = matcher.end();
            int exprEnd = findDefaultExpressionStatementEnd(rewritten, exprStart);
            String expression = trimTrailingSemicolon(rewritten.substring(exprStart, exprEnd));
            out.append(rewritten, cursor, start);
            out.append("const ")
                    .append(defaultLocal)
                    .append(" = ")
                    .append(expression)
                    .append(";");
            cursor = exprEnd;
            matcher.region(cursor, rewritten.length());
        }
        out.append(rewritten, cursor, rewritten.length());
        return out.toString();
    }

    private int findDefaultExpressionStatementEnd(String source, int fromIndex) {
        int parenDepth = 0;
        int braceDepth = 0;
        int bracketDepth = 0;
        boolean inSingle = false;
        boolean inDouble = false;
        boolean inTemplate = false;
        boolean escaping = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = fromIndex; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (ch == '\n') {
                    inLineComment = false;
                }
                continue;
            }
            if (inBlockComment) {
                if (ch == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }
            if (inSingle) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '\'') {
                    inSingle = false;
                }
                continue;
            }
            if (inDouble) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '"') {
                    inDouble = false;
                }
                continue;
            }
            if (inTemplate) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '`') {
                    inTemplate = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                inBlockComment = true;
                i++;
                continue;
            }
            if (ch == '\'') {
                inSingle = true;
                continue;
            }
            if (ch == '"') {
                inDouble = true;
                continue;
            }
            if (ch == '`') {
                inTemplate = true;
                continue;
            }

            if (ch == '(') {
                parenDepth++;
                continue;
            }
            if (ch == ')' && parenDepth > 0) {
                parenDepth--;
                continue;
            }
            if (ch == '{') {
                braceDepth++;
                continue;
            }
            if (ch == '}' && braceDepth > 0) {
                braceDepth--;
                continue;
            }
            if (ch == '[') {
                bracketDepth++;
                continue;
            }
            if (ch == ']' && bracketDepth > 0) {
                bracketDepth--;
                continue;
            }
            if (ch == ';' && parenDepth == 0 && braceDepth == 0 && bracketDepth == 0) {
                return i + 1;
            }
        }
        return source.length();
    }

    private String rewriteDefaultDeclarationExports(String source, String defaultSymbol) {
        Matcher matcher = EXPORT_DEFAULT_DECLARATION_PATTERN.matcher(source);
        if (!matcher.find()) {
            return source;
        }

        StringBuilder out = new StringBuilder();
        int cursor = 0;
        do {
            int start = matcher.start();
            out.append(source, cursor, start);

            String declarationKind = matcher.group(1);
            int declarationEnd = findDefaultDeclarationEnd(source, matcher.end());
            String declaration = source.substring(start, declarationEnd);
            out.append(rewriteDefaultDeclaration(declaration, declarationKind, defaultSymbol));
            cursor = declarationEnd;
            matcher.region(cursor, source.length());
        } while (matcher.find());

        out.append(source, cursor, source.length());
        return out.toString();
    }

    private String rewriteDefaultDeclaration(String declarationSource, String declarationKind, String defaultSymbol) {
        String declaration = declarationSource.strip();
        declaration = declaration.replaceFirst("^\\s*export\\s+default\\s+", "");
        String normalizedKind = declarationKind == null ? "" : declarationKind.trim();

        if ("function".equals(normalizedKind)) {
            Matcher named = DEFAULT_EXPR_FUNCTION_NAMED_PATTERN.matcher(declaration);
            if (named.find()) {
                String localName = named.group(1);
                return declaration + System.lineSeparator() + "const " + defaultSymbol + " = " + localName + ";";
            }
            return "const " + defaultSymbol + " = " + trimTrailingSemicolon(declaration) + ";";
        }

        if ("class".equals(normalizedKind)) {
            Matcher named = DEFAULT_EXPR_CLASS_NAMED_PATTERN.matcher(declaration);
            if (named.find()) {
                String localName = named.group(1);
                return declaration + System.lineSeparator() + "const " + defaultSymbol + " = " + localName + ";";
            }
            return "const " + defaultSymbol + " = " + trimTrailingSemicolon(declaration) + ";";
        }

        return "const " + defaultSymbol + " = " + trimTrailingSemicolon(declaration) + ";";
    }

    private String trimTrailingSemicolon(String text) {
        String value = text.strip();
        if (value.endsWith(";")) {
            return value.substring(0, value.length() - 1).stripTrailing();
        }
        return value;
    }

    private int findDefaultDeclarationEnd(String source, int afterKeywordIndex) {
        int bodyStart = findBodyStartBrace(source, afterKeywordIndex);
        if (bodyStart < 0) {
            return findLineEnd(source, afterKeywordIndex);
        }
        int bodyEnd = findMatchingBrace(source, bodyStart);
        if (bodyEnd < 0) {
            return source.length();
        }
        int i = bodyEnd + 1;
        while (i < source.length() && Character.isWhitespace(source.charAt(i))) {
            i++;
        }
        if (i < source.length() && source.charAt(i) == ';') {
            i++;
        }
        return i;
    }

    private int findBodyStartBrace(String source, int fromIndex) {
        boolean inSingle = false;
        boolean inDouble = false;
        boolean inTemplate = false;
        boolean escaping = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = fromIndex; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (ch == '\n') {
                    inLineComment = false;
                }
                continue;
            }
            if (inBlockComment) {
                if (ch == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }
            if (inSingle) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '\'') {
                    inSingle = false;
                }
                continue;
            }
            if (inDouble) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '"') {
                    inDouble = false;
                }
                continue;
            }
            if (inTemplate) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '`') {
                    inTemplate = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                inBlockComment = true;
                i++;
                continue;
            }
            if (ch == '\'') {
                inSingle = true;
                continue;
            }
            if (ch == '"') {
                inDouble = true;
                continue;
            }
            if (ch == '`') {
                inTemplate = true;
                continue;
            }
            if (ch == '{') {
                return i;
            }
        }
        return -1;
    }

    private int findMatchingBrace(String source, int bodyStart) {
        int depth = 0;
        boolean inSingle = false;
        boolean inDouble = false;
        boolean inTemplate = false;
        boolean escaping = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = bodyStart; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (ch == '\n') {
                    inLineComment = false;
                }
                continue;
            }
            if (inBlockComment) {
                if (ch == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }
            if (inSingle) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '\'') {
                    inSingle = false;
                }
                continue;
            }
            if (inDouble) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '"') {
                    inDouble = false;
                }
                continue;
            }
            if (inTemplate) {
                if (escaping) {
                    escaping = false;
                    continue;
                }
                if (ch == '\\') {
                    escaping = true;
                    continue;
                }
                if (ch == '`') {
                    inTemplate = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                inBlockComment = true;
                i++;
                continue;
            }
            if (ch == '\'') {
                inSingle = true;
                continue;
            }
            if (ch == '"') {
                inDouble = true;
                continue;
            }
            if (ch == '`') {
                inTemplate = true;
                continue;
            }

            if (ch == '{') {
                depth++;
                continue;
            }
            if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findLineEnd(String source, int fromIndex) {
        int idx = source.indexOf('\n', fromIndex);
        return idx >= 0 ? idx : source.length();
    }

    private List<String> emitExportSlotDeclarations(
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        LinkedHashSet<String> slots = new LinkedHashSet<>();
        for (ModuleParsed module : parsedModules.values()) {
            LinkedHashSet<String> directExports = new LinkedHashSet<>();
            for (ParsedExport parsedExport : module.exports()) {
                if (parsedExport.kind() != ExportKind.RE_EXPORT_ALL) {
                    directExports.add(parsedExport.exportName());
                    slots.add(exportSymbol(module.sourceFile(), parsedExport.exportName(), moduleIndex));
                }
            }

            for (ParsedExport parsedExport : module.exports()) {
                if (parsedExport.kind() != ExportKind.RE_EXPORT_ALL || parsedExport.resolvedModule() == null) {
                    continue;
                }
                List<String> inherited = resolveExportedNames(parsedExport.resolvedModule(), parsedModules, new HashSet<>(), 0);
                for (String exportName : inherited) {
                    if ("default".equals(exportName) || directExports.contains(exportName)) {
                        continue;
                    }
                    ExportResolution resolution = resolveExportName(
                            module.sourceFile(),
                            exportName,
                            parsedModules,
                            new HashSet<>(),
                            0);
                    if (!resolution.exists() || resolution.isAmbiguous()) {
                        continue;
                    }
                    slots.add(exportSymbol(module.sourceFile(), exportName, moduleIndex));
                }
            }
        }

        List<String> lines = new ArrayList<>();
        for (String slot : slots) {
            lines.add("const " + slot + " = " + exportSlotCreateCall() + ";");
        }
        return lines;
    }

    private String exportSlotCreateCall() {
        return "__qin_export_slot__()";
    }

    private String exportGetCall(String slotExpr) {
        return "__qin_export_get__(" + slotExpr + ")";
    }

    private String exportInitCall(String slotExpr, String valueExpr) {
        return "__qin_export_init__(" + slotExpr + ", " + valueExpr + ")";
    }

    private String exportInitDeclaration(String slotExpr, String valueExpr) {
        return "const " + exportInitTempSymbol(slotExpr) + " = " + exportInitCall(slotExpr, valueExpr) + ";";
    }

    private String exportInitTempSymbol(String slotExpr) {
        return "__qesm_init_" + slotExpr.replaceAll("[^A-Za-z0-9_$]", "_");
    }

    private List<String> emitImportAliases(
            ModuleParsed parsed,
            Map<Path, ModuleParsed> parsedModules,
            Map<Path, Integer> moduleIndex) {
        List<String> lines = new ArrayList<>();
        for (ParsedImport parsedImport : parsed.imports()) {
            if (parsedImport.kind() == ImportKind.SIDE_EFFECT) {
                continue;
            }
            if (parsedImport.resolvedModule() == null) {
                lines.add("var " + parsedImport.localName() + " = "
                        + runtimeGlobalLookupCall(parsedImport) + ";");
                continue;
            }

            if (parsedImport.kind() == ImportKind.NAMESPACE) {
                lines.add("const " + parsedImport.localName() + " = "
                        + namespaceLiteral(parsedImport.resolvedModule(), parsedModules, moduleIndex) + ";");
                continue;
            }

            lines.add("const " + parsedImport.localName() + " = "
                    + exportGetCall(exportSymbol(parsedImport.resolvedModule(), parsedImport.importedName(), moduleIndex))
                    + ";");
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
                    emitLocalDefaultExportAliasLine(
                            lines,
                            emittedExports,
                            parsed.sourceFile(),
                            parsedExport.localName(),
                            moduleIndex);
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
                            lines.add(exportInitDeclaration(
                                    target,
                                    namespaceLiteral(parsedExport.resolvedModule(), parsedModules, moduleIndex)));
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
        lines.add(exportInitDeclaration(target, localName));
    }

    private void emitLocalDefaultExportAliasLine(
            List<String> lines,
            Set<String> emittedExports,
            Path sourceFile,
            String localName,
            Map<Path, Integer> moduleIndex) {
        String target = exportSymbol(sourceFile, "default", moduleIndex);
        if (!emittedExports.add(target)) {
            return;
        }
        String valueExpr = (localName == null || localName.isBlank() || "default".equals(localName))
                ? defaultLocalSymbol(sourceFile, moduleIndex)
                : localName;
        lines.add(exportInitDeclaration(target, valueExpr));
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
        lines.add(exportInitDeclaration(
                target,
                exportGetCall(exportSymbol(resolvedModule, parsedExport.localName(), moduleIndex))));
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
            ExportResolution resolution = resolveExportName(parsed.sourceFile(), name, parsedModules,
                    new HashSet<>(), 0);
            if (!resolution.exists() || resolution.isAmbiguous()) {
                continue;
            }

            String target = exportSymbol(parsed.sourceFile(), name, moduleIndex);
            if (!emittedExports.add(target)) {
                continue;
            }
            lines.add(exportInitDeclaration(
                    target,
                    exportGetCall(exportSymbol(resolution.owner(), resolution.exportName(), moduleIndex))));
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
        boolean first = true;
        for (String name : names) {
            ExportResolution resolution = resolveExportName(
                    moduleFile,
                    name,
                    parsedModules,
                    new HashSet<>(),
                    0);
            if (!resolution.exists() || resolution.isAmbiguous()) {
                continue;
            }
            if (!first) {
                out.append(", ");
            }
            first = false;
            out.append(objectKey(name))
                    .append(": ")
                    .append(exportSymbol(resolution.owner(), resolution.exportName(), moduleIndex));
        }
        if (first) {
            return "{}";
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
                if ("default".equals(inherited)) {
                    continue;
                }
                ExportResolution resolution = resolveExportName(
                        moduleFile,
                        inherited,
                        parsedModules,
                        new HashSet<>(),
                        0);
                if (resolution.exists() && !resolution.isAmbiguous()) {
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
        try {
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
                    return resolveExportName(
                            parsedExport.resolvedModule(),
                            parsedExport.localName(),
                            parsedModules,
                            visiting,
                            depth + 1);
                }
                return ExportResolution.found(moduleFile, exportName);
            }

            ExportResolution found = ExportResolution.notResolvedResult();
            for (ParsedExport star : stars) {
                if (star.resolvedModule() == null) {
                    continue;
                }
                ExportResolution sub = resolveExportName(
                        star.resolvedModule(),
                        exportName,
                        parsedModules,
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

    private String runtimeGlobalLookupCall(ParsedImport parsedImport) {
        return "__qin_global__(" + stringLiteral(runtimeGlobalName(parsedImport)) + ")";
    }

    private String runtimeGlobalName(ParsedImport parsedImport) {
        if (parsedImport.kind() == ImportKind.NAMED) {
            String importedName = parsedImport.importedName();
            if (importedName != null && !importedName.isBlank()) {
                return importedName;
            }
        }
        String localName = parsedImport.localName();
        if (localName == null || localName.isBlank()) {
            throw new IllegalArgumentException("External import local name cannot be blank: " + parsedImport);
        }
        return localName;
    }

    private String stringLiteral(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                + "\"";
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
        String source = module.source();

        Matcher declarationMatcher = EXPORT_CALLABLE_DECLARATION_PATTERN.matcher(source);
        while (declarationMatcher.find()) {
            String name = declarationMatcher.group(2).trim();
            exports.add(new ParsedExport(ExportKind.LOCAL_NAMED, name, name, null));
        }
        parseVariableExportDeclarations(source, exports);

        Matcher defaultMatcher = EXPORT_DEFAULT_PREFIX_PATTERN.matcher(source);
        while (defaultMatcher.find()) {
            int exprStart = defaultMatcher.end();
            int exprEnd = findDefaultExpressionStatementEnd(source, exprStart);
            String defaultExpr = trimTrailingSemicolon(source.substring(exprStart, exprEnd).trim());
            String localName = "default";
            Matcher functionDeclMatcher = DEFAULT_EXPR_FUNCTION_NAMED_PATTERN.matcher(defaultExpr);
            if (functionDeclMatcher.find()) {
                localName = functionDeclMatcher.group(1).trim();
            } else {
                Matcher classDeclMatcher = DEFAULT_EXPR_CLASS_NAMED_PATTERN.matcher(defaultExpr);
                if (classDeclMatcher.find()) {
                    localName = classDeclMatcher.group(1).trim();
                }
            }
            exports.add(new ParsedExport(ExportKind.LOCAL_DEFAULT, "default", localName, null));
            defaultMatcher.region(exprEnd, source.length());
        }

        Matcher namedMatcher = EXPORT_NAMED_PATTERN.matcher(source);
        while (namedMatcher.find()) {
            String block = namedMatcher.group(1);
            String moduleSpecifier = namedMatcher.group(2) == null ? null : namedMatcher.group(2).trim();
            Path resolvedModule = moduleSpecifier == null ? null : resolveTargetModule(module, moduleSpecifier);
            parseNamedExportBlock(block, resolvedModule, exports);
        }

        Matcher exportAllMatcher = EXPORT_ALL_PATTERN.matcher(source);
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

    private void parseVariableExportDeclarations(String source, List<ParsedExport> exports) {
        Matcher matcher = EXPORT_VARIABLE_PREFIX_PATTERN.matcher(source);
        while (matcher.find()) {
            int declarationStart = matcher.end();
            int declarationEnd = findDefaultExpressionStatementEnd(source, declarationStart);
            String declarationList = trimTrailingSemicolon(source.substring(declarationStart, declarationEnd));
            for (String declarator : splitTopLevelByComma(declarationList)) {
                String localName = extractExportedDeclaratorName(declarator);
                if (localName != null && !localName.isBlank()) {
                    exports.add(new ParsedExport(ExportKind.LOCAL_NAMED, localName, localName, null));
                }
            }
            matcher.region(declarationEnd, source.length());
        }
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

    private List<String> splitTopLevelByComma(String text) {
        List<String> parts = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return parts;
        }
        int start = 0;
        int parenDepth = 0;
        int braceDepth = 0;
        int bracketDepth = 0;
        boolean inSingle = false;
        boolean inDouble = false;
        boolean inTemplate = false;
        boolean escaping = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (inSingle) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '\'') {
                    inSingle = false;
                }
                continue;
            }
            if (inDouble) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inDouble = false;
                }
                continue;
            }
            if (inTemplate) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '`') {
                    inTemplate = false;
                }
                continue;
            }
            if (ch == '\'') {
                inSingle = true;
                continue;
            }
            if (ch == '"') {
                inDouble = true;
                continue;
            }
            if (ch == '`') {
                inTemplate = true;
                continue;
            }
            if (ch == '(') {
                parenDepth++;
                continue;
            }
            if (ch == ')' && parenDepth > 0) {
                parenDepth--;
                continue;
            }
            if (ch == '{') {
                braceDepth++;
                continue;
            }
            if (ch == '}' && braceDepth > 0) {
                braceDepth--;
                continue;
            }
            if (ch == '[') {
                bracketDepth++;
                continue;
            }
            if (ch == ']' && bracketDepth > 0) {
                bracketDepth--;
                continue;
            }
            if (ch == ',' && parenDepth == 0 && braceDepth == 0 && bracketDepth == 0) {
                parts.add(text.substring(start, i));
                start = i + 1;
            }
        }
        if (start <= text.length()) {
            parts.add(text.substring(start));
        }
        return parts;
    }

    private String extractExportedDeclaratorName(String declaratorSource) {
        if (declaratorSource == null) {
            return null;
        }
        String declarator = declaratorSource.trim();
        if (declarator.isEmpty()) {
            return null;
        }
        int assignmentIndex = indexOfTopLevelChar(declarator, '=');
        String lhs = assignmentIndex >= 0 ? declarator.substring(0, assignmentIndex).trim() : declarator;
        if (IDENTIFIER_PATTERN.matcher(lhs).matches()) {
            return lhs;
        }
        return null;
    }

    private int indexOfTopLevelChar(String text, char target) {
        int parenDepth = 0;
        int braceDepth = 0;
        int bracketDepth = 0;
        boolean inSingle = false;
        boolean inDouble = false;
        boolean inTemplate = false;
        boolean escaping = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (inSingle) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '\'') {
                    inSingle = false;
                }
                continue;
            }
            if (inDouble) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inDouble = false;
                }
                continue;
            }
            if (inTemplate) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '`') {
                    inTemplate = false;
                }
                continue;
            }
            if (ch == '\'') {
                inSingle = true;
                continue;
            }
            if (ch == '"') {
                inDouble = true;
                continue;
            }
            if (ch == '`') {
                inTemplate = true;
                continue;
            }
            if (ch == '(') {
                parenDepth++;
                continue;
            }
            if (ch == ')' && parenDepth > 0) {
                parenDepth--;
                continue;
            }
            if (ch == '{') {
                braceDepth++;
                continue;
            }
            if (ch == '}' && braceDepth > 0) {
                braceDepth--;
                continue;
            }
            if (ch == '[') {
                bracketDepth++;
                continue;
            }
            if (ch == ']' && bracketDepth > 0) {
                bracketDepth--;
                continue;
            }
            if (ch == target && parenDepth == 0 && braceDepth == 0 && bracketDepth == 0) {
                return i;
            }
        }
        return -1;
    }

    private String exportSymbol(Path moduleFile, String exportName, Map<Path, Integer> moduleIndex) {
        int idx = moduleIndex.getOrDefault(moduleFile, -1);
        return "__qesm_m" + idx + "_e_" + sanitize(exportName);
    }

    private String defaultLocalSymbol(Path moduleFile, Map<Path, Integer> moduleIndex) {
        int idx = moduleIndex.getOrDefault(moduleFile, -1);
        return "__qesm_m" + idx + "_default_local";
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
