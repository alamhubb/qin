package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeRef;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.Statement;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.ArrayExpression;
import com.slime.ast.nodes.expressions.ArrowFunctionExpression;
import com.slime.ast.nodes.expressions.AssignmentExpression;
import com.slime.ast.nodes.expressions.BinaryExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.ClassExpression;
import com.slime.ast.nodes.expressions.ConditionalExpression;
import com.slime.ast.nodes.expressions.FunctionExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.ImportExpression;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.LogicalExpression;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.NewExpression;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.nodes.expressions.ParenthesizedExpression;
import com.slime.ast.nodes.expressions.ThisExpression;
import com.slime.ast.nodes.expressions.UnaryExpression;
import com.slime.ast.nodes.expressions.AwaitExpression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.misc.Decorator;
import com.slime.ast.nodes.misc.FunctionParameter;
import com.slime.ast.nodes.misc.MethodDefinition;
import com.slime.ast.nodes.misc.PropertyDefinition;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.ReturnStatement;
import com.slime.ast.nodes.typescript.TSKeywordType;
import com.slime.ast.nodes.typescript.TSTypeAnnotation;
import com.slime.ast.nodes.typescript.TSTypeReference;
import com.slime.parser.SlimeParser;
import com.slime.parser.SlimeJavascriptParser;
import com.slime.parser.cstToAst.SlimeCstToAstUtils;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;

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
    private static final Pattern SOURCE_IMPORT_META_URL_PATTERN = Pattern.compile("\\bimport\\s*\\.\\s*meta\\s*\\.\\s*url\\b");
    private static final Pattern SOURCE_DYNAMIC_IMPORT_PATTERN = Pattern.compile("\\bimport\\s*\\(([^\\n\\)]*)\\)");
    private static final Pattern SOURCE_TOP_LEVEL_AWAIT_PATTERN = Pattern.compile("(?m)^\\s*await\\s+([^;\\n]+)\\s*;?\\s*$");
    private static final Pattern SOURCE_ASSIGN_AWAIT_PATTERN = Pattern.compile("=\\s*await\\s+([^;\\n]+)");
    private static final Pattern SOURCE_CALL_ARG_AWAIT_PATTERN = Pattern.compile("\\(\\s*await\\s+([^;\\n]+)\\)");
    private static final Pattern SOURCE_SIMPLE_RETURN_FUNCTION_PATTERN = Pattern.compile(
            "(?m)^\\s*function\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*\\(\\s*\\)\\s*\\{\\s*return\\s+([^;{}]+?)\\s*;\\s*\\}");
    private static final Pattern SOURCE_SIMPLE_SWITCH_PATTERN = Pattern.compile(
            "(?s)switch\\s*\\(([^\\)]*)\\)\\s*\\{([^\\{\\}]*)\\}");
    private static final Pattern SOURCE_DECLARATION_CLASS_PATTERN = Pattern.compile("(?m)^\\s*(?:@\\w[\\w$]*(?:\\([^\\n]*\\))?\\s*)*class\\s+[A-Za-z_$][A-Za-z0-9_$]*\\b");
    private static final Pattern SOURCE_TYPED_CLASS_FIELD_PATTERN = Pattern.compile("(?m)^\\s*(?:public|private|protected|readonly|static|override\\s+)*[A-Za-z_$][A-Za-z0-9_$]*\\s*\\??\\s*:\\s*[A-Za-z_$][A-Za-z0-9_$.<>]*\\s*(?:=|$)");
    private static final int MAX_SIMPLE_SWITCH_REWRITES = 1;
    private static final String IMPORT_META_URL_SHIM = "__qin_import_meta_url__";
    private static final String DYNAMIC_IMPORT_SHIM = "__qin_dynamic_import__";
    private static final String TOP_LEVEL_AWAIT_SHIM = "__qin_top_level_await__";
    private static final String FUNCTION_CALL_SHIM = "__qin_call__";
    private static final String FUNCTION_MAKE_SHIM = "__qin_make_function__";
    private int functionModelBudgetRemaining = 0;
    private int currentSourceLength = 0;

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
                    List.of(),
                    List.of(),
                    List.of());
        }
        String parserInput = preprocessRuntimeSyntax(sourceForSlime);

        try {
            return parseProgramWithSlime(parserInput, List.of(), List.of());
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
                            List.of(),
                            extracted.javaImports(),
                            extracted.jsImports(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of());
                }
                String strippedParserInput = preprocessRuntimeSyntax(strippedSource);
                return parseProgramWithSlime(
                        strippedParserInput,
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
        currentSourceLength = source == null ? 0 : source.length();
        Program programAst = createProgramAst(source);
        return lowerProgramAst(programAst, preImports, preJsImports);
    }

    private String parseAstWithSlime(String source) throws ReflectiveOperationException {
        Program programAst = createProgramAst(source);
        return AstJsonEncoder.toJson(programAst, source);
    }

    private Program createProgramAst(String source) throws ReflectiveOperationException {
        if (requiresDeclarationParser(source)) {
            return createProgramAstWithDeclarationSupport(source);
        }

        SlimeJavascriptParser parser = SubhutiParser.create(SlimeJavascriptParser.class, source);

        // TS Slime's parse() defaults to module mode. Keep Java aligned and avoid
        // the no-arg Program() overload here because the ByteBuddy rule wrapper
        // re-enters Program() and triggers an infinite-loop guard.
        SubhutiCst cst = parser.Program(SlimeJavascriptParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        if (cst == null) {
            throw new IllegalArgumentException("Slime parser returned null CST");
        }

        Program programAst = SlimeCstToAstUtils.createProgramAst(cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private boolean requiresDeclarationParser(String source) {
        if (source == null || source.isBlank()) {
            return false;
        }
        return source.indexOf('@') >= 0
                || SOURCE_DECLARATION_CLASS_PATTERN.matcher(source).find()
                || SOURCE_TYPED_CLASS_FIELD_PATTERN.matcher(source).find();
    }

    private Program createProgramAstWithDeclarationSupport(String source) {
        SlimeParser parser = SubhutiParser.create(SlimeParser.class, source);
        parser.cache(true);
        SubhutiCst cst = parser.parse();
        if (cst == null) {
            cst = parser.getCurCst();
        }
        if (cst == null) {
            throw new IllegalArgumentException("Slime parser returned null CST");
        }

        Program programAst = SlimeCstToAstUtils.createProgramAst(cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private QinIrProgram lowerProgramAst(
            Program programAst,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports) {
        functionModelBudgetRemaining = computeFunctionModelBudget(currentSourceLength);
        List<AstNode> body = programAst.body();
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        List<QinIrConstDeclaration> declarations = new ArrayList<>();
        List<QinIrExpressionStatement> expressionStatements = new ArrayList<>();
        List<QinIrConsoleLogValue> consoleValueLogs = new ArrayList<>();
        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        List<QinIrJavaImport> javaImports = new ArrayList<>();
        List<QinIrJsImport> jsImports = new ArrayList<>();
        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls = new ArrayList<>();
        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = new ArrayList<>();
        List<QinIrClassDeclaration> classDeclarations = new ArrayList<>();
        List<QinIrProgram.TopLevelExecutionStep> executionSteps = new ArrayList<>();
        List<QinIrProgram.TopLevelExecutionStep> deferredGlobalBindingSteps = new ArrayList<>();
        Map<String, String> javaImportLookup = new HashMap<>();
        Map<String, QinIrExpression> declarationLookup = new HashMap<>();
        java.util.Set<String> localDeclarationNames = collectTopLevelClassNames(body);
        boolean enableGlobalBinding = currentSourceLength <= 200_000;
        predeclareTopLevelBindings(body, declarationLookup);
        if (preImports != null) {
            javaImports.addAll(preImports);
            for (QinIrJavaImport javaImport : preImports) {
                registerJavaImportLookup(javaImportLookup, javaImport);
            }
        }
        if (preJsImports != null) {
            jsImports.addAll(preJsImports);
        }

        for (AstNode statement : body) {
            String nodeType = statement.getClass().getSimpleName();
            if (statement instanceof ImportDeclaration importDeclaration) {
                LoweredImports loweredImports = lowerImportDeclaration(importDeclaration);
                javaImports.addAll(loweredImports.javaImports());
                jsImports.addAll(loweredImports.jsImports());
                for (QinIrJavaImport javaImport : loweredImports.javaImports()) {
                    registerJavaImportLookup(javaImportLookup, javaImport);
                }
                continue;
            }
            if (statement instanceof VariableDeclaration variableDeclaration) {
                List<QinIrConstDeclaration> loweredDeclarations = lowerVariableDeclaration(
                        variableDeclaration,
                        javaImportLookup,
                        declarationLookup);
                for (QinIrConstDeclaration declaration : loweredDeclarations) {
                    declarations.add(declaration);
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.DECLARATION,
                            declarations.size() - 1));
                    declarationLookup.put(declaration.name(), declaration.initializer());
                    if (enableGlobalBinding) {
                        expressionStatements.add(createGlobalBindingStatement(declaration.name()));
                        executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                                QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                                expressionStatements.size() - 1));
                    }
                }
                continue;
            }
            if (statement instanceof FunctionDeclaration functionDeclaration) {
                QinIrConstDeclaration declaration = lowerCallableDeclaration(
                        functionDeclaration,
                        javaImportLookup,
                        declarationLookup);
                declarations.add(declaration);
                executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        declarations.size() - 1));
                declarationLookup.put(declaration.name(), declaration.initializer());
                if (enableGlobalBinding) {
                    expressionStatements.add(createGlobalBindingStatement(declaration.name()));
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            expressionStatements.size() - 1));
                }
                continue;
            }
            if (statement instanceof ClassDeclaration classDeclaration) {
                QinIrClassDeclaration loweredClass = lowerClassDeclarationOrNull(
                        classDeclaration,
                        javaImportLookup,
                        localDeclarationNames);
                if (loweredClass != null) {
                    classDeclarations.add(loweredClass);
                }
                QinIrConstDeclaration declaration = lowerCallableDeclaration(
                        classDeclaration,
                        javaImportLookup,
                        declarationLookup);
                declarations.add(declaration);
                executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        declarations.size() - 1));
                declarationLookup.put(declaration.name(), declaration.initializer());
                if (enableGlobalBinding) {
                    expressionStatements.add(createGlobalBindingStatement(declaration.name()));
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            expressionStatements.size() - 1));
                }
                continue;
            }
            if (statement instanceof ExpressionStatement expressionStatement) {
                LoweredStatement lowered = lowerExpressionStatement(expressionStatement, javaImportLookup, declarationLookup);
                if (lowered.consoleValueLog() != null) {
                    consoleValueLogs.add(lowered.consoleValueLog());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.CONSOLE_VALUE,
                            consoleValueLogs.size() - 1));
                }
                if (lowered.expressionStatement() != null) {
                    expressionStatements.add(lowered.expressionStatement());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            expressionStatements.size() - 1));
                }
                if (lowered.objectLog() != null) {
                    consoleLogs.add(lowered.objectLog());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.CONSOLE_OBJECT,
                            consoleLogs.size() - 1));
                }
                if (lowered.javaStaticCall() != null) {
                    javaStaticConsoleLogs.add(lowered.javaStaticCall());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_STATIC_CONSOLE,
                            javaStaticConsoleLogs.size() - 1));
                }
                if (lowered.javaInstanceMethodCall() != null) {
                    javaInstanceMethodCalls.add(lowered.javaInstanceMethodCall());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_INSTANCE_CALL,
                            javaInstanceMethodCalls.size() - 1));
                }
                if (lowered.javaInstanceConsoleLog() != null) {
                    javaInstanceConsoleLogs.add(lowered.javaInstanceConsoleLog());
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_INSTANCE_CONSOLE,
                            javaInstanceConsoleLogs.size() - 1));
                }
                continue;
            }
            if (statement instanceof IfStatement) {
                // Temporary subset behavior: skip top-level conditional side effects we cannot lower yet.
                continue;
            }
            if (isTopLevelControlStatement(nodeType)) {
                QinIrExpressionStatement loweredControl = lowerTopLevelControlStatement(
                        statement,
                        nodeType,
                        javaImportLookup,
                        declarationLookup);
                if (loweredControl != null) {
                    expressionStatements.add(loweredControl);
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            expressionStatements.size() - 1));
                    continue;
                }
            }
            if (statement instanceof ExportNamedDeclaration) {
                lowerExportNamedDeclaration(
                        statement,
                        declarations,
                        expressionStatements,
                        executionSteps,
                        deferredGlobalBindingSteps,
                        enableGlobalBinding,
                        javaImportLookup,
                        declarationLookup);
                continue;
            }
            throw new IllegalArgumentException("Unsupported top-level statement type: " + nodeType);
        }

        if (declarations.isEmpty()
                && expressionStatements.isEmpty()
                && consoleValueLogs.isEmpty()
                && consoleLogs.isEmpty()
                && javaStaticConsoleLogs.isEmpty()
                && javaInstanceMethodCalls.isEmpty()
                && javaInstanceConsoleLogs.isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one supported statement");
        }
        return new QinIrProgram(
                declarations,
                expressionStatements,
                consoleValueLogs,
                consoleLogs,
                javaImports,
                jsImports,
                javaStaticConsoleLogs,
                javaInstanceMethodCalls,
                javaInstanceConsoleLogs,
                classDeclarations,
                executionSteps);
    }

    private QinIrClassDeclaration lowerClassDeclarationOrNull(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (classDeclaration == null || classDeclaration.id() == null) {
            return null;
        }

        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof PropertyDefinition propertyDefinition) {
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            localDeclarationNames);
                    if (loweredField != null) {
                        fields.add(loweredField);
                    }
                }
            }
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        DeclarationClassContext classContext = new DeclarationClassContext(classDeclaration.id().name(), fields);
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof MethodDefinition methodDefinition) {
                    QinIrMethodDeclaration loweredMethod = lowerMethodDeclarationOrNull(
                            methodDefinition,
                            javaImportLookup,
                            localDeclarationNames,
                            classContext);
                    if (loweredMethod != null) {
                        methods.add(loweredMethod);
                    }
                }
            }
        }

        return new QinIrClassDeclaration(
                null,
                classDeclaration.id().name(),
                QinIrTypeRef.classType("java.lang.Object"),
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                fields,
                methods);
    }

    private QinIrFieldDeclaration lowerFieldDeclarationOrNull(
            PropertyDefinition propertyDefinition,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (propertyDefinition == null || !(propertyDefinition.key() instanceof Identifier identifier)) {
            return null;
        }
        return new QinIrFieldDeclaration(
                identifier.name(),
                lowerParameterType(propertyDefinition.typeAnnotation(), javaImportLookup, localDeclarationNames),
                lowerAnnotations(propertyDefinition.decorators(), javaImportLookup),
                lowerFieldInitializer(propertyDefinition.value(), javaImportLookup));
    }

    private QinIrExpression lowerFieldInitializer(
            AstNode valueAst,
            Map<String, String> javaImportLookup) {
        if (valueAst == null) {
            return null;
        }

        QinIrExpression initializer = lowerExpression(valueAst, javaImportLookup);
        if (initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2013", "Only literal field initializers are supported in declaration subset");
    }

    private QinIrMethodDeclaration lowerMethodDeclarationOrNull(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames,
            DeclarationClassContext classContext) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            return null;
        }

        FunctionExpression function = methodDefinition.value();
        List<QinIrParameter> parameters = lowerParameters(function, javaImportLookup, localDeclarationNames);
        QinIrExpression returnExpression = lowerMethodReturnExpression(function, javaImportLookup, classContext);
        QinIrTypeRef returnType = inferDeclarationReturnType(returnExpression);

        return new QinIrMethodDeclaration(
                identifier.name(),
                returnType,
                parameters,
                lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                returnExpression);
    }

    private List<QinIrParameter> lowerParameters(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (function == null) {
            return List.of();
        }

        if (function.parameterMetadata() != null && !function.parameterMetadata().isEmpty()) {
            List<QinIrParameter> parameters = new ArrayList<>();
            for (FunctionParameter parameter : function.parameterMetadata()) {
                if (parameter == null || !(parameter.pattern() instanceof Identifier identifier)) {
                    throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
                }
                parameters.add(new QinIrParameter(
                        identifier.name(),
                        lowerParameterType(parameter.typeAnnotation(), javaImportLookup, localDeclarationNames),
                        lowerAnnotations(parameter.decorators(), javaImportLookup)));
            }
            return List.copyOf(parameters);
        }

        if (function.params() == null || function.params().isEmpty()) {
            return List.of();
        }

        List<QinIrParameter> parameters = new ArrayList<>();
        for (com.slime.ast.Pattern pattern : function.params()) {
            if (!(pattern instanceof Identifier identifier)) {
                throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
            }
            parameters.add(new QinIrParameter(
                    identifier.name(),
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of()));
        }
        return List.copyOf(parameters);
    }

    private QinIrTypeRef lowerParameterType(
            AstNode typeAnnotationAst,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (!(typeAnnotationAst instanceof TSTypeAnnotation annotation) || annotation.typeAnnotation() == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        AstNode typeAst = annotation.typeAnnotation();
        if (typeAst instanceof TSKeywordType keywordType) {
            return switch (keywordType.keyword()) {
                case "string" -> QinIrTypeRef.stringType();
                case "boolean" -> QinIrTypeRef.booleanType();
                case "number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        if (typeAst instanceof TSTypeReference typeReference && typeReference.typeName() instanceof Identifier identifier) {
            String importedBinaryName = javaImportLookup.get(identifier.name());
            if (importedBinaryName != null && !importedBinaryName.isBlank()) {
                return QinIrTypeRef.classType(importedBinaryName);
            }
            if (localDeclarationNames.contains(identifier.name())) {
                return QinIrTypeRef.classType(identifier.name());
            }
            return switch (identifier.name()) {
                case "String" -> QinIrTypeRef.stringType();
                case "Boolean" -> QinIrTypeRef.booleanType();
                case "Double", "Number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType(identifier.name());
            };
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private java.util.Set<String> collectTopLevelClassNames(List<? extends AstNode> body) {
        if (body == null || body.isEmpty()) {
            return java.util.Set.of();
        }
        java.util.Set<String> names = new java.util.LinkedHashSet<>();
        for (AstNode statement : body) {
            if (!(statement instanceof ClassDeclaration classDeclaration)
                    || classDeclaration.id() == null
                    || classDeclaration.id().name() == null
                    || classDeclaration.id().name().isBlank()) {
                continue;
            }
            names.add(classDeclaration.id().name());
        }
        return java.util.Set.copyOf(names);
    }

    private QinIrExpression lowerMethodReturnExpression(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return new QinIrNullLiteral();
        }
        List<Statement> statements = function.body().body();
        if (statements.size() != 1 || !(statements.get(0) instanceof ReturnStatement returnStatement)) {
            return new QinIrNullLiteral();
        }
        if (returnStatement.argument() == null) {
            return new QinIrNullLiteral();
        }
        return lowerDeclarationExpression(returnStatement.argument(), javaImportLookup, classContext);
    }

    private QinIrTypeRef inferDeclarationReturnType(QinIrExpression returnExpression) {
        if (returnExpression instanceof QinIrStringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (returnExpression instanceof QinIrBooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (returnExpression instanceof QinIrNumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        if (returnExpression instanceof QinIrNullLiteral) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrExpression lowerDeclarationExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerDeclarationExpression(parenthesizedExpression.expression(), javaImportLookup, classContext);
        }
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof Identifier identifier) {
            return lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return new QinIrThisExpression();
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            return lowerDeclarationMemberAccessExpression(memberExpression, javaImportLookup, classContext);
        }
        if (expressionAst instanceof CallExpression callExpression) {
            return lowerDeclarationCallExpression(callExpression, javaImportLookup, classContext);
        }

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerDeclarationExpression(invokeByName(expressionAst, "expression"), javaImportLookup, classContext);
        }
        if ("Literal".equals(nodeType) || "Identifier".equals(nodeType)) {
            return lowerExpression(expressionAst, javaImportLookup);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrThisExpression();
        }
        if ("MemberExpression".equals(nodeType)) {
            return lowerDeclarationMemberAccessExpression(expressionAst, javaImportLookup, classContext);
        }
        if ("CallExpression".equals(nodeType)) {
            return lowerDeclarationCallExpression(expressionAst, javaImportLookup, classContext);
        }
        throw qjsError("QJS2014", "Unsupported declaration return expression: " + nodeType);
    }

    private QinIrExpression lowerDeclarationMemberAccessExpression(
            Object memberExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));
        if (computed) {
            throw qjsError("QJS2015", "Computed member access is not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                invokeByName(memberExpressionAst, "object"),
                javaImportLookup,
                classContext);
        String propertyName = extractMemberPropertyName(invokeByName(memberExpressionAst, "property"));
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrExpression lowerDeclarationMemberAccessExpression(
            MemberExpression memberExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (memberExpressionAst.computed()) {
            throw qjsError("QJS2015", "Computed member access is not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpressionAst.object(),
                javaImportLookup,
                classContext);
        String propertyName = extractMemberPropertyName(memberExpressionAst.property());
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrInstanceMethodCallExpression lowerDeclarationCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2016", "Only instance method calls are supported in declaration subset");
        }
        boolean computed = Boolean.TRUE.equals(invokeByName(callee, "computed"));
        if (computed) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                invokeByName(callee, "object"),
                javaImportLookup,
                classContext);
        String methodName = extractMemberPropertyName(invokeByName(callee, "property"));
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments"),
                javaImportLookup,
                classContext);
        return new QinIrInstanceMethodCallExpression(receiver, methodName, arguments);
    }

    private QinIrInstanceMethodCallExpression lowerDeclarationCallExpression(
            CallExpression callExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (!(callExpression.callee() instanceof MemberExpression memberExpression)) {
            throw qjsError("QJS2016", "Only instance method calls are supported in declaration subset");
        }
        if (memberExpression.computed()) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpression.object(),
                javaImportLookup,
                classContext);
        String methodName = extractMemberPropertyName(memberExpression.property());
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext);
        return new QinIrInstanceMethodCallExpression(receiver, methodName, arguments);
    }

    private List<QinIrExpression> lowerDeclarationCallArguments(
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (arguments == null || arguments.isEmpty()) {
            return List.of();
        }
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerDeclarationExpression(argument, javaImportLookup, classContext));
        }
        return List.copyOf(lowered);
    }

    private QinIrExpression lowerDeclarationReceiver(
            Object receiverAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        QinIrExpression receiver = lowerDeclarationExpression(receiverAst, javaImportLookup, classContext);
        if (receiver instanceof QinIrStringLiteral
                || receiver instanceof QinIrBooleanLiteral
                || receiver instanceof QinIrNumberLiteral
                || receiver instanceof QinIrNullLiteral
                || receiver instanceof QinIrInstanceMethodCallExpression) {
            throw qjsError("QJS2018", "Unsupported declaration receiver expression");
        }
        return receiver;
    }

    private record DeclarationClassContext(
            String className,
            Map<String, QinIrFieldDeclaration> fields) {
        private DeclarationClassContext(String className, List<QinIrFieldDeclaration> fields) {
            this(
                    Objects.requireNonNull(className, "className cannot be null"),
                    indexFields(fields));
        }

        private static Map<String, QinIrFieldDeclaration> indexFields(List<QinIrFieldDeclaration> fields) {
            Map<String, QinIrFieldDeclaration> indexed = new LinkedHashMap<>();
            if (fields != null) {
                for (QinIrFieldDeclaration field : fields) {
                    indexed.put(field.name(), field);
                }
            }
            return Map.copyOf(indexed);
        }
    }

    private List<QinIrAnnotation> lowerAnnotations(
            List<Decorator> decorators,
            Map<String, String> javaImportLookup) {
        if (decorators == null || decorators.isEmpty()) {
            return List.of();
        }

        List<QinIrAnnotation> annotations = new ArrayList<>();
        for (Decorator decorator : decorators) {
            QinIrAnnotation annotation = lowerAnnotationOrNull(decorator, javaImportLookup);
            if (annotation != null) {
                annotations.add(annotation);
            }
        }
        return List.copyOf(annotations);
    }

    private QinIrAnnotation lowerAnnotationOrNull(
            Decorator decorator,
            Map<String, String> javaImportLookup) {
        if (decorator == null || decorator.expression() == null) {
            return null;
        }

        Expression expression = decorator.expression();
        if (expression instanceof Identifier identifier) {
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                return null;
            }
            return new QinIrAnnotation(binaryName, List.of());
        }

        if (expression instanceof CallExpression callExpression && callExpression.callee() instanceof Identifier identifier) {
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                return null;
            }

            List<QinIrAnnotationArgument> arguments = new ArrayList<>();
            if (!callExpression.arguments().isEmpty()) {
                if (callExpression.arguments().size() == 1) {
                    arguments.add(new QinIrAnnotationArgument(
                            "value",
                            lowerAnnotationLiteralExpression(callExpression.arguments().get(0))));
                } else {
                    List<QinIrExpression> values = new ArrayList<>();
                    for (Expression argument : callExpression.arguments()) {
                        values.add(lowerAnnotationLiteralExpression(argument));
                    }
                    arguments.add(new QinIrAnnotationArgument("value", new QinIrArrayLiteral(values)));
                }
            }
            return new QinIrAnnotation(binaryName, arguments);
        }

        return null;
    }

    private QinIrExpression lowerAnnotationLiteralExpression(Expression expression) {
        if (expression instanceof Literal literal) {
            Object value = literal.value();
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.doubleValue());
            }
        }
        throw qjsError("QJS2012", "Only literal annotation arguments are supported in declaration subset");
    }

    private boolean isTopLevelControlStatement(String nodeType) {
        return "ForStatement".equals(nodeType)
                || "WhileStatement".equals(nodeType)
                || "DoWhileStatement".equals(nodeType)
                || "SwitchStatement".equals(nodeType)
                || "BlockStatement".equals(nodeType);
    }

    private QinIrExpressionStatement lowerTopLevelControlStatement(
            Object statementAst,
            String nodeType,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object syntheticFunctionAst = createSyntheticTopLevelFunctionAst(statementAst);
        QinIrObjectLiteral runtimeDefinition = lowerFunctionRuntimeDefinition(
                syntheticFunctionAst,
                "TopLevel" + nodeType,
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition == null) {
            return null;
        }
        QinIrBuiltinCallExpression makeFunction = new QinIrBuiltinCallExpression(
                "Global",
                FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
        QinIrBuiltinCallExpression executeFunction = new QinIrBuiltinCallExpression(
                "Global",
                FUNCTION_CALL_SHIM,
                List.of(makeFunction));
        return new QinIrExpressionStatement(executeFunction);
    }

    private Object createSyntheticTopLevelFunctionAst(Object statementAst) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "BlockStatement");
        body.put("body", List.of(statementAst));

        Map<String, Object> functionAst = new LinkedHashMap<>();
        functionAst.put("type", "FunctionExpression");
        functionAst.put("id", null);
        functionAst.put("params", List.of());
        functionAst.put("body", body);
        functionAst.put("generator", false);
        functionAst.put("async", false);
        return functionAst;
    }

    private void predeclareTopLevelBindings(List<? extends AstNode> body, Map<String, QinIrExpression> declarationLookup) {
        for (AstNode statement : body) {
            String nodeType = statement.getClass().getSimpleName();
            if (statement instanceof VariableDeclaration) {
                List<?> declarators = asList(invokeByName(statement, "declarations"), "VariableDeclaration.declarations");
                for (Object declarator : declarators) {
                    String name = extractIdentifierName(invokeByName(declarator, "id"), "VariableDeclarator.id");
                    if (!name.isBlank()) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
                continue;
            }
            if (statement instanceof FunctionDeclaration || statement instanceof ClassDeclaration) {
                String name = extractIdentifierName(invokeByName(statement, "id"), nodeType + ".id");
                if (!name.isBlank()) {
                    declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                }
                continue;
            }
            if (statement instanceof ExportNamedDeclaration) {
                Object declaration = invokeByName(statement, "declaration");
                if (declaration == null) {
                    continue;
                }
                String declarationType = simpleName(declaration);
                if ("VariableDeclaration".equals(declarationType)) {
                    List<?> declarators = asList(invokeByName(declaration, "declarations"), "VariableDeclaration.declarations");
                    for (Object declarator : declarators) {
                        String name = extractIdentifierName(invokeByName(declarator, "id"), "VariableDeclarator.id");
                        if (!name.isBlank()) {
                            declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                        }
                    }
                    continue;
                }
                if ("FunctionDeclaration".equals(declarationType) || "ClassDeclaration".equals(declarationType)) {
                    String name = extractIdentifierName(invokeByName(declaration, "id"), declarationType + ".id");
                    if (!name.isBlank()) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
            }
        }
    }

    private QinIrExpressionStatement createGlobalBindingStatement(String name) {
        QinIrBuiltinCallExpression bind = new QinIrBuiltinCallExpression(
                "Global",
                "__qin_bind_global__",
                List.of(new QinIrStringLiteral(name), new QinIrIdentifierReference(name)));
        return new QinIrExpressionStatement(bind);
    }

    private static int computeFunctionModelBudget(int sourceLength) {
        if (sourceLength <= 0) {
            return 120000;
        }
        if (sourceLength <= 25_000) {
            return 240000;
        }
        if (sourceLength <= 80_000) {
            return 180000;
        }
        if (sourceLength <= 300_000) {
            return 140000;
        }
        if (sourceLength <= 500_000) {
            return 100000;
        }
        // Keep function-model semantics enabled for very large bundles as well.
        // Bytecode-size pressure is handled downstream by JSON-literal fallback in CFA backend.
        return 80000;
    }

    private void lowerExportNamedDeclaration(
            Object exportNamedDeclarationAst,
            List<QinIrConstDeclaration> declarations,
            List<QinIrExpressionStatement> expressionStatements,
            List<QinIrProgram.TopLevelExecutionStep> executionSteps,
            List<QinIrProgram.TopLevelExecutionStep> deferredGlobalBindingSteps,
            boolean enableGlobalBinding,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object declaration = invokeByName(exportNamedDeclarationAst, "declaration");
        if (declaration == null) {
            // Re-export forms without local declaration are handled by module linker/runtime.
            return;
        }

        String declarationType = simpleName(declaration);
        if ("VariableDeclaration".equals(declarationType)) {
            List<QinIrConstDeclaration> loweredDeclarations = lowerVariableDeclaration(
                    declaration,
                    javaImportLookup,
                    declarationLookup);
            for (QinIrConstDeclaration lowered : loweredDeclarations) {
                declarations.add(lowered);
                executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        declarations.size() - 1));
                declarationLookup.put(lowered.name(), lowered.initializer());
                if (enableGlobalBinding) {
                    expressionStatements.add(createGlobalBindingStatement(lowered.name()));
                    executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            expressionStatements.size() - 1));
                }
            }
            return;
        }

        if ("FunctionDeclaration".equals(declarationType) || "ClassDeclaration".equals(declarationType)) {
            QinIrConstDeclaration lowered = lowerCallableDeclaration(
                    declaration,
                    declarationType,
                    javaImportLookup,
                    declarationLookup);
            declarations.add(lowered);
            executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.DECLARATION,
                    declarations.size() - 1));
            declarationLookup.put(lowered.name(), lowered.initializer());
            if (enableGlobalBinding) {
                expressionStatements.add(createGlobalBindingStatement(lowered.name()));
                executionSteps.add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                        expressionStatements.size() - 1));
            }
            return;
        }

        throw new IllegalArgumentException("Unsupported export declaration type: " + declarationType);
    }

    private LoweredImports lowerImportDeclaration(Object importDeclarationAst) {
        Object sourceNode = invokeByName(importDeclarationAst, "source");
        String sourceValue = asString(invokeByName(sourceNode, "value"), "ImportDeclaration.source.value");
        List<?> specifiers = asList(invokeByName(importDeclarationAst, "specifiers"), "ImportDeclaration.specifiers");

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

    private LoweredImports lowerImportDeclaration(ImportDeclaration importDeclarationAst) {
        List<QinIrJavaImport> javaImports = QinJavaImportSupport.lowerJavaImportDeclaration(importDeclarationAst);
        if (!javaImports.isEmpty()) {
            return new LoweredImports(javaImports, List.of());
        }
        return lowerImportDeclaration((Object) importDeclarationAst);
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

    private List<QinIrConstDeclaration> lowerVariableDeclaration(
            Object variableDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String kind = asString(invokeByName(variableDeclarationAst, "kind"), "VariableDeclaration.kind");
        if (!"const".equals(kind) && !"let".equals(kind) && !"var".equals(kind)) {
            throw qjsError("QJS2002", "Only const/let/var declaration is supported, but got: " + kind);
        }

        List<?> declarators = asList(invokeByName(variableDeclarationAst, "declarations"),
                "VariableDeclaration.declarations");
        if (declarators.isEmpty()) {
            throw qjsError("QJS2002", "Variable declaration must contain at least one declarator");
        }

        List<QinIrConstDeclaration> lowered = new ArrayList<>();
        for (Object declarator : declarators) {
            Object id = invokeByName(declarator, "id");
            Object init = invokeByName(declarator, "init");
            String name = extractIdentifierName(id, "VariableDeclarator.id");
            QinIrExpression initializer = init == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationInitializer(init, javaImportLookup, declarationLookup);
            lowered.add(new QinIrConstDeclaration(name, initializer));
        }
        return lowered;
    }

    private List<QinIrConstDeclaration> lowerVariableDeclaration(
            VariableDeclaration variableDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerVariableDeclaration((Object) variableDeclarationAst, javaImportLookup, declarationLookup);
    }

    private QinIrConstDeclaration lowerCallableDeclaration(
            Object declarationAst,
            String nodeType,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object id = invokeByName(declarationAst, "id");
        if (id == null) {
            throw qjsError("QJS2010", "Anonymous " + nodeType + " is not supported in Qin subset");
        }
        String name = extractIdentifierName(id, nodeType + ".id");
        if ("FunctionDeclaration".equals(nodeType)) {
            return new QinIrConstDeclaration(
                    name,
                    lowerFunctionDeclarationOrNull(declarationAst, javaImportLookup, declarationLookup));
        }
        return new QinIrConstDeclaration(name, new QinIrNullLiteral());
    }

    private QinIrConstDeclaration lowerCallableDeclaration(
            FunctionDeclaration declarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Identifier id = declarationAst.id();
        if (id == null) {
            throw qjsError("QJS2010", "Anonymous FunctionDeclaration is not supported in Qin subset");
        }
        return new QinIrConstDeclaration(
                id.name(),
                lowerFunctionDeclarationOrNull(declarationAst, javaImportLookup, declarationLookup));
    }

    private QinIrConstDeclaration lowerCallableDeclaration(
            ClassDeclaration declarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerCallableDeclaration((Object) declarationAst, "ClassDeclaration", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerDeclarationInitializer(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression initializer = lowerRuntimeExpression(expressionAst, javaImportLookup, declarationLookup);
        if (initializer instanceof QinIrObjectLiteral
                || initializer instanceof QinIrJavaNewExpression
                || initializer instanceof QinIrIdentifierReference
                || initializer instanceof QinIrMemberAccessExpression
                || initializer instanceof QinIrBuiltinCallExpression
                || initializer instanceof QinIrFunctionLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrArrayLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2002", "Unsupported const initializer expression");
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

    private QinIrObjectLiteral lowerObjectLiteral(ObjectExpression objectExpressionAst) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                throw qjsError(
                        "QJS2002",
                        "Only normal object property is supported, got: "
                                + propertyNode.getClass().getSimpleName());
            }
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerObjectPropertyValue(property.value());
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrArrayLiteral lowerArrayLiteral(
            Object arrayExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean runtimeMode) {
        List<?> elements = asList(invokeByName(arrayExpressionAst, "elements"), "ArrayExpression.elements");
        List<QinIrExpression> irElements = new ArrayList<>();
        for (Object element : elements) {
            if (element == null) {
                irElements.add(new QinIrNullLiteral());
                continue;
            }
            QinIrExpression lowered = runtimeMode
                    ? lowerRuntimeExpression(element, javaImportLookup, declarationLookup)
                    : lowerExpression(element, javaImportLookup);
            irElements.add(lowered);
        }
        return new QinIrArrayLiteral(irElements);
    }

    private QinIrArrayLiteral lowerArrayLiteral(
            ArrayExpression arrayExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean runtimeMode) {
        List<QinIrExpression> irElements = new ArrayList<>();
        for (com.slime.ast.Expression element : arrayExpressionAst.elements()) {
            if (element == null) {
                irElements.add(new QinIrNullLiteral());
                continue;
            }
            QinIrExpression lowered = runtimeMode
                    ? lowerRuntimeExpression(element, javaImportLookup, declarationLookup)
                    : lowerExpression(element, javaImportLookup);
            irElements.add(lowered);
        }
        return new QinIrArrayLiteral(irElements);
    }

    private QinIrExpression lowerFunctionDeclarationOrNull(
            Object functionDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(
                functionDeclarationAst,
                "FunctionDeclaration",
                javaImportLookup,
                declarationLookup);
    }

    private QinIrExpression lowerFunctionDeclarationOrNull(
            FunctionDeclaration functionDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(functionDeclarationAst, "FunctionDeclaration", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            com.slime.ast.Expression functionExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (functionExpressionAst instanceof FunctionExpression) {
            return lowerFunctionLikeOrNull(
                    (FunctionExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        if (functionExpressionAst instanceof ArrowFunctionExpression) {
            return lowerFunctionLikeOrNull(
                    (ArrowFunctionExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        if (functionExpressionAst instanceof ClassExpression) {
            return lowerFunctionLikeOrNull(
                    (ClassExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        throw qjsError(
                "QJS2001",
                "Unsupported function-like expression type: " + functionExpressionAst.getClass().getSimpleName());
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(functionAst, "FunctionExpression", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            ArrowFunctionExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerFunctionRuntimeDefinition(
                functionAst,
                "ArrowFunctionExpression",
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression("Global", FUNCTION_MAKE_SHIM, List.of(runtimeDefinition));
        }
        if (functionAst.body() instanceof BlockStatement blockStatement) {
            return lowerFunctionLiteralFromBlock(blockStatement, true);
        }
        if (functionAst.body() instanceof com.slime.ast.Expression expressionBody) {
            QinIrExpression returnExpression = lowerFunctionReturnExpression(expressionBody, true);
            if (returnExpression != null) {
                return new QinIrFunctionLiteral(returnExpression);
            }
        }
        return new QinIrFunctionLiteral(new QinIrNullLiteral());
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            ClassExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(functionAst, "ClassExpression", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionDeclaration functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression("Global", FUNCTION_MAKE_SHIM, List.of(runtimeDefinition));
        }
        return lowerFunctionLiteralFromBlock(functionAst.body(), true);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionExpression functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression("Global", FUNCTION_MAKE_SHIM, List.of(runtimeDefinition));
        }
        return lowerFunctionLiteralFromBlock(functionAst.body(), true);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression("Global", FUNCTION_MAKE_SHIM, List.of(runtimeDefinition));
        }

        // Backward-compatible fallback for extremely unsupported shapes.
        Object body = invokeByName(functionAst, "body");
        if (!"BlockStatement".equals(simpleName(body))) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        List<?> statements = asList(invokeByName(body, "body"), debugNodeName + ".body.body");
        if (statements.size() != 1) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }

        Object onlyStatement = statements.get(0);
        if (!"ReturnStatement".equals(simpleName(onlyStatement))) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        Object argument = invokeByName(onlyStatement, "argument");
        if (argument == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        QinIrExpression returnExpression = lowerFunctionReturnExpression(argument, true);
        if (returnExpression == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        return new QinIrFunctionLiteral(returnExpression);
    }

    private QinIrExpression lowerFunctionLiteralFromBlock(BlockStatement body, boolean permissive) {
        List<com.slime.ast.Statement> statements = body.body();
        if (statements.size() != 1) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        com.slime.ast.Statement onlyStatement = statements.get(0);
        if (!(onlyStatement instanceof ReturnStatement returnStatement)) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        if (returnStatement.argument() == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        QinIrExpression returnExpression = lowerFunctionReturnExpression(returnStatement.argument(), permissive);
        if (returnExpression == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        return new QinIrFunctionLiteral(returnExpression);
    }

    private QinIrExpression lowerFunctionReturnExpression(Object expressionAst) {
        return lowerFunctionReturnExpression(expressionAst, false);
    }

    private QinIrExpression lowerFunctionReturnExpression(Object expressionAst, boolean permissive) {
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof FunctionExpression functionExpression) {
            return lowerFunctionLikeOrNull(functionExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ArrowFunctionExpression arrowFunctionExpression) {
            return lowerFunctionLikeOrNull(arrowFunctionExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ClassExpression classExpression) {
            return lowerFunctionLikeOrNull(classExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerFunctionReturnObjectLiteral(objectExpression, permissive);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, Map.of(), Map.of(), false);
        }

        String nodeType = simpleName(expressionAst);
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(expressionAst, "value");
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.doubleValue());
            }
            if (value instanceof String text) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(text);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
            return null;
        }
        if ("FunctionExpression".equals(nodeType)
                || "ArrowFunctionExpression".equals(nodeType)
                || "ClassExpression".equals(nodeType)) {
            return lowerFunctionLikeOrNull(expressionAst, nodeType, Map.of(), Map.of());
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerFunctionReturnObjectLiteral(expressionAst, permissive);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, Map.of(), Map.of(), false);
        }
        if (permissive) {
            return new QinIrNullLiteral();
        }
        return null;
    }

    private QinIrObjectLiteral lowerFunctionReturnObjectLiteral(Object objectExpressionAst, boolean permissive) {
        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                if (permissive) {
                    return new QinIrObjectLiteral(List.of());
                }
                return null;
            }
            String key = extractPropertyKey(invokeByName(property, "key"));
            QinIrExpression value = lowerFunctionReturnExpression(invokeByName(property, "value"), permissive);
            if (value == null) {
                if (permissive) {
                    value = new QinIrNullLiteral();
                } else {
                    return null;
                }
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerFunctionReturnObjectLiteral(
            ObjectExpression objectExpressionAst,
            boolean permissive) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                if (permissive) {
                    return new QinIrObjectLiteral(List.of());
                }
                return null;
            }
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerFunctionReturnExpression(property.value(), permissive);
            if (value == null) {
                if (permissive) {
                    value = new QinIrNullLiteral();
                } else {
                    return null;
                }
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerFunctionRuntimeDefinition(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (functionModelBudgetRemaining <= 0) {
            return null;
        }
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        int[] encodedNodeCount = new int[] {0};
        boolean[] overflow = new boolean[] {false};
        int perFunctionNodeLimit = computeFunctionAstNodeLimit(currentSourceLength);
        QinIrExpression astExpression = encodeFunctionAstNode(
                functionAst,
                0,
                seen,
                encodedNodeCount,
                perFunctionNodeLimit,
                overflow);
        if (overflow[0]) {
            return null;
        }
        if (!(astExpression instanceof QinIrObjectLiteral astObject)) {
            return null;
        }
        if (encodedNodeCount[0] > functionModelBudgetRemaining) {
            return null;
        }
        functionModelBudgetRemaining -= encodedNodeCount[0];

        QinIrObjectLiteral closureObject = buildFunctionClosureObject(declarationLookup);
        List<QinIrObjectProperty> properties = new ArrayList<>();
        properties.add(new QinIrObjectProperty("__qin_function_model", new QinIrStringLiteral("slime-ast-v1")));
        properties.add(new QinIrObjectProperty("debugNode", new QinIrStringLiteral(debugNodeName)));
        properties.add(new QinIrObjectProperty("ast", astObject));
        properties.add(new QinIrObjectProperty("closure", closureObject));
        properties.add(new QinIrObjectProperty(
                "javaImportCount",
                new QinIrNumberLiteral(javaImportLookup == null ? 0 : javaImportLookup.size())));
        return new QinIrObjectLiteral(properties);
    }

    private static int computeFunctionAstNodeLimit(int sourceLength) {
        if (sourceLength <= 0) {
            return 14000;
        }
        if (sourceLength <= 25_000) {
            return 18000;
        }
        if (sourceLength <= 80_000) {
            return 14000;
        }
        if (sourceLength <= 300_000) {
            return 10000;
        }
        return 8000;
    }

    private QinIrObjectLiteral buildFunctionClosureObject(Map<String, QinIrExpression> declarationLookup) {
        if (declarationLookup == null || declarationLookup.isEmpty()) {
            return new QinIrObjectLiteral(List.of());
        }
        List<String> names = new ArrayList<>(declarationLookup.keySet());
        names.sort(String::compareTo);
        List<QinIrObjectProperty> properties = new ArrayList<>();
        for (String name : names) {
            if (name == null || name.isBlank() || !IDENTIFIER_PATTERN.matcher(name).matches()) {
                continue;
            }
            // Capture lexical/global references lazily by symbol name to avoid early null snapshots.
            QinIrObjectLiteral refDescriptor = new QinIrObjectLiteral(List.of(
                    new QinIrObjectProperty("__qin_ref_name", new QinIrStringLiteral(name))));
            properties.add(new QinIrObjectProperty(name, refDescriptor));
        }
        return new QinIrObjectLiteral(properties);
    }

    private QinIrExpression encodeFunctionAstNode(
            Object node,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int[] encodedNodeCount,
            int maxNodes,
            boolean[] overflow) {
        if (node == null) {
            return new QinIrNullLiteral();
        }
        if (depth > 180 || encodedNodeCount[0] > maxNodes) {
            overflow[0] = true;
            return new QinIrNullLiteral();
        }
        encodedNodeCount[0] += 1;
        if (node instanceof String text) {
            return new QinIrStringLiteral(text);
        }
        if (node instanceof Boolean boolValue) {
            return new QinIrBooleanLiteral(boolValue);
        }
        if (node instanceof Number number) {
            return new QinIrNumberLiteral(number.doubleValue());
        }
        if (node instanceof Character character) {
            return new QinIrStringLiteral(String.valueOf(character));
        }
        if (node.getClass().isEnum()) {
            return new QinIrStringLiteral(String.valueOf(node));
        }
        if (node instanceof Collection<?> collection) {
            List<QinIrExpression> elements = new ArrayList<>();
            for (Object element : collection) {
                elements.add(encodeFunctionAstNode(element, depth + 1, seen, encodedNodeCount, maxNodes, overflow));
            }
            return new QinIrArrayLiteral(elements);
        }
        if (node.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(node);
            List<QinIrExpression> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Object element = java.lang.reflect.Array.get(node, i);
                elements.add(encodeFunctionAstNode(element, depth + 1, seen, encodedNodeCount, maxNodes, overflow));
            }
            return new QinIrArrayLiteral(elements);
        }
        if (node instanceof Map<?, ?> map) {
            List<QinIrObjectProperty> properties = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                properties.add(new QinIrObjectProperty(
                        key,
                        encodeFunctionAstNode(entry.getValue(), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
            }
            return new QinIrObjectLiteral(properties);
        }
        if (seen.containsKey(node)) {
            return new QinIrNullLiteral();
        }
        seen.put(node, Boolean.TRUE);
        try {
            QinIrObjectLiteral knownAstNode = encodeKnownAstNode(node, depth, seen, encodedNodeCount, maxNodes, overflow);
            if (knownAstNode != null) {
                return knownAstNode;
            }
            if (node.getClass().isRecord()) {
                List<QinIrObjectProperty> properties = new ArrayList<>();
                properties.add(new QinIrObjectProperty("type", new QinIrStringLiteral(node.getClass().getSimpleName())));
                for (RecordComponent component : node.getClass().getRecordComponents()) {
                    if ("location".equals(component.getName())) {
                        continue;
                    }
                    Object value = invokeRecordComponent(node, component);
                    properties.add(new QinIrObjectProperty(
                            component.getName(),
                            encodeFunctionAstNode(value, depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                }
                return new QinIrObjectLiteral(properties);
            }
            List<QinIrObjectProperty> beanProperties = new ArrayList<>();
            beanProperties.add(new QinIrObjectProperty("type", new QinIrStringLiteral(node.getClass().getSimpleName())));
            Method[] methods = node.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterCount() != 0) {
                    continue;
                }
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (method.getDeclaringClass() == Object.class) {
                    continue;
                }
                String propertyName = toBeanPropertyName(method.getName());
                if (propertyName == null || propertyName.isBlank()) {
                    continue;
                }
                if ("class".equals(propertyName) || "location".equals(propertyName)) {
                    continue;
                }
                Object value;
                try {
                    value = method.invoke(node);
                } catch (ReflectiveOperationException ignored) {
                    continue;
                }
                beanProperties.add(new QinIrObjectProperty(
                        propertyName,
                        encodeFunctionAstNode(value, depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
            }
            if (beanProperties.size() > 1) {
                return new QinIrObjectLiteral(beanProperties);
            }
            return new QinIrStringLiteral(String.valueOf(node));
        } finally {
            seen.remove(node);
        }
    }

    private QinIrObjectLiteral encodeKnownAstNode(
            Object node,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int[] encodedNodeCount,
            int maxNodes,
            boolean[] overflow) {
        if (node instanceof ArrowFunctionExpression arrowFunctionExpression) {
            List<QinIrObjectProperty> props = new ArrayList<>();
            props.add(new QinIrObjectProperty("type", new QinIrStringLiteral("ArrowFunctionExpression")));
            props.add(new QinIrObjectProperty("id", new QinIrNullLiteral()));
            props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                    arrowFunctionExpression.params(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                    arrowFunctionExpression.body(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                    arrowFunctionExpression.async(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                    arrowFunctionExpression.expression(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            return new QinIrObjectLiteral(props);
        }
        String type = simpleName(node);
        if (type == null || type.isBlank()) {
            return null;
        }
        List<QinIrObjectProperty> props = new ArrayList<>();
        props.add(new QinIrObjectProperty("type", new QinIrStringLiteral(type)));
        switch (type) {
            case "Identifier" -> {
                props.add(new QinIrObjectProperty("name", encodeFunctionAstNode(
                        invokeByName(node, "name"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "Literal" -> {
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("raw", encodeFunctionAstNode(
                        invokeByName(node, "raw"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("regex", encodeFunctionAstNode(
                        invokeByName(node, "regex"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("bigint", encodeFunctionAstNode(
                        invokeByName(node, "bigint"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ThisExpression", "BreakStatement", "ContinueStatement", "EmptyStatement" -> {
                return new QinIrObjectLiteral(props);
            }
            case "ParenthesizedExpression" -> {
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ArrayExpression" -> {
                props.add(new QinIrObjectProperty("elements", encodeFunctionAstNode(
                        invokeByName(node, "elements"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ObjectExpression" -> {
                props.add(new QinIrObjectProperty("properties", encodeFunctionAstNode(
                        invokeByName(node, "properties"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "Property" -> {
                props.add(new QinIrObjectProperty("key", encodeFunctionAstNode(
                        invokeByName(node, "key"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "MemberExpression" -> {
                props.add(new QinIrObjectProperty("object", encodeFunctionAstNode(
                        invokeByName(node, "object"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("property", encodeFunctionAstNode(
                        invokeByName(node, "property"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("optional", encodeFunctionAstNode(
                        invokeByName(node, "optional"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "CallExpression", "NewExpression" -> {
                props.add(new QinIrObjectProperty("callee", encodeFunctionAstNode(
                        invokeByName(node, "callee"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("arguments", encodeFunctionAstNode(
                        invokeByName(node, "arguments"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                if ("CallExpression".equals(type)) {
                    props.add(new QinIrObjectProperty("optional", encodeFunctionAstNode(
                            invokeByName(node, "optional"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                }
                return new QinIrObjectLiteral(props);
            }
            case "UnaryExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("prefix", encodeFunctionAstNode(
                        invokeByName(node, "prefix"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("left", encodeFunctionAstNode(
                        invokeByName(node, "left"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("right", encodeFunctionAstNode(
                        invokeByName(node, "right"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "UpdateExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("prefix", encodeFunctionAstNode(
                        invokeByName(node, "prefix"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ConditionalExpression" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("alternate", encodeFunctionAstNode(
                        invokeByName(node, "alternate"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ExpressionStatement" -> {
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ReturnStatement" -> {
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "VariableDeclaration" -> {
                props.add(new QinIrObjectProperty("kind", encodeFunctionAstNode(
                        invokeByName(node, "kind"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("declarations", encodeFunctionAstNode(
                        invokeByName(node, "declarations"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "VariableDeclarator" -> {
                props.add(new QinIrObjectProperty("id", encodeFunctionAstNode(
                        invokeByName(node, "id"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("init", encodeFunctionAstNode(
                        invokeByName(node, "init"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "BlockStatement" -> {
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "IfStatement" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("alternate", encodeFunctionAstNode(
                        invokeByName(node, "alternate"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ForStatement" -> {
                props.add(new QinIrObjectProperty("init", encodeFunctionAstNode(
                        invokeByName(node, "init"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("update", encodeFunctionAstNode(
                        invokeByName(node, "update"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SwitchStatement" -> {
                props.add(new QinIrObjectProperty("discriminant", encodeFunctionAstNode(
                        invokeByName(node, "discriminant"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("cases", encodeFunctionAstNode(
                        invokeByName(node, "cases"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SwitchCase" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "FunctionExpression", "FunctionDeclaration" -> {
                props.add(new QinIrObjectProperty("id", encodeFunctionAstNode(
                        invokeByName(node, "id"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                        invokeByName(node, "params"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("generator", encodeFunctionAstNode(
                        invokeByName(node, "generator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                        invokeByName(node, "async"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ArrowFunctionExpression" -> {
                props.add(new QinIrObjectProperty("id", new QinIrNullLiteral()));
                props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                        invokeByName(node, "params"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                        invokeByName(node, "async"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SequenceExpression" -> {
                props.add(new QinIrObjectProperty("expressions", encodeFunctionAstNode(
                        invokeByName(node, "expressions"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            default -> {
                return null;
            }
        }
    }

    private static String toBeanPropertyName(String methodName) {
        if (methodName == null || methodName.isBlank()) {
            return null;
        }
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        }
        if (methodName.startsWith("is") && methodName.length() > 2) {
            return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }
        return null;
    }

    private Object invokeRecordComponent(Object node, RecordComponent component) {
        try {
            return component.getAccessor().invoke(node);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private QinIrExpression lowerObjectPropertyValue(Object expressionAst) {
        QinIrExpression value = lowerExpression(expressionAst, Map.of());
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrArrayLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression) {
            return value;
        }
        throw qjsError("QJS2002", "Unsupported object property value expression");
    }

    private QinIrExpression lowerObjectPropertyValue(com.slime.ast.Expression expressionAst) {
        QinIrExpression value = lowerExpression(expressionAst, Map.of());
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrArrayLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression) {
            return value;
        }
        throw qjsError("QJS2002", "Unsupported object property value expression");
    }

    private LoweredStatement lowerExpressionStatement(
            Object expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        String expressionNodeType = simpleName(expression);
        if ("AwaitExpression".equals(expressionNodeType)) {
            return lowerAwaitExpressionStatement(expression, javaImportLookup, declarationLookup);
        }
        if ("ImportExpression".equals(expressionNodeType)) {
            return lowerImportExpressionStatement(expression, javaImportLookup, declarationLookup);
        }
        if ("Identifier".equals(expressionNodeType)) {
            String name = asString(invokeByName(expression, "name"), "ExpressionStatement.expression.name");
            if ("import".equals(name) || "await".equals(name)) {
                return new LoweredStatement(null, null, null, null, null, null);
            }
        }
        if (!"CallExpression".equals(expressionNodeType)) {
            QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
            return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
        }

        Object callee = invokeByName(expression, "callee");
        if (isRuntimeShimCall(callee)) {
            QinIrBuiltinCallExpression shim = lowerGlobalBuiltinCallExpression(
                    expression,
                    javaImportLookup,
                    declarationLookup);
            return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
        }
        if (isConsoleLogCallee(callee)) {
            return lowerConsoleLogCall(expression, javaImportLookup, declarationLookup);
        }
        if (isDynamicImportCallee(callee)) {
            return lowerDynamicImportCalleeStatement(expression, javaImportLookup, declarationLookup);
        }
        if (isJavaInstanceMethodCallee(callee, declarationLookup)) {
            return lowerJavaInstanceMethodStatement(expression, declarationLookup, javaImportLookup);
        }

        QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
        return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
    }

    private LoweredStatement lowerExpressionStatement(
            ExpressionStatement expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        com.slime.ast.Expression expression = expressionStatementAst.expression();
        if (expression instanceof AwaitExpression awaitExpression) {
            return lowerAwaitExpressionStatement(awaitExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof ImportExpression importExpression) {
            return lowerImportExpressionStatement(importExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof Identifier identifier) {
            String name = identifier.name();
            if ("import".equals(name) || "await".equals(name)) {
                return new LoweredStatement(null, null, null, null, null, null);
            }
        }
        if (expression instanceof CallExpression callExpression) {
            com.slime.ast.Expression callee = callExpression.callee();
            if (isRuntimeShimCall(callee)) {
                QinIrBuiltinCallExpression shim = lowerGlobalBuiltinCallExpression(
                        callExpression,
                        javaImportLookup,
                        declarationLookup);
                return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
            }
            if (isConsoleLogCallee(callee)) {
                return lowerConsoleLogCall(callExpression, javaImportLookup, declarationLookup);
            }
            if (isDynamicImportCallee(callee)) {
                return lowerDynamicImportCalleeStatement(callExpression, javaImportLookup, declarationLookup);
            }
            if (isJavaInstanceMethodCallee(callee, declarationLookup)) {
                return lowerJavaInstanceMethodStatement(callExpression, declarationLookup, javaImportLookup);
            }
        }

        QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
        return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
    }

    private LoweredStatement lowerAwaitExpressionStatement(
            Object awaitExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object argumentAst = invokeByName(awaitExpressionAst, "argument");
        QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                TOP_LEVEL_AWAIT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerAwaitExpressionStatement(
            AwaitExpression awaitExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument =
                lowerRuntimeExpression(awaitExpressionAst.argument(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                TOP_LEVEL_AWAIT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerImportExpressionStatement(
            Object importExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object sourceAst = invokeByName(importExpressionAst, "source");
        QinIrExpression argument = lowerRuntimeExpression(sourceAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                DYNAMIC_IMPORT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerImportExpressionStatement(
            ImportExpression importExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument =
                lowerRuntimeExpression(importExpressionAst.source(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                DYNAMIC_IMPORT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerDynamicImportCalleeStatement(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                DYNAMIC_IMPORT_SHIM,
                arguments);
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerDynamicImportCalleeStatement(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                DYNAMIC_IMPORT_SHIM,
                arguments);
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
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
        return new LoweredStatement(new QinIrConsoleLogValue(value), null, null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogCall(
            CallExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!isConsoleLogCallee(expressionAst.callee())) {
            throw qjsError("QJS2001", "Only console.log(...) call is supported");
        }

        List<com.slime.ast.Expression> arguments = expressionAst.arguments();
        if (arguments.size() != 1) {
            throw qjsError("QJS2002", "console.log(...) must have exactly one argument");
        }

        com.slime.ast.Expression firstArgument = arguments.get(0);
        if (firstArgument instanceof CallExpression nestedCallExpression
                && nestedCallExpression.callee() instanceof MemberExpression nestedMemberCallee
                && nestedMemberCallee.object() instanceof Identifier objectIdentifier) {
            String receiverName = objectIdentifier.name();
            if (javaImportLookup.containsKey(receiverName)
                    || declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                return lowerConsoleLogJavaCall(nestedCallExpression, javaImportLookup, declarationLookup);
            }
        }

        QinIrExpression value = lowerRuntimeExpression(firstArgument, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(value), null, null, null, null, null);
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
                    null,
                    new QinIrConsoleLogJavaInstanceCall(
                            receiverName,
                            javaNewExpression.ownerBinaryName(),
                            methodName,
                            arguments));
        }

        QinIrBuiltinCallExpression builtin =
                lowerBuiltinCallExpression(callExpressionAst, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(builtin), null, null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogJavaCall(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "console.log call argument must be member call like Math.random()");
        }

        String receiverName = objectIdentifier.name();
        String methodName = propertyIdentifier.name();

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
            return new LoweredStatement(
                    null,
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
        return new LoweredStatement(new QinIrConsoleLogValue(builtin), null, null, null, null, null);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            Object callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (isNoOpRuntimeShimCall(callExpressionAst)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        Object callee = invokeByName(callExpressionAst, "callee");
        if (isDynamicImportCallee(callee)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
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
                null,
                new QinIrJavaInstanceMethodCall(
                        receiverName,
                        javaNewExpression.ownerBinaryName(),
                        methodName,
                        arguments),
                null);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            CallExpression callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (isNoOpRuntimeShimCall(callExpressionAst)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        com.slime.ast.Expression callee = callExpressionAst.callee();
        if (isDynamicImportCallee(callee)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        if (!(callee instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "Only member call expression statement is supported");
        }

        String receiverName = objectIdentifier.name();
        String methodName = propertyIdentifier.name();
        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (!(declaration instanceof QinIrJavaNewExpression javaNewExpression)) {
            throw qjsError("QJS2003", "Only Java instance method call statement is supported: " + receiverName);
        }

        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
        return new LoweredStatement(
                null,
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
        Object objectAst = invokeByName(calleeAst, "object");
        Object propertyAst = invokeByName(calleeAst, "property");
        if (!"Identifier".equals(simpleName(objectAst)) || !"Identifier".equals(simpleName(propertyAst))) {
            return false;
        }
        String objectName = extractIdentifierName(objectAst, "callee.object");
        String propertyName = extractIdentifierName(propertyAst, "callee.property");
        return "console".equals(objectName) && "log".equals(propertyName);
    }

    private boolean isDynamicImportCallee(Object calleeAst) {
        return "Import".equals(simpleName(calleeAst));
    }

    private boolean isDynamicImportCallee(com.slime.ast.Expression calleeAst) {
        return calleeAst instanceof ImportExpression;
    }

    private boolean isNoOpRuntimeShimCall(Object callExpressionAst) {
        if (!"CallExpression".equals(simpleName(callExpressionAst))) {
            return false;
        }
        Object callee = invokeByName(callExpressionAst, "callee");
        return isRuntimeShimCall(callee);
    }

    private boolean isNoOpRuntimeShimCall(CallExpression callExpressionAst) {
        return isRuntimeShimCall(callExpressionAst.callee());
    }

    private boolean isRuntimeShimCall(Object calleeAst) {
        if (!"Identifier".equals(simpleName(calleeAst))) {
            return false;
        }
        String name = asString(invokeByName(calleeAst, "name"), "CallExpression.callee.name");
        return DYNAMIC_IMPORT_SHIM.equals(name) || TOP_LEVEL_AWAIT_SHIM.equals(name);
    }

    private boolean isRuntimeShimCall(com.slime.ast.Expression calleeAst) {
        if (!(calleeAst instanceof Identifier identifier)) {
            return false;
        }
        String name = identifier.name();
        return DYNAMIC_IMPORT_SHIM.equals(name) || TOP_LEVEL_AWAIT_SHIM.equals(name);
    }

    private boolean isJavaInstanceMethodCallee(Object calleeAst, Map<String, QinIrExpression> declarationLookup) {
        if (!"MemberExpression".equals(simpleName(calleeAst))) {
            return false;
        }
        Object objectAst = invokeByName(calleeAst, "object");
        if (!"Identifier".equals(simpleName(objectAst))) {
            return false;
        }
        String receiverName = extractIdentifierName(objectAst, "CallExpression.callee.object");
        return declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression;
    }

    private boolean isJavaInstanceMethodCallee(
            com.slime.ast.Expression calleeAst,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(calleeAst instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)) {
            return false;
        }
        return declarationLookup.get(objectIdentifier.name()) instanceof QinIrJavaNewExpression;
    }

    private boolean isImportMetaUrlExpression(Object expressionAst) {
        if (!"MemberExpression".equals(simpleName(expressionAst))) {
            return false;
        }
        Object object = invokeByName(expressionAst, "object");
        Object property = invokeByName(expressionAst, "property");
        if (!isImportMeta(object)) {
            return false;
        }
        if (!"Identifier".equals(simpleName(property))) {
            return false;
        }
        return "url".equals(asString(invokeByName(property, "name"), "MemberExpression.property.name"));
    }

    private boolean isImportMeta(Object astNode) {
        if (!"MetaProperty".equals(simpleName(astNode))) {
            return false;
        }
        Object meta = invokeByName(astNode, "meta");
        Object property = invokeByName(astNode, "property");
        if (!"Identifier".equals(simpleName(meta)) || !"Identifier".equals(simpleName(property))) {
            return false;
        }
        String metaName = asString(invokeByName(meta, "name"), "MetaProperty.meta.name");
        String propertyName = asString(invokeByName(property, "name"), "MetaProperty.property.name");
        return "import".equals(metaName) && "meta".equals(propertyName);
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

    private String extractPropertyKey(AstNode keyNode) {
        if (keyNode instanceof Identifier identifier) {
            return identifier.name();
        }
        if (keyNode instanceof Literal literal) {
            Object value = literal.value();
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException(
                "Unsupported object key node type: "
                        + (keyNode == null ? "null" : keyNode.getClass().getSimpleName()));
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

    private List<QinIrExpression> lowerCallArguments(
            List<? extends com.slime.ast.Expression> arguments,
            Map<String, String> javaImportLookup) {
        List<QinIrExpression> lowered = new ArrayList<>();
        for (com.slime.ast.Expression argument : arguments) {
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
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerExpression(parenthesizedExpression.expression(), javaImportLookup);
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerObjectLiteral(objectExpression);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, javaImportLookup, Map.of(), false);
        }
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof Identifier identifier) {
            return lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return globalThisExpression();
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            return lowerMemberAccessExpression(memberExpression);
        }
        if (expressionAst instanceof NewExpression newExpression) {
            return lowerJavaNewExpression(newExpression, javaImportLookup);
        }

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerExpression(invokeByName(expressionAst, "expression"), javaImportLookup);
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerObjectLiteral(expressionAst);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, javaImportLookup, Map.of(), false);
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
                return new QinIrNumberLiteral(number.doubleValue());
            }
            if (value instanceof String text) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(text);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
        }
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(expressionAst, "Identifier");
            if (isRegexLiteralIdentifier(name)) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(name);
            }
            return new QinIrIdentifierReference(name);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral("globalThis")));
        }
        if ("MemberExpression".equals(nodeType)) {
            return lowerMemberAccessExpression(expressionAst);
        }
        throw qjsError("QJS2001", "Unsupported expression type: " + nodeType);
    }

    private QinIrExpression lowerLiteralExpression(Literal literal) {
        Object value = literal.value();
        if (value == null) {
            return new QinIrNullLiteral();
        }
        if (value instanceof Number number) {
            return new QinIrNumberLiteral(number.doubleValue());
        }
        if (value instanceof String text) {
            ParsedRegexLiteral regexLiteral = parseRegexLiteral(text);
            if (regexLiteral != null) {
                return createRegexLiteralExpression(regexLiteral);
            }
            return new QinIrStringLiteral(normalizeStringLiteral(text));
        }
        if (value instanceof Boolean boolValue) {
            return new QinIrBooleanLiteral(boolValue);
        }
        throw qjsError("QJS2001", "Unsupported literal value type: " + value.getClass().getSimpleName());
    }

    private QinIrExpression lowerIdentifierExpression(Identifier identifier) {
        String name = identifier.name();
        if (isRegexLiteralIdentifier(name)) {
            ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
            if (regexLiteral != null) {
                return createRegexLiteralExpression(regexLiteral);
            }
            return new QinIrStringLiteral(name);
        }
        return new QinIrIdentifierReference(name);
    }

    private QinIrExpression globalThisExpression() {
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_global__",
                List.of(new QinIrStringLiteral("globalThis")));
    }

    private QinIrMemberAccessExpression lowerMemberAccessExpression(Object memberExpressionAst) {
        String objectName = extractIdentifierName(invokeByName(memberExpressionAst, "object"), "MemberExpression.object");
        String propertyName = extractMemberPropertyName(invokeByName(memberExpressionAst, "property"));
        return new QinIrMemberAccessExpression(objectName, propertyName);
    }

    private QinIrMemberAccessExpression lowerMemberAccessExpression(MemberExpression memberExpressionAst) {
        if (!(memberExpressionAst.object() instanceof Identifier objectIdentifier)) {
            throw qjsError("QJS2001", "Only identifier-based member access is supported");
        }
        return new QinIrMemberAccessExpression(
                objectIdentifier.name(),
                extractMemberPropertyName(memberExpressionAst.property()));
    }

    private QinIrExpression lowerRuntimeMemberAccessExpression(
            Object memberExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object objectAst = invokeByName(memberExpressionAst, "object");
        Object propertyAst = invokeByName(memberExpressionAst, "property");
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));

        if ("Identifier".equals(simpleName(objectAst))) {
            String objectName = extractIdentifierName(objectAst, "MemberExpression.object");
            if (!computed || "Literal".equals(simpleName(propertyAst))) {
                String propertyName = extractMemberPropertyName(propertyAst);
                return new QinIrMemberAccessExpression(objectName, propertyName);
            }
        }

        QinIrExpression targetExpression = lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
        QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                memberExpressionAst,
                propertyAst,
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_member_get__",
                List.of(targetExpression, propertyExpression));
    }

    private QinIrExpression lowerRuntimeMemberAccessExpression(
            MemberExpression memberExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (memberExpressionAst.object() instanceof Identifier objectIdentifier) {
            if (!memberExpressionAst.computed() || memberExpressionAst.property() instanceof Literal) {
                return new QinIrMemberAccessExpression(
                        objectIdentifier.name(),
                        extractMemberPropertyName(memberExpressionAst.property()));
            }
        }

        QinIrExpression targetExpression = lowerRuntimeExpression(
                memberExpressionAst.object(),
                javaImportLookup,
                declarationLookup);
        QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                memberExpressionAst,
                memberExpressionAst.property(),
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_member_get__",
                List.of(targetExpression, propertyExpression));
    }

    private QinIrExpression lowerRuntimeMemberPropertyExpression(
            Object memberExpressionAst,
            Object propertyAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));
        if ("Identifier".equals(simpleName(propertyAst)) && !computed) {
            return new QinIrStringLiteral(extractIdentifierName(propertyAst, "MemberExpression.property"));
        }
        return lowerRuntimeExpression(propertyAst, javaImportLookup, declarationLookup);
    }

    private String extractMemberPropertyName(Object propertyNode) {
        String nodeType = simpleName(propertyNode);
        if ("Identifier".equals(nodeType)) {
            return extractIdentifierName(propertyNode, "MemberExpression.property");
        }
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(propertyNode, "value");
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException("MemberExpression.property must be Identifier or Literal, got: " + nodeType);
    }

    private String extractMemberPropertyName(com.slime.ast.Expression propertyNode) {
        if (propertyNode instanceof Identifier identifier) {
            return identifier.name();
        }
        if (propertyNode instanceof Literal literal) {
            Object value = literal.value();
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException(
                "MemberExpression.property must be Identifier or Literal, got: "
                        + propertyNode.getClass().getSimpleName());
    }

    private QinIrExpression lowerRuntimeExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerRuntimeExpression(parenthesizedExpression.expression(), javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerRuntimeObjectLiteral(objectExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, javaImportLookup, declarationLookup, true);
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            if (isImportMetaUrlExpression(memberExpression)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            return lowerRuntimeMemberAccessExpression(memberExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof Identifier identifier) {
            String name = identifier.name();
            if (IMPORT_META_URL_SHIM.equals(name)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            return lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return globalThisExpression();
        }
        if (expressionAst instanceof CallExpression callExpression) {
            return lowerRuntimeCallExpression(callExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ImportExpression importExpression) {
            QinIrExpression source =
                    lowerRuntimeExpression(importExpression.source(), javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression("Global", DYNAMIC_IMPORT_SHIM, List.of(source));
        }
        if (expressionAst instanceof AwaitExpression awaitExpression) {
            QinIrExpression argument =
                    lowerRuntimeExpression(awaitExpression.argument(), javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression("Global", TOP_LEVEL_AWAIT_SHIM, List.of(argument));
        }
        if (expressionAst instanceof BinaryExpression binaryExpression) {
            return lowerRuntimeBinaryExpression(binaryExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof LogicalExpression logicalExpression) {
            return lowerRuntimeLogicalExpression(logicalExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof UnaryExpression unaryExpression) {
            return lowerRuntimeUnaryExpression(unaryExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ConditionalExpression conditionalExpression) {
            return lowerRuntimeConditionalExpression(conditionalExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof AssignmentExpression assignmentExpression) {
            return lowerRuntimeAssignmentExpression(assignmentExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof NewExpression newExpression) {
            return lowerRuntimeNewExpression(newExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof FunctionExpression functionExpression) {
            return lowerFunctionLikeOrNull(functionExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ArrowFunctionExpression arrowFunctionExpression) {
            return lowerFunctionLikeOrNull(arrowFunctionExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ClassExpression classExpression) {
            return lowerFunctionLikeOrNull(classExpression, javaImportLookup, declarationLookup);
        }

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerRuntimeExpression(invokeByName(expressionAst, "expression"), javaImportLookup, declarationLookup);
        }
        if ("Literal".equals(nodeType)) {
            return lowerExpression(expressionAst, javaImportLookup);
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerRuntimeObjectLiteral(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, javaImportLookup, declarationLookup, true);
        }
        if ("MemberExpression".equals(nodeType)) {
            if (isImportMetaUrlExpression(expressionAst)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            return lowerRuntimeMemberAccessExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("MetaProperty".equals(nodeType) && isImportMeta(expressionAst)) {
            return new QinIrStringLiteral("import.meta");
        }
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(expressionAst, "Identifier");
            if (IMPORT_META_URL_SHIM.equals(name)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            if (isRegexLiteralIdentifier(name)) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(name);
            }
            return new QinIrIdentifierReference(name);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral("globalThis")));
        }
        if ("ImportExpression".equals(nodeType)) {
            Object sourceAst = invokeByName(expressionAst, "source");
            QinIrExpression source = lowerRuntimeExpression(sourceAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression("Global", DYNAMIC_IMPORT_SHIM, List.of(source));
        }
        if ("AwaitExpression".equals(nodeType)) {
            Object argumentAst = invokeByName(expressionAst, "argument");
            QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression("Global", TOP_LEVEL_AWAIT_SHIM, List.of(argument));
        }
        if ("BinaryExpression".equals(nodeType)) {
            return lowerRuntimeBinaryExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("LogicalExpression".equals(nodeType)) {
            return lowerRuntimeLogicalExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("UnaryExpression".equals(nodeType)) {
            return lowerRuntimeUnaryExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("ConditionalExpression".equals(nodeType)) {
            return lowerRuntimeConditionalExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("AssignmentExpression".equals(nodeType)) {
            return lowerRuntimeAssignmentExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("NewExpression".equals(nodeType)) {
            return lowerRuntimeNewExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("FunctionExpression".equals(nodeType)
                || "ClassExpression".equals(nodeType)
                || "ArrowFunctionExpression".equals(nodeType)) {
            return lowerFunctionLikeOrNull(expressionAst, nodeType, javaImportLookup, declarationLookup);
        }
        if ("CallExpression".equals(nodeType)) {
            if (isNoOpRuntimeShimCall(expressionAst)) {
                return lowerGlobalBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
            }
            Object callee = invokeByName(expressionAst, "callee");
            if (isDynamicImportCallee(callee)) {
                List<QinIrExpression> args = lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup);
                return new QinIrBuiltinCallExpression("Global", DYNAMIC_IMPORT_SHIM, args);
            }
            if ("MemberExpression".equals(simpleName(callee))) {
                Object objectAst = invokeByName(callee, "object");
                Object propertyAst = invokeByName(callee, "property");
                if ("Identifier".equals(simpleName(objectAst))) {
                    String receiverName = extractIdentifierName(objectAst, "CallExpression.callee.object");
                    if (javaImportLookup.containsKey(receiverName)) {
                        throw qjsError("QJS2004", "Java static call must be wrapped by console.log java interop path");
                    }
                    if (declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                        throw qjsError("QJS2005", "Java instance call must be statement form");
                    }
                    if (declarationLookup.containsKey(receiverName) && "Identifier".equals(simpleName(propertyAst))) {
                        String methodName = extractIdentifierName(
                                propertyAst,
                                "CallExpression.callee.property");
                        List<QinIrExpression> arguments = new ArrayList<>();
                        arguments.add(new QinIrIdentifierReference(receiverName));
                        arguments.add(new QinIrStringLiteral(methodName));
                        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                        return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
                    }
                    if (!declarationLookup.containsKey(receiverName) && "Identifier".equals(simpleName(propertyAst))) {
                        String methodName = extractIdentifierName(propertyAst, "CallExpression.callee.property");
                        List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                                expressionAst,
                                javaImportLookup,
                                declarationLookup);
                        if (QinBuiltinRegistry.resolve(receiverName, methodName, runtimeArguments.size()).isPresent()) {
                            return new QinIrBuiltinCallExpression(receiverName, methodName, runtimeArguments);
                        }
                        List<QinIrExpression> arguments = new ArrayList<>();
                        arguments.add(new QinIrBuiltinCallExpression(
                                "Global",
                                "__qin_global__",
                                List.of(new QinIrStringLiteral(receiverName))));
                        arguments.add(new QinIrStringLiteral(methodName));
                        arguments.addAll(runtimeArguments);
                        return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
                    }
                }
                QinIrExpression targetExpression =
                        lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
                QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                        callee,
                        propertyAst,
                        javaImportLookup,
                        declarationLookup);
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(targetExpression);
                arguments.add(propertyExpression);
                arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
            }
            if ("Identifier".equals(simpleName(callee))) {
                String calleeName = extractIdentifierName(callee, "CallExpression.callee");
                List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                        expressionAst,
                        javaImportLookup,
                        declarationLookup);
                if (declarationLookup.containsKey(calleeName)) {
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrIdentifierReference(calleeName));
                    arguments.addAll(runtimeArguments);
                    return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
                }
                if (QinBuiltinRegistry.resolve("Global", calleeName, runtimeArguments.size()).isPresent()) {
                    return new QinIrBuiltinCallExpression("Global", calleeName, runtimeArguments);
                }
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName))));
                arguments.addAll(runtimeArguments);
                return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
            }
            QinIrExpression calleeExpression = lowerRuntimeExpression(callee, javaImportLookup, declarationLookup);
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(calleeExpression);
            arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
            return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
        }
        throw qjsError("QJS2001", "Unsupported runtime expression type: " + nodeType);
    }

    private QinIrExpression lowerRuntimeCallExpression(
            CallExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        com.slime.ast.Expression callee = expressionAst.callee();
        if (isNoOpRuntimeShimCall(expressionAst)) {
            return lowerGlobalBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if (isDynamicImportCallee(callee)) {
            List<QinIrExpression> args = lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression("Global", DYNAMIC_IMPORT_SHIM, args);
        }
        if (callee instanceof MemberExpression memberCallee) {
            com.slime.ast.Expression objectAst = memberCallee.object();
            com.slime.ast.Expression propertyAst = memberCallee.property();
            if (objectAst instanceof Identifier objectIdentifier) {
                String receiverName = objectIdentifier.name();
                if (javaImportLookup.containsKey(receiverName)) {
                    throw qjsError("QJS2004", "Java static call must be wrapped by console.log java interop path");
                }
                if (declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                    throw qjsError("QJS2005", "Java instance call must be statement form");
                }
                if (declarationLookup.containsKey(receiverName) && propertyAst instanceof Identifier propertyIdentifier) {
                    String methodName = propertyIdentifier.name();
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrIdentifierReference(receiverName));
                    arguments.add(new QinIrStringLiteral(methodName));
                    arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                    return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
                }
                if (!declarationLookup.containsKey(receiverName) && propertyAst instanceof Identifier propertyIdentifier) {
                    String methodName = propertyIdentifier.name();
                    List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                            expressionAst,
                            javaImportLookup,
                            declarationLookup);
                    if (QinBuiltinRegistry.resolve(receiverName, methodName, runtimeArguments.size()).isPresent()) {
                        return new QinIrBuiltinCallExpression(receiverName, methodName, runtimeArguments);
                    }
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrBuiltinCallExpression(
                            "Global",
                            "__qin_global__",
                            List.of(new QinIrStringLiteral(receiverName))));
                    arguments.add(new QinIrStringLiteral(methodName));
                    arguments.addAll(runtimeArguments);
                    return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
                }
            }
            QinIrExpression targetExpression =
                    lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    memberCallee,
                    propertyAst,
                    javaImportLookup,
                    declarationLookup);
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(targetExpression);
            arguments.add(propertyExpression);
            arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
            return new QinIrBuiltinCallExpression("Global", "__qin_call_method__", arguments);
        }
        if (callee instanceof Identifier identifierCallee) {
            String calleeName = identifierCallee.name();
            List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                    expressionAst,
                    javaImportLookup,
                    declarationLookup);
            if (declarationLookup.containsKey(calleeName)) {
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(new QinIrIdentifierReference(calleeName));
                arguments.addAll(runtimeArguments);
                return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
            }
            if (QinBuiltinRegistry.resolve("Global", calleeName, runtimeArguments.size()).isPresent()) {
                return new QinIrBuiltinCallExpression("Global", calleeName, runtimeArguments);
            }
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral(calleeName))));
            arguments.addAll(runtimeArguments);
            return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
        }
        QinIrExpression calleeExpression = lowerRuntimeExpression(callee, javaImportLookup, declarationLookup);
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(calleeExpression);
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression("Global", FUNCTION_CALL_SHIM, arguments);
    }

    private QinIrObjectLiteral lowerRuntimeObjectLiteral(
            Object objectExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                throw qjsError("QJS2002", "Only normal object property is supported, got: " + simpleName(property));
            }
            String key = extractPropertyKey(invokeByName(property, "key"));
            QinIrExpression value = lowerRuntimeExpression(invokeByName(property, "value"), javaImportLookup, declarationLookup);
            if (!(value instanceof QinIrNumberLiteral
                    || value instanceof QinIrStringLiteral
                    || value instanceof QinIrBooleanLiteral
                    || value instanceof QinIrNullLiteral
                    || value instanceof QinIrFunctionLiteral
                    || value instanceof QinIrIdentifierReference
                    || value instanceof QinIrMemberAccessExpression
                    || value instanceof QinIrBuiltinCallExpression
                    || value instanceof QinIrObjectLiteral
                    || value instanceof QinIrArrayLiteral)) {
                throw qjsError("QJS2002", "Unsupported runtime object property value expression");
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerRuntimeObjectLiteral(
            ObjectExpression objectExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                throw qjsError(
                        "QJS2002",
                        "Only normal object property is supported, got: "
                                + propertyNode.getClass().getSimpleName());
            }
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerRuntimeExpression(property.value(), javaImportLookup, declarationLookup);
            if (!(value instanceof QinIrNumberLiteral
                    || value instanceof QinIrStringLiteral
                    || value instanceof QinIrBooleanLiteral
                    || value instanceof QinIrNullLiteral
                    || value instanceof QinIrFunctionLiteral
                    || value instanceof QinIrIdentifierReference
                    || value instanceof QinIrMemberAccessExpression
                    || value instanceof QinIrBuiltinCallExpression
                    || value instanceof QinIrObjectLiteral
                    || value instanceof QinIrArrayLiteral)) {
                throw qjsError("QJS2002", "Unsupported runtime object property value expression");
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrExpression lowerRuntimeAssignmentExpression(
            Object assignmentExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(assignmentExpressionAst, "operator"), "AssignmentExpression.operator");
        if (!"=".equals(operator)) {
            throw qjsError("QJS2001", "Only simple assignment operator is supported: " + operator);
        }

        Object leftAst = invokeByName(assignmentExpressionAst, "left");
        Object rightAst = invokeByName(assignmentExpressionAst, "right");
        QinIrExpression valueExpression = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);

        if ("MemberExpression".equals(simpleName(leftAst))) {
            Object targetAst = invokeByName(leftAst, "object");
            Object propertyAst = invokeByName(leftAst, "property");
            QinIrExpression targetExpression = lowerRuntimeExpression(targetAst, javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    leftAst,
                    propertyAst,
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_set__",
                    List.of(targetExpression, propertyExpression, valueExpression));
        }

        throw qjsError("QJS2001", "Only member assignment is supported in expression statement");
    }

    private QinIrExpression lowerRuntimeAssignmentExpression(
            AssignmentExpression assignmentExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = assignmentExpressionAst.operator();
        if (!"=".equals(operator)) {
            throw qjsError("QJS2001", "Only simple assignment operator is supported: " + operator);
        }

        Object leftAst = assignmentExpressionAst.left();
        QinIrExpression valueExpression =
                lowerRuntimeExpression(assignmentExpressionAst.right(), javaImportLookup, declarationLookup);

        if (leftAst instanceof MemberExpression memberExpression) {
            QinIrExpression targetExpression =
                    lowerRuntimeExpression(memberExpression.object(), javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    memberExpression,
                    memberExpression.property(),
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_set__",
                    List.of(targetExpression, propertyExpression, valueExpression));
        }

        throw qjsError("QJS2001", "Only member assignment is supported in expression statement");
    }

    private QinIrBuiltinCallExpression lowerRuntimeBinaryExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "BinaryExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerRuntimeExpression(leftAst, javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeBinaryExpression(
            BinaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression left = lowerRuntimeExpression(expressionAst.left(), javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(expressionAst.right(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeLogicalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "LogicalExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerRuntimeExpression(leftAst, javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeLogicalExpression(
            LogicalExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression left = lowerRuntimeExpression(expressionAst.left(), javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(expressionAst.right(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeUnaryExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "UnaryExpression.operator");
        Object argumentAst = invokeByName(expressionAst, "argument");
        QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_unary__",
                List.of(new QinIrStringLiteral(operator), argument));
    }

    private QinIrBuiltinCallExpression lowerRuntimeUnaryExpression(
            UnaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument =
                lowerRuntimeExpression(expressionAst.argument(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_unary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), argument));
    }

    private QinIrBuiltinCallExpression lowerRuntimeConditionalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object testAst = invokeByName(expressionAst, "test");
        Object consequentAst = invokeByName(expressionAst, "consequent");
        Object alternateAst = invokeByName(expressionAst, "alternate");
        QinIrExpression test = lowerRuntimeExpression(testAst, javaImportLookup, declarationLookup);
        QinIrExpression consequent = lowerRuntimeExpression(consequentAst, javaImportLookup, declarationLookup);
        QinIrExpression alternate = lowerRuntimeExpression(alternateAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrBuiltinCallExpression lowerRuntimeConditionalExpression(
            ConditionalExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression test = lowerRuntimeExpression(expressionAst.test(), javaImportLookup, declarationLookup);
        QinIrExpression consequent =
                lowerRuntimeExpression(expressionAst.consequent(), javaImportLookup, declarationLookup);
        QinIrExpression alternate =
                lowerRuntimeExpression(expressionAst.alternate(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrBuiltinCallExpression lowerRuntimeNewExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object calleeAst = invokeByName(expressionAst, "callee");
        QinIrExpression callee;
        if ("Identifier".equals(simpleName(calleeAst))) {
            String calleeName = extractIdentifierName(calleeAst, "NewExpression.callee");
            if (declarationLookup.containsKey(calleeName)) {
                callee = new QinIrIdentifierReference(calleeName);
            } else if (isKnownGlobalConstructor(calleeName)) {
                callee = new QinIrStringLiteral(calleeName);
            } else {
                callee = new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName)));
            }
        } else {
            callee = lowerRuntimeExpression(calleeAst, javaImportLookup, declarationLookup);
        }
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(callee);
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression("Global", "__qin_new__", arguments);
    }

    private QinIrBuiltinCallExpression lowerRuntimeNewExpression(
            NewExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression callee;
        if (expressionAst.callee() instanceof Identifier calleeIdentifier) {
            String calleeName = calleeIdentifier.name();
            if (declarationLookup.containsKey(calleeName)) {
                callee = new QinIrIdentifierReference(calleeName);
            } else if (isKnownGlobalConstructor(calleeName)) {
                callee = new QinIrStringLiteral(calleeName);
            } else {
                callee = new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName)));
            }
        } else {
            callee = lowerRuntimeExpression(expressionAst.callee(), javaImportLookup, declarationLookup);
        }
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(callee);
        arguments.addAll(lowerRuntimeArguments(expressionAst.arguments(), javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression("Global", "__qin_new__", arguments);
    }

    private static boolean isKnownGlobalConstructor(String name) {
        return switch (name) {
            case "Date",
                    "Array",
                    "Object",
                    "Map",
                    "WeakMap",
                    "Set",
                    "WeakSet",
                    "Uint8Array",
                    "String",
                    "Boolean",
                    "Number",
                    "RegExp",
                    "Error",
                    "TypeError",
                    "RangeError" -> true;
            default -> false;
        };
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

    private QinIrBuiltinCallExpression lowerBuiltinCallExpression(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "Built-in call must be member call");
        }
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                objectIdentifier.name(),
                propertyIdentifier.name(),
                arguments);
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

    private QinIrBuiltinCallExpression lowerGlobalBuiltinCallExpression(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof Identifier identifier)) {
            throw qjsError("QJS2001", "Global built-in call must use identifier callee");
        }
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression("Global", identifier.name(), arguments);
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

    private List<QinIrExpression> lowerRuntimeArguments(
            List<? extends com.slime.ast.Expression> arguments,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> lowered = new ArrayList<>();
        for (com.slime.ast.Expression argument : arguments) {
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

    private QinIrJavaNewExpression lowerJavaNewExpression(
            NewExpression newExpressionAst,
            Map<String, String> javaImportLookup) {
        if (!(newExpressionAst.callee() instanceof Identifier calleeIdentifier)) {
            throw qjsError("QJS2001", "Java constructor callee must be identifier");
        }
        String classLocalName = calleeIdentifier.name();
        String ownerBinaryName = javaImportLookup.get(classLocalName);
        if (ownerBinaryName == null) {
            throw qjsError("QJS2003", "Unknown java class in constructor call: " + classLocalName);
        }
        return new QinIrJavaNewExpression(
                classLocalName,
                ownerBinaryName,
                lowerCallArguments(newExpressionAst.arguments(), javaImportLookup));
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

    private String preprocessRuntimeSyntax(String source) {
        String rewritten = source == null ? "" : source;
        rewritten = SOURCE_IMPORT_META_URL_PATTERN.matcher(rewritten).replaceAll(IMPORT_META_URL_SHIM);
        rewritten = SOURCE_DYNAMIC_IMPORT_PATTERN.matcher(rewritten).replaceAll(DYNAMIC_IMPORT_SHIM + "($1)");
        rewritten = SOURCE_ASSIGN_AWAIT_PATTERN.matcher(rewritten).replaceAll("= " + TOP_LEVEL_AWAIT_SHIM + "($1)");
        rewritten = SOURCE_CALL_ARG_AWAIT_PATTERN.matcher(rewritten).replaceAll("(" + TOP_LEVEL_AWAIT_SHIM + "($1))");
        rewritten = SOURCE_TOP_LEVEL_AWAIT_PATTERN.matcher(rewritten).replaceAll(TOP_LEVEL_AWAIT_SHIM + "($1);");
        rewritten = rewriteSimpleSwitchStatements(rewritten);
        rewritten = rewriteSimpleReturnFunctions(rewritten);
        return rewritten;
    }

    private String rewriteSimpleSwitchStatements(String source) {
        if (source == null || source.length() > 10_000) {
            return source;
        }
        Matcher matcher = SOURCE_SIMPLE_SWITCH_PATTERN.matcher(source);
        StringBuffer out = new StringBuffer();
        int switchId = 0;
        int rewrittenCount = 0;
        while (matcher.find()) {
            String discriminant = matcher.group(1);
            String body = matcher.group(2);
            String lowered = lowerSimpleSwitch(discriminant, body, switchId++);
            if (lowered == null || rewrittenCount >= MAX_SIMPLE_SWITCH_REWRITES) {
                matcher.appendReplacement(out, Matcher.quoteReplacement(matcher.group()));
            } else {
                rewrittenCount++;
                matcher.appendReplacement(out, Matcher.quoteReplacement(lowered));
            }
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private String lowerSimpleSwitch(String discriminant, String body, int switchId) {
        if (discriminant == null || body == null) {
            return null;
        }
        String discriminantExpr = discriminant.trim();
        if (discriminantExpr.isEmpty()) {
            return null;
        }
        String tempName = "__qin_switch_" + switchId;
        List<String> caseExpressions = new ArrayList<>();
        List<String> caseReturns = new ArrayList<>();
        int position = 0;
        int caseCount = 0;
        String defaultReturn = null;
        while (position < body.length()) {
            position = skipWhitespace(body, position);
            if (position >= body.length()) {
                break;
            }
            if (startsWithWord(body, position, "case")) {
                int expressionStart = position + "case".length();
                int colon = body.indexOf(':', expressionStart);
                if (colon < 0) {
                    return null;
                }
                String caseExpression = body.substring(expressionStart, colon).trim();
                int next = findNextCaseOrDefault(body, colon + 1);
                String caseBlock = next < 0 ? body.substring(colon + 1) : body.substring(colon + 1, next);
                String returnExpression = extractReturnExpression(caseBlock);
                if (returnExpression == null) {
                    return null;
                }
                caseExpressions.add(caseExpression);
                caseReturns.add(returnExpression);
                caseCount++;
                position = next < 0 ? body.length() : next;
                continue;
            }
            if (startsWithWord(body, position, "default")) {
                int colon = body.indexOf(':', position + "default".length());
                if (colon < 0) {
                    return null;
                }
                int next = findNextCaseOrDefault(body, colon + 1);
                String defaultBlock = next < 0 ? body.substring(colon + 1) : body.substring(colon + 1, next);
                defaultReturn = extractReturnExpression(defaultBlock);
                position = next < 0 ? body.length() : next;
                continue;
            }
            return null;
        }
        if (caseCount == 0) {
            return null;
        }
        String fallback = defaultReturn == null ? "null" : defaultReturn;
        String reduced = fallback;
        for (int i = caseExpressions.size() - 1; i >= 0; i--) {
            reduced = "(" + tempName + " === " + caseExpressions.get(i) + " ? "
                    + caseReturns.get(i) + " : " + reduced + ")";
        }
        return "var " + tempName + " = " + discriminantExpr + "; return " + reduced + ";";
    }

    private int skipWhitespace(String text, int from) {
        int index = Math.max(0, from);
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private boolean startsWithWord(String text, int from, String word) {
        if (from < 0 || from + word.length() > text.length()) {
            return false;
        }
        if (!text.regionMatches(from, word, 0, word.length())) {
            return false;
        }
        int before = from - 1;
        int after = from + word.length();
        boolean beforeOk = before < 0 || !Character.isLetterOrDigit(text.charAt(before)) && text.charAt(before) != '_';
        boolean afterOk = after >= text.length()
                || !Character.isLetterOrDigit(text.charAt(after)) && text.charAt(after) != '_';
        return beforeOk && afterOk;
    }

    private int findNextCaseOrDefault(String body, int from) {
        int best = -1;
        for (int i = Math.max(0, from); i < body.length(); i++) {
            if (startsWithWord(body, i, "case") || startsWithWord(body, i, "default")) {
                best = i;
                break;
            }
        }
        return best;
    }

    private String extractReturnExpression(String block) {
        if (block == null) {
            return null;
        }
        String text = block.trim();
        if (text.isEmpty() || text.startsWith("break")) {
            return null;
        }
        int returnIndex = text.indexOf("return");
        if (returnIndex < 0) {
            return null;
        }
        String afterReturn = text.substring(returnIndex + "return".length()).trim();
        int semicolon = afterReturn.indexOf(';');
        String expression = semicolon >= 0 ? afterReturn.substring(0, semicolon).trim() : afterReturn.trim();
        if (expression.isEmpty()) {
            return null;
        }
        return expression;
    }

    private String rewriteSimpleReturnFunctions(String source) {
        Matcher matcher = SOURCE_SIMPLE_RETURN_FUNCTION_PATTERN.matcher(source);
        StringBuffer out = new StringBuffer();
        while (matcher.find()) {
            String functionName = matcher.group(1);
            String returnExpression = matcher.group(2).trim();
            String replacement = "const " + functionName + " = " + FUNCTION_MAKE_SHIM + "(" + returnExpression + ");";
            matcher.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(out);
        return out.toString();
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

    private boolean isRegexLiteralIdentifier(String name) {
        if (name == null || name.length() < 2 || name.charAt(0) != '/') {
            return false;
        }
        int lastSlash = name.lastIndexOf('/');
        return lastSlash > 0;
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

    private ParsedRegexLiteral parseRegexLiteral(String text) {
        if (text == null) {
            return null;
        }
        String candidate = text.strip();
        if (candidate.length() >= 2) {
            char first = candidate.charAt(0);
            char last = candidate.charAt(candidate.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                candidate = unescapeJsString(candidate.substring(1, candidate.length() - 1));
            }
        }
        if (candidate.length() < 2 || candidate.charAt(0) != '/') {
            return null;
        }
        int endSlash = -1;
        boolean escaping = false;
        for (int i = 1; i < candidate.length(); i++) {
            char ch = candidate.charAt(i);
            if (escaping) {
                escaping = false;
                continue;
            }
            if (ch == '\\') {
                escaping = true;
                continue;
            }
            if (ch == '/') {
                endSlash = i;
            }
        }
        if (endSlash <= 0) {
            return null;
        }
        String pattern = candidate.substring(1, endSlash);
        String flags = candidate.substring(endSlash + 1);
        if (!flags.chars().allMatch(ch -> Character.isLetter(ch))) {
            return null;
        }
        return new ParsedRegexLiteral(pattern, flags);
    }

    private QinIrExpression createRegexLiteralExpression(ParsedRegexLiteral regexLiteral) {
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_new__",
                List.of(
                        new QinIrStringLiteral("RegExp"),
                        new QinIrStringLiteral(regexLiteral.pattern()),
                        new QinIrStringLiteral(regexLiteral.flags())));
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
            QinIrExpressionStatement expressionStatement,
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
    }

    private record LoweredImports(
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
    }

    private record ParsedRegexLiteral(
            String pattern,
            String flags) {
    }

    private static final class AstJsonEncoder {
        private static final int MAX_DEPTH = 128;
        private static final Pattern REGEX_LITERAL_PATTERN = Pattern.compile("^/(.*)/([a-z]*)$");

        private final IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        private final StringBuilder out = new StringBuilder();
        private final String sourceText;

        private AstJsonEncoder(String sourceText) {
            this.sourceText = sourceText == null ? "" : sourceText;
        }

        private static String toJson(Object value, String sourceText) {
            return new AstJsonEncoder(sourceText).encode(value);
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
            if (seen.put(value, Boolean.TRUE) != null) {
                out.append("null");
                return;
            }
            try {
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
            } finally {
                seen.remove(value);
            }
        }

        private Map<String, Object> extractFields(Object value) {
            Map<String, Object> special = extractSpecialFields(value);
            if (special != null) {
                return special;
            }

            Class<?> type = value.getClass();
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            if (value instanceof com.slime.ast.AstNode astNode) {
                fields.put("type", toAstTypeName(astNode.type()));
            }
            if (type.isRecord()) {
                RecordComponent[] components = type.getRecordComponents();
                if (components != null) {
                    for (RecordComponent component : components) {
                        String originalName = component.getName();
                        String normalizedName = normalizeFieldName(originalName);
                        try {
                            Object rawFieldValue = component.getAccessor().invoke(value);
                            putFieldIfVisible(fields, normalizedName, normalizeFieldValue(value, originalName, rawFieldValue));
                        } catch (Exception e) {
                            putFieldIfVisible(fields, normalizedName, "<error:" + e.getClass().getSimpleName() + ">");
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
                    String originalName = field.getName();
                    String normalizedName = normalizeFieldName(originalName);
                    if (!visitedNames.add(normalizedName)) {
                        continue;
                    }
                    try {
                        field.setAccessible(true);
                        Object rawFieldValue = field.get(value);
                        putFieldIfVisible(fields, normalizedName, normalizeFieldValue(value, originalName, rawFieldValue));
                    } catch (Exception e) {
                        putFieldIfVisible(fields, normalizedName, "<error:" + e.getClass().getSimpleName() + ">");
                    }
                }
                current = current.getSuperclass();
            }
            return fields;
        }

        private void putFieldIfVisible(LinkedHashMap<String, Object> fields, String fieldName, Object value) {
            if (value == null && (fieldName.endsWith("Token") || fieldName.endsWith("Tokens"))) {
                return;
            }
            fields.put(fieldName, value);
        }

        private void putIfNotNull(LinkedHashMap<String, Object> fields, String fieldName, Object value) {
            if (value != null) {
                fields.put(fieldName, value);
            }
        }

        private Map<String, Object> extractSpecialFields(Object value) {
            String simpleName = value.getClass().getSimpleName();

            if ("SourceLocation".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                Object type = readProperty(value, "type");
                Object locValue = readProperty(value, "value");
                Object start = readProperty(value, "start");
                Object end = readProperty(value, "end");
                putLocationFields(fields, type, locValue, start, end);
                return fields;
            }

            if ("Position".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                int index = asInt(readProperty(value, "index"));
                int rawLine = asInt(readProperty(value, "line"));
                int rawColumn = asInt(readProperty(value, "column"));
                fields.put("index", index);
                if (index == 0 && rawLine == 0 && rawColumn == 0) {
                    fields.put("line", 0);
                    fields.put("column", 0);
                } else {
                    PositionInfo positionInfo = resolvePositionInfo(index, rawLine, rawColumn);
                    fields.put("line", positionInfo.line());
                    fields.put("column", positionInfo.column());
                }
                return fields;
            }

            if ("MemberExpression".equals(simpleName)) {
                return extractMemberExpressionFields(value);
            }

            if ("CallExpression".equals(simpleName)) {
                return extractCallExpressionFields(value);
            }

            if ("SequenceExpression".equals(simpleName)) {
                return extractSequenceExpressionFields(value);
            }

            if ("ForOfStatement".equals(simpleName)) {
                return extractForOfStatementFields(value);
            }

            if ("ParenthesizedExpression".equals(simpleName)) {
                return extractParenthesizedExpressionFields(value);
            }

            if ("BlockStatement".equals(simpleName)) {
                return extractBlockStatementFields(value);
            }

            if ("ExpressionStatement".equals(simpleName)) {
                return extractExpressionStatementFields(value);
            }

            if ("VariableDeclaration".equals(simpleName)) {
                return extractVariableDeclarationFields(value);
            }

            if ("VariableDeclarator".equals(simpleName)) {
                return extractVariableDeclaratorFields(value);
            }

            if ("AssignmentExpression".equals(simpleName)) {
                return extractAssignmentExpressionFields(value);
            }

            if ("UnaryExpression".equals(simpleName) || "UpdateExpression".equals(simpleName)) {
                return extractUnaryLikeExpressionFields(value, simpleName);
            }

            if ("BinaryExpression".equals(simpleName) || "LogicalExpression".equals(simpleName)) {
                return extractBinaryLikeExpressionFields(value, simpleName);
            }

            if ("ReturnStatement".equals(simpleName)) {
                return extractReturnStatementFields(value);
            }

            if ("IfStatement".equals(simpleName)) {
                return extractIfStatementFields(value);
            }

            if ("FunctionDeclaration".equals(simpleName)) {
                return extractFunctionDeclarationFields(value);
            }

            if ("NewExpression".equals(simpleName)) {
                return extractNewExpressionFields(value);
            }

        if ("ThrowStatement".equals(simpleName)) {
            return extractThrowStatementFields(value);
        }

        if ("BreakStatement".equals(simpleName)) {
            return extractBreakStatementFields(value);
        }

        if ("ContinueStatement".equals(simpleName)) {
            return extractContinueStatementFields(value);
        }

        if ("ConditionalExpression".equals(simpleName)) {
            return extractConditionalExpressionFields(value);
        }

            if ("ObjectExpression".equals(simpleName)) {
                return extractObjectExpressionFields(value);
            }

            if ("ArrayExpression".equals(simpleName)) {
                return extractArrayExpressionFields(value);
            }

            if ("ForStatement".equals(simpleName)) {
                return extractForStatementFields(value);
            }

            if ("SwitchCase".equals(simpleName)) {
                return extractSwitchCaseFields(value);
            }

            if ("SwitchStatement".equals(simpleName)) {
                return extractSwitchStatementFields(value);
            }

            if ("ImportNamespaceSpecifier".equals(simpleName)) {
                return extractImportNamespaceSpecifierFields(value);
            }

            if ("ExportSpecifier".equals(simpleName)) {
                return extractExportSpecifierFields(value);
            }

            if ("ExportNamedDeclaration".equals(simpleName)) {
                return extractExportNamedDeclarationFields(value);
            }

            if ("ExportDefaultDeclaration".equals(simpleName)) {
                return extractExportDefaultDeclarationFields(value);
            }

            if ("SpreadElement".equals(simpleName)) {
                return extractSpreadElementFields(value);
            }

            if ("Property".equals(simpleName)) {
                return extractPropertyFields(value);
            }

            if ("AssignmentPattern".equals(simpleName)) {
                return extractAssignmentPatternFields(value);
            }

            if ("Literal".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                String raw = asString(readProperty(value, "raw"));
                Object literalValue = readProperty(value, "value");
                fields.put("type", "Literal");
                if (looksLikeRegexLiteral(raw) && (literalValue == null || literalValue instanceof String)) {
                    fields.put("value", new LinkedHashMap<String, Object>());
                    fields.put("raw", raw);
                    fields.put("regex", parseRegexLiteral(raw));
                } else {
                    fields.put("value", literalValue);
                    if (raw != null && literalValue != null && !(literalValue instanceof Boolean)) {
                        fields.put("raw", raw);
                    }
                    Object regex = readProperty(value, "regex");
                    if (regex != null) {
                        fields.put("regex", regex);
                    }
                    Object bigint = readProperty(value, "bigint");
                    if (bigint != null) {
                        fields.put("bigint", bigint);
                    }
                }
                fields.put("loc", firstNonNull(
                        createLiteralLocation(raw, literalValue, value),
                        copyLocation(readProperty(value, "location"), null)));
                return fields;
            }

            if ("TemplateElement".equals(simpleName)) {
                boolean tail = asBoolean(readProperty(value, "tail"));
                LinkedHashMap<String, Object> valueMap = new LinkedHashMap<>();
                valueMap.put("raw", normalizeTemplateChunk(asString(readProperty(value, "raw")), tail));
                valueMap.put("cooked", normalizeTemplateChunk(asString(readProperty(value, "cooked")), tail));

                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                fields.put("type", "TemplateElement");
                fields.put("tail", tail);
                fields.put("value", valueMap);
                fields.put("loc", readProperty(value, "location"));
                return fields;
            }

            return null;
        }

        private String normalizeFieldName(String fieldName) {
            if ("location".equals(fieldName)) {
                return "loc";
            }
            return fieldName;
        }

        private Object normalizeFieldValue(Object owner, String originalName, Object value) {
            if (value == null) {
                return null;
            }
            if ("operator".equals(originalName)) {
                Object token = createOperatorToken(owner, value);
                if (token != null) {
                    return token;
                }
            }
            if (value instanceof List<?> list) {
                String wrapperKey = wrapperKey(owner, originalName);
                if (wrapperKey != null) {
                    return wrapListItems(list, wrapperKey);
                }
            }
            return value;
        }

        private Map<String, Object> extractMemberExpressionFields(Object value) {
            Object object = readProperty(value, "object");
            Object property = readProperty(value, "property");
            boolean computed = asBoolean(readProperty(value, "computed"));
            boolean optional = asBoolean(readProperty(value, "optional"));
            Object rawLocation = readProperty(value, "location");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", optional ? "OptionalMemberExpression" : "MemberExpression");
            fields.put("object", object);
            if (!computed && !optional) {
                putIfNotNull(fields, "dot", createMemberDotToken(object, property, value));
            }
            fields.put("property", property);
            fields.put("computed", computed);
            fields.put("optional", optional);
            if (optional) {
                putIfNotNull(fields, "optionalChainingToken", createGapToken("OptionalChaining", "?.", object, property, false));
            }
            if (computed) {
                putIfNotNull(fields, "lBracketToken", firstNonNull(
                        createComputedMemberLBracketToken(property),
                        createGapToken("LBracket", "[", object, property, false)));
                putIfNotNull(fields, "rBracketToken", firstNonNull(
                        createComputedMemberRBracketToken(property),
                        createClosingDelimiterToken("RBracket", "]", property, value)));
            }
            fields.put("loc", optional
                    ? copyLocationWithoutValue(rawLocation, "OptionalChain")
                    : normalizeMemberExpressionLocation(object, rawLocation));
            return fields;
        }

        private Object createMemberDotToken(Object object, Object property, Object owner) {
            Object direct = createGapToken("Dot", ".", object, property, false);
            if (direct != null) {
                return direct;
            }
            int propertyStart = startIndex(property);
            if (propertyStart >= 0) {
                return createTokenBetween("Dot", ".", Math.max(0, propertyStart - 8), propertyStart, true);
            }
            return createGapToken("Dot", ".", object, owner, true);
        }

        private Map<String, Object> extractCallExpressionFields(Object value) {
            Object callee = readProperty(value, "callee");
            List<?> arguments = asList(readProperty(value, "arguments"));
            boolean optional = asBoolean(readProperty(value, "optional"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", optional ? "OptionalCallExpression" : "CallExpression");
            fields.put("callee", callee);
            fields.put("arguments", wrapListItems(arguments, "argument"));
            fields.put("optional", optional);
            if (optional) {
                putIfNotNull(fields, "optionalChainingToken", createOptionalCallToken(callee, arguments, value));
            }
            putIfNotNull(fields, "lParenToken", createCallLParenToken(callee, arguments, value));
            putIfNotNull(fields, "rParenToken", createCallRParenToken(arguments, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSequenceExpressionFields(Object value) {
            List<?> expressions = asList(readProperty(value, "expressions"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SequenceExpression");
            fields.put("expressions", expressions);
            if (expressions.size() > 1) {
                putIfNotNull(fields, "commaTokens", createCommaTokens(expressions));
            }
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractForOfStatementFields(Object value) {
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ForOfStatement");
            fields.put("left", readProperty(value, "left"));
            fields.put("right", readProperty(value, "right"));
            fields.put("body", readProperty(value, "body"));
            if (asBoolean(readProperty(value, "await"))) {
                fields.put("await", true);
            }
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractParenthesizedExpressionFields(Object value) {
            Object expression = readProperty(value, "expression");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ParenthesizedExpression");
            fields.put("expression", expression);
            fields.put("loc", readProperty(value, "location"));
            putIfNotNull(fields, "lParenToken", createLeadingToken("LParen", "(", value, expression));
            putIfNotNull(fields, "rParenToken", createClosingDelimiterToken("RParen", ")", expression, value));
            return fields;
        }

        private Map<String, Object> extractBlockStatementFields(Object value) {
            List<?> body = asList(readProperty(value, "body"));
            Object rawLocation = readProperty(value, "location");
            Object rawType = readProperty(rawLocation, "type");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "BlockStatement");
            fields.put("body", body);
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, body, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, body, true));
            fields.put("loc", copyLocation(rawLocation, "BlockStatement".equals(rawType) ? "Block" : null));
            return fields;
        }

        private Map<String, Object> extractExpressionStatementFields(Object value) {
            Object expression = readProperty(value, "expression");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExpressionStatement");
            fields.put("expression", expression);
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", expression, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractVariableDeclarationFields(Object value) {
            List<?> declarations = asList(readProperty(value, "declarations"));
            String kind = asString(readProperty(value, "kind"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "VariableDeclaration");
            fields.put("declarations", declarations);
            Object kindToken = createVariableKindToken(kind, value, firstItem(declarations));
            fields.put("kind", kindToken != null ? kindToken : kind);
            fields.put("loc", readProperty(value, "location"));
            putIfNotNull(fields, "semicolonToken", createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true));
            return fields;
        }

        private Map<String, Object> extractVariableDeclaratorFields(Object value) {
            Object id = readProperty(value, "id");
            Object init = readProperty(value, "init");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "VariableDeclarator");
            fields.put("id", id);
            if (init != null) {
                putIfNotNull(fields, "eqToken", createGapToken("Assign", "=", id, init, false));
            }
            fields.put("init", init);
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractAssignmentExpressionFields(Object value) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            Object operator = readProperty(value, "operator");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "AssignmentExpression");
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            fields.put("left", left);
            fields.put("right", right);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractUnaryLikeExpressionFields(Object value, String type) {
            Object operator = readProperty(value, "operator");
            Object argument = readProperty(value, "argument");
            boolean prefix = asBoolean(readProperty(value, "prefix"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", type);
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            if ("UpdateExpression".equals(type)) {
                fields.put("argument", argument);
                fields.put("prefix", prefix);
            } else {
                fields.put("prefix", prefix);
                fields.put("argument", argument);
            }
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractBinaryLikeExpressionFields(Object value, String type) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            Object operator = readProperty(value, "operator");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", type);
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            fields.put("left", left);
            fields.put("right", right);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractReturnStatementFields(Object value) {
            Object argument = readProperty(value, "argument");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ReturnStatement");
            fields.put("argument", argument);
            putIfNotNull(fields, "returnToken", createLeadingKeywordToken("Return", "return", value, argument));
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", argument, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractIfStatementFields(Object value) {
            Object test = readProperty(value, "test");
            Object consequent = readProperty(value, "consequent");
            Object alternate = readProperty(value, "alternate");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "IfStatement");
            fields.put("test", test);
            fields.put("consequent", consequent);
            fields.put("alternate", alternate);
            Object ifToken = createLeadingKeywordToken("If", "if", value, test);
            putIfNotNull(fields, "ifToken", ifToken);
            if (alternate != null) {
                putIfNotNull(fields, "elseToken", createGapToken("Else", "else", consequent, alternate, true));
            }
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", ifToken, test, false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", test, consequent, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractFunctionDeclarationFields(Object value) {
            Object id = readProperty(value, "id");
            List<?> params = asList(readProperty(value, "params"));
            Object body = readProperty(value, "body");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "FunctionDeclaration");
            fields.put("id", id);
            fields.put("params", wrapListItems(params, "param"));
            fields.put("body", body);
            fields.put("generator", asBoolean(readProperty(value, "generator")));
            fields.put("async", asBoolean(readProperty(value, "async")));
            Object functionToken = createLeadingKeywordToken("Function", "function", value, firstNonNull(id, firstItem(params), body));
            putIfNotNull(fields, "functionToken", functionToken);
            putIfNotNull(fields, "lParenToken", createFunctionLParenToken(id, params, body, value));
            putIfNotNull(fields, "rParenToken", createFunctionRParenToken(params, body, value));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", body, asList(readProperty(body, "body")), false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", body, asList(readProperty(body, "body")), true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractNewExpressionFields(Object value) {
            Object callee = readProperty(value, "callee");
            List<?> arguments = asList(readProperty(value, "arguments"));
            Object rawLocation = readProperty(value, "location");
            Object rawType = readProperty(rawLocation, "type");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "NewExpression");
            fields.put("callee", callee);
            fields.put("arguments", wrapListItems(arguments, "argument"));
            putIfNotNull(fields, "newToken", createLeadingKeywordToken("New", "new", value, callee));
            putIfNotNull(fields, "lParenToken", createCallLParenToken(callee, arguments, value));
            putIfNotNull(fields, "rParenToken", createCallRParenToken(arguments, value));
            fields.put("loc", copyLocation(rawLocation, "NewExpression".equals(rawType) ? "MemberExpression" : null));
            return fields;
        }

    private Map<String, Object> extractThrowStatementFields(Object value) {
        Object argument = readProperty(value, "argument");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "ThrowStatement");
        fields.put("argument", argument);
            putIfNotNull(fields, "throwToken", createLeadingKeywordToken("Throw", "throw", value, argument));
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", argument, value));
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractBreakStatementFields(Object value) {
        Object label = readProperty(value, "label");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "BreakStatement");
        fields.put("label", label);
        Object semicolonToken = createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true);
        Object breakToken = createLeadingKeywordToken("Break", "break", value, firstNonNull(label, semicolonToken, value));
        putIfNotNull(fields, "breakToken", breakToken);
        putIfNotNull(fields, "semicolonToken", semicolonToken);
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractContinueStatementFields(Object value) {
        Object label = readProperty(value, "label");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "ContinueStatement");
        fields.put("label", label);
        Object semicolonToken = createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true);
        Object continueToken = createLeadingKeywordToken("Continue", "continue", value, firstNonNull(label, semicolonToken, value));
        putIfNotNull(fields, "continueToken", continueToken);
        putIfNotNull(fields, "semicolonToken", semicolonToken);
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractConditionalExpressionFields(Object value) {
        Object test = readProperty(value, "test");
        Object consequent = readProperty(value, "consequent");
        Object alternate = readProperty(value, "alternate");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ConditionalExpression");
            fields.put("test", test);
            fields.put("consequent", consequent);
            fields.put("alternate", alternate);
            putIfNotNull(fields, "questionToken", createGapToken("Question", "?", test, consequent, false));
            putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", consequent, alternate, false));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractObjectExpressionFields(Object value) {
            List<?> properties = asList(readProperty(value, "properties"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ObjectExpression");
            fields.put("properties", wrapListItems(properties, "property"));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, properties, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, properties, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractArrayExpressionFields(Object value) {
            List<?> elements = asList(readProperty(value, "elements"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ArrayExpression");
            fields.put("elements", wrapListItems(elements, "element"));
            putIfNotNull(fields, "lBracketToken", createEnclosingToken("LBracket", "[", value, elements, false));
            putIfNotNull(fields, "rBracketToken", createEnclosingToken("RBracket", "]", value, elements, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractForStatementFields(Object value) {
            Object init = readProperty(value, "init");
            Object test = readProperty(value, "test");
            Object update = readProperty(value, "update");
            Object body = readProperty(value, "body");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ForStatement");
            fields.put("init", init);
            fields.put("test", test);
            fields.put("update", update);
            fields.put("body", body);
            Object forToken = createLeadingKeywordToken("For", "for", value, firstNonNull(init, test, update, body));
            putIfNotNull(fields, "forToken", forToken);
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", forToken, firstNonNull(init, test, update, body), false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", lastNonNull(update, test, init), body, true));
            putIfNotNull(fields, "semicolon1Token", createHeaderSemicolonToken(init, test, firstNonNull(update, body), value, 1));
            putIfNotNull(fields, "semicolon2Token", createHeaderSemicolonToken(test, firstNonNull(update, body), value, 2));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSwitchCaseFields(Object value) {
            Object test = readProperty(value, "test");
            List<?> consequent = asList(readProperty(value, "consequent"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SwitchCase");
            fields.put("test", test);
            fields.put("consequent", consequent);
            Object leadingToken = test != null
                    ? createLeadingKeywordToken("Case", "case", value, test)
                    : createLeadingKeywordToken("Default", "default", value, firstItem(consequent));
            if (test != null) {
                putIfNotNull(fields, "caseToken", leadingToken);
            } else {
                putIfNotNull(fields, "defaultToken", leadingToken);
            }
            putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", test != null ? test : leadingToken, firstItem(consequent), false));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSwitchStatementFields(Object value) {
            Object discriminant = readProperty(value, "discriminant");
            List<?> cases = asList(readProperty(value, "cases"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SwitchStatement");
            fields.put("discriminant", discriminant);
            fields.put("cases", cases);
            Object switchToken = createLeadingKeywordToken("Switch", "switch", value, discriminant);
            putIfNotNull(fields, "switchToken", switchToken);
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", switchToken, discriminant, false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", discriminant, firstItem(cases), true));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, cases, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, cases, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractImportNamespaceSpecifierFields(Object value) {
            Object local = readProperty(value, "local");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ImportNamespaceSpecifier");
            fields.put("local", local);
            Object asteriskToken = createLeadingToken("Asterisk", "*", value, local);
            putIfNotNull(fields, "asteriskToken", asteriskToken);
            putIfNotNull(fields, "asToken",
                    rewriteTokenLocationType(createGapToken("as", "as", asteriskToken, local, false), "IdentifierName"));
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractExportSpecifierFields(Object value) {
            Object local = readProperty(value, "local");
            Object exported = readProperty(value, "exported");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportSpecifier");
            fields.put("local", local);
            fields.put("exported", exported);
            if (!sameNodeSpan(local, exported) || !Objects.equals(readProperty(local, "name"), readProperty(exported, "name"))) {
                putIfNotNull(fields, "asToken",
                        rewriteTokenLocationType(createGapToken("as", "as", local, exported, false), "IdentifierName"));
            }
            fields.put("loc", firstNonNull(
                    createSyntheticLocation("ExportSpecifier", local, exported),
                    copyLocation(readProperty(value, "location"), "ExportSpecifier")));
            return fields;
        }

        private Map<String, Object> extractExportDefaultDeclarationFields(Object value) {
            Object declaration = readProperty(value, "declaration");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportDefaultDeclaration");
            fields.put("declaration", declaration);
            Object exportToken = createLeadingKeywordToken("Export", "export", value, declaration);
            putIfNotNull(fields, "exportToken", exportToken);
            putIfNotNull(fields, "defaultToken", createGapToken("Default", "default", exportToken, declaration, false));
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            String declarationType = declaration == null ? null : declaration.getClass().getSimpleName();
            if (!"FunctionDeclaration".equals(declarationType) && !"ClassDeclaration".equals(declarationType)) {
                putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", declaration, value));
            }
            return fields;
        }

        private Map<String, Object> extractExportNamedDeclarationFields(Object value) {
            Object declaration = readProperty(value, "declaration");
            List<?> specifiers = asList(readProperty(value, "specifiers"));
            Object source = readProperty(value, "source");
            String declarationType = declaration == null ? null : declaration.getClass().getSimpleName();
            Object semicolonToken = null;
            if (declaration == null
                    || (!"FunctionDeclaration".equals(declarationType) && !"ClassDeclaration".equals(declarationType))) {
                semicolonToken = createTrailingToken(
                        "Semicolon",
                        ";",
                        firstNonNull(source, lastItem(specifiers), declaration),
                        value);
            }
            Object exportToken = createLeadingKeywordToken(
                    "Export",
                    "export",
                    value,
                    firstNonNull(declaration, firstItem(specifiers), source, semicolonToken, value));
            Object fromToken = null;
            if (source != null) {
                Object fromAnchor = !specifiers.isEmpty() ? lastItem(specifiers) : firstNonNull(declaration, exportToken);
                fromToken = rewriteTokenLocationType(
                        createGapToken("from", "from", fromAnchor, source, false),
                        "IdentifierName");
            }
            boolean hasBraceTokens = declaration == null;
            Object lBraceToken = null;
            Object rBraceToken = null;
            if (hasBraceTokens) {
                if (!specifiers.isEmpty()) {
                    lBraceToken = createGapToken("LBrace", "{", exportToken, firstItem(specifiers), false);
                    rBraceToken = createGapToken("RBrace", "}", lastItem(specifiers), firstNonNull(source, semicolonToken, value), true);
                } else {
                    lBraceToken = createGapToken("LBrace", "{", exportToken, firstNonNull(fromToken, semicolonToken, value), false);
                    rBraceToken = createGapToken("RBrace", "}", firstNonNull(lBraceToken, exportToken), firstNonNull(fromToken, semicolonToken, value), true);
                }
            }
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportNamedDeclaration");
            fields.put("declaration", declaration);
            fields.put("specifiers", wrapListItems(specifiers, "specifier"));
            fields.put("source", source);
            putIfNotNull(fields, "exportToken", exportToken);
            putIfNotNull(fields, "fromToken", fromToken);
            putIfNotNull(fields, "lBraceToken", lBraceToken);
            putIfNotNull(fields, "rBraceToken", rBraceToken);
            putIfNotNull(fields, "semicolonToken", semicolonToken);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractSpreadElementFields(Object value) {
            Object argument = readProperty(value, "argument");
            Object rawLocation = readProperty(value, "location");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SpreadElement");
            fields.put("argument", argument);
            Object ellipsisToken = createLeadingToken("Ellipsis", "...", value, argument);
            putIfNotNull(fields, "ellipsisToken", ellipsisToken);
            fields.put("loc", firstNonNull(
                    copyLocation(rawLocation, null),
                    createSyntheticLocation("PropertyDefinition", ellipsisToken, argument)));
            return fields;
        }

        private Map<String, Object> extractPropertyFields(Object value) {
            Object key = readProperty(value, "key");
            Object propertyValue = readProperty(value, "value");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "Property");
            fields.put("key", key);
            fields.put("value", propertyValue);
            fields.put("kind", readProperty(value, "kind"));
            fields.put("method", asBoolean(readProperty(value, "method")));
            fields.put("shorthand", asBoolean(readProperty(value, "shorthand")));
            fields.put("computed", asBoolean(readProperty(value, "computed")));
            fields.put("loc", createZeroLocation("Property"));
            if (!asBoolean(readProperty(value, "shorthand")) && !asBoolean(readProperty(value, "method"))) {
                putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", key, propertyValue, false));
            }
            return fields;
        }

        private Map<String, Object> extractAssignmentPatternFields(Object value) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "AssignmentPattern");
            fields.put("left", left);
            fields.put("right", right);
            putIfNotNull(fields, "equalToken", createGapToken("Assign", "=", left, right, false));
            fields.put("loc", firstNonNull(
                    createSyntheticLocation("SingleNameBinding", left, right),
                    copyLocation(readProperty(value, "location"), "SingleNameBinding")));
            return fields;
        }

        private Object firstNonNull(Object... values) {
            if (values == null) {
                return null;
            }
            for (Object value : values) {
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        private Object lastNonNull(Object... values) {
            if (values == null) {
                return null;
            }
            for (int index = values.length - 1; index >= 0; index--) {
                if (values[index] != null) {
                    return values[index];
                }
            }
            return null;
        }

        private Object firstItem(List<?> items) {
            return items == null || items.isEmpty() ? null : items.get(0);
        }

        private Object lastItem(List<?> items) {
            return items == null || items.isEmpty() ? null : items.get(items.size() - 1);
        }

        private Object createFunctionLParenToken(Object id, List<?> params, Object body, Object owner) {
            Object anchor = id != null ? id : createLeadingKeywordToken("Function", "function", owner, firstNonNull(firstItem(params), body));
            return createGapToken("LParen", "(", anchor, firstNonNull(firstItem(params), body, owner), false);
        }

        private Object createFunctionRParenToken(List<?> params, Object body, Object owner) {
            Object anchor = lastItem(params);
            if (anchor == null) {
                anchor = createFunctionLParenToken(null, params, body, owner);
            }
            return createGapToken("RParen", ")", anchor, body != null ? body : owner, true);
        }

        private Object createOptionalCallToken(Object callee, List<?> arguments, Object owner) {
            Object firstArgument = firstItem(arguments);
            Object direct = createGapToken("OptionalChaining", "?.", callee, firstNonNull(firstArgument, owner), false);
            if (direct != null) {
                return direct;
            }
            int from = endIndex(callee);
            int to = endIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween("OptionalChaining", "?.", from, to, false);
        }

        private Object createCallLParenToken(Object callee, List<?> arguments, Object owner) {
            Object firstArgument = firstItem(arguments);
            Object direct = firstArgument != null
                    ? createGapToken("LParen", "(", callee, firstArgument, false)
                    : null;
            if (direct != null) {
                return direct;
            }
            if (firstArgument == null) {
                int calleeEnd = contentEndIndex(callee);
                int ownerEnd = contentEndIndex(owner);
                if (calleeEnd >= 0 && ownerEnd >= 0) {
                    Object afterCallee = createTokenBetween("LParen", "(", calleeEnd, ownerEnd, false);
                    if (afterCallee != null) {
                        return afterCallee;
                    }
                }
            }
            int ownerStart = contentStartIndex(owner);
            int beforeArgument = firstArgument != null ? contentStartIndex(firstArgument) : contentEndIndex(owner);
            if (ownerStart >= 0 && beforeArgument >= 0) {
                Object nearestInOwner = createTokenBetween("LParen", "(", ownerStart, beforeArgument, true);
                if (nearestInOwner != null) {
                    return nearestInOwner;
                }
            }
            int from = contentEndIndex(callee);
            int to = contentEndIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween("LParen", "(", from, to, false);
        }

        private Object createCallRParenToken(List<?> arguments, Object owner) {
            Object anchor = lastItem(arguments);
            if (anchor == null) {
                anchor = createCallLParenToken(readProperty(owner, "callee"), arguments, owner);
            }
            return createClosingDelimiterToken("RParen", ")", anchor, owner);
        }

        private Object createClosingDelimiterToken(String type, String tokenValue, Object anchorNode, Object owner) {
            int from = contentEndIndex(anchorNode);
            int to = contentEndIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween(type, tokenValue, from, to, true);
        }

        private Object createHeaderSemicolonToken(Object left, Object right, Object fallback, Object owner, int order) {
            if (order == 1) {
                Object next = right != null ? right : fallback;
                int from = startIndex(left);
                int to = startIndex(next);
                if (from >= 0 && to >= 0) {
                    Object firstHeaderSemicolon = createTokenBetween("Semicolon", ";", from, to, true);
                    if (firstHeaderSemicolon != null) {
                        return firstHeaderSemicolon;
                    }
                }
            }
            Object next = right != null ? right : fallback;
            Object anchor = left != null ? left : createGapToken("LParen", "(", createLeadingKeywordToken("For", "for", owner, next), next != null ? next : owner, false);
            return createGapToken("Semicolon", ";", anchor, next != null ? next : owner, false);
        }

        private Object createHeaderSemicolonToken(Object left, Object right, Object owner, int order) {
            return createGapToken("Semicolon", ";", left != null ? left : owner, right != null ? right : owner, false);
        }

        private Object createLeadingKeywordToken(String type, String tokenValue, Object owner, Object nextNode) {
            return createLeadingToken(type, tokenValue, owner, nextNode);
        }

        private Object createLeadingToken(String type, String tokenValue, Object owner, Object nextNode) {
            int ownerStart = contentStartIndex(owner);
            int nextStart = contentStartIndex(nextNode);
            if (ownerStart < 0 && nextStart < 0) {
                return null;
            }
            int from = ownerStart >= 0 ? ownerStart : Math.max(0, nextStart - 32);
            int to = nextStart >= 0 ? nextStart : Math.min(sourceText.length(), from + 32);
            return createTokenBetween(type, tokenValue, from, to, false);
        }

        private Object createTrailingToken(String type, String tokenValue, Object anchorNode, Object owner) {
            int anchorEnd = contentEndIndex(anchorNode);
            int ownerEnd = contentEndIndex(owner);
            if (anchorEnd < 0 && ownerEnd < 0) {
                return null;
            }
            int from = anchorEnd >= 0 ? anchorEnd : Math.max(0, ownerEnd - 32);
            int to = ownerEnd >= 0 ? Math.min(sourceText.length(), ownerEnd + tokenValue.length() + 8) : Math.min(sourceText.length(), from + 32);
            return createTokenBetween(type, tokenValue, from, to, true);
        }

        private Object createGapToken(String type, String tokenValue, Object leftNode, Object rightNode, boolean preferLast) {
            int from = contentEndIndex(leftNode);
            int to = contentStartIndex(rightNode);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween(type, tokenValue, from, to, preferLast);
        }

        private Object createEnclosingToken(String type, String tokenValue, Object owner, List<?> items, boolean trailing) {
            int ownerStart = contentStartIndex(owner);
            int ownerEnd = contentEndIndex(owner);
            Object first = firstItem(items);
            Object last = lastItem(items);
            Object boundaryToken = createBoundaryToken(type, tokenValue, owner, trailing);
            if (!trailing) {
                if (boundaryToken != null) {
                    return boundaryToken;
                }
                int to = first != null ? contentStartIndex(first) : Math.min(sourceText.length(), ownerStart + 32);
                return createTokenBetween(type, tokenValue, Math.max(0, ownerStart - 32), to, true);
            }
            int from = last != null ? contentEndIndex(last) : Math.max(0, ownerEnd - 32);
            Object trailingToken = createTokenBetween(type, tokenValue, from, Math.min(sourceText.length(), ownerEnd + 32), false);
            if (trailingToken != null) {
                return trailingToken;
            }
            return boundaryToken;
        }

        private Map<String, Object> createTokenBetween(String type, String tokenValue, int from, int to, boolean preferLast) {
            if (sourceText.isEmpty()) {
                return null;
            }
            int normalizedFrom = clampIndex(Math.min(from, to));
            int normalizedTo = clampIndex(Math.max(from, to));
            if (normalizedTo < normalizedFrom) {
                return null;
            }
            int tokenIndex = preferLast
                    ? findLastTokenIndex(tokenValue, normalizedFrom, normalizedTo)
                    : findTokenIndex(tokenValue, normalizedFrom, normalizedTo);
            if (tokenIndex < 0) {
                return null;
            }
            return createToken(type, tokenValue, tokenIndex, tokenIndex + tokenValue.length());
        }

        private List<Map<String, Object>> createCommaTokens(List<?> items) {
            List<Map<String, Object>> tokens = new ArrayList<>();
            if (items == null) {
                return tokens;
            }
            for (int index = 0; index + 1 < items.size(); index++) {
                Map<String, Object> commaToken = createCommaToken(items.get(index), items.get(index + 1));
                if (commaToken != null) {
                    tokens.add(commaToken);
                }
            }
            return tokens;
        }

        private Map<String, Object> createCommaToken(Object current, Object next) {
            int from = contentEndIndex(current);
            int to = contentStartIndex(next);
            if (from >= 0 && to >= 0) {
                return createTokenBetween("Comma", ",", from, to, false);
            }
            if (from >= 0) {
                return createTokenBetween("Comma", ",", from, Math.min(sourceText.length(), from + 32), false);
            }
            if (to >= 0) {
                return createTokenBetween("Comma", ",", Math.max(0, to - 32), to, true);
            }
            return null;
        }

        private int findTokenIndex(String tokenValue, int from, int to) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return -1;
            }
            int start = clampIndex(from);
            int end = clampIndex(to);
            if (end < start) {
                return -1;
            }
            int index = sourceText.indexOf(tokenValue, start);
            if (index < 0) {
                return -1;
            }
            return index + tokenValue.length() <= end ? index : -1;
        }

        private int findLastTokenIndex(String tokenValue, int from, int to) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return -1;
            }
            int start = clampIndex(from);
            int end = clampIndex(to);
            if (end < start) {
                return -1;
            }
            int searchFrom = Math.max(start, end - tokenValue.length());
            int index = sourceText.lastIndexOf(tokenValue, searchFrom);
            if (index < start) {
                return -1;
            }
            return index + tokenValue.length() <= end ? index : -1;
        }

        private int clampIndex(int index) {
            return Math.max(0, Math.min(index, sourceText.length()));
        }

        private int contentStartIndex(Object value) {
            int direct = startIndex(value);
            return switch (simpleName(value)) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    int infixStart = contentStartIndex(readProperty(value, "left"));
                    yield infixStart >= 0 ? infixStart : direct;
                }
                case "ConditionalExpression" -> {
                    int conditionalStart = contentStartIndex(readProperty(value, "test"));
                    yield conditionalStart >= 0 ? conditionalStart : direct;
                }
                case "SequenceExpression" -> {
                    int sequenceStart = contentStartIndex(firstItem(asList(readProperty(value, "expressions"))));
                    yield sequenceStart >= 0 ? sequenceStart : direct;
                }
                case "Property" -> {
                    int propertyStart = firstNonNegative(
                            contentStartIndex(readProperty(value, "key")),
                            contentStartIndex(readProperty(value, "value")));
                    yield propertyStart >= 0 ? propertyStart : direct;
                }
                case "SpreadElement" -> {
                    int argumentStart = contentStartIndex(readProperty(value, "argument"));
                    if (argumentStart >= 3 && sourceMatches(argumentStart - 3, "...")) {
                        yield argumentStart - 3;
                    }
                    yield argumentStart;
                }
                case "AssignmentPattern" -> {
                    int assignmentStart = firstNonNegative(
                            contentStartIndex(readProperty(value, "left")),
                            contentStartIndex(readProperty(value, "right")));
                    yield assignmentStart >= 0 ? assignmentStart : direct;
                }
                case "ChainExpression" -> {
                    int chainStart = contentStartIndex(readProperty(value, "expression"));
                    yield chainStart >= 0 ? chainStart : direct;
                }
                default -> direct;
            };
        }

        private int contentEndIndex(Object value) {
            int direct = endIndex(value);
            return switch (simpleName(value)) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    int infixEnd = contentEndIndex(readProperty(value, "right"));
                    yield infixEnd >= 0 ? infixEnd : direct;
                }
                case "ConditionalExpression" -> {
                    int conditionalEnd = contentEndIndex(readProperty(value, "alternate"));
                    yield conditionalEnd >= 0 ? conditionalEnd : direct;
                }
                case "SequenceExpression" -> {
                    int sequenceEnd = contentEndIndex(lastItem(asList(readProperty(value, "expressions"))));
                    yield sequenceEnd >= 0 ? sequenceEnd : direct;
                }
                case "Property" -> {
                    int propertyEnd = firstNonNegative(
                            contentEndIndex(readProperty(value, "value")),
                            contentEndIndex(readProperty(value, "key")));
                    yield propertyEnd >= 0 ? propertyEnd : direct;
                }
                case "SpreadElement" -> {
                    int spreadEnd = contentEndIndex(readProperty(value, "argument"));
                    yield spreadEnd >= 0 ? spreadEnd : direct;
                }
                case "AssignmentPattern" -> {
                    int assignmentEnd = firstNonNegative(
                            contentEndIndex(readProperty(value, "right")),
                            contentEndIndex(readProperty(value, "left")));
                    yield assignmentEnd >= 0 ? assignmentEnd : direct;
                }
                case "CallExpression", "NewExpression" -> {
                    Object callee = readProperty(value, "callee");
                    List<?> arguments = asList(readProperty(value, "arguments"));
                    Object lastArgument = lastItem(arguments);
                    int calleeEnd = contentEndIndex(callee);
                    if (lastArgument != null) {
                        int lastArgumentEnd = contentEndIndex(lastArgument);
                        if (lastArgumentEnd >= 0 && sourceMatches(lastArgumentEnd, ")")) {
                            yield lastArgumentEnd + 1;
                        }
                    } else if (calleeEnd >= 0 && sourceMatches(calleeEnd, "(") && sourceMatches(calleeEnd + 1, ")")) {
                        yield calleeEnd + 2;
                    }
                    if (calleeEnd >= 0) {
                        int closingParen = findTokenIndex(")", calleeEnd, Math.min(sourceText.length(), calleeEnd + 32));
                        if (closingParen >= 0) {
                            yield closingParen + 1;
                        }
                    }
                    yield direct;
                }
                case "MemberExpression" -> {
                    Object property = readProperty(value, "property");
                    boolean computed = asBoolean(readProperty(value, "computed"));
                    int propertyEnd = contentEndIndex(property);
                    if (computed && propertyEnd >= 0 && sourceMatches(propertyEnd, "]")) {
                        yield propertyEnd + 1;
                    }
                    yield propertyEnd >= 0 ? propertyEnd : direct;
                }
                case "ChainExpression" -> contentEndIndex(readProperty(value, "expression"));
                default -> direct;
            };
        }

        private int firstNonNegative(int... candidates) {
            if (candidates == null) {
                return -1;
            }
            for (int candidate : candidates) {
                if (candidate >= 0) {
                    return candidate;
                }
            }
            return -1;
        }

        private int startIndex(Object value) {
            Object location = locationOf(value);
            if (location == null) {
                return -1;
            }
            Object start = readProperty(location, "start");
            return asInt(readProperty(start, "index"));
        }

        private int endIndex(Object value) {
            Object location = locationOf(value);
            if (location == null) {
                return -1;
            }
            Object end = readProperty(location, "end");
            return asInt(readProperty(end, "index"));
        }

        private Object locationOf(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Map<?, ?> map) {
                if (map.containsKey("loc")) {
                    return map.get("loc");
                }
                if (map.containsKey("location")) {
                    return map.get("location");
                }
                return value;
            }
            if ("SourceLocation".equals(value.getClass().getSimpleName())) {
                return value;
            }
            return readProperty(value, "location");
        }

        private Object createComputedMemberLBracketToken(Object property) {
            int propertyStart = contentStartIndex(property);
            if (propertyStart > 0 && sourceMatches(propertyStart - 1, "[")) {
                return createToken("LBracket", "[", propertyStart - 1, propertyStart);
            }
            return null;
        }

        private Object createComputedMemberRBracketToken(Object property) {
            int propertyEnd = contentEndIndex(property);
            if (propertyEnd >= 0 && sourceMatches(propertyEnd, "]")) {
                return createToken("RBracket", "]", propertyEnd, propertyEnd + 1);
            }
            return null;
        }

        private Object createBoundaryToken(String type, String tokenValue, Object owner, boolean trailing) {
            int index = trailing
                    ? contentEndIndex(owner) - tokenValue.length()
                    : contentStartIndex(owner);
            if (index < 0 || !sourceMatches(index, tokenValue)) {
                return null;
            }
            return createToken(type, tokenValue, index, index + tokenValue.length());
        }

        private boolean sourceMatches(int index, String tokenValue) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return false;
            }
            if (index < 0 || index + tokenValue.length() > sourceText.length()) {
                return false;
            }
            return sourceText.startsWith(tokenValue, index);
        }

        private Map<String, Object> createToken(String type, String tokenValue, int startIndex, int endIndex) {
            LinkedHashMap<String, Object> token = new LinkedHashMap<>();
            token.put("type", type);
            token.put("value", tokenValue);
            token.put("loc", createLocation(type, tokenValue, startIndex, endIndex));
            return token;
        }

        private Map<String, Object> createLocation(String type, String value, int startIndex, int endIndex) {
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            if (type != null) {
                location.put("type", type);
            }
            if (value != null) {
                location.put("value", value);
            }
            location.put("start", createPosition(startIndex));
            location.put("end", createPosition(endIndex));
            return location;
        }

        private Object createSyntheticLocation(String type, Object startNode, Object endNode) {
            int start = startIndex(startNode);
            int end = endIndex(endNode);
            if (start < 0 || end < 0 || end < start) {
                return null;
            }
            return createLocation(type, null, start, end);
        }

        private Object createLiteralLocation(String raw, Object literalValue, Object owner) {
            int start = startIndex(owner);
            int end = endIndex(owner);
            if (start < 0 || end < 0 || end < start) {
                return null;
            }
            if (literalValue == null) {
                return createLocation("NullLiteral", "null", start, end);
            }
            if (raw != null && raw.startsWith("/") && raw.lastIndexOf('/') > 0) {
                return createLocation("RegularExpressionLiteral", raw, start, end);
            }
            if (literalValue instanceof String) {
                return createLocation("StringLiteral", raw, start, end);
            }
            if (literalValue instanceof Number) {
                return createLocation("NumericLiteral", raw, start, end);
            }
            if (literalValue instanceof Boolean) {
                String booleanValue = raw != null ? raw : literalValue.toString();
                return createLocation(Boolean.TRUE.equals(literalValue) ? "True" : "False", booleanValue, start, end);
            }
            return null;
        }

        private Map<String, Object> createPosition(int index) {
            LinkedHashMap<String, Object> position = new LinkedHashMap<>();
            int safeIndex = clampIndex(index);
            PositionInfo positionInfo = resolvePositionInfo(safeIndex, 0, 0);
            position.put("index", safeIndex);
            position.put("line", positionInfo.line());
            position.put("column", positionInfo.column());
            return position;
        }

        private String wrapperKey(Object owner, String originalName) {
            String simpleName = owner.getClass().getSimpleName();
            if ("specifiers".equals(originalName)
                    && ("ImportDeclaration".equals(simpleName) || "ExportNamedDeclaration".equals(simpleName))) {
                return "specifier";
            }
            if ("arguments".equals(originalName)
                    && ("CallExpression".equals(simpleName) || "NewExpression".equals(simpleName))) {
                return "argument";
            }
            if ("params".equals(originalName)
                    && ("FunctionDeclaration".equals(simpleName)
                    || "FunctionExpression".equals(simpleName)
                    || "ArrowFunctionExpression".equals(simpleName))) {
                return "param";
            }
            if ("elements".equals(originalName)
                    && ("ArrayExpression".equals(simpleName) || "ArrayPattern".equals(simpleName))) {
                return "element";
            }
            if ("properties".equals(originalName)
                    && ("ObjectExpression".equals(simpleName) || "ObjectPattern".equals(simpleName))) {
                return "property";
            }
            return null;
        }

        private String toAstTypeName(Object astType) {
            if (astType == null) {
                return "Unknown";
            }
            String rawName = astType instanceof Enum<?> e ? e.name() : String.valueOf(astType);
            String[] parts = rawName.toLowerCase().split("_+");
            StringBuilder out = new StringBuilder();
            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                out.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    out.append(part.substring(1));
                }
            }
            return out.length() == 0 ? rawName : out.toString();
        }

        private List<Map<String, Object>> wrapListItems(List<?> list, String key) {
            List<Map<String, Object>> wrapped = new ArrayList<>();
            for (int index = 0; index < list.size(); index++) {
                Object item = list.get(index);
                LinkedHashMap<String, Object> itemObject = new LinkedHashMap<>();
                itemObject.put(key, item);
                if (index + 1 < list.size()) {
                    Map<String, Object> commaToken = createCommaToken(item, list.get(index + 1));
                    if (commaToken != null) {
                        itemObject.put("commaToken", commaToken);
                    }
                }
                wrapped.add(itemObject);
            }
            return wrapped;
        }

        private List<?> asList(Object value) {
            if (value instanceof List<?> list) {
                return list;
            }
            if (value instanceof Collection<?> collection) {
                return new ArrayList<>(collection);
            }
            return List.of();
        }

        private Object readProperty(Object value, String propertyName) {
            if (value instanceof Map<?, ?> map) {
                return map.get(propertyName);
            }
            try {
                Method method = value.getClass().getMethod(propertyName);
                return method.invoke(value);
            } catch (Exception ignored) {
                return null;
            }
        }

        private boolean asBoolean(Object value) {
            return value instanceof Boolean bool && bool;
        }

        private int asInt(Object value) {
            return value instanceof Number number ? number.intValue() : 0;
        }

        private String asString(Object value) {
            return value instanceof String text ? text : null;
        }

        private boolean shouldPreserveNullLocationValue(Object type, Object start, Object end) {
            if (!(type instanceof String textType)) {
                return false;
            }
            if ("ImportSpecifier".equals(textType)
                    || "ImportDefaultSpecifier".equals(textType)
                    || "ImportNamespaceSpecifier".equals(textType)) {
                return true;
            }
            int startIndex = asInt(readProperty(start, "index"));
            int endIndex = asInt(readProperty(end, "index"));
            int startLine = asInt(readProperty(start, "line"));
            int startColumn = asInt(readProperty(start, "column"));
            int endLine = asInt(readProperty(end, "line"));
            int endColumn = asInt(readProperty(end, "column"));
            return startIndex == 0
                    && endIndex == 0
                    && startLine == 0
                    && startColumn == 0
                    && endLine == 0
                    && endColumn == 0;
        }

        private Object createOperatorToken(Object owner, Object rawOperator) {
            String operatorValue = asString(rawOperator);
            if (operatorValue == null || operatorValue.isBlank() || owner == null) {
                return null;
            }
            String simpleName = owner.getClass().getSimpleName();
            return switch (simpleName) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    Object left = readProperty(owner, "left");
                    Object right = readProperty(owner, "right");
                    Object direct = createGapToken(operatorTokenType(operatorValue), operatorValue, left, right, false);
                    if (direct != null) {
                        yield direct;
                    }
                    int ownerStart = contentStartIndex(owner);
                    int rightStart = contentStartIndex(right);
                    if (ownerStart >= 0 && rightStart >= 0) {
                        Object nearestInOwner = createTokenBetween(
                                operatorTokenType(operatorValue),
                                operatorValue,
                                ownerStart,
                                rightStart,
                                true);
                        if (nearestInOwner != null) {
                            yield nearestInOwner;
                        }
                    }
                    yield null;
                }
                case "UnaryExpression" -> {
                    Object argument = readProperty(owner, "argument");
                    yield createLeadingToken(operatorTokenType(operatorValue), operatorValue, owner, argument);
                }
                case "UpdateExpression" -> {
                    Object argument = readProperty(owner, "argument");
                    boolean prefix = asBoolean(readProperty(owner, "prefix"));
                    yield prefix
                            ? createLeadingToken(operatorTokenType(operatorValue), operatorValue, owner, argument)
                            : createTrailingToken(operatorTokenType(operatorValue), operatorValue, argument, owner);
                }
                default -> null;
            };
        }

        private Object createVariableKindToken(String kind, Object owner, Object firstDeclaration) {
            if (kind == null || kind.isBlank()) {
                return null;
            }
            if ("let".equals(kind)) {
                return rewriteTokenLocationType(createLeadingKeywordToken("let", kind, owner, firstDeclaration), "IdentifierName");
            }
            String tokenType = switch (kind) {
                case "const" -> "Const";
                case "var" -> "Var";
                default -> toAstTypeName(kind);
            };
            return createLeadingKeywordToken(tokenType, kind, owner, firstDeclaration);
        }

        private String operatorTokenType(String operatorValue) {
            return switch (operatorValue) {
                case "&&" -> "LogicalAnd";
                case "||" -> "LogicalOr";
                case "??" -> "NullishCoalescing";
                case "===" -> "StrictEqual";
                case "!==" -> "StrictNotEqual";
                case "==" -> "Equal";
                case "!=" -> "NotEqual";
                case "<" -> "Less";
                case ">" -> "Greater";
                case "<=" -> "LessEqual";
                case ">=" -> "GreaterEqual";
                case "instanceof" -> "Instanceof";
                case "in" -> "In";
                case "<<" -> "LeftShift";
                case ">>" -> "RightShift";
                case ">>>" -> "UnsignedRightShift";
                case "+" -> "Plus";
                case "-" -> "Minus";
                case "*" -> "Asterisk";
                case "/" -> "Slash";
                case "%" -> "Modulo";
                case "&" -> "BitwiseAnd";
                case "^" -> "BitwiseXor";
                case "|" -> "BitwiseOr";
                case "**" -> "Exponentiation";
                case "=" -> "Assign";
                case "+=" -> "PlusAssign";
                case "-=" -> "MinusAssign";
                case "*=" -> "MultiplyAssign";
                case "/=" -> "DivideAssign";
                case "%=" -> "ModuloAssign";
                case "<<=" -> "LeftShiftAssign";
                case ">>=" -> "RightShiftAssign";
                case ">>>=" -> "UnsignedRightShiftAssign";
                case "&=" -> "BitwiseAndAssign";
                case "^=" -> "BitwiseXorAssign";
                case "|=" -> "BitwiseOrAssign";
                case "&&=" -> "LogicalAndAssign";
                case "||=" -> "LogicalOrAssign";
                case "??=" -> "NullishCoalescingAssign";
                case "!" -> "LogicalNot";
                case "~" -> "BitwiseNot";
                case "typeof" -> "Typeof";
                case "void" -> "Void";
                case "delete" -> "Delete";
                case "++" -> "Increment";
                case "--" -> "Decrement";
                default -> toAstTypeName(operatorValue);
            };
        }

        private Object normalizeMemberExpressionLocation(Object object, Object rawLocation) {
            Object current = object;
            while (current != null && "MemberExpression".equals(simpleName(current))) {
                Object next = readProperty(current, "object");
                if (next == null) {
                    break;
                }
                current = next;
            }
            Object objectLocation = locationOf(current);
            if (objectLocation != null) {
                return copyLocation(objectLocation, null);
            }
            return copyLocation(rawLocation, null);
        }

        private Object copyLocation(Object rawLocation, String overrideType) {
            if (rawLocation == null) {
                return rawLocation;
            }
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            Object currentType = readProperty(rawLocation, "type");
            Object type = overrideType != null ? overrideType : currentType;
            Object start = readProperty(rawLocation, "start");
            Object end = readProperty(rawLocation, "end");
            Object value = readProperty(rawLocation, "value");
            putLocationFields(location, type, value, start, end);
            return location;
        }

        private Object copyLocationWithoutValue(Object rawLocation, String overrideType) {
            if (rawLocation == null) {
                return null;
            }
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            Object currentType = readProperty(rawLocation, "type");
            Object type = overrideType != null ? overrideType : currentType;
            Object start = readProperty(rawLocation, "start");
            Object end = readProperty(rawLocation, "end");
            if (type != null) {
                location.put("type", type);
            }
            if (start != null) {
                location.put("start", start);
            }
            if (end != null) {
                location.put("end", end);
            }
            return location;
        }

        private void putLocationFields(
                LinkedHashMap<String, Object> location,
                Object type,
                Object value,
                Object start,
                Object end) {
            boolean preserveNullValue = shouldPreserveNullLocationValue(type, start, end);
            if (preserveNullValue) {
                location.put("value", value);
            }
            if (type != null) {
                location.put("type", type);
            }
            if (!preserveNullValue && value != null) {
                location.put("value", value);
            }
            if (start != null) {
                location.put("start", start);
            }
            if (end != null) {
                location.put("end", end);
            }
        }

        private Object createZeroLocation(String type) {
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            putLocationFields(location, type, null, createZeroPosition(), createZeroPosition());
            return location;
        }

        private Map<String, Object> createZeroPosition() {
            LinkedHashMap<String, Object> position = new LinkedHashMap<>();
            position.put("index", 0);
            position.put("line", 0);
            position.put("column", 0);
            return position;
        }

        private Object rewriteTokenLocationType(Object rawToken, String locationType) {
            if (!(rawToken instanceof Map<?, ?> tokenMap)) {
                return rawToken;
            }
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, Object> token = new LinkedHashMap<>((Map<String, Object>) tokenMap);
            Object rawLoc = token.get("loc");
            if (rawLoc instanceof Map<?, ?> locMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> loc = new LinkedHashMap<>((Map<String, Object>) locMap);
                loc.put("type", locationType);
                token.put("loc", loc);
            }
            return token;
        }

        private boolean sameNodeSpan(Object left, Object right) {
            if (left == null || right == null) {
                return false;
            }
            return startIndex(left) == startIndex(right) && endIndex(left) == endIndex(right);
        }

        private PositionInfo resolvePositionInfo(int index, int fallbackLine, int fallbackColumn) {
            if (index < 0 || sourceText.isEmpty()) {
                return new PositionInfo(fallbackLine, fallbackColumn);
            }
            int safeIndex = Math.min(index, sourceText.length());
            int line = 1;
            int column = 1;
            int i = 0;
            while (i < safeIndex) {
                char ch = sourceText.charAt(i);
                if (ch == '\r') {
                    if (i + 1 < safeIndex && sourceText.charAt(i + 1) == '\n') {
                        i++;
                    }
                    line++;
                    column = 1;
                } else if (ch == '\n' || ch == '\u2028' || ch == '\u2029') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }
                i++;
            }
            return new PositionInfo(line, column);
        }

        private record PositionInfo(int line, int column) {
        }

        private Map<String, Object> createToken(String type, String tokenValue) {
            LinkedHashMap<String, Object> token = new LinkedHashMap<>();
            token.put("type", type);
            token.put("value", tokenValue);
            return token;
        }

        private List<Map<String, Object>> createRepeatedTokens(String type, String tokenValue, int count) {
            List<Map<String, Object>> tokens = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                tokens.add(createToken(type, tokenValue));
            }
            return tokens;
        }

        private boolean looksLikeRegexLiteral(String raw) {
            return raw != null && REGEX_LITERAL_PATTERN.matcher(raw).matches();
        }

        private boolean looksLikeQuotedString(String raw) {
            if (raw == null || raw.length() < 2) {
                return false;
            }
            char first = raw.charAt(0);
            char last = raw.charAt(raw.length() - 1);
            return (first == '"' && last == '"') || (first == '\'' && last == '\'');
        }

        private Map<String, Object> parseRegexLiteral(String raw) {
            LinkedHashMap<String, Object> regex = new LinkedHashMap<>();
            Matcher matcher = REGEX_LITERAL_PATTERN.matcher(raw == null ? "" : raw);
            if (matcher.matches()) {
                regex.put("pattern", matcher.group(1));
                regex.put("flags", matcher.group(2));
            }
            return regex;
        }

        private String normalizeTemplateChunk(String text, boolean tail) {
            if (text == null) {
                return null;
            }
            String normalized = text;
            if (!normalized.isEmpty() && (normalized.charAt(0) == '`' || normalized.charAt(0) == '}')) {
                normalized = normalized.substring(1);
            }
            if (tail) {
                if (!normalized.isEmpty() && normalized.charAt(normalized.length() - 1) == '`') {
                    normalized = normalized.substring(0, normalized.length() - 1);
                }
            } else if (normalized.endsWith("${")) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }
            return normalized;
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
