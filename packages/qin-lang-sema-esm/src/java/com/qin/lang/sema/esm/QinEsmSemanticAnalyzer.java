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
            "(?m)^\\s*import\\s+(?!type\\b)(.+?)\\s+from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$");
    private static final Pattern EXPORT_DECLARATION_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+(?:declare\\s+)?(?:abstract\\s+)?"
                    + "(const|let|var|function|class|interface|type|enum)\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_DEFAULT_DECLARATION_PATTERN = Pattern.compile(
            "\\bexport\\s+default\\s+(?:abstract\\s+)?"
                    + "(?:class|function)\\s+([A-Za-z_$][\\w$]*)(?:\\s*<[^>{};=]*>)?\\b");
    private static final Pattern EXPORT_DEFAULT_PATTERN = Pattern.compile(
            "\\bexport\\s+default\\b");
    private static final Pattern EXPORT_NAMED_PATTERN = Pattern.compile(
            "\\bexport\\s*(type\\s+)?\\{([^}]*)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?");
    private static final Pattern EXPORT_ALL_PATTERN = Pattern.compile(
            "\\bexport\\s*\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?");

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
        boolean[] code = codeMask(module.source());
        Matcher matcher = IMPORT_FROM_PATTERN.matcher(module.source());
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
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
            if (!isCodePosition(code, sideEffectMatcher.start())) {
                continue;
            }
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
        if (isVirtualDefaultExportModule(module)) {
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_DEFAULT,
                    "default",
                    "default",
                    false,
                    null,
                    null,
                    1,
                    1));
        }
        boolean[] code = codeMask(module.source());
        Matcher matcher = EXPORT_DECLARATION_PATTERN.matcher(module.source());
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String declarationKind = matcher.group(1).trim();
            String name = matcher.group(2).trim();
            int[] lineCol = lineCol(module.source(), matcher.start(2));
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_NAMED,
                    name,
                    name,
                    isTypeOnlyDeclarationKind(declarationKind),
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
        }

        Matcher defaultMatcher = EXPORT_DEFAULT_PATTERN.matcher(module.source());
        while (defaultMatcher.find()) {
            if (!isCodePosition(code, defaultMatcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(module.source(), defaultMatcher.start());
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_DEFAULT,
                    "default",
                    "default",
                    false,
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
        }
        addDefaultDeclarationNamedExports(module, code, exports);

        Matcher namedMatcher = EXPORT_NAMED_PATTERN.matcher(module.source());
        while (namedMatcher.find()) {
            if (!isCodePosition(code, exportKeywordIndex(module.source(), namedMatcher.start(), namedMatcher.end()))) {
                continue;
            }
            boolean statementTypeOnly = namedMatcher.group(1) != null && !namedMatcher.group(1).isBlank();
            String block = namedMatcher.group(2);
            String moduleSpecifier = namedMatcher.group(3) == null ? null : namedMatcher.group(3).trim();
            Path resolvedModule = moduleSpecifier == null ? null : resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(module.source(), namedMatcher.start());
            parseNamedExportBlock(
                    module.file(),
                    block,
                    statementTypeOnly,
                    moduleSpecifier,
                    resolvedModule,
                    lineCol[0],
                    lineCol[1],
                    exports);
        }
        parseNamedExportLines(module, code, exports);

        Matcher exportAllMatcher = EXPORT_ALL_PATTERN.matcher(module.source());
        while (exportAllMatcher.find()) {
            if (!isCodePosition(code, exportKeywordIndex(module.source(), exportAllMatcher.start(), exportAllMatcher.end()))) {
                continue;
            }
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
                        false,
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
                        false,
                        moduleSpecifier,
                        resolvedModule,
                        lineCol[0],
                        lineCol[1]));
            }
        }
        return deduplicateExports(exports);
    }

    private boolean isVirtualDefaultExportModule(QinModuleSource module) {
        if (module == null || module.file() == null || module.file().getFileName() == null) {
            return false;
        }
        String fileName = module.file().getFileName().toString().toLowerCase();
        return fileName.endsWith(".vue")
                || fileName.endsWith(".ovs")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp")
                || fileName.endsWith(".ico")
                || fileName.endsWith(".avif");
    }

    private void addDefaultDeclarationNamedExports(
            QinModuleSource module,
            boolean[] code,
            List<QinEsmExportBinding> exports) {
        Matcher matcher = EXPORT_DEFAULT_DECLARATION_PATTERN.matcher(module.source());
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String name = matcher.group(1).trim();
            int[] lineCol = lineCol(module.source(), matcher.start(1));
            exports.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_NAMED,
                    name,
                    name,
                    false,
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
        }
    }

    private List<QinEsmExportBinding> deduplicateExports(List<QinEsmExportBinding> exports) {
        Map<String, QinEsmExportBinding> dedup = new LinkedHashMap<>();
        for (QinEsmExportBinding exportBinding : exports) {
            String key = exportBinding.kind()
                    + "|" + exportBinding.exportName()
                    + "|" + exportBinding.localName()
                    + "|" + exportBinding.typeOnly()
                    + "|" + exportBinding.moduleSpecifier()
                    + "|" + exportBinding.resolvedModule();
            dedup.putIfAbsent(key, exportBinding);
        }
        return new ArrayList<>(dedup.values());
    }

    private void parseNamedExportLines(
            QinModuleSource module,
            boolean[] code,
            List<QinEsmExportBinding> exports) {
        String source = module.source();
        int lineStart = 0;
        while (lineStart < source.length()) {
            int lineEnd = source.indexOf('\n', lineStart);
            if (lineEnd < 0) {
                lineEnd = source.length();
            }
            String line = source.substring(lineStart, lineEnd);
            int exportOffset = firstNonWhitespaceOffset(line);
            int exportIndex = exportOffset < 0 ? -1 : lineStart + exportOffset;
            if (exportIndex >= 0 && line.startsWith("export", exportOffset)) {
                parseNamedExportLine(module, line, lineStart, exportOffset, exports);
            }
            lineStart = lineEnd + 1;
        }
    }

    private void parseNamedExportLine(
            QinModuleSource module,
            String line,
            int lineStart,
            int exportOffset,
            List<QinEsmExportBinding> exports) {
        int braceStart = line.indexOf('{', exportOffset + "export".length());
        int braceEnd = line.lastIndexOf('}');
        if (braceStart < 0 || braceEnd <= braceStart) {
            return;
        }
        String prefix = line.substring(exportOffset, braceStart).trim();
        if (!"export".equals(prefix) && !"export type".equals(prefix)) {
            return;
        }
        String after = line.substring(braceEnd + 1).trim();
        String moduleSpecifier = null;
        if (after.startsWith("from")) {
            Matcher fromMatcher = Pattern.compile("^from\\s*[\"']([^\"']+)[\"']").matcher(after);
            if (fromMatcher.find()) {
                moduleSpecifier = fromMatcher.group(1).trim();
            }
        }
        boolean typeOnly = "export type".equals(prefix);
        Path resolvedModule = moduleSpecifier == null ? null : resolveTargetModule(module, moduleSpecifier);
        int[] lineCol = lineCol(module.source(), lineStart + exportOffset);
        parseNamedExportBlock(
                module.file(),
                line.substring(braceStart + 1, braceEnd),
                typeOnly,
                moduleSpecifier,
                resolvedModule,
                lineCol[0],
                lineCol[1],
                exports);
    }

    private int firstNonWhitespaceOffset(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }
        return -1;
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
                if (spec.startsWith("type ")) {
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
            boolean statementTypeOnly,
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
            boolean typeOnly = statementTypeOnly;
            if (spec.startsWith("type ")) {
                spec = spec.substring("type ".length()).trim();
                typeOnly = true;
                if (spec.isEmpty()) {
                    continue;
                }
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
                        typeOnly,
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
                        typeOnly,
                        moduleSpecifier,
                        resolvedModule,
                        line,
                        column));
            }
        }
    }

    private Path resolveTargetModule(QinModuleSource module, String specifier) {
        Path fallback = null;
        for (QinResolvedImport resolvedImport : module.imports()) {
            if (specifier.equals(resolvedImport.descriptor().moduleSpecifier())) {
                if (resolvedImport.resolvedModule() != null) {
                    return resolvedImport.resolvedModule();
                }
                if (fallback == null) {
                    fallback = resolvedImport.resolvedModule();
                }
            }
        }
        return fallback;
    }

    private boolean isTypeOnlyDeclarationKind(String declarationKind) {
        return "type".equals(declarationKind) || "interface".equals(declarationKind);
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

    private boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        int templateExpressionDepth = 0;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';

            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                    code[i] = true;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (single) {
                if (ch == '\'' && !isEscaped(source, i)) {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && !isEscaped(source, i)) {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '$' && next == '{' && !isEscaped(source, i)) {
                    code[i] = true;
                    code[i + 1] = true;
                    templateExpressionDepth = 1;
                    template = false;
                    i++;
                    continue;
                }
                if (ch == '`' && !isEscaped(source, i)) {
                    template = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '/' && startsRegexLiteral(source, i)) {
                i = skipRegexLiteral(source, i);
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                if (templateExpressionDepth > 0) {
                    i = skipTemplateLiteral(source, i);
                } else {
                    template = true;
                }
            } else {
                code[i] = true;
                if (templateExpressionDepth > 0) {
                    if (ch == '{') {
                        templateExpressionDepth++;
                    } else if (ch == '}') {
                        templateExpressionDepth--;
                        if (templateExpressionDepth == 0) {
                            template = true;
                        }
                    }
                }
            }
        }
        return code;
    }

    private boolean isCodePosition(boolean[] code, int index) {
        return index >= 0 && index < code.length && code[index];
    }

    private int exportKeywordIndex(String source, int start, int end) {
        int index = source.indexOf("export", start);
        return index >= 0 && index < end ? index : start;
    }

    private boolean startsRegexLiteral(String source, int slashIndex) {
        int previous = slashIndex - 1;
        while (previous >= 0 && Character.isWhitespace(source.charAt(previous))) {
            previous--;
        }
        if (previous < 0) {
            return true;
        }
        char ch = source.charAt(previous);
        return "([{:;,=!?&|+-*~^<>%".indexOf(ch) >= 0;
    }

    private int skipRegexLiteral(String source, int slashIndex) {
        boolean inClass = false;
        for (int i = slashIndex + 1; i < source.length(); i++) {
            char ch = source.charAt(i);
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (ch == '\n' || ch == '\r') {
                return i - 1;
            }
            if (ch == '[' && !isEscaped(source, i)) {
                inClass = true;
            } else if (ch == ']' && !isEscaped(source, i)) {
                inClass = false;
            } else if (ch == '/' && !isEscaped(source, i) && !inClass) {
                while (i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))) {
                    i++;
                }
                return i;
            }
        }
        return slashIndex;
    }

    private int skipTemplateLiteral(String source, int startIndex) {
        int expressionDepth = 0;
        for (int i = startIndex + 1; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            if (ch == '`' && expressionDepth == 0 && !isEscaped(source, i)) {
                return i;
            }
            if (ch == '$' && next == '{' && !isEscaped(source, i)) {
                expressionDepth++;
                i++;
                continue;
            }
            if (ch == '}' && expressionDepth > 0 && !isEscaped(source, i)) {
                expressionDepth--;
            }
        }
        return startIndex;
    }

    private boolean isEscaped(String source, int index) {
        int backslashes = 0;
        for (int i = index - 1; i >= 0 && source.charAt(i) == '\\'; i--) {
            backslashes++;
        }
        return (backslashes & 1) == 1;
    }
}
