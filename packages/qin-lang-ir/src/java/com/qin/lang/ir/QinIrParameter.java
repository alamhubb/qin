package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Method parameter in declaration IR.
 */
public record QinIrParameter(
        String name,
        QinIrTypeRef type,
        List<QinIrAnnotation> annotations) {
    public QinIrParameter {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(annotations, "annotations cannot be null");
        name = name.trim();
        annotations = List.copyOf(annotations);
    }
}
