package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Frontend adapter that lowers a tiny JS-like subset into Qin IR.
 *
 * Parsing strategy: always use Slime Java parser (real CST/AST frontend).
 */
public final class QinSlimeFrontendAdapter {
    private static final Pattern JAVA_IMPORT_PATTERN = Pattern.compile(
            "import\\s*\\{([^}]*)}\\s*from\\s*[\"']java:([^\"']+)[\"']\\s*;?",
            Pattern.MULTILINE);
    private static final Pattern JS_IMPORT_PATTERN = Pattern.compile(
            "import\\s*\\{([^}]*)}\\s*from\\s*[\"']([^\"']+)[\"']\\s*;?",
            Pattern.MULTILINE);

    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        try {
            ImportExtraction javaImportExtraction = extractJavaImports(source);
            JSImportExtraction jsImportExtraction = extractJsImports(javaImportExtraction.remainingSource());
            String sourceForSlime = jsImportExtraction.remainingSource().trim();
            if (sourceForSlime.isEmpty()) {
                return new QinIrProgram(
                        List.of(),
                        List.of(),
                        javaImportExtraction.imports(),
                        jsImportExtraction.imports(),
                        List.of(),
                        List.of(),
                        List.of());
            }
            return parseProgramWithSlime(sourceForSlime, javaImportExtraction.imports(), jsImportExtraction.imports());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse Qin source with Slime frontend.\n" +
                            "Make sure Slime Java modules are on classpath.\n" +
                            "Cause: " + safeMessage(e),
                    e);
        }
    }

    public QinIrProgram parseConstObjectDeclaration(String source) {
        return parseProgram(source);
    }

    private QinIrProgram parseProgramWithSlime(
            String source,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports)
            throws ReflectiveOperationException {
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

        return lowerProgramAst(programAst, preImports, preJsImports);
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
                && consoleLogs.isEmpty()
                && javaStaticConsoleLogs.isEmpty()
                && javaInstanceMethodCalls.isEmpty()
                && javaInstanceConsoleLogs.isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one supported statement");
        }

        return new QinIrProgram(
                declarations,
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
        if (specifiers.isEmpty()) {
            throw new IllegalArgumentException("Import must include at least one named specifier");
        }

        if (sourceValue.startsWith("java:")) {
            String javaModule = sourceValue.substring("java:".length());
            if (javaModule.isBlank()) {
                throw new IllegalArgumentException("java: import module cannot be blank");
            }
            List<QinIrJavaImport> imports = new ArrayList<>();
            for (Object specifier : specifiers) {
                if (!"ImportSpecifier".equals(simpleName(specifier))) {
                    throw new IllegalArgumentException("Only named import specifier is supported for java: imports");
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
            for (Object specifier : specifiers) {
                if (!"ImportSpecifier".equals(simpleName(specifier))) {
                    throw new IllegalArgumentException("Only named import specifier is supported for js imports");
                }
                String importedName = extractIdentifierName(invokeByName(specifier, "imported"), "ImportSpecifier.imported");
                String localName = extractIdentifierName(invokeByName(specifier, "local"), "ImportSpecifier.local");
                imports.add(new QinIrJsImport(sourceValue, importedName, localName));
            }
            return new LoweredImports(List.of(), imports);
        }

        throw new IllegalArgumentException("Unsupported import module: " + sourceValue);
    }

    private void registerJavaImportLookup(Map<String, String> lookup, QinIrJavaImport javaImport) {
        lookup.put(javaImport.localName(), javaImport.ownerBinaryName());
    }

    private ImportExtraction extractJavaImports(String source) {
        Matcher matcher = JAVA_IMPORT_PATTERN.matcher(source);
        StringBuffer remaining = new StringBuffer();
        List<QinIrJavaImport> imports = new ArrayList<>();
        while (matcher.find()) {
            String specifierBlock = matcher.group(1);
            String javaModule = matcher.group(2).trim();
            if (javaModule.isEmpty()) {
                throw new IllegalArgumentException("java: import module cannot be blank");
            }
            parseImportSpecifiers(imports, specifierBlock, javaModule);
            matcher.appendReplacement(remaining, "");
        }
        matcher.appendTail(remaining);
        return new ImportExtraction(imports, remaining.toString());
    }

    private void parseImportSpecifiers(List<QinIrJavaImport> imports, String specifierBlock, String javaModule) {
        String[] parts = specifierBlock.split(",");
        for (String raw : parts) {
            String spec = raw.trim();
            if (spec.isEmpty()) {
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
            if (importedName.isEmpty() || localName.isEmpty()) {
                throw new IllegalArgumentException("Invalid java import specifier: " + spec);
            }
            String ownerBinaryName = javaModule + "." + importedName;
            imports.add(new QinIrJavaImport("java:" + javaModule, importedName, localName, ownerBinaryName));
        }
    }

    private JSImportExtraction extractJsImports(String source) {
        Matcher matcher = JS_IMPORT_PATTERN.matcher(source);
        StringBuffer remaining = new StringBuffer();
        List<QinIrJsImport> imports = new ArrayList<>();
        while (matcher.find()) {
            String specifierBlock = matcher.group(1);
            String moduleName = matcher.group(2).trim();
            if (!isJsModule(moduleName)) {
                matcher.appendReplacement(remaining, Matcher.quoteReplacement(matcher.group()));
                continue;
            }
            parseJsImportSpecifiers(imports, specifierBlock, moduleName);
            matcher.appendReplacement(remaining, "");
        }
        matcher.appendTail(remaining);
        return new JSImportExtraction(imports, remaining.toString());
    }

    private void parseJsImportSpecifiers(List<QinIrJsImport> imports, String specifierBlock, String moduleName) {
        String[] parts = specifierBlock.split(",");
        for (String raw : parts) {
            String spec = raw.trim();
            if (spec.isEmpty()) {
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
            if (importedName.isEmpty() || localName.isEmpty()) {
                throw new IllegalArgumentException("Invalid js import specifier: " + spec);
            }
            imports.add(new QinIrJsImport(moduleName, importedName, localName));
        }
    }

    private boolean isJsModule(String moduleName) {
        if (moduleName == null || moduleName.isBlank()) {
            return false;
        }
        return moduleName.startsWith("js:")
                || moduleName.endsWith(".js")
                || moduleName.endsWith(".mjs")
                || (!moduleName.startsWith("java:") && !moduleName.endsWith(".qin"));
    }

    private QinIrConstDeclaration lowerVariableDeclaration(
            Object variableDeclarationAst,
            Map<String, String> javaImportLookup) {
        String kind = asString(invokeByName(variableDeclarationAst, "kind"), "VariableDeclaration.kind");
        if (!"const".equals(kind)) {
            throw new IllegalArgumentException("Only const declaration is supported, but got: " + kind);
        }

        List<?> declarators = asList(invokeByName(variableDeclarationAst, "declarations"),
                "VariableDeclaration.declarations");
        if (declarators.size() != 1) {
            throw new IllegalArgumentException("Only one declarator is supported in const declaration");
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
        throw new IllegalArgumentException(
                "Only object literal or Java constructor initializer is supported in const declaration");
    }

    private QinIrObjectLiteral lowerObjectLiteral(Object objectExpressionAst) {
        String nodeType = simpleName(objectExpressionAst);
        if (!"ObjectExpression".equals(nodeType)) {
            throw new IllegalArgumentException("Only object literal initializer is supported, got: " + nodeType);
        }

        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();

        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                throw new IllegalArgumentException("Only normal object property is supported, got: " + simpleName(property));
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
        if (value instanceof QinIrNumberLiteral || value instanceof QinIrStringLiteral) {
            return value;
        }
        throw new IllegalArgumentException("Only integer and string literal are supported in object value");
    }

    private LoweredStatement lowerExpressionStatement(
            Object expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        if (!"CallExpression".equals(simpleName(expression))) {
            throw new IllegalArgumentException("Only call expression statement is supported");
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
            throw new IllegalArgumentException("Only console.log(...) expression statement is supported");
        }

        Object callee = invokeByName(expressionAst, "callee");
        if (!isConsoleLogCallee(callee)) {
            throw new IllegalArgumentException("Only console.log(...) call is supported");
        }

        List<?> arguments = asList(invokeByName(expressionAst, "arguments"), "CallExpression.arguments");
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("console.log(...) must have exactly one argument");
        }

        Object firstArgument = arguments.get(0);
        if ("MemberExpression".equals(simpleName(firstArgument))) {
            Object targetObject = invokeByName(firstArgument, "object");
            Object targetProperty = invokeByName(firstArgument, "property");
            String objectName = extractIdentifierName(targetObject, "console.log argument object");
            String propertyName = extractIdentifierName(targetProperty, "console.log argument property");
            return new LoweredStatement(new QinIrConsoleLogStatement(objectName, propertyName), null, null, null);
        }

        if ("CallExpression".equals(simpleName(firstArgument))) {
            return lowerConsoleLogJavaCall(firstArgument, javaImportLookup, declarationLookup);
        }

        throw new IllegalArgumentException("console.log argument must be member access or call expression");
    }

    private LoweredStatement lowerConsoleLogJavaCall(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw new IllegalArgumentException("console.log call argument must be member call like Math.random()");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            return new LoweredStatement(
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaInstanceCall(
                            receiverName,
                            javaNewExpression.ownerBinaryName(),
                            methodName,
                            arguments));
        }

        throw new IllegalArgumentException("Unknown java receiver in console.log: " + receiverName);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            Object callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw new IllegalArgumentException("Only member call expression statement is supported");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (!(declaration instanceof QinIrJavaNewExpression javaNewExpression)) {
            throw new IllegalArgumentException("Only Java instance method call statement is supported: " + receiverName);
        }

        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
        return new LoweredStatement(
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
        throw new IllegalArgumentException("Only integer and string call arguments are supported");
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
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.intValue());
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(text);
            }
        }
        throw new IllegalArgumentException("Unsupported expression type: " + nodeType);
    }

    private QinIrJavaNewExpression lowerJavaNewExpression(
            Object newExpressionAst,
            Map<String, String> javaImportLookup) {
        Object callee = invokeByName(newExpressionAst, "callee");
        String classLocalName = extractIdentifierName(callee, "NewExpression.callee");
        String ownerBinaryName = javaImportLookup.get(classLocalName);
        if (ownerBinaryName == null) {
            throw new IllegalArgumentException("Unknown java class in constructor call: " + classLocalName);
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
        boolean maybeModule = trimmed.startsWith("import ") || trimmed.startsWith("export ");
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

    private record LoweredStatement(
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
    }

    private record LoweredImports(
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
    }

    private record ImportExtraction(
            List<QinIrJavaImport> imports,
            String remainingSource) {
    }

    private record JSImportExtraction(
            List<QinIrJsImport> imports,
            String remainingSource) {
    }
}
