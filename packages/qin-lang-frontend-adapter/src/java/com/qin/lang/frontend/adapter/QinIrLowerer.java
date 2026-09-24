package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.parser.QinParsedSource;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Qin-owned lowering façade from parsed frontend output to Qin IR.
 *
 * <p>This is the long-term semantic lowering boundary. During migration it
 * delegates to the existing Slime-based adapter implementation.
 */
public final class QinIrLowerer extends QinSlimeIrLoweringSupport {
    private final QinLegacySlimeIrLowerer legacyLowerer = new QinLegacySlimeIrLowerer();
    private final QinTopLevelIrAssembler topLevelIrAssembler = new QinTopLevelIrAssembler(legacyLowerer);

    public QinIrProgram lowerParsedSource(QinParsedSource parsed) {
        return lowerParsedSource(parsed, Map.of());
    }

    public QinIrProgram lowerParsedSource(QinParsedSource parsed, Map<String, String> declarationClassExportSlots) {
        return lowerParsedSource(parsed, declarationClassExportSlots, Map.of());
    }

    public QinIrProgram lowerParsedSource(
            QinParsedSource parsed,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> staticExportSlotValues) {
        Objects.requireNonNull(parsed, "parsed cannot be null");
        long startNanos = System.nanoTime();
        if (!parsed.hasProgram()) {
            return importOnlyProgram(parsed);
        }
        currentSourceText = parsed.effectiveSource() == null ? "" : parsed.effectiveSource();
        currentSourceLength = currentSourceText.length();
        loweringContext.setSourceLength(currentSourceLength);
        QinIrProgram program = topLevelIrAssembler.assembleProgram(
                parsed.requireProgram(),
                parsed.javaImports(),
                parsed.jsImports(),
                declarationClassExportSlots,
                staticExportSlotValues,
                currentSourceLength,
                currentSourceText);
        return program;
    }

    public String renderParsedAst(QinParsedSource parsed) {
        Objects.requireNonNull(parsed, "parsed cannot be null");
        if (!parsed.hasProgram()) {
            return "Program(import-only)";
        }
        return legacyLowerer.renderLegacyParsedAst(parsed);
    }

    public QinIrProgram emptyProgram() {
        return QinSlimeFrontendAdapter.createEmptyProgram();
    }

    public QinIrProgram importOnlyProgram(QinParsedSource parsed) {
        return QinSlimeFrontendAdapter.createImportOnlyProgram(parsed);
    }

}
