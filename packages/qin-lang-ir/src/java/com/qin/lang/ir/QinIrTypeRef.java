package com.qin.lang.ir;

import java.util.Objects;

/**
 * First-phase JVM-oriented type reference for declaration IR.
 */
public record QinIrTypeRef(
        QinIrTypeKind kind,
        String binaryName) {
    public QinIrTypeRef {
        Objects.requireNonNull(kind, "kind cannot be null");
        if (kind == QinIrTypeKind.CLASS || kind == QinIrTypeKind.STRING) {
            if (binaryName == null || binaryName.isBlank()) {
                throw new IllegalArgumentException("binaryName cannot be blank for reference-like types");
            }
            binaryName = binaryName.trim();
        } else if (binaryName != null && !binaryName.isBlank()) {
            binaryName = binaryName.trim();
        }
    }

    public static QinIrTypeRef voidType() {
        return new QinIrTypeRef(QinIrTypeKind.VOID, null);
    }

    public static QinIrTypeRef booleanType() {
        return new QinIrTypeRef(QinIrTypeKind.BOOLEAN, null);
    }

    public static QinIrTypeRef intType() {
        return new QinIrTypeRef(QinIrTypeKind.INT, null);
    }

    public static QinIrTypeRef doubleType() {
        return new QinIrTypeRef(QinIrTypeKind.DOUBLE, null);
    }

    public static QinIrTypeRef stringType() {
        return new QinIrTypeRef(QinIrTypeKind.STRING, "java.lang.String");
    }

    public static QinIrTypeRef classType(String binaryName) {
        return new QinIrTypeRef(QinIrTypeKind.CLASS, binaryName);
    }
}
