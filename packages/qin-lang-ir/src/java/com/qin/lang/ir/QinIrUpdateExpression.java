package com.qin.lang.ir;

import java.util.Objects;

/**
 * Java-style prefix or postfix update expression.
 */
public record QinIrUpdateExpression(
        QinIrExpression target,
        String operator,
        boolean prefix) implements QinIrExpression {
    public QinIrUpdateExpression {
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        if (!"++".equals(operator) && !"--".equals(operator)) {
            throw new IllegalArgumentException("Unsupported update operator: " + operator);
        }
    }
}
