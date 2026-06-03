package com.qin.lang.ir;

import java.util.Objects;

public record QinIrElementAccessExpression(
        QinIrExpression receiver,
        QinIrExpression index) implements QinIrExpression {
    public QinIrElementAccessExpression {
        Objects.requireNonNull(receiver, "receiver cannot be null");
        Objects.requireNonNull(index, "index cannot be null");
    }
}
