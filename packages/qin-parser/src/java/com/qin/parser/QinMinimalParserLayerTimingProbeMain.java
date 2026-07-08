package com.qin.parser;

import com.slime.ast.nodes.misc.Program;
import com.subhuti.aop.ByteBuddyParserFactory;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;

public final class QinMinimalParserLayerTimingProbeMain {
    private static final String SOURCE = "export const result = 1\n";

    private QinMinimalParserLayerTimingProbeMain() {
    }

    public static void main(String[] args) {
        String mode = args.length == 0 ? "enhanced" : args[0];
        runRound(mode, "cold");
        runRound(mode, "warm");
        System.out.println("QinMinimalParserLayerTimingProbeMain OK");
    }

    private static void runRound(String mode, String label) {
        long createStarted = System.nanoTime();
        QinParser parser = createParser(mode);
        parser.cache(true);
        long createMs = elapsedMs(createStarted);

        long cstStarted = System.nanoTime();
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        long cstMs = elapsedMs(cstStarted);
        if (cst == null) {
            throw new IllegalStateException("Expected CST for " + label);
        }

        long astStarted = System.nanoTime();
        Program ast = new QinProgramCstToAst().createProgramAst(cst);
        long astMs = elapsedMs(astStarted);
        if (ast == null) {
            throw new IllegalStateException("Expected AST Program for " + label);
        }

        System.out.println("QinMinimalParserLayerTimingProbeMain " + mode + "-" + label
                + " createMs=" + createMs
                + " cstMs=" + cstMs
                + " astMs=" + astMs);
    }

    private static QinParser createParser(String mode) {
        if ("raw".equals(mode)) {
            return ByteBuddyParserFactory.createRaw(QinParser.class, SOURCE);
        }
        if ("static".equals(mode)) {
            return QinParserStaticEnhanced.create(SOURCE);
        }
        if ("enhanced".equals(mode)) {
            return SubhutiParser.create(QinParser.class, SOURCE);
        }
        throw new IllegalArgumentException("Unsupported mode: " + mode);
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }
}
