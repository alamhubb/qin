package com.qin.lang.pipeline.cfa.ir;

import java.util.List;
import java.util.Objects;

/**
 * CFA-facing backend IR for JVM Class-File emission.
 *
 * This layer intentionally sits between language IR and concrete bytecode APIs.
 */
public record QinCfaProgram(
        List<ConstDeclaration> declarations,
        List<ExpressionStatement> expressionStatements,
        List<ConsoleLogValue> consoleValueLogs,
        List<ConsoleLogStatement> consoleLogs,
        List<JavaImport> javaImports,
        List<JsImport> jsImports,
        List<ConsoleLogJavaStaticCall> javaStaticConsoleLogs,
        List<JavaInstanceMethodCall> javaInstanceMethodCalls,
        List<ConsoleLogJavaInstanceCall> javaInstanceConsoleLogs,
        List<TopLevelExecutionStep> executionSteps) {
    public QinCfaProgram(
            List<ConstDeclaration> declarations,
            List<ExpressionStatement> expressionStatements,
            List<ConsoleLogValue> consoleValueLogs,
            List<ConsoleLogStatement> consoleLogs,
            List<JavaImport> javaImports,
            List<JsImport> jsImports,
            List<ConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<JavaInstanceMethodCall> javaInstanceMethodCalls,
            List<ConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
        this(
                declarations,
                expressionStatements,
                consoleValueLogs,
                consoleLogs,
                javaImports,
                jsImports,
                javaStaticConsoleLogs,
                javaInstanceMethodCalls,
                javaInstanceConsoleLogs,
                buildDefaultExecutionSteps(
                        declarations,
                        expressionStatements,
                        consoleValueLogs,
                        consoleLogs,
                        javaStaticConsoleLogs,
                        javaInstanceMethodCalls,
                        javaInstanceConsoleLogs));
    }

    public QinCfaProgram {
        Objects.requireNonNull(declarations, "declarations cannot be null");
        Objects.requireNonNull(expressionStatements, "expressionStatements cannot be null");
        Objects.requireNonNull(consoleValueLogs, "consoleValueLogs cannot be null");
        Objects.requireNonNull(consoleLogs, "consoleLogs cannot be null");
        Objects.requireNonNull(javaImports, "javaImports cannot be null");
        Objects.requireNonNull(jsImports, "jsImports cannot be null");
        Objects.requireNonNull(javaStaticConsoleLogs, "javaStaticConsoleLogs cannot be null");
        Objects.requireNonNull(javaInstanceMethodCalls, "javaInstanceMethodCalls cannot be null");
        Objects.requireNonNull(javaInstanceConsoleLogs, "javaInstanceConsoleLogs cannot be null");
        Objects.requireNonNull(executionSteps, "executionSteps cannot be null");
        declarations = List.copyOf(declarations);
        expressionStatements = List.copyOf(expressionStatements);
        consoleValueLogs = List.copyOf(consoleValueLogs);
        consoleLogs = List.copyOf(consoleLogs);
        javaImports = List.copyOf(javaImports);
        jsImports = List.copyOf(jsImports);
        javaStaticConsoleLogs = List.copyOf(javaStaticConsoleLogs);
        javaInstanceMethodCalls = List.copyOf(javaInstanceMethodCalls);
        javaInstanceConsoleLogs = List.copyOf(javaInstanceConsoleLogs);
        executionSteps = List.copyOf(executionSteps);
    }

    private static List<TopLevelExecutionStep> buildDefaultExecutionSteps(
            List<ConstDeclaration> declarations,
            List<ExpressionStatement> expressionStatements,
            List<ConsoleLogValue> consoleValueLogs,
            List<ConsoleLogStatement> consoleLogs,
            List<ConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<JavaInstanceMethodCall> javaInstanceMethodCalls,
            List<ConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
        List<TopLevelExecutionStep> steps = new java.util.ArrayList<>();
        for (int i = 0; i < declarations.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.DECLARATION, i));
        }
        for (int i = 0; i < expressionStatements.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.EXPRESSION_STATEMENT, i));
        }
        for (int i = 0; i < consoleValueLogs.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.CONSOLE_VALUE, i));
        }
        for (int i = 0; i < consoleLogs.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.CONSOLE_OBJECT, i));
        }
        for (int i = 0; i < javaStaticConsoleLogs.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.JAVA_STATIC_CONSOLE, i));
        }
        for (int i = 0; i < javaInstanceMethodCalls.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.JAVA_INSTANCE_CALL, i));
        }
        for (int i = 0; i < javaInstanceConsoleLogs.size(); i++) {
            steps.add(new TopLevelExecutionStep(TopLevelStatementKind.JAVA_INSTANCE_CONSOLE, i));
        }
        return List.copyOf(steps);
    }

    public enum TopLevelStatementKind {
        DECLARATION,
        EXPRESSION_STATEMENT,
        CONSOLE_VALUE,
        CONSOLE_OBJECT,
        JAVA_STATIC_CONSOLE,
        JAVA_INSTANCE_CALL,
        JAVA_INSTANCE_CONSOLE
    }

    public record TopLevelExecutionStep(TopLevelStatementKind kind, int index) {
        public TopLevelExecutionStep {
            Objects.requireNonNull(kind, "kind cannot be null");
            if (index < 0) {
                throw new IllegalArgumentException("index cannot be negative");
            }
        }
    }

    public sealed interface Expression permits
            ArrayLiteral,
            BooleanLiteral,
            BuiltinCallExpression,
            FunctionLiteral,
            IdentifierReference,
            JavaClassLiteralExpression,
            JavaNewExpression,
            LetExpression,
            MemberAccessExpression,
            NullLiteral,
            NumberLiteral,
            ObjectLiteral,
            SequenceExpression,
            StringLiteral {
    }

    public record ConstDeclaration(String name, Expression initializer) {
        public ConstDeclaration {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(initializer, "initializer cannot be null");
        }
    }

    public record ExpressionStatement(Expression expression) {
        public ExpressionStatement {
            Objects.requireNonNull(expression, "expression cannot be null");
        }
    }

    public record ConsoleLogValue(Expression value) {
        public ConsoleLogValue {
            Objects.requireNonNull(value, "value cannot be null");
        }
    }

    public record ConsoleLogStatement(String objectName, String propertyName) {
        public ConsoleLogStatement {
            Objects.requireNonNull(objectName, "objectName cannot be null");
            Objects.requireNonNull(propertyName, "propertyName cannot be null");
        }
    }

    public record JavaImport(
            String moduleName,
            String importedName,
            String localName,
            String ownerBinaryName) {
        public JavaImport {
            Objects.requireNonNull(moduleName, "moduleName cannot be null");
            Objects.requireNonNull(importedName, "importedName cannot be null");
            Objects.requireNonNull(localName, "localName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        }
    }

    public record JsImport(
            String moduleName,
            String importedName,
            String localName) {
        public JsImport {
            Objects.requireNonNull(moduleName, "moduleName cannot be null");
            Objects.requireNonNull(importedName, "importedName cannot be null");
            Objects.requireNonNull(localName, "localName cannot be null");
        }
    }

    public record ConsoleLogJavaStaticCall(
            String receiverName,
            String ownerBinaryName,
            String methodName,
            List<Expression> arguments) {
        public ConsoleLogJavaStaticCall {
            Objects.requireNonNull(receiverName, "receiverName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
            Objects.requireNonNull(methodName, "methodName cannot be null");
            Objects.requireNonNull(arguments, "arguments cannot be null");
            arguments = List.copyOf(arguments);
        }
    }

    public record JavaInstanceMethodCall(
            String receiverName,
            String ownerBinaryName,
            String methodName,
            List<Expression> arguments) {
        public JavaInstanceMethodCall {
            Objects.requireNonNull(receiverName, "receiverName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
            Objects.requireNonNull(methodName, "methodName cannot be null");
            Objects.requireNonNull(arguments, "arguments cannot be null");
            arguments = List.copyOf(arguments);
        }
    }

    public record ConsoleLogJavaInstanceCall(
            String receiverName,
            String ownerBinaryName,
            String methodName,
            List<Expression> arguments) {
        public ConsoleLogJavaInstanceCall {
            Objects.requireNonNull(receiverName, "receiverName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
            Objects.requireNonNull(methodName, "methodName cannot be null");
            Objects.requireNonNull(arguments, "arguments cannot be null");
            arguments = List.copyOf(arguments);
        }
    }

    public record BooleanLiteral(boolean value) implements Expression {
    }

    public record BuiltinCallExpression(
            String receiverName,
            String methodName,
            List<Expression> arguments) implements Expression {
        public BuiltinCallExpression {
            Objects.requireNonNull(receiverName, "receiverName cannot be null");
            Objects.requireNonNull(methodName, "methodName cannot be null");
            Objects.requireNonNull(arguments, "arguments cannot be null");
            arguments = List.copyOf(arguments);
        }
    }

    public record FunctionLiteral(Expression returnExpression) implements Expression {
        public FunctionLiteral {
            Objects.requireNonNull(returnExpression, "returnExpression cannot be null");
        }
    }

    public record IdentifierReference(String name) implements Expression {
        public IdentifierReference {
            Objects.requireNonNull(name, "name cannot be null");
            if (name.isBlank()) {
                throw new IllegalArgumentException("name cannot be blank");
            }
        }
    }

    public record JavaClassLiteralExpression(String typeName, String binaryName) implements Expression {
        public JavaClassLiteralExpression {
            Objects.requireNonNull(typeName, "typeName cannot be null");
            if (typeName.isBlank()) {
                throw new IllegalArgumentException("typeName cannot be blank");
            }
            typeName = typeName.trim();
            if (binaryName != null && binaryName.isBlank()) {
                binaryName = null;
            }
        }
    }

    public record JavaNewExpression(
            String classLocalName,
            String ownerBinaryName,
            List<Expression> arguments) implements Expression {
        public JavaNewExpression {
            Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
            Objects.requireNonNull(arguments, "arguments cannot be null");
            arguments = List.copyOf(arguments);
        }
    }

    public record LetExpression(
            List<LocalVariableDeclaration> localDeclarations,
            List<Expression> leadingExpressions,
            Expression resultExpression) implements Expression {
        public LetExpression {
            Objects.requireNonNull(localDeclarations, "localDeclarations cannot be null");
            Objects.requireNonNull(leadingExpressions, "leadingExpressions cannot be null");
            Objects.requireNonNull(resultExpression, "resultExpression cannot be null");
            localDeclarations = List.copyOf(localDeclarations);
            leadingExpressions = List.copyOf(leadingExpressions);
        }
    }

    public record LocalVariableDeclaration(String name, Expression initializer) {
        public LocalVariableDeclaration {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(initializer, "initializer cannot be null");
        }
    }

    public record MemberAccessExpression(
            String objectName,
            String propertyName) implements Expression {
        public MemberAccessExpression {
            Objects.requireNonNull(objectName, "objectName cannot be null");
            Objects.requireNonNull(propertyName, "propertyName cannot be null");
        }
    }

    public record NullLiteral() implements Expression {
    }

    public record NumberLiteral(double value) implements Expression {
    }

    public record ObjectLiteral(List<ObjectProperty> properties) implements Expression {
        public ObjectLiteral {
            Objects.requireNonNull(properties, "properties cannot be null");
            properties = List.copyOf(properties);
        }
    }

    public record ObjectProperty(String key, Expression value) {
        public ObjectProperty {
            Objects.requireNonNull(key, "key cannot be null");
            Objects.requireNonNull(value, "value cannot be null");
        }
    }

    public record SequenceExpression(
            List<Expression> leadingExpressions,
            Expression resultExpression) implements Expression {
        public SequenceExpression {
            Objects.requireNonNull(leadingExpressions, "leadingExpressions cannot be null");
            Objects.requireNonNull(resultExpression, "resultExpression cannot be null");
            leadingExpressions = List.copyOf(leadingExpressions);
            if (leadingExpressions.isEmpty()) {
                throw new IllegalArgumentException("leadingExpressions cannot be empty");
            }
        }
    }

    public record StringLiteral(String value) implements Expression {
        public StringLiteral {
            Objects.requireNonNull(value, "value cannot be null");
        }
    }

    public record ArrayLiteral(List<Expression> elements) implements Expression {
        public ArrayLiteral {
            Objects.requireNonNull(elements, "elements cannot be null");
            elements = List.copyOf(elements);
        }
    }
}
