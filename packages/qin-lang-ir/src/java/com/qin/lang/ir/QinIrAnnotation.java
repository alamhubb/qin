package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Resolved Java annotation in declaration IR.
 */
public record QinIrAnnotation(
        String ownerBinaryName,
        List<QinIrAnnotationArgument> arguments) {
    public QinIrAnnotation {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()) {
            throw new IllegalArgumentException("ownerBinaryName cannot be blank");
        }
        Objects.requireNonNull(arguments, "arguments cannot be null");
        ownerBinaryName = ownerBinaryName.trim();
        arguments = List.copyOf(arguments);
    }
}
