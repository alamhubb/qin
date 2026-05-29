package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserFacade;

/**
 * New semantic frontend/lowering entry.
 *
 * <p>This class provides a Qin-owned name for the frontend-lowering phase while
 * old {@link QinSlimeFrontendAdapter} remains as a compatibility entrypoint.
 */
public final class QinFrontendLowerer {
    private final QinParserFacade parserFacade = new QinParserFacade();
    private final QinIrLowerer irLowerer = new QinIrLowerer();

    public QinIrProgram lowerSource(String source) {
        long startNanos = System.nanoTime();
        logPhase("parse start", startNanos, "chars=" + (source == null ? 0 : source.length()));
        QinParsedSource parsed = parserFacade.parseSource(source);
        logPhase("parse done", startNanos, "hasProgram=" + parsed.hasProgram());
        logPhase("ir lower start", startNanos, "chars=" + (source == null ? 0 : source.length()));
        QinIrProgram program = irLowerer.lowerParsedSource(parsed);
        logPhase("ir lower done", startNanos, "chars=" + (source == null ? 0 : source.length()));
        return program;
    }

    public String parseAst(String source) {
        QinParsedSource parsed = parserFacade.parseSource(source);
        return irLowerer.renderParsedAst(parsed);
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinFrontendLowerer] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }
}
