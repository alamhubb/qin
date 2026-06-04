package com.qin.lang.ir;

import java.util.Objects;

/**
 * Statement-position local declaration used by Java block lowering when source
 * order matters.
 */
public record QinIrLocalDeclarationExpression(
        String name,
        QinIrExpression initializer) implements QinIrExpression {
    public QinIrLocalDeclarationExpression {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(initializer, "initializer cannot be null");
    }
}
