package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * First-phase class declaration node for JVM-oriented Qin IR.
 */
public record QinIrClassDeclaration(
        String packageName,
        String simpleName,
        QinIrTypeRef superType,
        List<QinIrAnnotation> annotations,
        List<QinIrFieldDeclaration> fields,
        List<QinIrMethodDeclaration> methods,
        List<QinIrExpression> staticInitializers,
        boolean recordClass) {
    public QinIrClassDeclaration(
            String packageName,
            String simpleName,
            QinIrTypeRef superType,
            List<QinIrAnnotation> annotations,
            List<QinIrFieldDeclaration> fields,
            List<QinIrMethodDeclaration> methods,
            List<QinIrExpression> staticInitializers) {
        this(packageName, simpleName, superType, annotations, fields, methods, staticInitializers, false);
    }

    public QinIrClassDeclaration(
            String packageName,
            String simpleName,
            QinIrTypeRef superType,
            List<QinIrAnnotation> annotations,
            List<QinIrFieldDeclaration> fields,
            List<QinIrMethodDeclaration> methods) {
        this(packageName, simpleName, superType, annotations, fields, methods, List.of(), false);
    }

    public QinIrClassDeclaration {
        if (simpleName == null || simpleName.isBlank()) {
            throw new IllegalArgumentException("simpleName cannot be blank");
        }
        Objects.requireNonNull(annotations, "annotations cannot be null");
        Objects.requireNonNull(fields, "fields cannot be null");
        Objects.requireNonNull(methods, "methods cannot be null");
        Objects.requireNonNull(staticInitializers, "staticInitializers cannot be null");
        if (packageName != null && packageName.isBlank()) {
            packageName = null;
        }
        simpleName = simpleName.trim();
        annotations = List.copyOf(annotations);
        fields = List.copyOf(fields);
        methods = List.copyOf(methods);
        staticInitializers = List.copyOf(staticInitializers);
    }

    public String binaryName() {
        return packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
    }
}
