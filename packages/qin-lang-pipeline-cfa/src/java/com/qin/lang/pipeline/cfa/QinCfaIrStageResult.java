package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.util.Objects;

/**
 * Output of frontend AST->IR + lowering + CFA IR stage.
 */
public record QinCfaIrStageResult(
        QinIrProgram irBeforeLowering,
        QinIrProgram loweredProgram,
        QinCfaProgram cfaProgram,
        String astText) {
    public QinCfaIrStageResult {
        Objects.requireNonNull(irBeforeLowering, "irBeforeLowering cannot be null");
        Objects.requireNonNull(loweredProgram, "loweredProgram cannot be null");
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");
        astText = astText == null ? "" : astText;
    }
}
