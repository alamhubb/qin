package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrSwitchExpression(
        QinIrExpression discriminant,
        List<QinIrSwitchCase> cases) implements QinIrExpression {
    public QinIrSwitchExpression {
        Objects.requireNonNull(discriminant, "discriminant cannot be null");
        cases = cases == null ? List.of() : List.copyOf(cases);
    }
}
