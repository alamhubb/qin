package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Java instance method call statement, for example:
 * list.add("hello")
 */
public record QinIrJavaInstanceMethodCall(
        String receiverName,
        String ownerBinaryName,
        String methodName,
        List<QinIrExpression> arguments) {
    public QinIrJavaInstanceMethodCall {
        Objects.requireNonNull(receiverName, "receiverName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}
