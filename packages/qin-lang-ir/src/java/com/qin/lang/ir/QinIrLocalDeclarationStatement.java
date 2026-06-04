package com.qin.lang.ir;

import java.util.Objects;

public record QinIrLocalDeclarationStatement(String name, QinIrExpression initializer) implements QinIrStatement {
    public QinIrLocalDeclarationStatement {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(initializer, "initializer cannot be null");
        name = name.trim();
    }
}
