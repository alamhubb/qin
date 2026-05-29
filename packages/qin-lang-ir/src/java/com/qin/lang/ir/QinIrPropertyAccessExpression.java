package com.qin.lang.ir;

import java.util.Objects;

/**
 * Receiver-based property access, for example this.service or payload.name.
 */
public record QinIrPropertyAccessExpression(
        QinIrExpression receiver,
        String propertyName) implements QinIrExpression {
    public QinIrPropertyAccessExpression {
        Objects.requireNonNull(receiver, "receiver cannot be null");
        Objects.requireNonNull(propertyName, "propertyName cannot be null");
        if (propertyName.isBlank()) {
            throw new IllegalArgumentException("propertyName cannot be blank");
        }
    }
}
