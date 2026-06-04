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
        QinIrLocalDeclarationExpression,
        QinIrMemberAccessExpression,
        QinIrNullLiteral,
        QinIrNumberLiteral,
        QinIrObjectLiteral,
        QinIrPropertyAccessExpression,
        QinIrSequenceExpression,
        QinIrSpreadArgumentExpression,
        QinIrStaticMethodCallExpression,
        QinIrThrowExpression,
        QinIrThisExpression,
        QinIrStringLiteral,
        QinIrWhileExpression {
}
