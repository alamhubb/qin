package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Static method call expression, for example Objects.toString(value).
 */
public record QinIrStaticMethodCallExpression(
        String classLocalName,
        String ownerBinaryName,
        String methodName,
        List<QinIrExpression> arguments) implements QinIrExpression {
    public QinIrStaticMethodCallExpression {
        Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}

