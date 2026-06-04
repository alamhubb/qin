package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrForEachStatement(
        String itemName,
        QinIrExpression iterable,
        List<QinIrStatement> body) implements QinIrStatement {
    public QinIrForEachStatement {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("itemName cannot be blank");
        }
        Objects.requireNonNull(iterable, "iterable cannot be null");
        Objects.requireNonNull(body, "body cannot be null");
        itemName = itemName.trim();
        body = List.copyOf(body);
    }
}
