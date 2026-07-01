package com.qin.lang.ir;

import java.util.Objects;

public record QinIrJavaInstanceofExpression(
        QinIrExpression value,
        String classLocalName,
        String ownerBinaryName) implements QinIrExpression {
    public QinIrJavaInstanceofExpression {
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
    }
}
