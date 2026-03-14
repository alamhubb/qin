package com.qin.lang.lowering.jvm;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.util.Objects;

/**
 * No-op lowering implementation for initial integration.
 */
public final class QinNoOpEsmJvmLowerer implements QinEsmJvmLowerer {
    @Override
    public QinIrProgram lower(
            QinIrProgram program,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
        Objects.requireNonNull(context, "context cannot be null");
        return program;
    }
}
