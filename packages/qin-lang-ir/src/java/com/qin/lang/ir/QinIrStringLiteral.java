package com.qin.lang.ir;

import java.util.Objects;

/**
 * String literal expression in Qin IR.
 */
public record QinIrStringLiteral(String value) implements QinIrExpression {
    public QinIrStringLiteral {
        Objects.requireNonNull(value, "value cannot be null");
    }
}
