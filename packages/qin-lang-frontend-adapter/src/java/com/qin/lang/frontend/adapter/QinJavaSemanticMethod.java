package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeRef;
import java.util.List;

public record QinJavaSemanticMethod(
        String name,
        QinIrTypeRef returnType,
        List<QinJavaSemanticParameter> parameters,
        QinIrTypeRef returnExpressionType) {
    public QinJavaSemanticMethod {
        parameters = List.copyOf(parameters);
    }
}
