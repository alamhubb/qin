package com.qin.lang.ir;

/**
 * Boolean literal expression in Qin IR.
 */
public record QinIrBooleanLiteral(boolean value) implements QinIrExpression {
}
