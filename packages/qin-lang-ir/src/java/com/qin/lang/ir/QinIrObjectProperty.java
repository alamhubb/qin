package com.qin.lang.ir;

import java.util.Objects;

/**
 * Object literal property in Qin IR.
 */
public record QinIrObjectProperty(String key, QinIrExpression value) {
    public QinIrObjectProperty {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
    }
}

