package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Array literal expression in Qin IR.
 */
public record QinIrArrayLiteral(List<QinIrExpression> elements) implements QinIrExpression {
    public QinIrArrayLiteral {
        Objects.requireNonNull(elements, "elements cannot be null");
        elements = List.copyOf(elements);
    }
}
