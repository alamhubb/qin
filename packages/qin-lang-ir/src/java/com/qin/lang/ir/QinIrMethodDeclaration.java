package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * First-phase method declaration node for JVM-oriented Qin IR.
 */
public record QinIrMethodDeclaration(
        String name,
        QinIrTypeRef returnType,
        List<QinIrParameter> parameters,
        List<QinIrAnnotation> annotations,
        QinIrExpression returnExpression,
        QinIrObjectLiteral runtimeFunctionDefinition,
        boolean staticMethod) {
    public QinIrMethodDeclaration(
            String name,
            QinIrTypeRef returnType,
            List<QinIrParameter> parameters,
            List<QinIrAnnotation> annotations,
            QinIrExpression returnExpression,
            QinIrObjectLiteral runtimeFunctionDefinition) {
        this(name, returnType, parameters, annotations, returnExpression, runtimeFunctionDefinition, false);
    }

    public QinIrMethodDeclaration(
            String name,
            QinIrTypeRef returnType,
            List<QinIrParameter> parameters,
            List<QinIrAnnotation> annotations,
            QinIrExpression returnExpression) {
        this(name, returnType, parameters, annotations, returnExpression, null, false);
    }

    public QinIrMethodDeclaration {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(returnType, "returnType cannot be null");
        Objects.requireNonNull(parameters, "parameters cannot be null");
        Objects.requireNonNull(annotations, "annotations cannot be null");
        name = name.trim();
        parameters = List.copyOf(parameters);
        annotations = List.copyOf(annotations);
    }
}
