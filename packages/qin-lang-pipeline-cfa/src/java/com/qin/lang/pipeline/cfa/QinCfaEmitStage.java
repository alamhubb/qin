package com.qin.lang.pipeline.cfa;

import java.util.Objects;

/**
 * Stage 3: CFA IR -> Class-File bytes.
 */
public final class QinCfaEmitStage {
    private final QinCfaClassFileEmitter classFileEmitter;

    public QinCfaEmitStage() {
        this(new QinCfaClassFileEmitter());
    }

    public QinCfaEmitStage(QinCfaClassFileEmitter classFileEmitter) {
        this.classFileEmitter = Objects.requireNonNull(classFileEmitter, "classFileEmitter cannot be null");
    }

    public byte[] emit(QinCfaIrStageResult irStageResult, String className) {
        Objects.requireNonNull(irStageResult, "irStageResult cannot be null");
        return classFileEmitter.emit(irStageResult.cfaProgram(), className);
    }
}
