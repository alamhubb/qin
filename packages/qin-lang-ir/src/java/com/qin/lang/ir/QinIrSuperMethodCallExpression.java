package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Direct superclass method call, for example {@code super.debug()}.
 */
public record QinIrSuperMethodCallExpression(
        String methodName,
        List<QinIrExpression> arguments) implements QinIrExpression {
    public QinIrSuperMethodCallExpression {
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        if (methodName.isBlank()) {
            throw new IllegalArgumentException("methodName cannot be blank");
        }
        arguments = List.copyOf(arguments);
    }
}
