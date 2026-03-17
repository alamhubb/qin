package com.qin.lang.ir;

/**
 * Base type for expression nodes in Qin IR.
 */
public sealed interface QinIrExpression permits
        QinIrArrayLiteral,
        QinIrBooleanLiteral,
        QinIrBuiltinCallExpression,
        QinIrFunctionLiteral,
        QinIrIdentifierReference,
        QinIrJavaNewExpression,
        QinIrMemberAccessExpression,
        QinIrNullLiteral,
        QinIrNumberLiteral,
        QinIrObjectLiteral,
        QinIrStringLiteral {
}
