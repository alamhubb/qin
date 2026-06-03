package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Expression-backed for loop for lowered Java method bodies.
 */
public record QinIrForExpression(
        List<QinIrLocalVariableDeclaration> initializerDeclarations,
        List<QinIrExpression> initializerExpressions,
        QinIrExpression test,
        List<QinIrExpression> updateExpressions,
        List<QinIrLocalVariableDeclaration> bodyLocalDeclarations,
        List<QinIrExpression> bodyExpressions) implements QinIrExpression {
    public QinIrForExpression {
        Objects.requireNonNull(initializerDeclarations, "initializerDeclarations cannot be null");
        Objects.requireNonNull(initializerExpressions, "initializerExpressions cannot be null");
        Objects.requireNonNull(updateExpressions, "updateExpressions cannot be null");
        Objects.requireNonNull(bodyLocalDeclarations, "bodyLocalDeclarations cannot be null");
        Objects.requireNonNull(bodyExpressions, "bodyExpressions cannot be null");
        initializerDeclarations = List.copyOf(initializerDeclarations);
        initializerExpressions = List.copyOf(initializerExpressions);
        updateExpressions = List.copyOf(updateExpressions);
        bodyLocalDeclarations = List.copyOf(bodyLocalDeclarations);
        bodyExpressions = List.copyOf(bodyExpressions);
    }
}
