package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for Qin-owned Subhuti decorator compile-time lowering.
 */
public final class QinDeclarationIrSubhutiDecoratorSmokeTestMain {
    private QinDeclarationIrSubhutiDecoratorSmokeTestMain() {
    }

    public static void main(String[] args) {
        String text = """
                import { Subhuti, SubhutiRule } from "subhuti"

                @Subhuti
                class ProbeParser {
                  @SubhutiRule
                  Rule(): number {
                    return 1
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException("Expected one static class declaration, got "
                    + program.classDeclarations().size());
        }
        QinIrClassDeclaration parser = program.classDeclarations().get(0);
        if (!"ProbeParser".equals(parser.simpleName())) {
            throw new IllegalStateException("Unexpected class name: " + parser.simpleName());
        }
        if (!parser.annotations().isEmpty()) {
            throw new IllegalStateException("@Subhuti should lower as a compile-time class marker, got "
                    + parser.annotations());
        }
        if (parser.methods().size() != 1) {
            throw new IllegalStateException("Expected one parser rule method, got " + parser.methods().size());
        }
        QinIrMethodDeclaration rule = parser.methods().get(0);
        if (!"Rule".equals(rule.name())) {
            throw new IllegalStateException("Unexpected method name: " + rule.name());
        }
        if (rule.annotations().size() != 1) {
            throw new IllegalStateException("Expected one rule annotation, got " + rule.annotations().size());
        }
        QinIrAnnotation annotation = rule.annotations().get(0);
        if (!"com.subhuti.parser.SubhutiRule".equals(annotation.ownerBinaryName())) {
            throw new IllegalStateException("Unexpected rule annotation: " + annotation.ownerBinaryName());
        }

        System.out.println("QinDeclarationIrSubhutiDecoratorSmokeTestMain passed.");
    }
}
