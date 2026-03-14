package com.qin.lang.ir;

import java.util.Objects;

/**
 * console.log(expr) statement lowered as value expression.
 */
public record QinIrConsoleLogValue(QinIrExpression value) {
    public QinIrConsoleLogValue {
        Objects.requireNonNull(value, "value cannot be null");
    }
}
