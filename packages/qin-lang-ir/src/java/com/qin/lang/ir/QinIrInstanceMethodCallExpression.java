package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Receiver-based instance method call, for example this.service.message().
 */
public record QinIrInstanceMethodCallExpression(
        QinIrExpression receiver,
        String methodName,
        List<QinIrExpression> arguments) implements QinIrExpression {
    public QinIrInstanceMethodCallExpression {
        Objects.requireNonNull(receiver, "receiver cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        if (methodName.isBlank()) {
            throw new IllegalArgumentException("methodName cannot be blank");
        }
        arguments = List.copyOf(arguments);
    }
}
