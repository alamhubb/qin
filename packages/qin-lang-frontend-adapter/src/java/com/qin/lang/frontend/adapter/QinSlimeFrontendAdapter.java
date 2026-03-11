package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;

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
            "import\\s*\\{([^}]*)}\\s*from\\s*\"java:([^\"]+)\"\\s*;?",
            Pattern.MULTILINE);

    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        try {
            ImportExtraction importExtraction = extractJavaImports(source);
            String sourceForSlime = importExtraction.remainingSource().trim();
            if (sourceForSlime.isEmpty()) {
                return new QinIrProgram(List.of(), List.of(), importExtraction.imports(), List.of());
            }
            return parseProgramWithSlime(sourceForSlime, importExtraction.imports());
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

    private QinIrProgram parseProgramWithSlime(String source, List<QinIrJavaImport> preImports)
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

        Method programMethod = parser.getClass().getMethod("Program", sourceTypeClass);
        invoke(programMethod, parser, sourceType);

        Method getCstMethod = parser.getClass().getMethod("getCst");
        Object cst = invoke(getCstMethod, parser);
        if (cst == null) {
            throw new IllegalArgumentException("Slime parser returned null CST");
        }

        Class<?> cstToAstUtilsClass = Class.forName("com.slime.parser.cstToAst.SlimeCstToAstUtils");
        Method createProgramAstMethod = findMethod(cstToAstUtilsClass, "createProgramAst", 1);
        Object programAst = invokeStatic(createProgramAstMethod, cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }

        return lowerProgramAst(programAst, preImports);
    }

    private QinIrProgram lowerProgramAst(Object programAst, List<QinIrJavaImport> preImports) {
        List<?> body = asList(invokeByName(programAst, "body"), "Program.body");
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        List<QinIrConstDeclaration> declarations = new ArrayList<>();
        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        List<QinIrJavaImport> javaImports = new ArrayList<>();
        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        Map<String, String> javaImportLookup = new HashMap<>();
        if (preImports != null) {
            javaImports.addAll(preImports);
            for (QinIrJavaImport javaImport : preImports) {
                registerJavaImportLookup(javaImportLookup, javaImport);
            }
        }

        for (Object statement : body) {
            String nodeType = simpleName(statement);
            if ("ImportDeclaration".equals(nodeType)) {
                List<QinIrJavaImport> loweredImports = lowerImportDeclaration(statement);
                javaImports.addAll(loweredImports);
                for (QinIrJavaImport javaImport : loweredImports) {
                    registerJavaImportLookup(javaImportLookup, javaImport);
                }
                continue;
            }
            if ("VariableDeclaration".equals(nodeType)) {
                if (!declarations.isEmpty()) {
                    throw new IllegalArgumentException("Only one top-level const declaration is supported");
                }
                declarations.add(lowerVariableDeclaration(statement));
                continue;
            }
            if ("ExpressionStatement".equals(nodeType)) {
                LoweredConsoleLog lowered = lowerExpressionStatement(statement, javaImportLookup);
                if (lowered.objectLog() != null) {
                    consoleLogs.add(lowered.objectLog());
                }
                if (lowered.javaStaticCall() != null) {
                    javaStaticConsoleLogs.add(lowered.javaStaticCall());
                }
                continue;
            }
            throw new IllegalArgumentException("Unsupported top-level statement type: " + nodeType);
        }

        if (declarations.isEmpty() && consoleLogs.isEmpty() && javaStaticConsoleLogs.isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one supported statement");
        }

        return new QinIrProgram(declarations, consoleLogs, javaImports, javaStaticConsoleLogs);
    }

    private List<QinIrJavaImport> lowerImportDeclaration(Object importDeclarationAst) {
        Object sourceNode = invokeByName(importDeclarationAst, "source");
        String sourceValue = asString(invokeByName(sourceNode, "value"), "ImportDeclaration.source.value");
        if (!sourceValue.startsWith("java:")) {
            throw new IllegalArgumentException("Only java: imports are supported, got: " + sourceValue);
        }
        String javaModule = sourceValue.substring("java:".length());
        if (javaModule.isBlank()) {
            throw new IllegalArgumentException("java: import module cannot be blank");
        }

        List<?> specifiers = asList(invokeByName(importDeclarationAst, "specifiers"), "ImportDeclaration.specifiers");
        if (specifiers.isEmpty()) {
            throw new IllegalArgumentException("java: import must include at least one named specifier");
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
        return imports;
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

    private QinIrConstDeclaration lowerVariableDeclaration(Object variableDeclarationAst) {
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
        QinIrObjectLiteral initializer = lowerObjectLiteral(init);

        return new QinIrConstDeclaration(name, initializer);
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
            QinIrNumberLiteral value = lowerNumericLiteral(valueNode);
            irProperties.add(new QinIrObjectProperty(key, value));
        }

        return new QinIrObjectLiteral(irProperties);
    }

    private LoweredConsoleLog lowerExpressionStatement(
            Object expressionStatementAst,
            Map<String, String> javaImportLookup) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        return lowerConsoleLogCall(expression, javaImportLookup);
    }

    private LoweredConsoleLog lowerConsoleLogCall(Object expressionAst, Map<String, String> javaImportLookup) {
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
            return new LoweredConsoleLog(new QinIrConsoleLogStatement(objectName, propertyName), null);
        }

        if ("CallExpression".equals(simpleName(firstArgument))) {
            return lowerConsoleLogJavaStaticCall(firstArgument, javaImportLookup);
        }

        throw new IllegalArgumentException("console.log argument must be member access or zero-arg call expression");
    }

    private LoweredConsoleLog lowerConsoleLogJavaStaticCall(Object callExpressionAst, Map<String, String> javaImportLookup) {
        List<?> args = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("java static call in console.log currently supports zero arguments only");
        }
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw new IllegalArgumentException("console.log call argument must be member call like Math.random()");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName == null) {
            throw new IllegalArgumentException("Unknown java import receiver in console.log: " + receiverName);
        }
        return new LoweredConsoleLog(null, new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName));
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

    private QinIrNumberLiteral lowerNumericLiteral(Object expressionAst) {
        if (!"Literal".equals(simpleName(expressionAst))) {
            throw new IllegalArgumentException("Only numeric literal is supported in object value");
        }
        Object value = invokeByName(expressionAst, "value");
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Only numeric literal is supported in object value, got: " + value);
        }
        return new QinIrNumberLiteral(number.intValue());
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

    private record LoweredConsoleLog(
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall) {
    }

    private record ImportExtraction(
            List<QinIrJavaImport> imports,
            String remainingSource) {
    }
}
