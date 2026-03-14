package com.qin.lang.ir;

import java.util.Objects;

/**
 * Member access on a top-level identifier, e.g. a.age.
 */
public record QinIrMemberAccessExpression(
        String objectName,
        String propertyName) implements QinIrExpression {
    public QinIrMemberAccessExpression {
        Objects.requireNonNull(objectName, "objectName cannot be null");
        Objects.requireNonNull(propertyName, "propertyName cannot be null");
    }
}
