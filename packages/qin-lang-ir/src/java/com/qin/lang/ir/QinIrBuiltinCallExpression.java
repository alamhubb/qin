package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Built-in global call expression, e.g. Math.random(), JSON.stringify(x).
 */
public record QinIrBuiltinCallExpression(
        String receiverName,
        String methodName,
        List<QinIrExpression> arguments) implements QinIrExpression {
    public QinIrBuiltinCallExpression {
        Objects.requireNonNull(receiverName, "receiverName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}
