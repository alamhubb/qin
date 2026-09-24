package com.qin.parser;

import com.subhuti.struct.SubhutiCst;

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
        SubhutiCst cst = parseProgram("const text = 'x'");
        require(!containsNode(cst, "QinObjectDeclarationBody"),
                "const must not parse through Qin object branch");
    }

    private static void probesConstProgram() {
        SubhutiCst cst = parseProgram("const text = 'x'");
        require(cst != null, "const program should produce CST");
    }

    private static void probesTypeDeclaration() {
        SubhutiCst cst = parseProgram("type Box = object");
        require(!containsNode(cst, "QinObjectDeclarationBody"),
                "type keyword object must stay in TypeScript type grammar");
    }

    private static void probesObjectDeclaration() {
        SubhutiCst cst = parseProgram("object Store {}");
        require(containsNode(cst, "QinObjectDeclarationBody"),
                "object should parse through Qin object declaration");
    }

    private static SubhutiCst parseProgram(String source) {
        QinParser parser = QinParserStaticEnhanced.create(source);
        parser.cache(true);
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        require(!parser.isParserFail(), "program should parse: " + source + " stats=" + parser.getOrPredictionStats());
        require(cst != null, "program should produce CST: " + source);
        return cst;
    }

    private static boolean containsNode(SubhutiCst cst, String name) {
        if (cst == null) {
            return false;
        }
        if (name.equals(cst.getName())) {
            return true;
        }
        if (cst.getChildren() != null) {
            for (SubhutiCst child : cst.getChildren()) {
                if (containsNode(child, name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
