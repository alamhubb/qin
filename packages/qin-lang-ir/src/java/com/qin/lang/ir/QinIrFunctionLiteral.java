package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Minimal function literal in Qin IR.
 * Current stage models JavaScript-compatible function parameters and a synthesized return expression body.
 */
public record QinIrFunctionLiteral(List<String> parameterNames, QinIrExpression returnExpression) implements QinIrExpression {
    public QinIrFunctionLiteral(QinIrExpression returnExpression) {
        this(List.of(), returnExpression);
    }

    public QinIrFunctionLiteral {
        Objects.requireNonNull(parameterNames, "parameterNames cannot be null");
        Objects.requireNonNull(returnExpression, "returnExpression cannot be null");
        parameterNames = List.copyOf(parameterNames);
    }
}
