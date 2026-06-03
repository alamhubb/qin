package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Expression-backed Java enhanced for loop, for example:
 * for (String name : names) { ... }
 */
public record QinIrForEachExpression(
        String itemName,
        QinIrExpression iterable,
        List<QinIrLocalVariableDeclaration> bodyLocalDeclarations,
        List<QinIrExpression> bodyExpressions) implements QinIrExpression {
    public QinIrForEachExpression {
        Objects.requireNonNull(itemName, "itemName cannot be null");
        Objects.requireNonNull(iterable, "iterable cannot be null");
        Objects.requireNonNull(bodyLocalDeclarations, "bodyLocalDeclarations cannot be null");
        Objects.requireNonNull(bodyExpressions, "bodyExpressions cannot be null");
        bodyLocalDeclarations = List.copyOf(bodyLocalDeclarations);
        bodyExpressions = List.copyOf(bodyExpressions);
    }
}
