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
    private static final Pattern COMMONJS_EXPORT_PATTERN = Pattern.compile(
            "\\bmodule\\s*\\.\\s*exports\\b|\\bexports\\s*\\.");
    private static final boolean PROFILE = Boolean.getBoolean("qin.esm.sema.profile");
    private final QinEsmStaticBindingCollector staticBindingCollector = new QinEsmStaticBindingCollector();

    public QinEsmSemanticModel analyze(QinModuleGraph graph) {
        Map<Path, QinEsmModuleSemantic> modules = new LinkedHashMap<>();
        int index = 0;
        for (QinModuleSource module : graph.modules()) {
            long started = System.nanoTime();
            if (PROFILE) {
                System.out.println("[QinEsmSemanticAnalyzer] module start index=" + index
                        + " chars=" + module.source().length()
                        + " file=" + module.file());
            }
            QinEsmStaticBindingCollector.Result staticBindings = collectStaticBindings(module);
            List<QinEsmImportBinding> imports = staticBindings.imports();
            List<QinEsmExportBinding> exports = finalizeExports(module, staticBindings.exports());
            modules.put(
                    module.file(),
                    new QinEsmModuleSemantic(module.file(), imports, exports));
            if (PROFILE) {
                long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
                System.out.println("[QinEsmSemanticAnalyzer] module done index=" + index
                        + " elapsedMs=" + elapsedMs
                        + " imports=" + imports.size()
                        + " exports=" + exports.size()
                        + " file=" + module.file());
            }
            index++;
        }
        return new QinEsmSemanticModel(graph.entryFile(), modules);
    }

    private QinEsmStaticBindingCollector.Result collectStaticBindings(QinModuleSource module) {
        if (!shouldCollectAstBindings(module)) {
            return new QinEsmStaticBindingCollector.Result(List.of(), List.of());
        }
        try {
            return staticBindingCollector.collect(module);
        } catch (RuntimeException error) {
            throw new IllegalArgumentException(
                    "Failed to collect ESM static bindings for module: " + module.file(),
                    error);
        }
    }

    private boolean shouldCollectAstBindings(QinModuleSource module) {
        return !isVirtualDefaultExportModule(module);
    }

    private List<QinEsmExportBinding> finalizeExports(
            QinModuleSource module,
            List<QinEsmExportBinding> astExports) {
        List<QinEsmExportBinding> exports = new ArrayList<>();
        boolean[] code = codeMask(module.source());
        addSyntheticDefaultExports(module, code, exports);
        exports.addAll(astExports == null ? List.of() : astExports);
        return deduplicateExports(exports);
    }

    private void addSyntheticDefaultExports(
            QinModuleSource module,
            boolean[] code,
            List<QinEsmExportBinding> exports) {
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
        if (isCommonJsInteropDefaultModule(module, code)) {
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
    }

    private boolean isVirtualDefaultExportModule(QinModuleSource module) {
        if (module == null || module.file() == null || module.file().getFileName() == null) {
            return false;
        }
        String fileName = module.file().getFileName().toString().toLowerCase();
        return fileName.endsWith(".vue")
                || fileName.endsWith(".ovs")
                || fileName.endsWith(".cssts")
                || fileName.endsWith(".css")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp")
                || fileName.endsWith(".ico")
                || fileName.endsWith(".avif");
    }

    private boolean isCommonJsInteropDefaultModule(QinModuleSource module, boolean[] code) {
        if (module == null || module.source() == null) {
            return false;
        }
        Matcher matcher = COMMONJS_EXPORT_PATTERN.matcher(module.source());
        while (matcher.find()) {
            if (isCodePosition(code, matcher.start())) {
                return true;
            }
        }
        return false;
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

    static Path resolveTargetModule(QinModuleSource module, String specifier) {
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
