package com.qin.lang.ir;

import java.util.Objects;

/**
 * Expression-backed if/else control flow for lowered Java method bodies.
 */
public record QinIrIfExpression(
        QinIrExpression test,
        QinIrExpression consequent,
        QinIrExpression alternate) implements QinIrExpression {
    public QinIrIfExpression {
        Objects.requireNonNull(test, "test cannot be null");
        Objects.requireNonNull(consequent, "consequent cannot be null");
        Objects.requireNonNull(alternate, "alternate cannot be null");
    }
}
