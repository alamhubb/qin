package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrSwitchStatement(
        QinIrExpression discriminant,
        List<QinIrSwitchCase> cases) implements QinIrStatement {
    public QinIrSwitchStatement {
        Objects.requireNonNull(discriminant, "discriminant cannot be null");
        cases = cases == null ? List.of() : List.copyOf(cases);
    }
}
