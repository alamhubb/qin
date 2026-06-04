package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrForStatement(
        List<QinIrLocalVariableDeclaration> initializerDeclarations,
        List<QinIrExpression> initializerExpressions,
        QinIrExpression test,
        List<QinIrExpression> updateExpressions,
        List<QinIrStatement> body) implements QinIrStatement {
    public QinIrForStatement {
        Objects.requireNonNull(initializerDeclarations, "initializerDeclarations cannot be null");
        Objects.requireNonNull(initializerExpressions, "initializerExpressions cannot be null");
        Objects.requireNonNull(updateExpressions, "updateExpressions cannot be null");
        Objects.requireNonNull(body, "body cannot be null");
        initializerDeclarations = List.copyOf(initializerDeclarations);
        initializerExpressions = List.copyOf(initializerExpressions);
        updateExpressions = List.copyOf(updateExpressions);
        body = List.copyOf(body);
    }
}
