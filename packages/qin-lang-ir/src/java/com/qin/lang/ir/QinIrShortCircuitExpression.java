package com.qin.lang.ir;

import java.util.Objects;

public record QinIrShortCircuitExpression(
        QinIrExpression left,
        String operator,
        QinIrExpression right) implements QinIrExpression {
    public QinIrShortCircuitExpression {
        Objects.requireNonNull(left, "left cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(right, "right cannot be null");
        if (!"&&".equals(operator) && !"||".equals(operator) && !"??".equals(operator)) {
            throw new IllegalArgumentException("Unsupported short-circuit operator: " + operator);
        }
    }
}
