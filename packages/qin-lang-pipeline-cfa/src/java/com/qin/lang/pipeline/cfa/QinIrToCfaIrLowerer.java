package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lowering pass: Qin language IR -> CFA backend IR.
 */
public final class QinIrToCfaIrLowerer {
    public QinCfaProgram lower(QinIrProgram program) {
        Objects.requireNonNull(program, "program cannot be null");

        List<QinCfaProgram.ConstDeclaration> declarations = new ArrayList<>();
        for (QinIrConstDeclaration declaration : program.declarations()) {
            declarations.add(new QinCfaProgram.ConstDeclaration(
                    declaration.name(),
                    lowerExpression(declaration.initializer())));
        }

        List<QinCfaProgram.ExpressionStatement> expressionStatements = new ArrayList<>();
        for (QinIrExpressionStatement expressionStatement : program.expressionStatements()) {
            expressionStatements.add(new QinCfaProgram.ExpressionStatement(
                    lowerExpression(expressionStatement.expression())));
        }

        List<QinCfaProgram.ConsoleLogValue> consoleValueLogs = new ArrayList<>();
        for (QinIrConsoleLogValue consoleValueLog : program.consoleValueLogs()) {
            consoleValueLogs.add(new QinCfaProgram.ConsoleLogValue(lowerExpression(consoleValueLog.value())));
        }

        List<QinCfaProgram.ConsoleLogStatement> consoleLogs = new ArrayList<>();
        for (QinIrConsoleLogStatement consoleLog : program.consoleLogs()) {
            consoleLogs.add(new QinCfaProgram.ConsoleLogStatement(
                    consoleLog.objectName(),
                    consoleLog.propertyName()));
        }

        List<QinCfaProgram.JavaImport> javaImports = new ArrayList<>();
        for (QinIrJavaImport javaImport : program.javaImports()) {
            javaImports.add(new QinCfaProgram.JavaImport(
                    javaImport.moduleName(),
                    javaImport.importedName(),
                    javaImport.localName(),
                    javaImport.ownerBinaryName()));
        }

        List<QinCfaProgram.JsImport> jsImports = new ArrayList<>();
        for (QinIrJsImport jsImport : program.jsImports()) {
            jsImports.add(new QinCfaProgram.JsImport(
                    jsImport.moduleName(),
                    jsImport.importedName(),
                    jsImport.localName()));
        }

        List<QinCfaProgram.ConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        for (QinIrConsoleLogJavaStaticCall javaStaticCall : program.javaStaticConsoleLogs()) {
            javaStaticConsoleLogs.add(new QinCfaProgram.ConsoleLogJavaStaticCall(
                    javaStaticCall.receiverName(),
                    javaStaticCall.ownerBinaryName(),
                    javaStaticCall.methodName(),
                    lowerExpressions(javaStaticCall.arguments())));
        }

        List<QinCfaProgram.JavaInstanceMethodCall> javaInstanceMethodCalls = new ArrayList<>();
        for (QinIrJavaInstanceMethodCall javaInstanceMethodCall : program.javaInstanceMethodCalls()) {
            javaInstanceMethodCalls.add(new QinCfaProgram.JavaInstanceMethodCall(
                    javaInstanceMethodCall.receiverName(),
                    javaInstanceMethodCall.ownerBinaryName(),
                    javaInstanceMethodCall.methodName(),
                    lowerExpressions(javaInstanceMethodCall.arguments())));
        }

        List<QinCfaProgram.ConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = new ArrayList<>();
        for (QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog : program.javaInstanceConsoleLogs()) {
            javaInstanceConsoleLogs.add(new QinCfaProgram.ConsoleLogJavaInstanceCall(
                    javaInstanceConsoleLog.receiverName(),
                    javaInstanceConsoleLog.ownerBinaryName(),
                    javaInstanceConsoleLog.methodName(),
                    lowerExpressions(javaInstanceConsoleLog.arguments())));
        }

        List<QinCfaProgram.TopLevelExecutionStep> executionSteps = new ArrayList<>();
        for (QinIrProgram.TopLevelExecutionStep executionStep : program.executionSteps()) {
            executionSteps.add(new QinCfaProgram.TopLevelExecutionStep(
                    mapKind(executionStep.kind()),
                    executionStep.index()));
        }

        return new QinCfaProgram(
                declarations,
                expressionStatements,
                consoleValueLogs,
                consoleLogs,
                javaImports,
                jsImports,
                javaStaticConsoleLogs,
                javaInstanceMethodCalls,
                javaInstanceConsoleLogs,
                executionSteps);
    }

