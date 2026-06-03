package com.qin.lang.ir;

import java.util.Objects;

public record QinIrJavaClassLiteralExpression(
        String typeName,
        String binaryName) implements QinIrExpression {
    public QinIrJavaClassLiteralExpression {
        Objects.requireNonNull(typeName, "typeName cannot be null");
        Objects.requireNonNull(binaryName, "binaryName cannot be null");
        if (typeName.isBlank()) {
            throw new IllegalArgumentException("typeName cannot be blank");
        }
        if (binaryName.isBlank()) {
            throw new IllegalArgumentException("binaryName cannot be blank");
        }
    }
}
