package com.qin.lang.ir;

import java.util.Objects;

/**
 * Statement-position throw expression used by Java block lowering.
 */
public record QinIrThrowExpression(QinIrExpression value) implements QinIrExpression {
    public QinIrThrowExpression {
        Objects.requireNonNull(value, "value cannot be null");
    }
}
