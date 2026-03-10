package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Object literal expression in Qin IR.
 */
public record QinIrObjectLiteral(List<QinIrObjectProperty> properties) implements QinIrExpression {
    public QinIrObjectLiteral {
        Objects.requireNonNull(properties, "properties cannot be null");
        properties = List.copyOf(properties);
    }
}

