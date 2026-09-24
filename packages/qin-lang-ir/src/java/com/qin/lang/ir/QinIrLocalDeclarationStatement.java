package com.qin.lang.ir;

import java.util.Objects;

public record QinIrLocalDeclarationStatement(
        String name,
        QinIrExpression initializer,
        QinIrTypeRef declaredType) implements QinIrStatement {
    public QinIrLocalDeclarationStatement(String name, QinIrExpression initializer) {
        this(name, initializer, null);
    }

    public QinIrLocalDeclarationStatement {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(initializer, "initializer cannot be null");
        name = name.trim();
    }
}
