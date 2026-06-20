package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Guards runtime-level ESM features that are not implemented yet.
 */
public final class QinEsmRuntimeFeatureValidator {
    private static final Pattern DYNAMIC_IMPORT_PATTERN = Pattern.compile("\\bimport\\s*\\(");
    private static final List<UnsupportedFeatureRule> STRICT_JVM_RULES = List.of(
            new UnsupportedFeatureRule(
                    Pattern.compile("\\beval\\s*\\("),
                    "QIN_JS_UNSUPPORTED_EVAL",
                    "eval is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Function\\s*\\("),
                    "QIN_JS_UNSUPPORTED_NEW_FUNCTION",
                    "new Function is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bwith\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WITH",
                    "with statements are not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Proxy\\s*\\("),
                    "QIN_JS_UNSUPPORTED_PROXY",
                    "Proxy is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bReflect\\s*\\."),
                    "QIN_JS_UNSUPPORTED_REFLECT",
                    "Reflect is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])require\\s*\\("),
                    "QIN_JS_UNSUPPORTED_REQUIRE",
                    "CommonJS require is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])arguments\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_ARGUMENTS_OBJECT",
                    "the JavaScript arguments object is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bObject\\s*\\.\\s*defineProperty\\s*\\("),
                    "QIN_JS_UNSUPPORTED_OBJECT_DEFINE_PROPERTY",
                    "Object.defineProperty is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:Array|String|Object|Number|Boolean|Promise|Set|Map|RegExp|Date)\\s*\\.\\s*prototype\\s*(?:\\.\\s*[A-Za-z_$][\\w$]*|\\[[^\\]]+\\])\\s*(?:=|\\+\\+|--)"),
                    "QIN_JS_UNSUPPORTED_BUILTIN_PROTOTYPE_MUTATION",
                    "mutating built-in prototypes is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+(?:WeakMap|WeakSet|WeakRef|FinalizationRegistry)\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WEAK_REF",
                    "weak reference collections are not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:async\\s+)?function\\s*\\*"),
                    "QIN_JS_UNSUPPORTED_GENERATOR",
                    "generators are not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?m)^\\s*await\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_TOP_LEVEL_AWAIT",
                    "top-level await is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bimport\\s*\\.\\s*meta\\b"),
                    "QIN_JS_UNSUPPORTED_IMPORT_META",
                    "import.meta is not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bSymbol\\s*\\.\\s*(?!iterator\\b)[A-Za-z_$][\\w$]*|\\bSymbol\\s*\\("),
                    "QIN_JS_UNSUPPORTED_SYMBOL",
                    "advanced Symbol features are not supported by the Qin JVM target"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bIntl\\s*\\."),
                    "QIN_JS_UNSUPPORTED_INTL",
                    "Intl is not supported by the Qin JVM target"));
    private final boolean allowRuntimeDynamicImport;
    private final boolean strictJvmFeatureSubset;

    public QinEsmRuntimeFeatureValidator() {
        this(false, true);
    }

    private QinEsmRuntimeFeatureValidator(boolean allowRuntimeDynamicImport, boolean strictJvmFeatureSubset) {
        this.allowRuntimeDynamicImport = allowRuntimeDynamicImport;
        this.strictJvmFeatureSubset = strictJvmFeatureSubset;
    }

    public static QinEsmRuntimeFeatureValidator forBrowserFrontend() {
        return new QinEsmRuntimeFeatureValidator(true, false);
    }

    public void validate(QinModuleGraph graph) {
        List<QinEsmDiagnostic> diagnostics = new ArrayList<>();
        for (QinModuleSource module : graph.modules()) {
            scanOne(module, diagnostics);
        }
        if (!diagnostics.isEmpty()) {
            throw new QinEsmSemanticException(diagnostics);
        }
    }

    private void scanOne(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        if (!allowRuntimeDynamicImport) {
            addDynamicImportIfMatched(module, diagnostics);
        }
        if (strictJvmFeatureSubset && !isQinOwnedRuntimeSupportModule(module)) {
            addStrictJvmFeatureDiagnostics(module, diagnostics);
        }
    }

    private void addDynamicImportIfMatched(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        Matcher matcher = DYNAMIC_IMPORT_PATTERN.matcher(module.source());
        boolean[] code = codeMask(module.source());
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(module.source(), matcher.start());
            diagnostics.add(new QinEsmDiagnostic(
                    "ESM3001",
                    "dynamic import is not implemented for the JVM runtime target yet",
                    module.file(),
                    lineCol[0],
                    lineCol[1]));
            return;
        }
    }

    private void addIfMatched(
            QinModuleSource module,
            List<QinEsmDiagnostic> diagnostics,
            Pattern pattern,
            String code,
            String message) {
        Matcher matcher = pattern.matcher(module.source());
        boolean[] codeMask = codeMask(module.source());
        while (matcher.find()) {
            if (!isCodePosition(codeMask, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(module.source(), matcher.start());
            diagnostics.add(new QinEsmDiagnostic(
                    code,
                    message,
                    module.file(),
                    lineCol[0],
                    lineCol[1]));
            return;
        }
    }

    private void addStrictJvmFeatureDiagnostics(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        for (UnsupportedFeatureRule rule : STRICT_JVM_RULES) {
            addIfMatched(module, diagnostics, rule.pattern(), rule.code(), rule.message());
        }
    }

    private boolean isQinOwnedRuntimeSupportModule(QinModuleSource module) {
        String normalized = module.file().toAbsolutePath().normalize().toString().replace('\\', '/');
        return normalized.contains("/packages/java-sdk-js/")
                || normalized.contains("/node_modules/@qin/java-sdk-js/")
                || normalized.contains("/src/generated/slime-parser.bundle.");
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
                if (ch == '\'' && previous != '\\') {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && previous != '\\') {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '`' && previous != '\\') {
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

    private record UnsupportedFeatureRule(Pattern pattern, String code, String message) {
    }
}
