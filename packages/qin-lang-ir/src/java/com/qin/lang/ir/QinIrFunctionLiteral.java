package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Minimal function literal in Qin IR.
 * Models JavaScript-compatible function parameters with either a synthesized return expression body or
 * a statement body for Java block lambdas.
 */
public record QinIrFunctionLiteral(
        List<String> parameterNames,
        QinIrExpression returnExpression,
        List<QinIrStatement> bodyStatements) implements QinIrExpression {
    public QinIrFunctionLiteral(List<String> parameterNames, QinIrExpression returnExpression) {
        this(parameterNames, returnExpression, List.of());
    }

    public QinIrFunctionLiteral(QinIrExpression returnExpression) {
        this(List.of(), returnExpression);
    }

    public QinIrFunctionLiteral(List<String> parameterNames, List<QinIrStatement> bodyStatements) {
        this(parameterNames, null, bodyStatements);
    }

    public QinIrFunctionLiteral {
        Objects.requireNonNull(parameterNames, "parameterNames cannot be null");
        Objects.requireNonNull(bodyStatements, "bodyStatements cannot be null");
        parameterNames = List.copyOf(parameterNames);
        bodyStatements = List.copyOf(bodyStatements);
    }
}
