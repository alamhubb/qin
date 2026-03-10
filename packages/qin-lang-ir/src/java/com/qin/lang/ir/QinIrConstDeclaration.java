package com.qin.lang.ir;

import java.util.Objects;

/**
 * Const declaration in Qin IR.
 */
public record QinIrConstDeclaration(String name, QinIrExpression initializer) {
    public QinIrConstDeclaration {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(initializer, "initializer cannot be null");
    }
}

