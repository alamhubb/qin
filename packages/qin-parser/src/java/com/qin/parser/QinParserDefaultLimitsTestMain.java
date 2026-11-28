package com.qin.parser;

import com.slime.chevrotain.SubhutiCstBuildSupport;

public final class QinParserDefaultLimitsTestMain {
    private static final int FORMER_SOURCE_LIMIT = 2_000_000;
    private static final int FORMER_LOOKAHEAD_BUDGET = 100_000;

    public static void main(String[] args) {
        acceptsSourceBeyondFormerSizeLimit();
        acceptsMoreLookaheadReadsThanFormerBudget();
    }

    private static void acceptsSourceBeyondFormerSizeLimit() {
        String source = "/*" + "x".repeat(FORMER_SOURCE_LIMIT) + "*/\nexport const value = 1;";
        var parsed = new QinParserFacade().parseSource(source);
        if (parsed.requireProgram().body().size() != 1) {
            throw new AssertionError("Expected the export declaration at the end of the large source");
        }
        System.out.println("PASS source characters=" + source.length());
    }

    private static void acceptsMoreLookaheadReadsThanFormerBudget() {
        String source = "f();\n".repeat(30_000);
        var parser = new LookaheadProbe(source);
        var cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null || parser.isParserFail() || cst.getLoc().end().index() != source.length() - 1) {
            throw new AssertionError("Expected all call statements to parse: " + parser.getDiagnostics());
        }
        int reads = parser.lookaheadReads();
        if (reads <= FORMER_LOOKAHEAD_BUDGET) {
            throw new AssertionError("Expected to exercise the former lookahead budget, reads=" + reads);
        }
        System.out.println("PASS lookahead reads=" + reads);
    }

    private static final class LookaheadProbe extends QinParser {
        private LookaheadProbe(String source) {
            super(source);
        }

        private int lookaheadReads() {
            return SubhutiCstBuildSupport.lookaheadReads(backend(true).parser());
        }
    }
}
