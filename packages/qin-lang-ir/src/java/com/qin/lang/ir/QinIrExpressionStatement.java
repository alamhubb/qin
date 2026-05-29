package com.qin.lang.ir;

import java.util.Objects;

/**
 * Expression statement in Qin IR.
 */
public record QinIrExpressionStatement(QinIrExpression expression) {
    public QinIrExpressionStatement {
        Objects.requireNonNull(expression, "expression cannot be null");
    }
}
