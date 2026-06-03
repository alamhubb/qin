package com.qin.lang.ir;

import java.util.Objects;

/**
 * Mutable local declaration inside an expression-backed method body.
 */
public record QinIrLocalVariableDeclaration(String name, QinIrExpression initializer) {
    public QinIrLocalVariableDeclaration {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(initializer, "initializer cannot be null");
    }
}
