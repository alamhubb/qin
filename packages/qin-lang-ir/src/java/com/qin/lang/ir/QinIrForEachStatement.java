package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrForEachStatement(
        String itemName,
        QinIrExpression iterable,
        QinIrTypeRef itemType,
        List<QinIrStatement> body) implements QinIrStatement {
    public QinIrForEachStatement {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("itemName cannot be blank");
        }
        Objects.requireNonNull(iterable, "iterable cannot be null");
        if (itemType == null) {
            itemType = QinIrTypeRef.classType("java.lang.Object");
        }
        Objects.requireNonNull(body, "body cannot be null");
        itemName = itemName.trim();
        body = List.copyOf(body);
    }

    public QinIrForEachStatement(
            String itemName,
            QinIrExpression iterable,
            List<QinIrStatement> body) {
        this(itemName, iterable, QinIrTypeRef.classType("java.lang.Object"), body);
    }
}
