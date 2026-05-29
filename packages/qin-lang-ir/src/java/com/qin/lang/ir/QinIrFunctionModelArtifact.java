package com.qin.lang.ir;

import java.util.Objects;

/**
 * Externalized runtime function model.
 *
 * <p>Large JS/npm packages can generate very large interpreted function AST
 * models. Keeping those models as IR artifacts lets the JVM backend emit a
 * small {@code astRef} into generated classes while the runtime host registers
 * the real model beside the class artifact.
 */
public record QinIrFunctionModelArtifact(
        String id,
        QinIrObjectLiteral ast) {
    public QinIrFunctionModelArtifact {
        Objects.requireNonNull(id, "id cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        Objects.requireNonNull(ast, "ast cannot be null");
    }
}
