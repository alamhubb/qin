package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrWhileStatementNode(QinIrExpression test, List<QinIrStatement> body) implements QinIrStatement {
    public QinIrWhileStatementNode {
        Objects.requireNonNull(test, "test cannot be null");
        Objects.requireNonNull(body, "body cannot be null");
        body = List.copyOf(body);
    }
}
