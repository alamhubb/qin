package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
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
 * Transition adapter from CFA backend IR to Qin language IR.
 * No longer used in the main runtime pipeline after direct CFA emitter adoption.
 */
@Deprecated(forRemoval = false)
public final class QinCfaIrToQinIrAdapter {
    public QinIrProgram toQinIr(QinCfaProgram cfaProgram) {
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");

        List<QinIrConstDeclaration> declarations = new ArrayList<>();
        for (QinCfaProgram.ConstDeclaration declaration : cfaProgram.declarations()) {
            declarations.add(new QinIrConstDeclaration(
                    declaration.name(),
                    toExpression(declaration.initializer())));
        }

        List<QinIrExpressionStatement> expressionStatements = new ArrayList<>();
        for (QinCfaProgram.ExpressionStatement expressionStatement : cfaProgram.expressionStatements()) {
            expressionStatements.add(new QinIrExpressionStatement(
                    toExpression(expressionStatement.expression())));
        }

        List<QinIrConsoleLogValue> consoleValueLogs = new ArrayList<>();
        for (QinCfaProgram.ConsoleLogValue consoleValueLog : cfaProgram.consoleValueLogs()) {
            consoleValueLogs.add(new QinIrConsoleLogValue(toExpression(consoleValueLog.value())));
        }

        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        for (QinCfaProgram.ConsoleLogStatement consoleLog : cfaProgram.consoleLogs()) {
            consoleLogs.add(new QinIrConsoleLogStatement(
                    consoleLog.objectName(),
                    consoleLog.propertyName()));
        }

        List<QinIrJavaImport> javaImports = new ArrayList<>();
        for (QinCfaProgram.JavaImport javaImport : cfaProgram.javaImports()) {
            javaImports.add(new QinIrJavaImport(
                    javaImport.moduleName(),
                    javaImport.importedName(),
                    javaImport.localName(),
                    javaImport.ownerBinaryName()));
        }

        List<QinIrJsImport> jsImports = new ArrayList<>();
        for (QinCfaProgram.JsImport jsImport : cfaProgram.jsImports()) {
            jsImports.add(new QinIrJsImport(
                    jsImport.moduleName(),
                    jsImport.importedName(),
                    jsImport.localName()));
        }

        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        for (QinCfaProgram.ConsoleLogJavaStaticCall javaStaticCall : cfaProgram.javaStaticConsoleLogs()) {
            javaStaticConsoleLogs.add(new QinIrConsoleLogJavaStaticCall(
                    javaStaticCall.receiverName(),
                    javaStaticCall.ownerBinaryName(),
                    javaStaticCall.methodName(),
                    toExpressions(javaStaticCall.arguments())));
        }

        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls = new ArrayList<>();
        for (QinCfaProgram.JavaInstanceMethodCall javaInstanceMethodCall : cfaProgram.javaInstanceMethodCalls()) {
            javaInstanceMethodCalls.add(new QinIrJavaInstanceMethodCall(
                    javaInstanceMethodCall.receiverName(),
                    javaInstanceMethodCall.ownerBinaryName(),
                    javaInstanceMethodCall.methodName(),
                    toExpressions(javaInstanceMethodCall.arguments())));
        }

        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = new ArrayList<>();
        for (QinCfaProgram.ConsoleLogJavaInstanceCall javaInstanceConsoleLog : cfaProgram.javaInstanceConsoleLogs()) {
            javaInstanceConsoleLogs.add(new QinIrConsoleLogJavaInstanceCall(
                    javaInstanceConsoleLog.receiverName(),
                    javaInstanceConsoleLog.ownerBinaryName(),
                    javaInstanceConsoleLog.methodName(),
                    toExpressions(javaInstanceConsoleLog.arguments())));
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
                javaInstanceConsoleLogs);
    }

    private List<QinIrExpression> toExpressions(List<QinCfaProgram.Expression> expressions) {
        List<QinIrExpression> adapted = new ArrayList<>();
        for (QinCfaProgram.Expression expression : expressions) {
            adapted.add(toExpression(expression));
        }
        return adapted;
    }

    private QinIrExpression toExpression(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.BooleanLiteral booleanLiteral) {
            return new QinIrBooleanLiteral(booleanLiteral.value());
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            return new QinIrBuiltinCallExpression(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName(),
                    toExpressions(builtinCallExpression.arguments()));
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            return new QinIrFunctionLiteral(toExpression(functionLiteral.returnExpression()));
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            return new QinIrIdentifierReference(identifierReference.name());
        }
        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            return new QinIrJavaNewExpression(
                    javaNewExpression.classLocalName(),
                    javaNewExpression.ownerBinaryName(),
                    toExpressions(javaNewExpression.arguments()));
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            return new QinIrMemberAccessExpression(
                    memberAccessExpression.objectName(),
                    memberAccessExpression.propertyName());
        }
        if (expression instanceof QinCfaProgram.NullLiteral) {
            return new QinIrNullLiteral();
        }
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return new QinIrNumberLiteral(numberLiteral.value());
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            return new QinIrObjectLiteral(toProperties(objectLiteral.properties()));
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            return new QinIrStringLiteral(stringLiteral.value());
        }
        throw new IllegalArgumentException("Unsupported QinCfa expression for adapter: "
                + expression.getClass().getName());
    }

    private List<QinIrObjectProperty> toProperties(List<QinCfaProgram.ObjectProperty> properties) {
        List<QinIrObjectProperty> adapted = new ArrayList<>();
        for (QinCfaProgram.ObjectProperty property : properties) {
            adapted.add(new QinIrObjectProperty(
                    property.key(),
                    toExpression(property.value())));
        }
        return adapted;
    }
}
