package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrTryStatement(
        List<QinIrTryResource> resources,
        List<QinIrStatement> tryBody,
        List<QinIrCatchClause> catchClauses,
        List<QinIrStatement> finallyBody) implements QinIrStatement {
    public QinIrTryStatement(
            List<QinIrStatement> tryBody,
            List<QinIrCatchClause> catchClauses,
            List<QinIrStatement> finallyBody) {
        this(List.of(), tryBody, catchClauses, finallyBody);
    }

    public QinIrTryStatement {
        Objects.requireNonNull(resources, "resources cannot be null");
        Objects.requireNonNull(tryBody, "tryBody cannot be null");
        Objects.requireNonNull(catchClauses, "catchClauses cannot be null");
        Objects.requireNonNull(finallyBody, "finallyBody cannot be null");
        resources = List.copyOf(resources);
        tryBody = List.copyOf(tryBody);
        catchClauses = List.copyOf(catchClauses);
        finallyBody = List.copyOf(finallyBody);
    }
}
