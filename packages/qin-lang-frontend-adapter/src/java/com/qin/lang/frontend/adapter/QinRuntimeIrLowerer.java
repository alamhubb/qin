package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.parser.QinParserRuntimeNames;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.nodes.expressions.AwaitExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.ImportExpression;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.statements.ExpressionStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Qin-owned boundary for runtime-oriented IR lowering.
 *
 * <p>This now owns the typed-AST top-level runtime statement dispatch path.
 * Deep runtime-expression semantics still temporarily reuse legacy adapter
 * helper bodies during migration, parallel to {@link QinDeclarationIrLowerer}.
 */
final class QinRuntimeIrLowerer {
    private final QinSlimeFrontendAdapter adapter;

    QinRuntimeIrLowerer(QinSlimeFrontendAdapter adapter) {
        this.adapter = Objects.requireNonNull(adapter, "adapter cannot be null");
    }

    QinTopLevelIrAssembler.LoweredStatement lowerExpressionStatement(
            ExpressionStatement expressionStatement,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Expression expression = expressionStatement.expression();
        if (expression instanceof AwaitExpression awaitExpression) {
            return lowerAwaitExpressionStatement(awaitExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof ImportExpression importExpression) {
            return lowerImportExpressionStatement(importExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof Identifier identifier) {
            String name = identifier.name();
            if ("import".equals(name) || "await".equals(name)) {
                return new QinTopLevelIrAssembler.LoweredStatement(null, null, null, null, null, null);
            }
        }
        if (expression instanceof CallExpression callExpression) {
            Expression callee = callExpression.callee();
            if (isRuntimeShimCall(callee)) {
                QinIrBuiltinCallExpression shim = adapter.lowerGlobalBuiltinCallExpression(
                        callExpression,
                        javaImportLookup,
                        declarationLookup);
                return new QinTopLevelIrAssembler.LoweredStatement(
                        null,
                        new QinIrExpressionStatement(shim),
                        null,
                        null,
                        null,
                        null);
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

        QinIrExpression runtimeExpression = adapter.lowerRuntimeExpression(
                expression,
                javaImportLookup,
                declarationLookup);
        return new QinTopLevelIrAssembler.LoweredStatement(
                null,
                new QinIrExpressionStatement(runtimeExpression),
                null,
                null,
                null,
                null);
    }

    QinIrExpressionStatement lowerTopLevelControlStatement(
            AstNode statementAst,
            String nodeType,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object syntheticFunctionAst = QinSlimeFrontendAdapter.createSyntheticTopLevelFunctionAstStatic(statementAst);
        QinIrObjectLiteral runtimeDefinition = adapter.lowerRequiredFunctionRuntimeDefinition(
                syntheticFunctionAst,
                "TopLevel" + nodeType,
                javaImportLookup,
                declarationLookup);
        if (runtimeDefinition == null) {
            return null;
        }
        QinIrBuiltinCallExpression makeFunction = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
        QinIrBuiltinCallExpression executeFunction = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                List.of(makeFunction));
        return new QinIrExpressionStatement(executeFunction);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerAwaitExpressionStatement(
            AwaitExpression awaitExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument = adapter.lowerRuntimeExpression(
                awaitExpression.argument(),
                javaImportLookup,
                declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM,
                List.of(argument));
        return new QinTopLevelIrAssembler.LoweredStatement(
                null,
                new QinIrExpressionStatement(shim),
                null,
                null,
                null,
                null);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerImportExpressionStatement(
            ImportExpression importExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument = adapter.lowerRuntimeExpression(
                importExpression.source(),
                javaImportLookup,
                declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                List.of(argument));
        return new QinTopLevelIrAssembler.LoweredStatement(
                null,
                new QinIrExpressionStatement(shim),
                null,
                null,
                null,
                null);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerDynamicImportCalleeStatement(
            CallExpression callExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments = adapter.lowerRuntimeArguments(
                callExpression.arguments(),
                javaImportLookup,
                declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                arguments);
        return new QinTopLevelIrAssembler.LoweredStatement(
                null,
                new QinIrExpressionStatement(shim),
                null,
                null,
                null,
                null);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerConsoleLogCall(
            CallExpression expression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!isConsoleLogCallee(expression.callee())) {
            throw qjsError("QJS2001", "Only console.log(...) call is supported");
        }

        List<Expression> arguments = expression.arguments();
        if (arguments.size() != 1) {
            throw qjsError("QJS2002", "console.log(...) must have exactly one argument");
        }

        Expression firstArgument = arguments.get(0);
        if (firstArgument instanceof CallExpression nestedCallExpression
                && nestedCallExpression.callee() instanceof MemberExpression nestedMemberCallee
                && nestedMemberCallee.object() instanceof Identifier objectIdentifier) {
            String receiverName = objectIdentifier.name();
            if (javaImportLookup.containsKey(receiverName)
                    || declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                return lowerConsoleLogJavaCall(nestedCallExpression, javaImportLookup, declarationLookup);
            }
        }

        QinIrExpression value = adapter.lowerRuntimeExpression(firstArgument, javaImportLookup, declarationLookup);
        return new QinTopLevelIrAssembler.LoweredStatement(
                new QinIrConsoleLogValue(value),
                null,
                null,
                null,
                null,
                null);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerConsoleLogJavaCall(
            CallExpression callExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpression.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "console.log call argument must be member call like Math.random()");
        }

        String receiverName = objectIdentifier.name();
        String methodName = propertyIdentifier.name();

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            List<QinIrExpression> arguments = adapter.lowerCallArguments(callExpression.arguments(), javaImportLookup);
            return new QinTopLevelIrAssembler.LoweredStatement(
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            List<QinIrExpression> arguments = adapter.lowerCallArguments(callExpression.arguments(), javaImportLookup);
            return new QinTopLevelIrAssembler.LoweredStatement(
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

        QinIrBuiltinCallExpression builtin = adapter.lowerBuiltinCallExpression(
                callExpression,
                javaImportLookup,
                declarationLookup);
        return new QinTopLevelIrAssembler.LoweredStatement(
                new QinIrConsoleLogValue(builtin),
                null,
                null,
                null,
                null,
                null);
    }

    private QinTopLevelIrAssembler.LoweredStatement lowerJavaInstanceMethodStatement(
            CallExpression callExpression,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (isNoOpRuntimeShimCall(callExpression)) {
            return new QinTopLevelIrAssembler.LoweredStatement(null, null, null, null, null, null);
        }
        Expression callee = callExpression.callee();
        if (isDynamicImportCallee(callee)) {
            return new QinTopLevelIrAssembler.LoweredStatement(null, null, null, null, null, null);
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

        List<QinIrExpression> arguments = adapter.lowerCallArguments(callExpression.arguments(), javaImportLookup);
        return new QinTopLevelIrAssembler.LoweredStatement(
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

    private boolean isConsoleLogCallee(Expression callee) {
        if (!(callee instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            return false;
        }
        return "console".equals(objectIdentifier.name()) && "log".equals(propertyIdentifier.name());
    }

    private boolean isDynamicImportCallee(Expression callee) {
        return callee instanceof ImportExpression;
    }

    private boolean isNoOpRuntimeShimCall(CallExpression callExpression) {
        return isRuntimeShimCall(callExpression.callee());
    }

    private boolean isRuntimeShimCall(Expression callee) {
        if (!(callee instanceof Identifier identifier)) {
            return false;
        }
        String name = identifier.name();
        return QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM.equals(name)
                || QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM.equals(name);
    }

    private boolean isJavaInstanceMethodCallee(
            Expression callee,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callee instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)) {
            return false;
        }
        return declarationLookup.get(objectIdentifier.name()) instanceof QinIrJavaNewExpression;
    }

    private IllegalArgumentException qjsError(String code, String message) {
        return new IllegalArgumentException(code + " " + message);
    }
}
