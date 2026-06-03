package com.qin.lang.ir;

import java.util.Objects;

public record QinIrJavaInstanceofPatternExpression(
        QinIrExpression value,
        String classLocalName,
        String ownerBinaryName,
        String variableName) implements QinIrExpression {
    public QinIrJavaInstanceofPatternExpression {
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(variableName, "variableName cannot be null");
    }
}
