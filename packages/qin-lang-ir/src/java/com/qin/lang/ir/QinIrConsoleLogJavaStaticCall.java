package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Console log statement for a Java static method call, for example:
 * console.log(math.random())
 */
public record QinIrConsoleLogJavaStaticCall(
        String receiverName,
        String ownerBinaryName,
        String methodName,
        List<QinIrExpression> arguments) {
    public QinIrConsoleLogJavaStaticCall {
        Objects.requireNonNull(receiverName, "receiverName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}
