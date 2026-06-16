package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Path;
import java.util.Objects;

public record QinCfaModuleClassFile(
        Path sourceFile,
        int moduleIndex,
        String className,
        QinIrProgram irBeforeLowering,
        QinIrProgram loweredProgram,
        QinCfaProgram cfaProgram,
        String astText,
        byte[] classBytes) {
    public QinCfaModuleClassFile {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        if (moduleIndex < -1) {
            throw new IllegalArgumentException("moduleIndex cannot be less than -1");
        }
        if (className == null || className.isBlank()) {
            throw new IllegalArgumentException("className cannot be blank");
        }
        Objects.requireNonNull(irBeforeLowering, "irBeforeLowering cannot be null");
        Objects.requireNonNull(loweredProgram, "loweredProgram cannot be null");
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");
        astText = astText == null ? "" : astText;
        classBytes = classBytes == null ? null : classBytes.clone();
    }

    @Override
    public byte[] classBytes() {
        return classBytes == null ? null : classBytes.clone();
    }
}
