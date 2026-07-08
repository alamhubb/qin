package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight top-level ESM import/export binding scanner.
 *
 * <p>This is the semantic analyzer's standard path. It collects only module
 * bindings needed by ESM link validation and intentionally does not build the
 * full Qin/Slime AST.
 */
final class QinEsmStaticBindingCollector {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+(?!type\\b)([^;]*?)\\s+from\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_NAMED_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?!type\\b)\\{([\\s\\S]*?)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_TYPE_NAMED_PATTERN = Pattern.compile(
            "^\\s*export\\s+type\\s*\\{([\\s\\S]*?)}\\s*(?:from\\s*[\"']([^\"']+)[\"'])?\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?!type\\b)\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_TYPE_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s+type\\s*\\*\\s*(?:as\\s+([A-Za-z_$][\\w$]*)\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_VARIABLE_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?:declare\\s+)?(?:const|let|var)\\s+([^;\\n]+)",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_DECLARATION_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?:declare\\s+)?(?:abstract\\s+)?(?:async\\s+)?"
                    + "(function|class|interface|type|enum|object)\\s+([A-Za-z_$][\\w$]*)",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_DEFAULT_DECLARATION_PATTERN = Pattern.compile(
            "^\\s*export\\s+default\\s+(?:abstract\\s+)?(?:async\\s+)?"
                    + "(function|class)\\s+([A-Za-z_$][\\w$]*)?",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_DEFAULT_PATTERN = Pattern.compile(
            "^\\s*export\\s+default\\b",
            Pattern.MULTILINE);
    private static final Pattern MODULE_EXPORT_PATTERN = Pattern.compile(
            "\\bmodule\\s*\\.\\s*exports\\b|\\bexports\\s*\\.");

    Result collect(QinModuleSource module) {
        if (module == null || module.source() == null) {
            return new Result(List.of(), List.of());
        }
        String source = module.source();
        boolean[] code = codeMask(source);
        List<QinEsmImportBinding> imports = new ArrayList<>();
        List<QinEsmExportBinding> exports = new ArrayList<>();
        collectImports(module, source, code, imports);
        collectExports(module, source, code, exports);
        return new Result(imports, exports);
    }

    private void collectImports(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmImportBinding> out) {
        Matcher sideEffect = IMPORT_SIDE_EFFECT_PATTERN.matcher(source);
        while (sideEffect.find()) {
            if (!isCodePosition(code, keywordIndex(source, sideEffect.start(), sideEffect.end(), "import"))) {
                continue;
            }
            String moduleSpecifier = sideEffect.group(1);
            int[] lineCol = lineCol(source, sideEffect.start(1));
            out.add(new QinEsmImportBinding(
                    module.file(),
                    moduleSpecifier,
                    QinEsmImportKind.SIDE_EFFECT,
                    "",
                    "",
                    lineCol[0],
                    lineCol[1],
                    QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier)));
        }

        Matcher from = IMPORT_FROM_PATTERN.matcher(source);
        while (from.find()) {
            if (!isCodePosition(code, keywordIndex(source, from.start(), from.end(), "import"))) {
                continue;
            }
            String clause = from.group(1).trim();
            if (clause.startsWith("type ")) {
                continue;
            }
            String moduleSpecifier = from.group(2);
            int[] lineCol = lineCol(source, from.start(2));
            collectImportClause(module, moduleSpecifier, clause, lineCol, out);
        }
    }

