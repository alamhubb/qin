package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Output of Slime AST -> JVM/CFA pipeline.
 */
public record QinCfaCompileResult(
        Path projectRoot,
        Path sourceFile,
        QinLinkedModuleSource linkedSource,
        QinEsmSemanticModel semanticModel,
        QinIrProgram irBeforeLowering,
        QinIrProgram loweredProgram,
        QinCfaProgram cfaProgram,
        String astText,
        byte[] classBytes) {
    public QinCfaCompileResult {
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(linkedSource, "linkedSource cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
        Objects.requireNonNull(irBeforeLowering, "irBeforeLowering cannot be null");
        Objects.requireNonNull(loweredProgram, "loweredProgram cannot be null");
        Objects.requireNonNull(cfaProgram, "cfaProgram cannot be null");
        astText = astText == null ? "" : astText;
        classBytes = classBytes == null ? null : classBytes.clone();
    }
}
