package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Root Qin IR program node.
 */
public record QinIrProgram(
        List<QinIrConstDeclaration> declarations,
        List<QinIrExpressionStatement> expressionStatements,
        List<QinIrConsoleLogValue> consoleValueLogs,
        List<QinIrConsoleLogStatement> consoleLogs,
        List<QinIrJavaImport> javaImports,
        List<QinIrJsImport> jsImports,
        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs,
        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls,
        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs,
        List<QinIrClassDeclaration> classDeclarations,
        List<TopLevelExecutionStep> executionSteps,
        List<QinIrFunctionModelArtifact> functionModelArtifacts) {
    public QinIrProgram(
            List<QinIrConstDeclaration> declarations,
            List<QinIrExpressionStatement> expressionStatements,
            List<QinIrConsoleLogValue> consoleValueLogs,
            List<QinIrConsoleLogStatement> consoleLogs,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls,
            List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs,
            List<QinIrClassDeclaration> classDeclarations) {
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
                classDeclarations,
                buildDefaultExecutionSteps(
                        declarations,
                        expressionStatements,
                        consoleValueLogs,
                        consoleLogs,
                        javaStaticConsoleLogs,
                        javaInstanceMethodCalls,
                        javaInstanceConsoleLogs),
                List.of());
    }

    public QinIrProgram(
            List<QinIrConstDeclaration> declarations,
            List<QinIrExpressionStatement> expressionStatements,
            List<QinIrConsoleLogValue> consoleValueLogs,
            List<QinIrConsoleLogStatement> consoleLogs,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls,
            List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs,
            List<QinIrClassDeclaration> classDeclarations,
            List<TopLevelExecutionStep> executionSteps) {
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
                classDeclarations,
                executionSteps,
                List.of());
    }

    public QinIrProgram {
        Objects.requireNonNull(declarations, "declarations cannot be null");
        Objects.requireNonNull(expressionStatements, "expressionStatements cannot be null");
        Objects.requireNonNull(consoleValueLogs, "consoleValueLogs cannot be null");
        Objects.requireNonNull(consoleLogs, "consoleLogs cannot be null");
        Objects.requireNonNull(javaImports, "javaImports cannot be null");
        Objects.requireNonNull(jsImports, "jsImports cannot be null");
        Objects.requireNonNull(javaStaticConsoleLogs, "javaStaticConsoleLogs cannot be null");
        Objects.requireNonNull(javaInstanceMethodCalls, "javaInstanceMethodCalls cannot be null");
        Objects.requireNonNull(javaInstanceConsoleLogs, "javaInstanceConsoleLogs cannot be null");
        Objects.requireNonNull(classDeclarations, "classDeclarations cannot be null");
        Objects.requireNonNull(executionSteps, "executionSteps cannot be null");
        Objects.requireNonNull(functionModelArtifacts, "functionModelArtifacts cannot be null");
        declarations = List.copyOf(declarations);
        expressionStatements = List.copyOf(expressionStatements);
        consoleValueLogs = List.copyOf(consoleValueLogs);
        consoleLogs = List.copyOf(consoleLogs);
        javaImports = List.copyOf(javaImports);
        jsImports = List.copyOf(jsImports);
        javaStaticConsoleLogs = List.copyOf(javaStaticConsoleLogs);
        javaInstanceMethodCalls = List.copyOf(javaInstanceMethodCalls);
        javaInstanceConsoleLogs = List.copyOf(javaInstanceConsoleLogs);
        classDeclarations = List.copyOf(classDeclarations);
        executionSteps = List.copyOf(executionSteps);
        functionModelArtifacts = List.copyOf(functionModelArtifacts);
    }

    private static List<TopLevelExecutionStep> buildDefaultExecutionSteps(
            List<QinIrConstDeclaration> declarations,
            List<QinIrExpressionStatement> expressionStatements,
            List<QinIrConsoleLogValue> consoleValueLogs,
            List<QinIrConsoleLogStatement> consoleLogs,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls,
            List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
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
}
