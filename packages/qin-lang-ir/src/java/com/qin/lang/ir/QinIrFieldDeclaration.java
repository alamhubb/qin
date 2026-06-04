package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * First-phase field declaration node for JVM-oriented Qin IR.
 */
public record QinIrFieldDeclaration(
        String name,
        QinIrTypeRef type,
        List<QinIrAnnotation> annotations,
        QinIrExpression initializer,
        boolean staticField) {
    public QinIrFieldDeclaration(
            String name,
            QinIrTypeRef type,
            List<QinIrAnnotation> annotations,
            QinIrExpression initializer) {
        this(name, type, annotations, initializer, false);
    }

    public QinIrFieldDeclaration {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(annotations, "annotations cannot be null");
        name = name.trim();
        annotations = List.copyOf(annotations);
    }
}
