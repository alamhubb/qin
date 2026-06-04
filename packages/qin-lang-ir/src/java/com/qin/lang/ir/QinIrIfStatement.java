package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrIfStatement(
        QinIrExpression test,
        List<QinIrStatement> consequent,
        List<QinIrStatement> alternate) implements QinIrStatement {
    public QinIrIfStatement {
        Objects.requireNonNull(test, "test cannot be null");
        Objects.requireNonNull(consequent, "consequent cannot be null");
        Objects.requireNonNull(alternate, "alternate cannot be null");
        consequent = List.copyOf(consequent);
        alternate = List.copyOf(alternate);
    }
}
