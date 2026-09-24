package com.qin.lang.pipeline.cfa;

import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;
import com.qin.lang.ir.QinIrClassDeclaration;

import java.util.Map;
import java.util.Objects;

/**
 * Emits JVM class bytes directly from CFA backend IR.
 */
public final class QinCfaClassFileEmitter {
    private final QinCfaJvmClassFileBackend backend = new QinCfaJvmClassFileBackend();

    public byte[] emit(QinCfaProgram cfaProgram, String className) {
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");
        Objects.requireNonNull(className, "className cannot be null");
        return backend.compileProgram(cfaProgram, className);
    }

    public byte[] emit(
            QinCfaProgram cfaProgram,
            String className,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");
        Objects.requireNonNull(className, "className cannot be null");
        return backend.compileProgram(cfaProgram, className, declarationIndex);
    }
}
