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
        List<QinIrStatement> bodyStatements,
        List<QinIrExpression> superArguments,
        QinIrObjectLiteral runtimeFunctionDefinition,
        boolean staticMethod) {
    public QinIrMethodDeclaration(
            String name,
            QinIrTypeRef returnType,
            List<QinIrParameter> parameters,
            List<QinIrAnnotation> annotations,
            QinIrExpression returnExpression,
            QinIrObjectLiteral runtimeFunctionDefinition,
            boolean staticMethod) {
        this(name, returnType, parameters, annotations, returnExpression, List.of(), List.of(), runtimeFunctionDefinition, staticMethod);
    }

    public QinIrMethodDeclaration(
            String name,
            QinIrTypeRef returnType,
            List<QinIrParameter> parameters,
            List<QinIrAnnotation> annotations,
            QinIrExpression returnExpression,
            List<QinIrExpression> superArguments,
            QinIrObjectLiteral runtimeFunctionDefinition,
            boolean staticMethod) {
        this(name, returnType, parameters, annotations, returnExpression, List.of(), superArguments, runtimeFunctionDefinition, staticMethod);
    }

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
        Objects.requireNonNull(bodyStatements, "bodyStatements cannot be null");
        Objects.requireNonNull(superArguments, "superArguments cannot be null");
        name = name.trim();
        parameters = List.copyOf(parameters);
        annotations = List.copyOf(annotations);
        bodyStatements = List.copyOf(bodyStatements);
        superArguments = List.copyOf(superArguments);
    }
}
