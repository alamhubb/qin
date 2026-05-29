package com.qin.lang.ir;

import java.util.Objects;

/**
 * Minimal function literal in Qin IR.
 * Current stage only models a synthesized return expression body.
 */
public record QinIrFunctionLiteral(QinIrExpression returnExpression) implements QinIrExpression {
    public QinIrFunctionLiteral {
        Objects.requireNonNull(returnExpression, "returnExpression cannot be null");
    }
}
