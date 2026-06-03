package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Scoped expression for method bodies that need local bindings and side effects
 * before producing a result value.
 */
public record QinIrLetExpression(
        List<QinIrLocalVariableDeclaration> localDeclarations,
        List<QinIrExpression> leadingExpressions,
        QinIrExpression resultExpression) implements QinIrExpression {
    public QinIrLetExpression {
        Objects.requireNonNull(localDeclarations, "localDeclarations cannot be null");
        Objects.requireNonNull(leadingExpressions, "leadingExpressions cannot be null");
        Objects.requireNonNull(resultExpression, "resultExpression cannot be null");
        localDeclarations = List.copyOf(localDeclarations);
        leadingExpressions = List.copyOf(leadingExpressions);
    }
}
