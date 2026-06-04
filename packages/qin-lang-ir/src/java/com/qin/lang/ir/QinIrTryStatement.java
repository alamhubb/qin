package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrTryStatement(
        List<QinIrStatement> tryBody,
        List<QinIrCatchClause> catchClauses,
        List<QinIrStatement> finallyBody) implements QinIrStatement {
    public QinIrTryStatement {
        Objects.requireNonNull(tryBody, "tryBody cannot be null");
        Objects.requireNonNull(catchClauses, "catchClauses cannot be null");
        Objects.requireNonNull(finallyBody, "finallyBody cannot be null");
        tryBody = List.copyOf(tryBody);
        catchClauses = List.copyOf(catchClauses);
        finallyBody = List.copyOf(finallyBody);
    }
}
