package com.qin.lang.ir;

import java.util.List;

public record QinIrSwitchCase(
        QinIrExpression test,
        List<QinIrStatement> consequent) {
    public QinIrSwitchCase {
        consequent = consequent == null ? List.of() : List.copyOf(consequent);
    }

    public boolean isDefault() {
        return test == null;
    }
}
