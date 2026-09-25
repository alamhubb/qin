package com.qin.lang.ir;

import java.util.Objects;

public record QinIrTryResource(
        String name,
        QinIrTypeRef type,
        QinIrExpression initializer,
        QinIrExpression reference) {
    public QinIrTryResource(
            String name,
            QinIrExpression initializer,
            QinIrExpression reference) {
        this(name, null, initializer, reference);
    }

    public QinIrTryResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        name = name.trim();
        if (initializer == null && reference == null) {
            throw new IllegalArgumentException("try resource must have an initializer or reference");
        }
        if (initializer != null && reference != null) {
            throw new IllegalArgumentException("try resource cannot have both initializer and reference");
        }
        if (reference != null) {
            Objects.requireNonNull(reference, "reference cannot be null");
        }
    }

    public boolean declaration() {
        return initializer != null;
    }
}
