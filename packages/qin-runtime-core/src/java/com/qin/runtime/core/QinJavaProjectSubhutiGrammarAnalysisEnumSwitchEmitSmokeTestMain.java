package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrSwitchExpression;
import com.slime.java.ast.JavaCstToAst;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSubhutiGrammarAnalysisEnumSwitchEmitSmokeTestMain {
    private QinJavaProjectSubhutiGrammarAnalysisEnumSwitchEmitSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("D:/project/qkyproject/qinall/qin").toAbsolutePath().normalize();
        Path outputRoot = Files.createTempDirectory("qin-subhuti-grammar-analysis-emit-");
        List<Path> sourceRoots = List.of(
                root.resolve("../slime/java-slime/subhuti-java/src/main/java").normalize());
        verifyLoweredIr(root);

        new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.subhuti.parser.SubhutiGrammarAnalysis",
                        List.of(),
                        outputRoot);

        Path generated = outputRoot.resolve("com")
                .resolve("subhuti")
                .resolve("parser")
                .resolve("SubhutiGrammarAnalysis.ts");
        String source = Files.readString(generated, StandardCharsets.UTF_8);
        if (source.contains("case TERMINAL:")) {
            throw new IllegalStateException("Generated enum switch case must not be a bare identifier: " + generated);
        }
        if (!source.contains(".__qin_field_TERMINAL")) {
            throw new IllegalStateException("Generated enum switch case must reference enum field: " + generated);
        }
        System.out.println("QinJavaProjectSubhutiGrammarAnalysisEnumSwitchEmitSmokeTestMain OK");
    }

    private static void verifyLoweredIr(Path root) throws Exception {
        Path subhutiRoot = root.resolve("../slime/java-slime/subhuti-java/src/main/java").normalize();
        QinIrProgram program = new QinJavaAstIrLowerer().lowerPrograms(List.of(
                JavaCstToAst.parse(Files.readString(
                        subhutiRoot.resolve("com/subhuti/parser/SubhutiGrammarNode.java"),
                        StandardCharsets.UTF_8)),
                JavaCstToAst.parse(Files.readString(
                        subhutiRoot.resolve("com/subhuti/parser/SubhutiGrammarAnalysis.java"),
                        StandardCharsets.UTF_8))));
        QinIrClassDeclaration analysis = program.classDeclarations().stream()
                .filter(type -> "com.subhuti.parser.SubhutiGrammarAnalysis".equals(type.binaryName()))
                .findFirst()
                .orElseThrow();
        QinIrMethodDeclaration analyze = analysis.methods().stream()
                .filter(method -> "analyze".equals(method.name()))
                .findFirst()
                .orElseThrow();
        QinIrSwitchExpression switchExpression = analyze.bodyStatements().stream()
                .filter(QinIrReturnStatement.class::isInstance)
                .map(QinIrReturnStatement.class::cast)
                .map(QinIrReturnStatement::value)
                .filter(QinIrSwitchExpression.class::isInstance)
                .map(QinIrSwitchExpression.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Expected SubhutiGrammarAnalysis.analyze switch expression IR"));
        if (!(switchExpression.cases().get(0).test() instanceof QinIrMemberAccessExpression enumCase)) {
            throw new IllegalStateException("Expected lowered enum member IR, got: "
                    + switchExpression.cases().get(0).test()
                    + "; parameters=" + analyze.parameters());
        }
        if (!"com.subhuti.parser.SubhutiGrammarNode$Kind".equals(enumCase.objectName())) {
            throw new IllegalStateException("Unexpected lowered enum owner: " + enumCase.objectName());
        }
    }
}
