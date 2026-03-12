package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Java constructor expression in Qin IR, for example:
 * new ArrayList()
 */
public record QinIrJavaNewExpression(
        String classLocalName,
        String ownerBinaryName,
        List<QinIrExpression> arguments) implements QinIrExpression {
    public QinIrJavaNewExpression {
        Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(arguments, "arguments cannot be null");
        arguments = List.copyOf(arguments);
    }
}
