package com.qin.lang.ir;

import java.util.Objects;

/**
 * Receiver-bound method reference, for example {@code this::display} or
 * {@code other::display}. The receiver is an expression, not a Java type.
 */
public record QinIrBoundMethodReferenceExpression(
        QinIrExpression receiver,
        String methodName) implements QinIrExpression {
    public QinIrBoundMethodReferenceExpression {
        Objects.requireNonNull(receiver, "receiver cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        if (methodName.isBlank()) {
            throw new IllegalArgumentException("methodName cannot be blank");
        }
    }
}
