package com.qin.lang.ir;

import java.util.Objects;

/**
 * Named annotation argument in declaration IR.
 */
public record QinIrAnnotationArgument(
        String name,
        QinIrExpression value) {
    public QinIrAnnotationArgument {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(value, "value cannot be null");
        name = name.trim();
    }
}
