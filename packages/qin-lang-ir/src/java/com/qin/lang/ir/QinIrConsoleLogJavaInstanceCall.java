package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Console log statement for a Java instance method call, for example:
 * console.log(list.size())
 */
public record QinIrConsoleLogJavaInstanceCall(
        String receiverName,
        String ownerBinaryName,
        String methodName,
        List<QinIrExpression> arguments) {
    public QinIrConsoleLogJavaInstanceCall {
        Objects.requireNonNull(receiverName, "receiverName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}
