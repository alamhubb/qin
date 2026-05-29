package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Unified compiler facade:
 * Slime AST -> ESM sema -> JVM lowering -> Class-File API bytecode.
 */
public final class QinSlimeCfaCompiler implements QinCfaPipeline {
    private static final int LARGE_LINKED_CLASS_SOURCE_LIMIT = 2_000_000;
    private static final boolean ALLOW_LARGE_LINKED_CLASS =
            Boolean.getBoolean("qin.allowLargeLinkedClass");

    private final QinCfaSemanticStage semanticStage;
    private final QinCfaIrStage irStage;
    private final QinCfaEmitStage emitStage;

    public QinSlimeCfaCompiler() {
        this(new QinCfaSemanticStage(), new QinCfaIrStage(), new QinCfaEmitStage());
    }

    public QinSlimeCfaCompiler(
            QinCfaSemanticStage semanticStage,
            QinCfaIrStage irStage,
            QinCfaEmitStage emitStage) {
        this.semanticStage = Objects.requireNonNull(semanticStage, "semanticStage cannot be null");
        this.irStage = Objects.requireNonNull(irStage, "irStage cannot be null");
        this.emitStage = Objects.requireNonNull(emitStage, "emitStage cannot be null");
    }

    public QinCfaCompileResult compile(QinCfaCompileRequest request) throws Exception {
        Objects.requireNonNull(request, "request cannot be null");
        long startNanos = System.nanoTime();

        Path sourceFile = requireFile(request.sourceFile());
        Path projectRoot = request.projectRoot().toAbsolutePath().normalize();

        logPhase("semantic start", startNanos, sourceFile.toString());
        QinCfaSemanticStageResult semanticStageResult = semanticStage.execute(sourceFile, projectRoot);
        logPhase("semantic done", startNanos, sourceFile.toString());
        validateLinkedClassBoundary(request, semanticStageResult);
        logPhase("ir start", startNanos, sourceFile.toString());
        QinCfaIrStageResult irStageResult = irStage.execute(semanticStageResult);
        logPhase("ir done", startNanos, sourceFile.toString());

        QinIrProgram irBeforeLowering = irStageResult.irBeforeLowering();
        QinIrProgram loweredProgram = irStageResult.loweredProgram();
        QinCfaProgram cfaProgram = irStageResult.cfaProgram();
        String astText = irStageResult.astText();

        byte[] classBytes = null;
        if (request.emitClassBytes()) {
            logPhase("emit start", startNanos, request.className());
            classBytes = emitStage.emit(irStageResult, request.className());
            logPhase("emit done", startNanos, request.className());
        }

        return new QinCfaCompileResult(
                projectRoot,
                sourceFile,
                semanticStageResult.linkedSource(),
                semanticStageResult.semanticModel(),
                irBeforeLowering,
                loweredProgram,
                cfaProgram,
                astText,
                classBytes);
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinSlimeCfaCompiler] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    private void validateLinkedClassBoundary(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult) {
        if (!request.emitClassBytes() || ALLOW_LARGE_LINKED_CLASS) {
            return;
        }
        int sourceLength = semanticStageResult.linkedSource().source().length();
        if (sourceLength <= LARGE_LINKED_CLASS_SOURCE_LIMIT) {
            return;
        }
        int moduleCount = semanticStageResult.linkedSource().modules().size();
        throw new IllegalStateException(
                "QJS9001 linked source is too large for the current single-class JVM backend: "
                        + sourceLength + " chars across " + moduleCount + " modules. "
                        + "This is a compiler architecture boundary, not a JS syntax fallback. "
                        + "Move this package path to module-level/class-cache compilation before treating it as supported. "
                        + "For compiler diagnostics only, rerun with -Dqin.allowLargeLinkedClass=true.");
    }

    private Path requireFile(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("source file cannot be null");
        }
        Path normalized = file.toAbsolutePath().normalize();
        if (!Files.exists(normalized) || !Files.isRegularFile(normalized)) {
            throw new IllegalArgumentException("Missing file: " + normalized);
        }
        return normalized;
    }
}
