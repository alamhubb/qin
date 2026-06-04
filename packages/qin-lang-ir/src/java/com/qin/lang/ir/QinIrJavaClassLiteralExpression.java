package com.qin.lang.ir;

import java.util.Objects;

public record QinIrJavaClassLiteralExpression(
        String typeName,
        String binaryName) implements QinIrExpression {
    public QinIrJavaClassLiteralExpression {
        Objects.requireNonNull(typeName, "typeName cannot be null");
        if (typeName.isBlank()) {
            throw new IllegalArgumentException("typeName cannot be blank");
        }
        typeName = typeName.trim();
        if (binaryName != null && binaryName.isBlank()) {
            binaryName = null;
        }
    }
}
