package com.qin.lang.ir;

import java.util.Objects;

public record QinIrJavaMethodReferenceExpression(
        String classLocalName,
        String ownerBinaryName,
        String methodName) implements QinIrExpression {
    public QinIrJavaMethodReferenceExpression {
        Objects.requireNonNull(classLocalName, "classLocalName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
    }
}
