package com.qin.lang.ir;

/**
 * Number literal in Qin IR.
 */
public record QinIrNumberLiteral(double value) implements QinIrExpression {
}
