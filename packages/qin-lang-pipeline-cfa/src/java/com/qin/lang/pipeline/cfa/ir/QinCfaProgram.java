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
        List<ConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
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
        declarations = List.copyOf(declarations);
        expressionStatements = List.copyOf(expressionStatements);
        consoleValueLogs = List.copyOf(consoleValueLogs);
        consoleLogs = List.copyOf(consoleLogs);
        javaImports = List.copyOf(javaImports);
        jsImports = List.copyOf(jsImports);
        javaStaticConsoleLogs = List.copyOf(javaStaticConsoleLogs);
        javaInstanceMethodCalls = List.copyOf(javaInstanceMethodCalls);
        javaInstanceConsoleLogs = List.copyOf(javaInstanceConsoleLogs);
    }

    public sealed interface Expression permits
            BooleanLiteral,
            BuiltinCallExpression,
            FunctionLiteral,
            IdentifierReference,
            JavaNewExpression,
            MemberAccessExpression,
            NullLiteral,
            NumberLiteral,
            ObjectLiteral,
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

    public record NumberLiteral(int value) implements Expression {
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

    public record StringLiteral(String value) implements Expression {
        public StringLiteral {
            Objects.requireNonNull(value, "value cannot be null");
        }
    }
}
