package com.qin.parser;

import com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams;

public final class QinParserDeclarationGateProbeMain {
    private QinParserDeclarationGateProbeMain() {
    }

    public static void main(String[] args) {
        probesConstDeclaration();
        probesConstProgram();
        probesTypeDeclaration();
        probesObjectDeclaration();
        System.out.println("QinParserDeclarationGateProbeMain OK");
    }

    private static void probesConstDeclaration() {
        ProbeParser parser = new ProbeParser("const text = 'x'");
        parser.cache(true);
        parser.Declaration(new DeclarationParams(false, true, false));
        require(!parser.isParserFail(), "const should parse through standard declaration");
        require(parser.qinObjectCalls == 0, "const must not execute Qin object branch");
        require(parser.standardGateCalls == 1 && parser.standardGateAllows == 1,
                "const should pass standard declaration gate: " + parser.summary());
    }

    private static void probesConstProgram() {
        ProbeParser parser = new ProbeParser("const text = 'x'");
        parser.cache(true);
        try {
            parser.Program(QinParser.SourceType.MODULE);
        } catch (RuntimeException error) {
            throw new AssertionError("const program failed before consuming input: " + parser.summary(), error);
        }
        require(!parser.isParserFail(), "const program should parse through standard declaration: " + parser.summary());
        require(parser.qinObjectCalls == 0, "const program must not execute Qin object branch: " + parser.summary());
        require(parser.standardGateAllows == 1, "const program should pass standard declaration gate: " + parser.summary());
    }

    private static void probesTypeDeclaration() {
        ProbeParser parser = new ProbeParser("type Box = object");
        parser.cache(true);
        parser.Declaration(new DeclarationParams(false, true, false));
        require(!parser.isParserFail(), "type should parse through standard/TS declaration");
        require(parser.qinObjectCalls == 0, "type must not execute Qin object branch");
        require(parser.standardGateAllows == 1, "type should pass non-Qin gate: " + parser.summary());
    }

    private static void probesObjectDeclaration() {
        ProbeParser parser = new ProbeParser("object Store {}");
        parser.cache(true);
        parser.Declaration(new DeclarationParams(false, true, false));
        require(!parser.isParserFail(), "object should parse through Qin object declaration");
        require(parser.qinObjectCalls == 1, "object should execute Qin object branch");
    }

    public static class ProbeParser extends QinParserStaticEnhanced {
        int qinObjectGateCalls;
        int qinObjectGateAllows;
        int standardGateCalls;
        int standardGateAllows;
        int qinObjectCalls;

        public ProbeParser(String sourceCode) {
            super(sourceCode);
        }

        @Override
        protected boolean canStartQinObjectDeclaration() {
            qinObjectGateCalls++;
            boolean allowed = super.canStartQinObjectDeclaration();
            if (allowed) {
                qinObjectGateAllows++;
            }
            return allowed;
        }

        @Override
        protected boolean canStartNonQinDeclaration() {
            standardGateCalls++;
            boolean allowed = super.canStartNonQinDeclaration();
            if (allowed) {
                standardGateAllows++;
            }
            return allowed;
        }

        @Override
        public void QinObjectDeclaration(DeclarationParams params) {
            qinObjectCalls++;
            super.QinObjectDeclaration(params);
        }

        String summary() {
            return "qinObjectGateCalls=" + qinObjectGateCalls
                    + ", qinObjectGateAllows=" + qinObjectGateAllows
                    + ", standardGateCalls=" + standardGateCalls
                    + ", standardGateAllows=" + standardGateAllows
                    + ", qinObjectCalls=" + qinObjectCalls
                    + ", stats=" + getOrPredictionStats();
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