    private void collectImportClause(
            QinModuleSource module,
            String moduleSpecifier,
            String clause,
            int[] lineCol,
            List<QinEsmImportBinding> out) {
        Path resolvedModule = QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
        String remaining = clause.trim();
        if (remaining.startsWith("*")) {
            String name = afterKeyword(remaining, "as");
            addImport(module, moduleSpecifier, QinEsmImportKind.NAMESPACE, "*", name, lineCol, resolvedModule, out);
            return;
        }
        int namedStart = remaining.indexOf('{');
        if (namedStart > 0) {
            String defaultName = remaining.substring(0, namedStart).replace(",", "").trim();
            if (!defaultName.isBlank()) {
                addImport(
                        module,
                        moduleSpecifier,
                        QinEsmImportKind.DEFAULT,
                        "default",
                        defaultName,
                        lineCol,
                        resolvedModule,
                        out);
            }
            collectNamedImports(module, moduleSpecifier, betweenBraces(remaining), lineCol, resolvedModule, out);
            return;
        }
        if (remaining.startsWith("{")) {
            collectNamedImports(module, moduleSpecifier, betweenBraces(remaining), lineCol, resolvedModule, out);
            return;
        }
        if (!remaining.isBlank()) {
            addImport(
                    module,
                    moduleSpecifier,
                    QinEsmImportKind.DEFAULT,
                    "default",
                    remaining,
                    lineCol,
                    resolvedModule,
                    out);
        }
    }

    private void collectNamedImports(
            QinModuleSource module,
            String moduleSpecifier,
            String names,
            int[] lineCol,
            Path resolvedModule,
            List<QinEsmImportBinding> out) {
        for (String part : splitTopLevel(names, ',')) {
            String spec = part.trim();
            if (spec.isBlank()) {
                continue;
            }
            spec = stripLeadingComments(spec).trim();
            if (spec.isBlank()) {
                continue;
            }
            if (spec.startsWith("type ")) {
                continue;
            }
            Alias alias = parseAlias(spec);
            if (alias.sourceName().isBlank()) {
                continue;
            }
            addImport(
                    module,
                    moduleSpecifier,
                    QinEsmImportKind.NAMED,
                    alias.sourceName(),
                    alias.localName(),
                    lineCol,
                    resolvedModule,
                    out);
        }
    }

    private void addImport(
            QinModuleSource module,
            String moduleSpecifier,
            QinEsmImportKind kind,
            String importedName,
            String localName,
            int[] lineCol,
            Path resolvedModule,
            List<QinEsmImportBinding> out) {
        if (moduleSpecifier == null || moduleSpecifier.isBlank() || moduleSpecifier.startsWith("java:")) {
            return;
        }
        out.add(new QinEsmImportBinding(
                module.file(),
                moduleSpecifier,
                kind,
                importedName == null ? "" : importedName,
                localName == null ? "" : localName,
                lineCol[0],
                lineCol[1],
                resolvedModule));
    }

    private void collectExports(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmExportBinding> out) {
        collectNamedExportPattern(module, source, code, EXPORT_NAMED_PATTERN, false, out);
        collectNamedExportPattern(module, source, code, EXPORT_TYPE_NAMED_PATTERN, true, out);
        collectExportAllPattern(module, source, code, EXPORT_ALL_PATTERN, false, out);
        collectExportAllPattern(module, source, code, EXPORT_TYPE_ALL_PATTERN, true, out);
        collectVariableExports(module, source, code, out);
        collectDeclarationExports(module, source, code, out);
        collectDefaultExports(module, source, code, out);
        collectCommonJsDefaultExport(module, source, code, out);
    }

