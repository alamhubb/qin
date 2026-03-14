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
        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
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
}
