package com.qin.runtime.core;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.util.Objects;

/**
 * Frontend compile output that includes IR plus ESM semantic metadata.
 */
public record QinFrontendCompileResult(
        QinIrProgram program,
        QinLinkedSource linkedSource,
        QinEsmSemanticModel semanticModel) {
    public QinFrontendCompileResult {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(linkedSource, "linkedSource cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
    }
}
