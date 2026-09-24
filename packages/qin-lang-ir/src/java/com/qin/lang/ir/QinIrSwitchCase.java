package com.qin.lang.ir;

import java.util.List;

public record QinIrSwitchCase(
        QinIrExpression test,
        List<QinIrStatement> consequent,
        boolean fallthroughAllowed) {
    public QinIrSwitchCase(QinIrExpression test, List<QinIrStatement> consequent) {
        this(test, consequent, true);
    }

    public QinIrSwitchCase {
        consequent = consequent == null ? List.of() : List.copyOf(consequent);
    }

    public boolean isDefault() {
        return test == null;
    }
}
