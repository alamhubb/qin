package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

public record QinIrCatchClause(
        String parameterName,
        QinIrTypeRef parameterType,
        List<QinIrStatement> body) {
    public QinIrCatchClause {
        if (parameterName == null || parameterName.isBlank()) {
            throw new IllegalArgumentException("parameterName cannot be blank");
        }
        Objects.requireNonNull(parameterType, "parameterType cannot be null");
        Objects.requireNonNull(body, "body cannot be null");
        parameterName = parameterName.trim();
        body = List.copyOf(body);
    }
}
