package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Expression-backed while loop for lowered Java method bodies.
 */
public record QinIrWhileExpression(
        QinIrExpression test,
        List<QinIrLocalVariableDeclaration> localDeclarations,
        List<QinIrExpression> bodyExpressions) implements QinIrExpression {
    public QinIrWhileExpression {
        Objects.requireNonNull(test, "test cannot be null");
        Objects.requireNonNull(localDeclarations, "localDeclarations cannot be null");
        Objects.requireNonNull(bodyExpressions, "bodyExpressions cannot be null");
        localDeclarations = List.copyOf(localDeclarations);
        bodyExpressions = List.copyOf(bodyExpressions);
    }
}
