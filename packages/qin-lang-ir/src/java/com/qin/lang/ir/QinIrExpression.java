package com.qin.lang.ir;

/**
 * Base type for expression nodes in Qin IR.
 */
public sealed interface QinIrExpression permits
        QinIrAssignmentExpression,
        QinIrArrayLiteral,
        QinIrBooleanLiteral,
        QinIrBuiltinCallExpression,
        QinIrDoWhileExpression,
        QinIrForExpression,
        QinIrFunctionLiteral,
        QinIrIfExpression,
        QinIrIdentifierReference,
        QinIrInstanceMethodCallExpression,
        QinIrJavaNewExpression,
        QinIrLetExpression,
        QinIrMemberAccessExpression,
        QinIrNullLiteral,
        QinIrNumberLiteral,
        QinIrObjectLiteral,
        QinIrPropertyAccessExpression,
        QinIrSequenceExpression,
        QinIrStaticMethodCallExpression,
        QinIrThisExpression,
        QinIrStringLiteral,
        QinIrWhileExpression {
}
