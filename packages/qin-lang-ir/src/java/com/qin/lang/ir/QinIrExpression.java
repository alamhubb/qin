package com.qin.lang.ir;

/**
 * Base type for expression nodes in Qin IR.
 */
public sealed interface QinIrExpression permits
        QinIrBooleanLiteral,
        QinIrBuiltinCallExpression,
        QinIrIdentifierReference,
        QinIrJavaNewExpression,
        QinIrMemberAccessExpression,
        QinIrNullLiteral,
        QinIrNumberLiteral,
        QinIrObjectLiteral,
        QinIrStringLiteral {
}
