package com.qin.lang.ir;

import java.util.Objects;

/**
 * Assignment expression used by Java-style method bodies.
 */
public record QinIrAssignmentExpression(
        QinIrExpression target,
        String operator,
        QinIrExpression value) implements QinIrExpression {
    public QinIrAssignmentExpression {
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
    }
}
