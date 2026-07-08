package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrSwitchExpression;

public final class QinJavaEnumMethodSwitchCaseLowererSmokeTestMain {
    private QinJavaEnumMethodSwitchCaseLowererSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                package com.example;
                import com.qin.runtime.core.QinBuildTarget;

                class Probe {
                    QinBuildTarget selected(QinBuildTarget target) { return target; }
                    String classify(QinBuildTarget target) {
                        return switch (this.selected(target)) {
                            case JVM -> "jvm";
                            default -> "other";
                        };
                    }
                }
                """;

        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource(source);
        QinIrClassDeclaration probe = program.classDeclarations().get(0);
        QinIrMethodDeclaration classify = probe.methods().get(1);
        if (!(classify.returnExpression() instanceof QinIrSwitchExpression switchExpression)) {
            throw new IllegalStateException("Expected switch expression, got: " + classify.returnExpression());
        }
        if (!(switchExpression.cases().get(0).test() instanceof QinIrMemberAccessExpression enumCase)) {
            throw new IllegalStateException("Expected enum member switch case, got: "
                    + switchExpression.cases().get(0).test());
        }
        if (!"com.qin.runtime.core.QinBuildTarget".equals(enumCase.objectName())) {
            throw new IllegalStateException("Unexpected enum owner: " + enumCase.objectName());
        }
        if (!"JVM".equals(enumCase.propertyName())) {
            throw new IllegalStateException("Unexpected enum case: " + enumCase.propertyName());
        }
        System.out.println("QinJavaEnumMethodSwitchCaseLowererSmokeTestMain OK");
        verifySamePackageEnumMethodSelector();
    }

    private static void verifySamePackageEnumMethodSelector() {
        String source = """
                package com.subhuti.parser;

                class Probe {
                    String classify(SubhutiGrammarNode node) {
                        return switch (node.kind()) {
                            case TERMINAL -> "terminal";
                            default -> "other";
                        };
                    }
                }
                """;

        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource(source);
        QinIrClassDeclaration probe = program.classDeclarations().get(0);
        QinIrMethodDeclaration classify = probe.methods().get(0);
        if (!(classify.returnExpression() instanceof QinIrSwitchExpression switchExpression)) {
            throw new IllegalStateException("Expected same-package switch expression, got: " + classify.returnExpression());
        }
        if (!(switchExpression.cases().get(0).test() instanceof QinIrMemberAccessExpression enumCase)) {
            throw new IllegalStateException("Expected same-package enum member switch case, got: "
                    + switchExpression.cases().get(0).test());
        }
        if (!"com.subhuti.parser.SubhutiGrammarNode$Kind".equals(enumCase.objectName())) {
            throw new IllegalStateException("Unexpected same-package enum owner: " + enumCase.objectName());
        }
        if (!"TERMINAL".equals(enumCase.propertyName())) {
            throw new IllegalStateException("Unexpected same-package enum case: " + enumCase.propertyName());
        }
    }
}