    private QinCfaProgram.TopLevelStatementKind mapKind(QinIrProgram.TopLevelStatementKind kind) {
        return switch (kind) {
            case DECLARATION -> QinCfaProgram.TopLevelStatementKind.DECLARATION;
            case EXPRESSION_STATEMENT -> QinCfaProgram.TopLevelStatementKind.EXPRESSION_STATEMENT;
            case CONSOLE_VALUE -> QinCfaProgram.TopLevelStatementKind.CONSOLE_VALUE;
            case CONSOLE_OBJECT -> QinCfaProgram.TopLevelStatementKind.CONSOLE_OBJECT;
            case JAVA_STATIC_CONSOLE -> QinCfaProgram.TopLevelStatementKind.JAVA_STATIC_CONSOLE;
            case JAVA_INSTANCE_CALL -> QinCfaProgram.TopLevelStatementKind.JAVA_INSTANCE_CALL;
            case JAVA_INSTANCE_CONSOLE -> QinCfaProgram.TopLevelStatementKind.JAVA_INSTANCE_CONSOLE;
        };
    }

    private List<QinCfaProgram.Expression> lowerExpressions(List<QinIrExpression> expressions) {
        List<QinCfaProgram.Expression> lowered = new ArrayList<>();
        for (QinIrExpression expression : expressions) {
            lowered.add(lowerExpression(expression));
        }
        return lowered;
    }

    private QinCfaProgram.Expression lowerExpression(QinIrExpression expression) {
        if (expression instanceof QinIrBooleanLiteral booleanLiteral) {
            return new QinCfaProgram.BooleanLiteral(booleanLiteral.value());
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return new QinCfaProgram.BuiltinCallExpression(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName(),
                    lowerExpressions(builtinCallExpression.arguments()));
        }
        if (expression instanceof QinIrFunctionLiteral functionLiteral) {
            return new QinCfaProgram.FunctionLiteral(lowerExpression(functionLiteral.returnExpression()));
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return new QinCfaProgram.IdentifierReference(identifierReference.name());
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            return new QinCfaProgram.JavaNewExpression(
                    javaNewExpression.classLocalName(),
                    javaNewExpression.ownerBinaryName(),
                    lowerExpressions(javaNewExpression.arguments()));
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            return new QinCfaProgram.MemberAccessExpression(
                    memberAccessExpression.objectName(),
                    memberAccessExpression.propertyName());
        }
        if (expression instanceof QinIrNullLiteral) {
            return new QinCfaProgram.NullLiteral();
        }
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            return new QinCfaProgram.NumberLiteral(numberLiteral.value());
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            return new QinCfaProgram.ObjectLiteral(lowerProperties(objectLiteral.properties()));
        }
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            return new QinCfaProgram.StringLiteral(stringLiteral.value());
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            return new QinCfaProgram.ArrayLiteral(lowerExpressions(arrayLiteral.elements()));
        }
        throw new IllegalArgumentException("Unsupported QinIr expression for CFA lowering: "
                + expression.getClass().getName());
    }

    private List<QinCfaProgram.ObjectProperty> lowerProperties(List<QinIrObjectProperty> properties) {
        List<QinCfaProgram.ObjectProperty> lowered = new ArrayList<>();
        for (QinIrObjectProperty property : properties) {
            lowered.add(new QinCfaProgram.ObjectProperty(
                    property.key(),
                    lowerExpression(property.value())));
        }
        return lowered;
    }
}
