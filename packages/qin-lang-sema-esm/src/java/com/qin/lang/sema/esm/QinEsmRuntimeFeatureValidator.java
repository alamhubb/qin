package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserFacade;
import com.slime.ast.AstNode;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.ImportExpression;
import com.slime.ast.nodes.expressions.MetaProperty;

import java.lang.reflect.Array;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
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

    private RuntimeSyntaxFeatures inspectRuntimeSyntax(QinModuleSource module) {
        QinParsedSource parsed = new QinParserFacade().parseSource(module.source());
        if (!parsed.hasProgram()) {
            return RuntimeSyntaxFeatures.NONE;
        }
        RuntimeSyntaxVisitor visitor = new RuntimeSyntaxVisitor();
        visitor.visit(parsed.programAst());
        return visitor.features();
    }

    private FeatureLocation featureLocation(SourceLocation location) {
        if (location == null || location.start() == null) {
            return new FeatureLocation(1, 1);
        }
        int line = Math.max(1, location.start().line());
        int column = Math.max(1, location.start().column());
        return new FeatureLocation(line, column);
    }

    private record UnsupportedFeatureRule(Pattern pattern, String code, String message) {
    }

    private record FeatureLocation(int line, int column) {
    }

    private record RuntimeSyntaxFeatures(FeatureLocation dynamicImport, FeatureLocation importMeta) {
        private static final RuntimeSyntaxFeatures NONE = new RuntimeSyntaxFeatures(null, null);
    }

    private final class RuntimeSyntaxVisitor {
        private final Map<Object, Boolean> seen = new IdentityHashMap<>();
        private FeatureLocation dynamicImport;
        private FeatureLocation importMeta;

        private RuntimeSyntaxFeatures features() {
            return new RuntimeSyntaxFeatures(dynamicImport, importMeta);
        }

        private void visit(Object value) {
            if (value == null || (dynamicImport != null && importMeta != null)) {
                return;
            }
            if (value instanceof ImportExpression importExpression) {
                if (dynamicImport == null) {
                    dynamicImport = featureLocation(importExpression.location());
                }
            }
            if (value instanceof MetaProperty metaProperty && isImportMeta(metaProperty)) {
                if (importMeta == null) {
                    importMeta = featureLocation(metaProperty.location());
                }
            }
            if (value instanceof Iterable<?> iterable) {
                for (Object item : iterable) {
                    visit(item);
                }
                return;
            }
            Class<?> type = value.getClass();
            if (type.isArray()) {
                int length = Array.getLength(value);
                for (int index = 0; index < length; index++) {
                    visit(Array.get(value, index));
                }
                return;
            }
            if (!shouldTraverseRecord(value, type) || seen.put(value, Boolean.TRUE) != null) {
                return;
            }
            for (RecordComponent component : type.getRecordComponents()) {
                try {
                    visit(component.getAccessor().invoke(value));
                } catch (ReflectiveOperationException error) {
                    throw new IllegalStateException(
                            "Failed to inspect AST record component "
                                    + type.getName()
                                    + "."
                                    + component.getName(),
                            error);
                }
            }
        }

        private boolean shouldTraverseRecord(Object value, Class<?> type) {
            return value instanceof AstNode || (type.isRecord() && type.getName().startsWith("com.slime.ast."));
        }

        private boolean isImportMeta(MetaProperty metaProperty) {
            Identifier meta = metaProperty.meta();
            Identifier property = metaProperty.property();
            return meta != null
                    && property != null
                    && "import".equals(meta.name())
                    && "meta".equals(property.name());
        }
    }
}
