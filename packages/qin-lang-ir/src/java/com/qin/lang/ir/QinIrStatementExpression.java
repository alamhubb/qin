package com.qin.lang.ir;

import java.util.Objects;

public record QinIrStatementExpression(QinIrExpression expression) implements QinIrStatement {
    public QinIrStatementExpression {
        Objects.requireNonNull(expression, "expression cannot be null");
    }
}
