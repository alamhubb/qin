package com.qin.lang.ir;

import java.util.Objects;

public record QinIrCastExpression(
        String typeName,
        QinIrExpression expression) implements QinIrExpression {
    public QinIrCastExpression {
        Objects.requireNonNull(typeName, "typeName cannot be null");
        Objects.requireNonNull(expression, "expression cannot be null");
    }
}
