package com.qin.lang.ir;

import java.util.Objects;

public record QinIrThrowStatement(QinIrExpression value) implements QinIrStatement {
    public QinIrThrowStatement {
        Objects.requireNonNull(value, "value cannot be null");
    }
}
