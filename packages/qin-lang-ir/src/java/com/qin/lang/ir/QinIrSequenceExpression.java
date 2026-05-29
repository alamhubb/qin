package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Sequential expression that evaluates leading expressions for side effects
 * before producing the final result expression.
 */
public record QinIrSequenceExpression(
        List<QinIrExpression> leadingExpressions,
        QinIrExpression resultExpression) implements QinIrExpression {
    public QinIrSequenceExpression {
        Objects.requireNonNull(leadingExpressions, "leadingExpressions cannot be null");
        Objects.requireNonNull(resultExpression, "resultExpression cannot be null");
        leadingExpressions = List.copyOf(leadingExpressions);
        if (leadingExpressions.isEmpty()) {
            throw new IllegalArgumentException("leadingExpressions cannot be empty");
        }
    }
}
