package com.qin.lang.ir;

import java.util.Objects;

public record QinIrUnaryExpression(
        String operator,
        QinIrExpression operand) implements QinIrExpression {
    public QinIrUnaryExpression {
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(operand, "operand cannot be null");
        if (!"!".equals(operator)) {
            throw new IllegalArgumentException("Unsupported unary operator: " + operator);
        }
    }
}
