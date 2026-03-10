package com.qin.lang.ir;

/**
 * Base type for expression nodes in Qin IR.
 */
public sealed interface QinIrExpression permits QinIrNumberLiteral, QinIrObjectLiteral {
}

