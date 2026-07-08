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
    private static final List<UnsupportedFeatureRule> STRICT_JVM_RULES = List.of(
            new UnsupportedFeatureRule(
                    Pattern.compile("\\beval\\s*\\("),
                    "QIN_JS_UNSUPPORTED_EVAL",
                    "eval is not supported by the Qin JVM target",
                    "eval"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Function\\s*\\("),
                    "QIN_JS_UNSUPPORTED_NEW_FUNCTION",
                    "new Function is not supported by the Qin JVM target",
                    "Function"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bwith\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WITH",
                    "with statements are not supported by the Qin JVM target",
                    "with"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Proxy\\s*\\("),
                    "QIN_JS_UNSUPPORTED_PROXY",
                    "Proxy is not supported by the Qin JVM target",
                    "Proxy"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bReflect\\s*\\."),
                    "QIN_JS_UNSUPPORTED_REFLECT",
                    "Reflect is not supported by the Qin JVM target",
                    "Reflect"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])require\\s*\\("),
                    "QIN_JS_UNSUPPORTED_REQUIRE",
                    "CommonJS require is not supported by the Qin JVM target",
                    "require"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])arguments\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_ARGUMENTS_OBJECT",
                    "the JavaScript arguments object is not supported by the Qin JVM target",
                    "arguments"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:Array|String|Object|Number|Boolean|Promise|Set|Map|RegExp|Date)\\s*\\.\\s*prototype\\s*(?:\\.\\s*[A-Za-z_$][\\w$]*|\\[[^\\]]+\\])\\s*(?:(?<![=!<>])=(?!=)|\\+\\+|--)"),
                    "QIN_JS_UNSUPPORTED_BUILTIN_PROTOTYPE_MUTATION",
                    "mutating built-in prototypes is not supported by the Qin JVM target",
                    "prototype"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+(?:WeakMap|WeakSet|WeakRef|FinalizationRegistry)\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WEAK_REF",
                    "weak reference collections are not supported by the Qin JVM target",
                    "Weak"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:async\\s+)?function\\s*\\*"),
                    "QIN_JS_UNSUPPORTED_GENERATOR",
                    "generators are not supported by the Qin JVM target",
                    "function"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?m)^\\s*await\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_TOP_LEVEL_AWAIT",
                    "top-level await is not supported by the Qin JVM target",
                    "await"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bSymbol\\s*\\.\\s*(?!iterator\\b|hasInstance\\b)[A-Za-z_$][\\w$]*|\\bSymbol\\s*\\("),
                    "QIN_JS_UNSUPPORTED_SYMBOL",
                    "advanced Symbol features are not supported by the Qin JVM target",
                    "Symbol"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bIntl\\s*\\."),
                    "QIN_JS_UNSUPPORTED_INTL",
                    "Intl is not supported by the Qin JVM target",
                    "Intl"));
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
        RuntimeSyntaxFeatures runtimeSyntaxFeatures = null;
        if (!allowRuntimeDynamicImport) {
            runtimeSyntaxFeatures = inspectRuntimeSyntax(module);
            if (runtimeSyntaxFeatures.dynamicImport() != null) {
                FeatureLocation location = runtimeSyntaxFeatures.dynamicImport();
                diagnostics.add(new QinEsmDiagnostic(
                        "ESM3001",
                        "dynamic import is not implemented for the JVM runtime target yet",
                        module.file(),
                        location.line(),
                        location.column()));
            }
        }
        if (strictJvmFeatureSubset && !isQinOwnedRuntimeSupportModule(module)) {
            if (runtimeSyntaxFeatures == null) {
                runtimeSyntaxFeatures = inspectRuntimeSyntax(module);
            }
            if (runtimeSyntaxFeatures.importMeta() != null) {
                FeatureLocation location = runtimeSyntaxFeatures.importMeta();
                diagnostics.add(new QinEsmDiagnostic(
                        "QIN_JS_UNSUPPORTED_IMPORT_META",
                        "import.meta is not supported by the Qin JVM target",
                        module.file(),
                        location.line(),
                        location.column()));
            }
            addStrictJvmFeatureDiagnostics(module, diagnostics);
        }
    }

    private void addIfMatched(
            QinModuleSource module,
            List<QinEsmDiagnostic> diagnostics,
            UnsupportedFeatureRule rule) {
        String source = module.source();
        if (!rule.mayMatch(source)) {
            return;
        }
        Matcher matcher = rule.pattern().matcher(source);
        boolean[] codeMask = codeMask(source);
        while (matcher.find()) {
            if (!isCodePosition(codeMask, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(source, matcher.start());
            diagnostics.add(new QinEsmDiagnostic(
                    rule.code(),
                    rule.message(),
                    module.file(),
                    lineCol[0],
                    lineCol[1]));
            return;
        }
    }

    private void addStrictJvmFeatureDiagnostics(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        for (UnsupportedFeatureRule rule : STRICT_JVM_RULES) {
            addIfMatched(module, diagnostics, rule);
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

    private RuntimeSyntaxFeatures inspectRuntimeSyntax(QinModuleSource module) {
        return new RuntimeSyntaxScanner(module.source()).scan();
    }

    private record UnsupportedFeatureRule(Pattern pattern, String code, String message, List<String> triggers) {
        private UnsupportedFeatureRule(Pattern pattern, String code, String message, String... triggers) {
            this(pattern, code, message, List.of(triggers));
        }

        private boolean mayMatch(String source) {
            if (source == null || source.isEmpty()) {
                return false;
            }
            for (String trigger : triggers) {
                if (trigger != null && !trigger.isEmpty() && source.contains(trigger)) {
                    return true;
                }
            }
            return triggers.isEmpty();
        }
    }

    private record FeatureLocation(int line, int column) {
    }

    private record RuntimeSyntaxFeatures(FeatureLocation dynamicImport, FeatureLocation importMeta) {
        private static final RuntimeSyntaxFeatures NONE = new RuntimeSyntaxFeatures(null, null);
    }

    private final class RuntimeSyntaxScanner {
        private final String source;
        private int index;
        private FeatureLocation dynamicImport;
        private FeatureLocation importMeta;

        private RuntimeSyntaxScanner(String source) {
            this.source = source == null ? "" : source;
        }

        private RuntimeSyntaxFeatures scan() {
            scanCode();
            return new RuntimeSyntaxFeatures(dynamicImport, importMeta);
        }

        private void scanCode() {
            while (index < source.length() && (dynamicImport == null || importMeta == null)) {
                if (skipTriviaOrLiteral()) {
                    continue;
                }
                inspectImportKeyword();
                advance();
            }
        }

        private void scanTemplateExpression() {
            int depth = 1;
            while (index < source.length() && depth > 0 && (dynamicImport == null || importMeta == null)) {
                if (skipTriviaOrLiteral()) {
                    continue;
                }
                char ch = source.charAt(index);
                if (ch == '{') {
                    depth++;
                    advance();
                } else if (ch == '}') {
                    depth--;
                    advance();
                } else {
                    inspectImportKeyword();
                    advance();
                }
            }
        }

        private boolean skipTriviaOrLiteral() {
            char ch = source.charAt(index);
            char next = peek(1);
            if (ch == '/' && next == '/') {
                skipLineComment();
                return true;
            }
            if (ch == '/' && next == '*') {
                skipBlockComment();
                return true;
            }
            if (ch == '\'' || ch == '"') {
                skipQuoted(ch);
                return true;
            }
            if (ch == '`') {
                skipTemplate();
                return true;
            }
            if (ch == '/' && shouldStartRegexLiteral()) {
                skipRegexLiteral();
                return true;
            }
            return false;
        }

        private void inspectImportKeyword() {
            if (!startsWithImportKeyword()) {
                return;
            }
            int importIndex = index;
            int afterKeyword = index + "import".length();
            int afterTrivia = skipWhitespaceAndComments(afterKeyword);
            if (dynamicImport == null && afterTrivia < source.length() && source.charAt(afterTrivia) == '(') {
                dynamicImport = location(importIndex);
            }
            if (importMeta == null
                    && afterKeyword < source.length()
                    && source.startsWith(".meta", afterKeyword)
                    && isIdentifierBoundary(afterKeyword + ".meta".length())) {
                importMeta = location(importIndex);
            }
        }

        private boolean startsWithImportKeyword() {
            int end = index + "import".length();
            return end <= source.length()
                    && source.startsWith("import", index)
                    && isIdentifierBoundary(index - 1)
                    && isIdentifierBoundary(end);
        }

        private int skipWhitespaceAndComments(int start) {
            int pos = start;
            while (pos < source.length()) {
                char ch = source.charAt(pos);
                char next = pos + 1 < source.length() ? source.charAt(pos + 1) : '\0';
                if (Character.isWhitespace(ch)) {
                    pos++;
                } else if (ch == '/' && next == '/') {
                    pos += 2;
                    while (pos < source.length() && source.charAt(pos) != '\n') {
                        pos++;
                    }
                } else if (ch == '/' && next == '*') {
                    pos += 2;
                    while (pos + 1 < source.length()
                            && !(source.charAt(pos) == '*' && source.charAt(pos + 1) == '/')) {
                        pos++;
                    }
                    pos = Math.min(source.length(), pos + 2);
                } else {
                    break;
                }
            }
            return pos;
        }

        private void skipLineComment() {
            while (index < source.length() && source.charAt(index) != '\n') {
                advance();
            }
        }

        private void skipBlockComment() {
            advance();
            advance();
            while (index < source.length()) {
                if (source.charAt(index) == '*' && peek(1) == '/') {
                    advance();
                    advance();
                    return;
                }
                advance();
            }
        }

        private void skipQuoted(char quote) {
            advance();
            while (index < source.length()) {
                char ch = source.charAt(index);
                if (ch == '\\') {
                    advance();
                    if (index < source.length()) {
                        advance();
                    }
                } else if (ch == quote) {
                    advance();
                    return;
                } else {
                    advance();
                }
            }
        }

        private void skipTemplate() {
            advance();
            while (index < source.length()) {
                char ch = source.charAt(index);
                if (ch == '\\') {
                    advance();
                    if (index < source.length()) {
                        advance();
                    }
                } else if (ch == '`') {
                    advance();
                    return;
                } else if (ch == '$' && peek(1) == '{') {
                    advance();
                    advance();
                    scanTemplateExpression();
                } else {
                    advance();
                }
            }
        }

        private void skipRegexLiteral() {
            boolean inClass = false;
            advance();
            while (index < source.length()) {
                char ch = source.charAt(index);
                if (ch == '\\') {
                    advance();
                    if (index < source.length()) {
                        advance();
                    }
                } else if (ch == '[') {
                    inClass = true;
                    advance();
                } else if (ch == ']') {
                    inClass = false;
                    advance();
                } else if (ch == '/' && !inClass) {
                    advance();
                    while (index < source.length() && isIdentifierPart(source.charAt(index))) {
                        advance();
                    }
                    return;
                } else {
                    advance();
                }
            }
        }

        private boolean shouldStartRegexLiteral() {
            int pos = index - 1;
            while (pos >= 0 && Character.isWhitespace(source.charAt(pos))) {
                pos--;
            }
            if (pos < 0) {
                return true;
            }
            char previous = source.charAt(pos);
            if ("([{:;,=!?&|+-*~^<>".indexOf(previous) >= 0) {
                return true;
            }
            if (isIdentifierPart(previous)) {
                int end = pos + 1;
                while (pos >= 0 && isIdentifierPart(source.charAt(pos))) {
                    pos--;
                }
                String previousWord = source.substring(pos + 1, end);
                return previousWord.equals("return")
                        || previousWord.equals("throw")
                        || previousWord.equals("yield")
                        || previousWord.equals("await")
                        || previousWord.equals("case")
                        || previousWord.equals("delete")
                        || previousWord.equals("typeof")
                        || previousWord.equals("void")
                        || previousWord.equals("in")
                        || previousWord.equals("of")
                        || previousWord.equals("instanceof");
            }
            return false;
        }

        private boolean isIdentifierBoundary(int pos) {
            return pos < 0 || pos >= source.length() || !isIdentifierPart(source.charAt(pos));
        }

        private boolean isIdentifierPart(char ch) {
            return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
        }

        private FeatureLocation location(int sourceIndex) {
            int[] lineCol = lineCol(source, sourceIndex);
            return new FeatureLocation(lineCol[0], lineCol[1]);
        }

        private char peek(int offset) {
            int pos = index + offset;
            return pos >= 0 && pos < source.length() ? source.charAt(pos) : '\0';
        }

        private void advance() {
            index++;
        }
    }
}
