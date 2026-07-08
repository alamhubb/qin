package com.qin.lang.frontend.adapter;

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
        Objects.requireNonNull(parsed, "parsed cannot be null");
        long startNanos = System.nanoTime();
        if (!parsed.hasProgram()) {
            logPhase("import only", startNanos, "imports=" + parsed.jsImports().size());
            return importOnlyProgram(parsed);
        }
        currentSourceLength = parsed.effectiveSource() == null ? 0 : parsed.effectiveSource().length();
        loweringContext.setSourceLength(currentSourceLength);
        logPhase("top level assemble start", startNanos, "chars=" + currentSourceLength);
        QinIrProgram program = topLevelIrAssembler.assembleProgram(
                parsed.requireProgram(),
                parsed.javaImports(),
                parsed.jsImports(),
                declarationClassExportSlots,
                currentSourceLength);
        logPhase("top level assemble done", startNanos, "chars=" + currentSourceLength);
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

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinIrLowerer] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }
}
