package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrDoWhileStatementNode(List<QinIrStatement> body, QinIrExpression test) implements QinIrStatement {
    public QinIrDoWhileStatementNode {
        Objects.requireNonNull(body, "body cannot be null");
        Objects.requireNonNull(test, "test cannot be null");
        body = List.copyOf(body);
    }
}
