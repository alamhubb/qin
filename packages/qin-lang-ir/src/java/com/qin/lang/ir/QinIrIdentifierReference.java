package com.qin.lang.ir;

import java.util.Objects;

/**
 * Identifier reference expression in Qin IR.
 */
public record QinIrIdentifierReference(String name) implements QinIrExpression {
    public QinIrIdentifierReference {
        Objects.requireNonNull(name, "name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }
}
