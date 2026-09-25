package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrArrayCreationExpression(
        QinIrTypeRef componentType,
        List<QinIrExpression> dimensions,
        int trailingEmptyDimensions) implements QinIrExpression {
    public QinIrArrayCreationExpression {
        Objects.requireNonNull(componentType, "componentType cannot be null");
        Objects.requireNonNull(dimensions, "dimensions cannot be null");
        dimensions = List.copyOf(dimensions);
        if (dimensions.isEmpty()) {
            throw new IllegalArgumentException("dimensions cannot be empty");
        }
        if (trailingEmptyDimensions < 0) {
            throw new IllegalArgumentException("trailingEmptyDimensions cannot be negative");
        }
    }
}
