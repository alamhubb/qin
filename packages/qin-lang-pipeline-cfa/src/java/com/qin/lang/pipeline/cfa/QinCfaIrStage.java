package com.qin.lang.pipeline.cfa;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.lowering.jvm.QinEsmJvmLoweringContext;
import com.qin.lang.lowering.jvm.QinStrictEsmJvmLowerer;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.util.Objects;

/**
 * Stage 2: Slime AST -> QinIr -> lowered QinIr -> QinCfaProgram.
 */
public final class QinCfaIrStage {
    private static final int FULL_AST_RENDER_SOURCE_LIMIT = 32_000;

    private final QinFrontendLowerer frontendLowerer;
    private final QinStrictEsmJvmLowerer lowerer;
    private final QinIrToCfaIrLowerer cfaIrLowerer;

    public QinCfaIrStage() {
        this(new QinFrontendLowerer(), new QinStrictEsmJvmLowerer(), new QinIrToCfaIrLowerer());
    }

    public QinCfaIrStage(
            QinFrontendLowerer frontendLowerer,
            QinStrictEsmJvmLowerer lowerer,
            QinIrToCfaIrLowerer cfaIrLowerer) {
        this.frontendLowerer = Objects.requireNonNull(frontendLowerer, "frontendLowerer cannot be null");
        this.lowerer = Objects.requireNonNull(lowerer, "lowerer cannot be null");
        this.cfaIrLowerer = Objects.requireNonNull(cfaIrLowerer, "cfaIrLowerer cannot be null");
    }

    public QinCfaIrStageResult execute(QinCfaSemanticStageResult semanticStageResult) {
        Objects.requireNonNull(semanticStageResult, "semanticStageResult cannot be null");

        String linkedSource = semanticStageResult.linkedSource().source();
        QinIrProgram irBeforeLowering = frontendLowerer.lowerSource(linkedSource);
        String astText = renderAstTextForSnapshot(linkedSource);

        QinIrProgram loweredProgram = lowerer.lower(
                irBeforeLowering,
                semanticStageResult.semanticModel(),
                new QinEsmJvmLoweringContext(
                        semanticStageResult.linkedSource().entryFile(),
                        semanticStageResult.linkedSource().modules()));
        QinCfaProgram cfaProgram = cfaIrLowerer.lower(loweredProgram);
        return new QinCfaIrStageResult(irBeforeLowering, loweredProgram, cfaProgram, astText);
    }

    private String renderAstTextForSnapshot(String source) {
        if (source == null || source.length() <= FULL_AST_RENDER_SOURCE_LIMIT) {
            return frontendLowerer.parseAst(source == null ? "" : source);
        }
        return "Program(AST snapshot skipped; sourceLength=" + source.length()
                + ", limit=" + FULL_AST_RENDER_SOURCE_LIMIT + ")";
    }
}