    private void collectNamedExportPattern(
            QinModuleSource module,
            String source,
            boolean[] code,
            Pattern pattern,
            boolean typeOnlyExport,
            List<QinEsmExportBinding> out) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, keywordIndex(source, matcher.start(), matcher.end(), "export"))) {
                continue;
            }
            String moduleSpecifier = matcher.group(2);
            boolean reExport = moduleSpecifier != null && !moduleSpecifier.isBlank();
            Path resolvedModule = reExport ? QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier) : null;
            int[] lineCol = lineCol(source, matcher.start());
            for (String part : splitTopLevel(matcher.group(1), ',')) {
                String spec = part.trim();
                if (spec.isBlank()) {
                    continue;
                }
                spec = stripLeadingComments(spec).trim();
                if (spec.isBlank()) {
                    continue;
                }
                boolean specifierTypeOnly = typeOnlyExport || spec.startsWith("type ");
                if (spec.startsWith("type ")) {
                    spec = spec.substring("type ".length()).trim();
                }
                Alias alias = parseAlias(spec);
                if (alias.sourceName().isBlank() || alias.localName().isBlank()) {
                    continue;
                }
                out.add(new QinEsmExportBinding(
                        module.file(),
                        reExport ? QinEsmExportKind.RE_EXPORT_NAMED : QinEsmExportKind.LOCAL_NAMED,
                        alias.localName(),
                        alias.sourceName(),
                        specifierTypeOnly,
                        reExport ? moduleSpecifier : null,
                        reExport ? resolvedModule : null,
                        lineCol[0],
                        lineCol[1]));
            }
        }
    }

    private void collectExportAllPattern(
            QinModuleSource module,
            String source,
            boolean[] code,
            Pattern pattern,
            boolean typeOnly,
            List<QinEsmExportBinding> out) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, keywordIndex(source, matcher.start(), matcher.end(), "export"))) {
                continue;
            }
            String exported = matcher.group(1);
            String moduleSpecifier = matcher.group(2);
            Path resolvedModule = QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
            int[] lineCol = lineCol(source, matcher.start());
            if (exported == null || exported.isBlank()) {
                out.add(new QinEsmExportBinding(
                        module.file(),
                        QinEsmExportKind.RE_EXPORT_ALL,
                        "*",
                        "*",
                        typeOnly,
                        moduleSpecifier,
                        resolvedModule,
                        lineCol[0],
                        lineCol[1]));
            } else {
                out.add(new QinEsmExportBinding(
                        module.file(),
                        QinEsmExportKind.RE_EXPORT_NAMESPACE,
                        exported.trim(),
                        "*",
                        typeOnly,
                        moduleSpecifier,
                        resolvedModule,
                        lineCol[0],
                        lineCol[1]));
            }
        }
    }

    private void collectVariableExports(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmExportBinding> out) {
        Matcher matcher = EXPORT_VARIABLE_PATTERN.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, keywordIndex(source, matcher.start(), matcher.end(), "export"))) {
                continue;
            }
            int[] lineCol = lineCol(source, matcher.start());
            for (String declarator : splitTopLevel(matcher.group(1), ',')) {
                String name = firstIdentifierBeforeInitializer(declarator);
                if (!name.isBlank()) {
                    addLocalExport(module, name, false, lineCol, out);
                }
            }
        }
    }

    private void collectDeclarationExports(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmExportBinding> out) {
        Matcher matcher = EXPORT_DECLARATION_PATTERN.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, keywordIndex(source, matcher.start(), matcher.end(), "export"))) {
                continue;
            }
            String type = matcher.group(1);
            String name = matcher.group(2);
            int[] lineCol = lineCol(source, matcher.start());
            addLocalExport(module, name, "interface".equals(type) || "type".equals(type), lineCol, out);
        }
    }

    private void collectDefaultExports(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmExportBinding> out) {
        Matcher matcher = EXPORT_DEFAULT_PATTERN.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, keywordIndex(source, matcher.start(), matcher.end(), "export"))) {
                continue;
            }
            int[] lineCol = lineCol(source, matcher.start());
            out.add(new QinEsmExportBinding(
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

        Matcher declaration = EXPORT_DEFAULT_DECLARATION_PATTERN.matcher(source);
        while (declaration.find()) {
            if (!isCodePosition(code, keywordIndex(source, declaration.start(), declaration.end(), "export"))) {
                continue;
            }
            String name = declaration.group(2);
            if (name != null && !name.isBlank()) {
                addLocalExport(module, name, false, lineCol(source, declaration.start()), out);
            }
        }
    }

    private void collectCommonJsDefaultExport(
            QinModuleSource module,
            String source,
            boolean[] code,
            List<QinEsmExportBinding> out) {
        Matcher matcher = MODULE_EXPORT_PATTERN.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(source, matcher.start());
            out.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.LOCAL_DEFAULT,
                    "default",
                    "default",
                    false,
                    null,
                    null,
                    lineCol[0],
                    lineCol[1]));
            return;
        }
    }

    private void addLocalExport(
            QinModuleSource module,
            String name,
            boolean typeOnly,
            int[] lineCol,
            List<QinEsmExportBinding> out) {
        out.add(new QinEsmExportBinding(
                module.file(),
                QinEsmExportKind.LOCAL_NAMED,
                name,
                name,
                typeOnly,
                null,
                null,
                lineCol[0],
                lineCol[1]));
    }

    private String betweenBraces(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start < 0 || end <= start) {
            return "";
        }
        return text.substring(start + 1, end);
    }

    private String afterKeyword(String text, String keyword) {
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\s+([A-Za-z_$][\\w$]*)\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private Alias parseAlias(String spec) {
        String[] parts = spec.split("\\s+as\\s+");
        if (parts.length == 2) {
            return new Alias(cleanIdentifier(parts[0]), cleanIdentifier(parts[1]));
        }
        String name = cleanIdentifier(spec);
        return new Alias(name, name);
    }

    private String stripLeadingComments(String text) {
        String current = text == null ? "" : text.trim();
        boolean changed = true;
        while (changed) {
            changed = false;
            if (current.startsWith("//")) {
                int newline = current.indexOf('\n');
                current = newline >= 0 ? current.substring(newline + 1).trim() : "";
                changed = true;
            }
            if (current.startsWith("/*")) {
                int end = current.indexOf("*/");
                current = end >= 0 ? current.substring(end + 2).trim() : "";
                changed = true;
            }
        }
        return current;
    }

    private String firstIdentifierBeforeInitializer(String declarator) {
        String text = declarator == null ? "" : declarator.trim();
        int equals = text.indexOf('=');
        if (equals >= 0) {
            text = text.substring(0, equals).trim();
        }
        Matcher matcher = Pattern.compile("^([A-Za-z_$][\\w$]*)\\b").matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String cleanIdentifier(String value) {
        String text = value == null ? "" : value.trim();
        Matcher matcher = Pattern.compile("^([A-Za-z_$][\\w$]*)$").matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private List<String> splitTopLevel(String text, char separator) {
        List<String> parts = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return parts;
        }
        int depth = 0;
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '(' || ch == '{' || ch == '[' || ch == '<') {
                depth++;
            } else if (ch == ')' || ch == '}' || ch == ']' || ch == '>') {
                depth = Math.max(0, depth - 1);
            } else if (ch == separator && depth == 0) {
                parts.add(text.substring(start, i));
                start = i + 1;
            }
        }
        parts.add(text.substring(start));
        return parts;
    }

    private boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';

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
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                template = true;
            } else {
                code[i] = true;
            }
        }
        return code;
    }

    private boolean isCodePosition(boolean[] code, int index) {
        return index >= 0 && index < code.length && code[index];
    }

    private int keywordIndex(String source, int start, int end, String keyword) {
        int index = source.indexOf(keyword, start);
        return index >= 0 && index < end ? index : start;
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

    private boolean isEscaped(String source, int index) {
        int backslashes = 0;
        for (int i = index - 1; i >= 0 && source.charAt(i) == '\\'; i--) {
            backslashes++;
        }
        return (backslashes & 1) == 1;
    }

    record Result(List<QinEsmImportBinding> imports, List<QinEsmExportBinding> exports) {
        Result {
            imports = imports == null ? List.of() : List.copyOf(imports);
            exports = exports == null ? List.of() : List.copyOf(exports);
        }
    }

    private record Alias(String sourceName, String localName) {
    }
}
