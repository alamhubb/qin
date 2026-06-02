package com.qin.lang.frontend.adapter;

import java.util.List;

public record QinJavaSemanticModel(List<QinJavaSemanticClass> classes) {
    public QinJavaSemanticModel {
        classes = List.copyOf(classes);
    }
}
