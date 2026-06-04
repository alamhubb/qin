package com.qin.lang.ir;

import java.util.Objects;

public record QinIrSpreadArgumentExpression(QinIrExpression expression) implements QinIrExpression {
    public QinIrSpreadArgumentExpression {
        Objects.requireNonNull(expression, "expression cannot be null");
    }
}
