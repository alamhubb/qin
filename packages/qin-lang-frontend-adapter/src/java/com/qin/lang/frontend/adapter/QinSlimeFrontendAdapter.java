package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Frontend adapter that lowers a tiny JS-like subset into Qin IR.
 *
 * Parsing strategy: always use Slime Java parser (real CST/AST frontend).
 */
public final class QinSlimeFrontendAdapter {
    private static final Pattern IMPORT_LINE_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+(?:[^;\\n]*?\\s+from\\s+)?[\"'][^\"'\\n]+[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+(.+?)\\s+from\\s+[\"']([^\"'\\n]+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"']([^\"'\\n]+)[\"']\\s*;?\\s*$");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z_$][A-Za-z0-9_$]*$");

    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        String sourceForSlime = source.trim();
        if (sourceForSlime.isEmpty()) {
            return new QinIrProgram(
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of());
        }

        try {
            return parseProgramWithSlime(sourceForSlime, List.of(), List.of());
        } catch (Exception primaryError) {
            try {
                ExtractedImports extracted = extractImports(sourceForSlime);
                if (!extracted.hasAnyImport()) {
                    throw primaryError;
                }
                String strippedSource = extracted.strippedSource().trim();
                if (strippedSource.isEmpty()) {
                    return new QinIrProgram(
                            List.of(),
                            List.of(),
                            List.of(),
                            extracted.javaImports(),
                            extracted.jsImports(),
                            List.of(),
                            List.of(),
                            List.of());
                }
                return parseProgramWithSlime(
                        strippedSource,
                        extracted.javaImports(),
                        extracted.jsImports());
            } catch (Exception fallbackError) {
                Throwable cause = fallbackError == primaryError ? primaryError : fallbackError;
                String message = fallbackError == primaryError
                        ? safeMessage(primaryError)
                        : ("primary=" + safeMessage(primaryError) + "; fallback=" + safeMessage(fallbackError));
                throw new IllegalArgumentException(
                        "Failed to parse Qin source with Slime frontend.\n" +
                                "Make sure Slime Java modules are on classpath.\n" +
                                "Cause: " + message,
                        cause);
            }
        }
    }

    public String parseAst(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        String sourceForSlime = source.trim();
        if (sourceForSlime.isEmpty()) {
            return "Program(empty)";
        }

        try {
            return parseAstWithSlime(sourceForSlime);
        } catch (Exception primaryError) {
            try {
                ExtractedImports extracted = extractImports(sourceForSlime);
                if (!extracted.hasAnyImport()) {
                    throw primaryError;
                }
                String strippedSource = extracted.strippedSource().trim();
                if (strippedSource.isEmpty()) {
                    return "Program(import-only)";
                }
                return parseAstWithSlime(strippedSource);
            } catch (Exception fallbackError) {
                Throwable cause = fallbackError == primaryError ? primaryError : fallbackError;
                String message = fallbackError == primaryError
                        ? safeMessage(primaryError)
                        : ("primary=" + safeMessage(primaryError) + "; fallback=" + safeMessage(fallbackError));
                throw new IllegalArgumentException(
                        "Failed to parse AST with Slime frontend.\n"
                                + "Cause: " + message,
                        cause);
            }
        }
    }

    private ExtractedImports extractImports(String source) {
        List<QinIrJavaImport> javaImports = new ArrayList<>();
        List<QinIrJsImport> jsImports = new ArrayList<>();
        Matcher matcher = IMPORT_LINE_PATTERN.matcher(source);
        StringBuilder stripped = new StringBuilder();
        int cursor = 0;
        while (matcher.find()) {
            stripped.append(source, cursor, matcher.start());
            String importLine = matcher.group();
            parseImportLine(importLine, javaImports, jsImports);
            cursor = matcher.end();
        }
        stripped.append(source, cursor, source.length());
        return new ExtractedImports(stripped.toString(), List.copyOf(javaImports), List.copyOf(jsImports));
    }

    private void parseImportLine(
            String importLine,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        Matcher fromMatcher = IMPORT_FROM_PATTERN.matcher(importLine);
        if (fromMatcher.matches()) {
            String clause = fromMatcher.group(1).trim();
            String module = fromMatcher.group(2).trim();
            parseImportClause(clause, module, javaImports, jsImports);
            return;
        }
        Matcher sideEffectMatcher = IMPORT_SIDE_EFFECT_PATTERN.matcher(importLine);
        if (sideEffectMatcher.matches()) {
            String module = sideEffectMatcher.group(1).trim();
            if (module.startsWith("java:")) {
                throw new IllegalArgumentException("java: import does not support side-effect form: " + module);
            }
            if (isJsModule(module)) {
                jsImports.add(new QinIrJsImport(module, "", ""));
                return;
            }
            throw new IllegalArgumentException("Unsupported import module: " + module);
        }
        throw new IllegalArgumentException("Unsupported import syntax: " + importLine);
    }

    private void parseImportClause(
            String clause,
            String module,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        ParsedImportClause parsed = parseSpecifierClause(clause);
        if (module.startsWith("java:")) {
            String javaModule = module.substring("java:".length()).trim();
            if (javaModule.isBlank()) {
                throw new IllegalArgumentException("java: import module cannot be blank");
            }
            if (parsed.defaultLocalName() != null || parsed.namespaceLocalName() != null) {
                throw new IllegalArgumentException("Only named import specifier is supported for java: imports");
            }
            if (parsed.namedImports().isEmpty()) {
                throw new IllegalArgumentException("java: import requires named specifiers");
            }
            for (NamedImport named : parsed.namedImports()) {
                String ownerBinaryName = javaModule + "." + named.importedName();
                javaImports.add(new QinIrJavaImport(module, named.importedName(), named.localName(), ownerBinaryName));
            }
            return;
        }

        if (!isJsModule(module)) {
            throw new IllegalArgumentException("Unsupported import module: " + module);
        }
        if (parsed.defaultLocalName() != null) {
            jsImports.add(new QinIrJsImport(module, "default", parsed.defaultLocalName()));
        }
        if (parsed.namespaceLocalName() != null) {
            jsImports.add(new QinIrJsImport(module, "*", parsed.namespaceLocalName()));
        }
        for (NamedImport named : parsed.namedImports()) {
            jsImports.add(new QinIrJsImport(module, named.importedName(), named.localName()));
        }
    }

    private ParsedImportClause parseSpecifierClause(String clause) {
        String normalized = clause == null ? "" : clause.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Import clause cannot be empty");
        }

        String defaultLocalName = null;
        String namespaceLocalName = null;
        List<NamedImport> namedImports = new ArrayList<>();
        int topLevelComma = findTopLevelComma(normalized);
        String head = topLevelComma >= 0 ? normalized.substring(0, topLevelComma).trim() : normalized;
        String tail = topLevelComma >= 0 ? normalized.substring(topLevelComma + 1).trim() : "";

        if (head.startsWith("{")) {
            parseNamedImports(head, namedImports);
        } else if (head.startsWith("*")) {
            namespaceLocalName = parseNamespaceClause(head);
        } else {
            defaultLocalName = parseIdentifier(head, "default import local name");
        }

        if (!tail.isEmpty()) {
            if (tail.startsWith("{")) {
                parseNamedImports(tail, namedImports);
            } else if (tail.startsWith("*")) {
                if (namespaceLocalName != null) {
                    throw new IllegalArgumentException("Duplicated namespace import specifier");
                }
                namespaceLocalName = parseNamespaceClause(tail);
            } else {
                throw new IllegalArgumentException("Unsupported import specifier tail: " + tail);
            }
        }

        return new ParsedImportClause(defaultLocalName, namespaceLocalName, List.copyOf(namedImports));
    }

    private void parseNamedImports(String braceClause, List<NamedImport> namedImports) {
        String content = parseBraceContent(braceClause);
        if (content.isBlank()) {
            return;
        }
        for (String rawPart : content.split(",")) {
            String part = rawPart.trim();
            if (part.isEmpty()) {
                continue;
            }
            String importedName;
            String localName;
            int asIndex = indexOfKeywordAs(part);
            if (asIndex >= 0) {
                importedName = parseIdentifier(part.substring(0, asIndex).trim(), "named import imported name");
                localName = parseIdentifier(part.substring(asIndex + 4).trim(), "named import local name");
            } else {
                importedName = parseIdentifier(part, "named import imported name");
                localName = importedName;
            }
            namedImports.add(new NamedImport(importedName, localName));
        }
    }

    private String parseNamespaceClause(String clause) {
        String text = clause.trim();
        if (!text.startsWith("*")) {
            throw new IllegalArgumentException("Namespace import must start with '*': " + clause);
        }
        String remainder = text.substring(1).trim();
        if (!remainder.startsWith("as ")) {
            throw new IllegalArgumentException("Namespace import must use 'as': " + clause);
        }
        return parseIdentifier(remainder.substring(3).trim(), "namespace import local name");
    }

    private static String parseBraceContent(String text) {
        String trimmed = text.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new IllegalArgumentException("Named import specifier must be wrapped in braces: " + text);
        }
        return trimmed.substring(1, trimmed.length() - 1).trim();
    }

    private static String parseIdentifier(String text, String where) {
        String identifier = text == null ? "" : text.trim();
        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(where + " must be Identifier, got: " + text);
        }
        return identifier;
    }

    private static int findTopLevelComma(String text) {
        int braceDepth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                braceDepth++;
                continue;
            }
            if (c == '}') {
                braceDepth = Math.max(0, braceDepth - 1);
                continue;
            }
            if (c == ',' && braceDepth == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int indexOfKeywordAs(String text) {
        String normalized = " " + text.trim().replaceAll("\\s+", " ") + " ";
        int idx = normalized.indexOf(" as ");
        if (idx < 0) {
            return -1;
        }
        String collapsed = text.trim().replaceAll("\\s+", " ");
        return collapsed.indexOf(" as ");
    }

    private record ExtractedImports(
            String strippedSource,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        private boolean hasAnyImport() {
            return !javaImports.isEmpty() || !jsImports.isEmpty();
        }
    }

    private record NamedImport(String importedName, String localName) {
    }

    private record ParsedImportClause(
            String defaultLocalName,
            String namespaceLocalName,
            List<NamedImport> namedImports) {
    }

    public QinIrProgram parseConstObjectDeclaration(String source) {
        return parseProgram(source);
    }

    private QinIrProgram parseProgramWithSlime(
            String source,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports)
            throws ReflectiveOperationException {
        Object programAst = createProgramAst(source);
        return lowerProgramAst(programAst, preImports, preJsImports);
    }

    private String parseAstWithSlime(String source) throws ReflectiveOperationException {
        Object programAst = createProgramAst(source);
        return AstJsonEncoder.toJson(programAst);
    }

    private Object createProgramAst(String source) throws ReflectiveOperationException {
        Class<?> slimeParserClass = Class.forName("com.slime.parser.SlimeJavascriptParser");
        Class<?> sourceTypeClass = Arrays.stream(slimeParserClass.getDeclaredClasses())
                .filter(c -> c.getSimpleName().equals("SourceType"))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("SlimeJavascriptParser.SourceType not found"));
        Object sourceType = selectSourceType(source, sourceTypeClass);

        Class<?> subhutiParserClass = Class.forName("com.subhuti.parser.SubhutiParser");
        Method createMethod = subhutiParserClass.getMethod("create", Class.class, Object[].class);
        Object parser = invokeStatic(createMethod, slimeParserClass, new Object[]{source});

        Method programMethod = findMethod(parser.getClass(), "Program", 1);
        Object cst = invoke(programMethod, parser, sourceType);
        if (cst == null) {
            try {
                Method getCstMethod = findMethod(parser.getClass(), "getCst", 0);
                cst = invoke(getCstMethod, parser);
            } catch (NoSuchMethodException ignored) {
                // keep null and fail with explicit message below
            }
        }
        if (cst == null) {
            throw new IllegalArgumentException("Slime parser returned null CST");
        }

        Class<?> cstToAstUtilsClass = Class.forName("com.slime.parser.cstToAst.SlimeCstToAstUtils");
        Method createProgramAstMethod = findMethod(cstToAstUtilsClass, "createProgramAst", 1);
        Object programAst = invokeStatic(createProgramAstMethod, cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private QinIrProgram lowerProgramAst(
            Object programAst,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports) {
        List<?> body = asList(invokeByName(programAst, "body"), "Program.body");
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        List<QinIrConstDeclaration> declarations = new ArrayList<>();
        List<QinIrConsoleLogValue> consoleValueLogs = new ArrayList<>();
        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        List<QinIrJavaImport> javaImports = new ArrayList<>();
        List<QinIrJsImport> jsImports = new ArrayList<>();
        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls = new ArrayList<>();
        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = new ArrayList<>();
        Map<String, String> javaImportLookup = new HashMap<>();
        Map<String, QinIrExpression> declarationLookup = new HashMap<>();
        if (preImports != null) {
            javaImports.addAll(preImports);
            for (QinIrJavaImport javaImport : preImports) {
                registerJavaImportLookup(javaImportLookup, javaImport);
            }
        }
        if (preJsImports != null) {
            jsImports.addAll(preJsImports);
        }

        for (Object statement : body) {
            String nodeType = simpleName(statement);
            if ("ImportDeclaration".equals(nodeType)) {
                LoweredImports loweredImports = lowerImportDeclaration(statement);
                javaImports.addAll(loweredImports.javaImports());
                jsImports.addAll(loweredImports.jsImports());
                for (QinIrJavaImport javaImport : loweredImports.javaImports()) {
                    registerJavaImportLookup(javaImportLookup, javaImport);
                }
                continue;
            }
            if ("VariableDeclaration".equals(nodeType)) {
                QinIrConstDeclaration declaration = lowerVariableDeclaration(statement, javaImportLookup);
                declarations.add(declaration);
                declarationLookup.put(declaration.name(), declaration.initializer());
                continue;
            }
            if ("ExpressionStatement".equals(nodeType)) {
                LoweredStatement lowered = lowerExpressionStatement(statement, javaImportLookup, declarationLookup);
                if (lowered.consoleValueLog() != null) {
                    consoleValueLogs.add(lowered.consoleValueLog());
                }
                if (lowered.objectLog() != null) {
                    consoleLogs.add(lowered.objectLog());
                }
                if (lowered.javaStaticCall() != null) {
                    javaStaticConsoleLogs.add(lowered.javaStaticCall());
                }
                if (lowered.javaInstanceMethodCall() != null) {
                    javaInstanceMethodCalls.add(lowered.javaInstanceMethodCall());
                }
                if (lowered.javaInstanceConsoleLog() != null) {
                    javaInstanceConsoleLogs.add(lowered.javaInstanceConsoleLog());
                }
                continue;
            }
            throw new IllegalArgumentException("Unsupported top-level statement type: " + nodeType);
        }

        if (declarations.isEmpty()
                && consoleValueLogs.isEmpty()
                && consoleLogs.isEmpty()
                && javaStaticConsoleLogs.isEmpty()
                && javaInstanceMethodCalls.isEmpty()
                && javaInstanceConsoleLogs.isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one supported statement");
        }

        return new QinIrProgram(
                declarations,
                consoleValueLogs,
                consoleLogs,
                javaImports,
                jsImports,
                javaStaticConsoleLogs,
                javaInstanceMethodCalls,
                javaInstanceConsoleLogs);
    }

    private LoweredImports lowerImportDeclaration(Object importDeclarationAst) {
        Object sourceNode = invokeByName(importDeclarationAst, "source");
        String sourceValue = asString(invokeByName(sourceNode, "value"), "ImportDeclaration.source.value");
        List<?> specifiers = asList(invokeByName(importDeclarationAst, "specifiers"), "ImportDeclaration.specifiers");

        if (sourceValue.startsWith("java:")) {
            String javaModule = sourceValue.substring("java:".length());
            if (javaModule.isBlank()) {
                throw new IllegalArgumentException("java: import module cannot be blank");
            }
            if (specifiers.isEmpty()) {
                throw new IllegalArgumentException("java: import does not support side-effect form: " + sourceValue);
            }
            List<QinIrJavaImport> imports = new ArrayList<>();
            for (Object specifier : specifiers) {
                if (!"ImportSpecifier".equals(simpleName(specifier))) {
                    throw new IllegalArgumentException("Only named import specifier is supported for java: imports, got: "
                            + simpleName(specifier));
                }
                String importedName = extractIdentifierName(invokeByName(specifier, "imported"), "ImportSpecifier.imported");
                String localName = extractIdentifierName(invokeByName(specifier, "local"), "ImportSpecifier.local");
                String ownerBinaryName = javaModule + "." + importedName;
                imports.add(new QinIrJavaImport(sourceValue, importedName, localName, ownerBinaryName));
            }
            return new LoweredImports(imports, List.of());
        }

        if (isJsModule(sourceValue)) {
            List<QinIrJsImport> imports = new ArrayList<>();
            if (specifiers.isEmpty()) {
                imports.add(new QinIrJsImport(sourceValue, "", ""));
                return new LoweredImports(List.of(), imports);
            }
            for (Object specifier : specifiers) {
                String nodeType = simpleName(specifier);
                if ("ImportSpecifier".equals(nodeType)) {
                    String importedName = extractIdentifierName(invokeByName(specifier, "imported"), "ImportSpecifier.imported");
                    String localName = extractIdentifierName(invokeByName(specifier, "local"), "ImportSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, importedName, localName));
                    continue;
                }
                if ("ImportDefaultSpecifier".equals(nodeType)) {
                    String localName = extractIdentifierName(invokeByName(specifier, "local"), "ImportDefaultSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, "default", localName));
                    continue;
                }
                if ("ImportNamespaceSpecifier".equals(nodeType)) {
                    String localName = extractIdentifierName(invokeByName(specifier, "local"), "ImportNamespaceSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, "*", localName));
                    continue;
                }
                throw new IllegalArgumentException("Unsupported js import specifier type: " + nodeType);
            }
            return new LoweredImports(List.of(), imports);
        }

        throw new IllegalArgumentException("Unsupported import module: " + sourceValue);
    }

    private void registerJavaImportLookup(Map<String, String> lookup, QinIrJavaImport javaImport) {
        lookup.put(javaImport.localName(), javaImport.ownerBinaryName());
    }

    private boolean isJsModule(String moduleName) {
        if (moduleName == null || moduleName.isBlank()) {
            return false;
        }
        return moduleName.startsWith("js:")
                || moduleName.endsWith(".js")
                || moduleName.endsWith(".mjs")
                || !moduleName.startsWith("java:");
    }

    private QinIrConstDeclaration lowerVariableDeclaration(
            Object variableDeclarationAst,
            Map<String, String> javaImportLookup) {
        String kind = asString(invokeByName(variableDeclarationAst, "kind"), "VariableDeclaration.kind");
        if (!"const".equals(kind)) {
            throw qjsError("QJS2002", "Only const declaration is supported, but got: " + kind);
        }

        List<?> declarators = asList(invokeByName(variableDeclarationAst, "declarations"),
                "VariableDeclaration.declarations");
        if (declarators.size() != 1) {
            throw qjsError("QJS2002", "Only one declarator is supported in const declaration");
        }

        Object declarator = declarators.get(0);
        Object id = invokeByName(declarator, "id");
        Object init = invokeByName(declarator, "init");

        String name = extractIdentifierName(id, "VariableDeclarator.id");
        QinIrExpression initializer = lowerDeclarationInitializer(init, javaImportLookup);

        return new QinIrConstDeclaration(name, initializer);
    }

    private QinIrExpression lowerDeclarationInitializer(Object expressionAst, Map<String, String> javaImportLookup) {
        QinIrExpression initializer = lowerExpression(expressionAst, javaImportLookup);
        if (initializer instanceof QinIrObjectLiteral || initializer instanceof QinIrJavaNewExpression) {
            return initializer;
        }
        throw qjsError("QJS2002", "Only object literal or Java constructor initializer is supported in const declaration");
    }

    private QinIrObjectLiteral lowerObjectLiteral(Object objectExpressionAst) {
        String nodeType = simpleName(objectExpressionAst);
        if (!"ObjectExpression".equals(nodeType)) {
            throw qjsError("QJS2002", "Only object literal initializer is supported, got: " + nodeType);
        }

        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();

        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                throw qjsError("QJS2002", "Only normal object property is supported, got: " + simpleName(property));
            }
            Object keyNode = invokeByName(property, "key");
            Object valueNode = invokeByName(property, "value");

            String key = extractPropertyKey(keyNode);
            QinIrExpression value = lowerObjectPropertyValue(valueNode);
            irProperties.add(new QinIrObjectProperty(key, value));
        }

        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrExpression lowerObjectPropertyValue(Object expressionAst) {
        QinIrExpression value = lowerExpression(expressionAst, Map.of());
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral) {
            return value;
        }
        throw qjsError("QJS2002", "Only primitive literal values are supported in object value");
    }

    private LoweredStatement lowerExpressionStatement(
            Object expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        if (!"CallExpression".equals(simpleName(expression))) {
            throw qjsError("QJS2001", "Only call expression statement is supported");
        }

        Object callee = invokeByName(expression, "callee");
        if (isConsoleLogCallee(callee)) {
            return lowerConsoleLogCall(expression, javaImportLookup, declarationLookup);
        }
        return lowerJavaInstanceMethodStatement(expression, declarationLookup, javaImportLookup);
    }

    private LoweredStatement lowerConsoleLogCall(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!"CallExpression".equals(simpleName(expressionAst))) {
            throw qjsError("QJS2001", "Only console.log(...) expression statement is supported");
        }

        Object callee = invokeByName(expressionAst, "callee");
        if (!isConsoleLogCallee(callee)) {
            throw qjsError("QJS2001", "Only console.log(...) call is supported");
        }

        List<?> arguments = asList(invokeByName(expressionAst, "arguments"), "CallExpression.arguments");
        if (arguments.size() != 1) {
            throw qjsError("QJS2002", "console.log(...) must have exactly one argument");
        }

        Object firstArgument = arguments.get(0);
        if ("CallExpression".equals(simpleName(firstArgument))) {
            Object nestedCallee = invokeByName(firstArgument, "callee");
            if ("MemberExpression".equals(simpleName(nestedCallee))) {
                String receiverName = extractIdentifierName(
                        invokeByName(nestedCallee, "object"),
                        "CallExpression.callee.object");
                if (javaImportLookup.containsKey(receiverName)
                        || declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                    return lowerConsoleLogJavaCall(firstArgument, javaImportLookup, declarationLookup);
                }
            }
        }

        QinIrExpression value = lowerRuntimeExpression(firstArgument, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(value), null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogJavaCall(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "console.log call argument must be member call like Math.random()");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaInstanceCall(
                            receiverName,
                            javaNewExpression.ownerBinaryName(),
                            methodName,
                            arguments));
        }

        QinIrBuiltinCallExpression builtin =
                lowerBuiltinCallExpression(callExpressionAst, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(builtin), null, null, null, null);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            Object callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "Only member call expression statement is supported");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (!(declaration instanceof QinIrJavaNewExpression javaNewExpression)) {
            throw qjsError("QJS2003", "Only Java instance method call statement is supported: " + receiverName);
        }

        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
        return new LoweredStatement(
                null,
                null,
                null,
                new QinIrJavaInstanceMethodCall(
                        receiverName,
                        javaNewExpression.ownerBinaryName(),
                        methodName,
                        arguments),
                null);
    }

    private boolean isConsoleLogCallee(Object calleeAst) {
        if (!"MemberExpression".equals(simpleName(calleeAst))) {
            return false;
        }
        String objectName = extractIdentifierName(invokeByName(calleeAst, "object"), "callee.object");
        String propertyName = extractIdentifierName(invokeByName(calleeAst, "property"), "callee.property");
        return "console".equals(objectName) && "log".equals(propertyName);
    }

    private String extractPropertyKey(Object keyNode) {
        String nodeType = simpleName(keyNode);
        if ("Identifier".equals(nodeType)) {
            return asString(invokeByName(keyNode, "name"), "Identifier.name");
        }
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(keyNode, "value");
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported object key node type: " + nodeType);
    }

    private String extractIdentifierName(Object astNode, String where) {
        String nodeType = simpleName(astNode);
        if (!"Identifier".equals(nodeType)) {
            throw new IllegalArgumentException(where + " must be Identifier, got: " + nodeType);
        }
        return asString(invokeByName(astNode, "name"), where + ".name");
    }

    private List<QinIrExpression> lowerCallArguments(Object callExpressionAst, Map<String, String> javaImportLookup) {
        List<?> arguments = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerCallArgument(argument, javaImportLookup));
        }
        return lowered;
    }

    private QinIrExpression lowerCallArgument(Object expressionAst, Map<String, String> javaImportLookup) {
        QinIrExpression expression = lowerExpression(expressionAst, javaImportLookup);
        if (expression instanceof QinIrNumberLiteral || expression instanceof QinIrStringLiteral) {
            return expression;
        }
        throw qjsError("QJS2002", "Only integer and string call arguments are supported");
    }

    private QinIrExpression lowerExpression(Object expressionAst, Map<String, String> javaImportLookup) {
        String nodeType = simpleName(expressionAst);
        if ("ObjectExpression".equals(nodeType)) {
            return lowerObjectLiteral(expressionAst);
        }
        if ("NewExpression".equals(nodeType)) {
            return lowerJavaNewExpression(expressionAst, javaImportLookup);
        }
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(expressionAst, "value");
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.intValue());
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
        }
        throw qjsError("QJS2001", "Unsupported expression type: " + nodeType);
    }

    private QinIrExpression lowerRuntimeExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String nodeType = simpleName(expressionAst);
        if ("Literal".equals(nodeType) || "ObjectExpression".equals(nodeType)) {
            return lowerExpression(expressionAst, javaImportLookup);
        }
        if ("MemberExpression".equals(nodeType)) {
            String objectName = extractIdentifierName(invokeByName(expressionAst, "object"), "MemberExpression.object");
            String propertyName = extractIdentifierName(invokeByName(expressionAst, "property"), "MemberExpression.property");
            return new QinIrMemberAccessExpression(objectName, propertyName);
        }
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(expressionAst, "Identifier");
            return new QinIrIdentifierReference(name);
        }
        if ("CallExpression".equals(nodeType)) {
            Object callee = invokeByName(expressionAst, "callee");
            if ("MemberExpression".equals(simpleName(callee))) {
                String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
                if (javaImportLookup.containsKey(receiverName)) {
                    throw qjsError("QJS2004", "Java static call must be wrapped by console.log java interop path");
                }
                if (declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                    throw qjsError("QJS2005", "Java instance call must be statement form");
                }
                return lowerBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
            }
            if ("Identifier".equals(simpleName(callee))) {
                return lowerGlobalBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
            }
        }
        throw qjsError("QJS2001", "Unsupported runtime expression type: " + nodeType);
    }

    private QinIrBuiltinCallExpression lowerBuiltinCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "Built-in call must be member call");
        }
        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(receiverName, methodName, arguments);
    }

    private QinIrBuiltinCallExpression lowerGlobalBuiltinCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        String methodName = extractIdentifierName(callee, "CallExpression.callee");
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression("Global", methodName, arguments);
    }

    private List<QinIrExpression> lowerRuntimeArguments(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> arguments = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerRuntimeExpression(argument, javaImportLookup, declarationLookup));
        }
        return lowered;
    }

    private QinIrJavaNewExpression lowerJavaNewExpression(
            Object newExpressionAst,
            Map<String, String> javaImportLookup) {
        Object callee = invokeByName(newExpressionAst, "callee");
        String classLocalName = extractIdentifierName(callee, "NewExpression.callee");
        String ownerBinaryName = javaImportLookup.get(classLocalName);
        if (ownerBinaryName == null) {
            throw qjsError("QJS2003", "Unknown java class in constructor call: " + classLocalName);
        }
        return new QinIrJavaNewExpression(
                classLocalName,
                ownerBinaryName,
                lowerCallArguments(newExpressionAst, javaImportLookup));
    }

    private static String simpleName(Object value) {
        if (value == null) {
            return "null";
        }
        return value.getClass().getSimpleName();
    }

    private static Object invokeByName(Object target, String methodName, Object... args) {
        try {
            Method method = findMethod(target.getClass(), methodName, args.length);
            return invoke(method, target, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to invoke " + target.getClass().getName() + "." + methodName, e);
        }
    }

    private static Method findMethod(Class<?> type, String methodName, int parameterCount)
            throws NoSuchMethodException {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == parameterCount) {
                return method;
            }
        }
        throw new NoSuchMethodException(type.getName() + "." + methodName + "/" + parameterCount);
    }

    private static Object invoke(Method method, Object target, Object... args) throws ReflectiveOperationException {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ReflectiveOperationException reflectiveCause) {
                throw reflectiveCause;
            }
            if (cause instanceof RuntimeException runtimeCause) {
                throw runtimeCause;
            }
            throw e;
        }
    }

    private static Object invokeStatic(Method method, Object... args) throws ReflectiveOperationException {
        return invoke(method, null, args);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object enumConstant(Class<?> enumClass, String constantName) {
        return Enum.valueOf((Class<? extends Enum>) enumClass, constantName);
    }

    private static Object selectSourceType(String source, Class<?> sourceTypeClass) {
        String trimmed = source == null ? "" : source.stripLeading();
        boolean maybeModule = trimmed.startsWith("import ")
                || trimmed.startsWith("export ")
                || trimmed.contains("\nimport ")
                || trimmed.contains("\nexport ")
                || trimmed.contains("\r\nimport ")
                || trimmed.contains("\r\nexport ");
        if (maybeModule) {
            try {
                return enumConstant(sourceTypeClass, "MODULE");
            } catch (IllegalArgumentException ignored) {
                // Fallback below.
            }
        }
        return enumConstant(sourceTypeClass, "SCRIPT");
    }

    private static List<?> asList(Object value, String where) {
        if (value instanceof List<?> list) {
            return list;
        }
        throw new IllegalArgumentException(where + " must be List, got: " + simpleName(value));
    }

    private static String asString(Object value, String where) {
        if (value instanceof String text) {
            return text;
        }
        throw new IllegalArgumentException(where + " must be String, got: " + simpleName(value));
    }

    private static String safeMessage(Throwable throwable) {
        if (throwable == null) {
            return "<none>";
        }
        String msg = throwable.getMessage();
        if (msg == null || msg.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return msg;
    }

    private String normalizeStringLiteral(String text) {
        String candidate = text == null ? "" : text;
        if (candidate.length() >= 2) {
            char first = candidate.charAt(0);
            char last = candidate.charAt(candidate.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return unescapeJsString(candidate.substring(1, candidate.length() - 1));
            }
        }
        return candidate;
    }

    private String unescapeJsString(String text) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch != '\\' || i == text.length() - 1) {
                out.append(ch);
                continue;
            }

            char esc = text.charAt(++i);
            switch (esc) {
                case '"' -> out.append('"');
                case '\'' -> out.append('\'');
                case '\\' -> out.append('\\');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'u' -> {
                    if (i + 4 >= text.length()) {
                        throw qjsError("QJS2002", "Invalid unicode escape in string literal");
                    }
                    String hex = text.substring(i + 1, i + 5);
                    try {
                        out.append((char) Integer.parseInt(hex, 16));
                    } catch (NumberFormatException ex) {
                        throw qjsError("QJS2002", "Invalid unicode escape in string literal");
                    }
                    i += 4;
                }
                default -> out.append(esc);
            }
        }
        return out.toString();
    }

    private IllegalArgumentException qjsError(String code, String message) {
        return new IllegalArgumentException(code + " " + message);
    }

    private record LoweredStatement(
            QinIrConsoleLogValue consoleValueLog,
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
    }

    private record LoweredImports(
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
    }

    private static final class AstJsonEncoder {
        private static final int MAX_DEPTH = 128;

        private final IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        private final StringBuilder out = new StringBuilder();

        private static String toJson(Object value) {
            return new AstJsonEncoder().encode(value);
        }

        private String encode(Object value) {
            writeValue(value, 0);
            return out.toString();
        }

        private void writeValue(Object value, int depth) {
            if (depth > MAX_DEPTH) {
                out.append("\"<max-depth>\"");
                return;
            }
            if (value == null) {
                out.append("null");
                return;
            }
            if (value instanceof String s) {
                writeString(s);
                return;
            }
            if (value instanceof Number || value instanceof Boolean) {
                out.append(value);
                return;
            }
            if (value instanceof Enum<?> e) {
                writeString(e.name());
                return;
            }
            if (value instanceof Class<?> c) {
                writeString(c.getName());
                return;
            }
            if (value instanceof Collection<?> collection) {
                writeCollection(collection, depth + 1);
                return;
            }
            if (value instanceof Map<?, ?> map) {
                writeMap(map, depth + 1);
                return;
            }
            if (value.getClass().isArray()) {
                int len = java.lang.reflect.Array.getLength(value);
                out.append('[');
                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        out.append(',');
                    }
                    writeValue(java.lang.reflect.Array.get(value, i), depth + 1);
                }
                out.append(']');
                return;
            }
            writeObject(value, depth + 1);
        }

        private void writeCollection(Collection<?> collection, int depth) {
            out.append('[');
            boolean first = true;
            for (Object item : collection) {
                if (!first) {
                    out.append(',');
                }
                first = false;
                writeValue(item, depth);
            }
            out.append(']');
        }

        private void writeMap(Map<?, ?> map, int depth) {
            out.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    out.append(',');
                }
                first = false;
                writeString(String.valueOf(entry.getKey()));
                out.append(':');
                writeValue(entry.getValue(), depth);
            }
            out.append('}');
        }

        private void writeObject(Object value, int depth) {
            if (seen.containsKey(value)) {
                out.append("null");
                return;
            }
            seen.put(value, Boolean.TRUE);

            out.append('{');
            Map<String, Object> fields = extractFields(value);
            boolean first = true;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                if (!first) {
                    out.append(',');
                }
                first = false;
                writeString(entry.getKey());
                out.append(':');
                writeValue(entry.getValue(), depth);
            }
            out.append('}');
        }

        private Map<String, Object> extractFields(Object value) {
            Class<?> type = value.getClass();
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            if (type.isRecord()) {
                RecordComponent[] components = type.getRecordComponents();
                if (components != null) {
                    for (RecordComponent component : components) {
                        try {
                            fields.put(component.getName(), component.getAccessor().invoke(value));
                        } catch (Exception e) {
                            fields.put(component.getName(), "<error:" + e.getClass().getSimpleName() + ">");
                        }
                    }
                    return fields;
                }
            }

            Set<String> visitedNames = new java.util.HashSet<>();
            Class<?> current = type;
            while (current != null && current != Object.class) {
                java.lang.reflect.Field[] declared = current.getDeclaredFields();
                for (java.lang.reflect.Field field : declared) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                        continue;
                    }
                    if (!visitedNames.add(field.getName())) {
                        continue;
                    }
                    try {
                        field.setAccessible(true);
                        fields.put(field.getName(), field.get(value));
                    } catch (Exception e) {
                        fields.put(field.getName(), "<error:" + e.getClass().getSimpleName() + ">");
                    }
                }
                current = current.getSuperclass();
            }
            return fields;
        }

        private void writeString(String text) {
            out.append('"');
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                switch (c) {
                    case '"' -> out.append("\\\"");
                    case '\\' -> out.append("\\\\");
                    case '\b' -> out.append("\\b");
                    case '\f' -> out.append("\\f");
                    case '\n' -> out.append("\\n");
                    case '\r' -> out.append("\\r");
                    case '\t' -> out.append("\\t");
                    default -> {
                        if (c < 0x20) {
                            out.append(String.format("\\u%04x", (int) c));
                        } else {
                            out.append(c);
                        }
                    }
                }
            }
            out.append('"');
        }
    }
}
