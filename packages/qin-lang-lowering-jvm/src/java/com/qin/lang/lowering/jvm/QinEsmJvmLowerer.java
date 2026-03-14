package com.qin.lang.lowering.jvm;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

/**
 * Target-specific lowering contract for JVM backend.
 */
public interface QinEsmJvmLowerer {
    QinIrProgram lower(
            QinIrProgram program,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context);
}
