package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                    "eval(...)",
                    "eval executes source text selected at runtime, so the receiver, binding set, and target lowering cannot be proven before JVM class emission",
                    "eval"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Function\\s*\\("),
                    "QIN_JS_UNSUPPORTED_NEW_FUNCTION",
                    "new Function is not supported by the Qin JVM target",
                    "new Function(...)",
                    "new Function constructs executable source text at runtime and cannot produce a fixed Qin IR or JVM method body during compilation",
                    "Function"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bwith\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WITH",
                    "with statements are not supported by the Qin JVM target",
                    "with (...)",
                    "with changes name resolution through runtime object scope, so local bindings and member owners are not statically known",
                    "with"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+Proxy\\s*\\("),
                    "QIN_JS_UNSUPPORTED_PROXY",
                    "Proxy is not supported by the Qin JVM target",
                    "new Proxy(...)",
                    "Proxy traps replace fixed property, call, construct, and prototype operations with runtime-selected behavior that is not isomorphic to JVM class members",
                    "Proxy"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bReflect\\s*\\."),
                    "QIN_JS_UNSUPPORTED_REFLECT",
                    "Reflect is not supported by the Qin JVM target",
                    "Reflect.*",
                    "Reflect exposes dynamic object protocol operations whose receiver/member behavior is not fixed at compile time",
                    "Reflect"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])require\\s*\\("),
                    "QIN_JS_UNSUPPORTED_REQUIRE",
                    "CommonJS require is not supported by the Qin JVM target",
                    "require(...)",
                    "CommonJS require resolves and executes modules through a mutable runtime loader instead of Qin's resolved static ESM graph",
                    "require"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?<![\\w.$])arguments\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_ARGUMENTS_OBJECT",
                    "the JavaScript arguments object is not supported by the Qin JVM target",
                    "arguments",
                    "the arguments object exposes caller-frame varargs dynamically instead of a declared Qin/JVM parameter list",
                    "arguments"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:Array|String|Object|Number|Boolean|Promise|Set|Map|RegExp|Date)\\s*\\.\\s*prototype\\s*(?:\\.\\s*[A-Za-z_$][\\w$]*|\\[[^\\]]+\\])\\s*(?:(?<![=!<>])=(?!=)|\\+\\+|--)"),
                    "QIN_JS_UNSUPPORTED_BUILTIN_PROTOTYPE_MUTATION",
                    "mutating built-in prototypes is not supported by the Qin JVM target",
                    "Builtin.prototype mutation",
                    "mutating builtin prototypes changes member lookup globally after compilation, so admitted method owners cannot remain fixed",
                    "prototype"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bnew\\s+(?:WeakMap|WeakSet|WeakRef|FinalizationRegistry)\\s*\\("),
                    "QIN_JS_UNSUPPORTED_WEAK_REF",
                    "weak reference collections are not supported by the Qin JVM target",
                    "new WeakMap/WeakSet/WeakRef/FinalizationRegistry(...)",
                    "weak lifetime and finalization semantics are host-runtime behavior outside Qin's current deterministic JVM collection subset",
                    "Weak"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\b(?:async\\s+)?function\\s*\\*"),
                    "QIN_JS_UNSUPPORTED_GENERATOR",
                    "generators are not supported by the Qin JVM target",
                    "function*",
                    "generators require resumable stack/state-machine semantics that are not part of Qin's current static JVM lowering surface",
                    "function"),
            new UnsupportedFeatureRule(
                    Pattern.compile("(?m)^\\s*await\\b(?!\\s*:)"),
                    "QIN_JS_UNSUPPORTED_TOP_LEVEL_AWAIT",
                    "top-level await is not supported by the Qin JVM target",
                    "top-level await",
                    "top-level await makes module initialization asynchronous; Qin JVM modules currently require a fixed synchronous initializer order",
                    "await"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bSymbol\\s*\\.\\s*(?!iterator\\b|hasInstance\\b)[A-Za-z_$][\\w$]*|\\bSymbol\\s*\\("),
                    "QIN_JS_UNSUPPORTED_SYMBOL",
                    "advanced Symbol features are not supported by the Qin JVM target",
                    "advanced Symbol",
                    "advanced Symbol values create non-string dynamic member keys beyond the current fixed Qin member/index model",
                    "Symbol"),
            new UnsupportedFeatureRule(
                    Pattern.compile("\\bIntl\\s*\\."),
                    "QIN_JS_UNSUPPORTED_INTL",
                    "Intl is not supported by the Qin JVM target",
                    "Intl.*",
                    "Intl depends on host locale data and APIs that are not modeled by Qin's current portable JVM runtime subset",
                    "Intl"));
    private static final List<String> THIRD_PARTY_STATIC_ADMISSION_CHOICES = List.of(
            "reject package",
            "write a Qin-owned facade",
            "select a different static package entry",
            "change project source with approval");
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
        java.util.regex.Matcher matcher = rule.pattern().matcher(source);
        boolean[] codeMask = codeMask(source);
        while (matcher.find()) {
            if (!isCodePosition(codeMask, matcher.start())) {
                continue;
            }
            if (isAllowedStaticMemberNameMatch(rule, source, codeMask, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(source, matcher.start());
            diagnostics.add(new QinEsmDiagnostic(
                    rule.code(),
                    messageFor(module, rule),
                    module.file(),
                    lineCol[0],
                    lineCol[1],
                    staticAdmissionReport(module, rule).orElse(null)));
            return;
        }
    }

    private String messageFor(QinModuleSource module, UnsupportedFeatureRule rule) {
        Optional<PackageLocation> packageLocation = thirdPartyPackageLocation(module);
        if (packageLocation.isEmpty()) {
            return rule.message();
        }
        return "third-party package " + packageLocation.get().packageName()
                + " failed Qin static admission: " + rule.message();
    }

    private Optional<QinEsmStaticAdmissionReport> staticAdmissionReport(
            QinModuleSource module,
            UnsupportedFeatureRule rule) {
        return thirdPartyPackageLocation(module)
                .map(packageLocation -> new QinEsmStaticAdmissionReport(
                        packageLocation.packageName(),
                        packageLocation.packageRoot(),
                        module.file(),
                        rule.unsupportedShape(),
                        rule.staticLoweringReason(),
                        THIRD_PARTY_STATIC_ADMISSION_CHOICES));
    }

    private Optional<PackageLocation> thirdPartyPackageLocation(QinModuleSource module) {
        if (module == null || module.file() == null) {
            return Optional.empty();
        }
        Path file = module.file().toAbsolutePath().normalize();
        List<String> parts = new ArrayList<>();
        for (Path part : file) {
            parts.add(part.toString());
        }
        for (int i = parts.size() - 1; i >= 0; i--) {
            if (!"node_modules".equals(parts.get(i))) {
                continue;
            }
            if (i + 1 >= parts.size()) {
                return Optional.empty();
            }
            String first = parts.get(i + 1);
            String packageName = first;
            int packageEnd = i + 1;
            if (first.startsWith("@")) {
                if (i + 2 >= parts.size()) {
                    return Optional.empty();
                }
                packageName = first + "/" + parts.get(i + 2);
                packageEnd = i + 2;
            }
            if (packageName.startsWith("@qin/")) {
                return Optional.empty();
            }
            Path packageRoot = file.getRoot();
            for (int j = 0; j <= packageEnd; j++) {
                packageRoot = packageRoot == null ? Path.of(parts.get(j)) : packageRoot.resolve(parts.get(j));
            }
            return Optional.of(new PackageLocation(packageName, packageRoot.normalize()));
        }
        return Optional.empty();
    }

    private boolean isAllowedStaticMemberNameMatch(
            UnsupportedFeatureRule rule,
            String source,
            boolean[] codeMask,
            int matchStart) {
        int tokenStart = nextCodeNonWhitespace(source, codeMask, matchStart);
        if (tokenStart < 0) {
            return false;
        }
        return ("QIN_JS_UNSUPPORTED_ARGUMENTS_OBJECT".equals(rule.code())
                || "QIN_JS_UNSUPPORTED_TOP_LEVEL_AWAIT".equals(rule.code()))
                && isMemberMethodDeclarationName(source, codeMask, tokenStart);
    }

    private boolean isMemberMethodDeclarationName(String source, boolean[] codeMask, int nameStart) {
        int previous = previousCodeNonWhitespace(source, codeMask, nameStart - 1);
        if (previous >= 0 && source.charAt(previous) != '{' && source.charAt(previous) != ';'
                && source.charAt(previous) != '}') {
            return false;
        }
        int afterName = nameStart;
        while (afterName < source.length() && isIdentifierPart(source.charAt(afterName))) {
            afterName++;
        }
        int openParen = nextCodeNonWhitespace(source, codeMask, afterName);
        if (openParen < 0 || source.charAt(openParen) != '(') {
            return false;
        }
        int closeParen = matchingCodeParen(source, codeMask, openParen);
        if (closeParen < 0) {
            return false;
        }
        int afterParen = nextCodeNonWhitespace(source, codeMask, closeParen + 1);
        if (afterParen < 0) {
            return false;
        }
        char next = source.charAt(afterParen);
        if (next == '{') {
            return true;
        }
        if (next != ':') {
            return false;
        }
        for (int i = afterParen + 1; i < source.length(); i++) {
            if (!isCodePosition(codeMask, i)) {
                continue;
            }
            char ch = source.charAt(i);
            if (ch == '{') {
                return true;
            }
            if (ch == ';' || ch == '=' || ch == '\n') {
                return false;
            }
        }
        return false;
    }

    private int previousCodeNonWhitespace(String source, boolean[] codeMask, int start) {
        for (int i = Math.min(start, source.length() - 1); i >= 0; i--) {
            if (isCodePosition(codeMask, i) && !Character.isWhitespace(source.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private int nextCodeNonWhitespace(String source, boolean[] codeMask, int start) {
        for (int i = Math.max(0, start); i < source.length(); i++) {
            if (isCodePosition(codeMask, i) && !Character.isWhitespace(source.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private int matchingCodeParen(String source, boolean[] codeMask, int openParen) {
        int depth = 0;
        for (int i = openParen; i < source.length(); i++) {
            if (!isCodePosition(codeMask, i)) {
                continue;
            }
            char ch = source.charAt(i);
            if (ch == '(') {
                depth++;
            } else if (ch == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isIdentifierPart(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
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

    private record UnsupportedFeatureRule(
            Pattern pattern,
            String code,
            String message,
            String unsupportedShape,
            String staticLoweringReason,
            List<String> triggers) {
        private UnsupportedFeatureRule(
                Pattern pattern,
                String code,
                String message,
                String unsupportedShape,
                String staticLoweringReason,
                String... triggers) {
            this(pattern, code, message, unsupportedShape, staticLoweringReason, List.of(triggers));
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

    private record PackageLocation(String packageName, Path packageRoot) {
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
            return QinEsmRuntimeFeatureValidator.this.isIdentifierPart(ch);
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
