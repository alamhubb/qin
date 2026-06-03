package com.qin.lang.ir;

/**
 * Base type for expression nodes in Qin IR.
 */
public sealed interface QinIrExpression permits
        QinIrAssignmentExpression,
        QinIrArrayLiteral,
        QinIrBooleanLiteral,
        QinIrBuiltinCallExpression,
        QinIrCastExpression,
        QinIrDoWhileExpression,
        QinIrElementAccessExpression,
        QinIrForEachExpression,
        QinIrForExpression,
        QinIrFunctionLiteral,
        QinIrIfExpression,
        QinIrIdentifierReference,
        QinIrInstanceMethodCallExpression,
        QinIrJavaClassLiteralExpression,
        QinIrJavaInstanceofPatternExpression,
        QinIrJavaMethodReferenceExpression,
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
