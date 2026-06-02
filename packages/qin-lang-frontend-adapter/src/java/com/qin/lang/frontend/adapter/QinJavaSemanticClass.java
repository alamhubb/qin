package com.qin.lang.frontend.adapter;

import java.util.List;

public record QinJavaSemanticClass(
        String packageName,
        String simpleName,
        String binaryName,
        List<QinJavaSemanticField> fields,
        List<QinJavaSemanticMethod> methods) {
    public QinJavaSemanticClass {
        fields = List.copyOf(fields);
        methods = List.copyOf(methods);
    }
}
