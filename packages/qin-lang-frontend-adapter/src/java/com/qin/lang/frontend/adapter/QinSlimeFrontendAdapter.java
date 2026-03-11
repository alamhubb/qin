package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Frontend adapter that lowers a tiny JS-like subset into Qin IR.
 *
 * Parsing strategy: always use Slime Java parser (real CST/AST frontend).
 */
public final class QinSlimeFrontendAdapter {
    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        try {
            return parseProgramWithSlime(source);
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

    private QinIrProgram parseProgramWithSlime(String source) throws ReflectiveOperationException {
        Class<?> slimeParserClass = Class.forName("com.slime.parser.SlimeJavascriptParser");
        Class<?> sourceTypeClass = Arrays.stream(slimeParserClass.getDeclaredClasses())
                .filter(c -> c.getSimpleName().equals("SourceType"))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("SlimeJavascriptParser.SourceType not found"));

        Object sourceTypeScript = enumConstant(sourceTypeClass, "SCRIPT");

        Class<?> subhutiParserClass = Class.forName("com.subhuti.parser.SubhutiParser");
        Method createMethod = subhutiParserClass.getMethod("create", Class.class, Object[].class);
        Object parser = invokeStatic(createMethod, slimeParserClass, new Object[]{source});

        Method programMethod = parser.getClass().getMethod("Program", sourceTypeClass);
        invoke(programMethod, parser, sourceTypeScript);

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

        return lowerProgramAst(programAst);
    }

    private QinIrProgram lowerProgramAst(Object programAst) {
        List<?> body = asList(invokeByName(programAst, "body"), "Program.body");
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        QinIrConstDeclaration declaration = null;
        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();

        for (Object statement : body) {
            String nodeType = simpleName(statement);
            if ("VariableDeclaration".equals(nodeType)) {
                if (declaration != null) {
                    throw new IllegalArgumentException("Only one top-level const declaration is supported");
                }
                declaration = lowerVariableDeclaration(statement);
                continue;
            }
            if ("ExpressionStatement".equals(nodeType)) {
                consoleLogs.add(lowerExpressionStatement(statement));
                continue;
            }
            throw new IllegalArgumentException("Unsupported top-level statement type: " + nodeType);
        }

        if (declaration == null) {
            throw new IllegalArgumentException("Program must contain one const declaration");
        }

        return new QinIrProgram(List.of(declaration), consoleLogs);
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

    private QinIrConsoleLogStatement lowerExpressionStatement(Object expressionStatementAst) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        return lowerConsoleLogCall(expression);
    }

    private QinIrConsoleLogStatement lowerConsoleLogCall(Object expressionAst) {
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
        if (!"MemberExpression".equals(simpleName(firstArgument))) {
            throw new IllegalArgumentException("console.log argument must be member expression like a.age");
        }

        Object targetObject = invokeByName(firstArgument, "object");
        Object targetProperty = invokeByName(firstArgument, "property");
        String objectName = extractIdentifierName(targetObject, "console.log argument object");
        String propertyName = extractIdentifierName(targetProperty, "console.log argument property");

        return new QinIrConsoleLogStatement(objectName, propertyName);
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
}
