package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for declaration object literal lowering into shared Qin IR.
 */
public final class QinDeclarationIrObjectLiteralSmokeTestMain {
    private QinDeclarationIrObjectLiteralSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloController {
                  detail() {
                    return { message: "hello", ok: true }
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration"));
        QinIrMethodDeclaration detail = declaration.methods().stream()
                .filter(candidate -> "detail".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing detail method"));

        if (!"java.util.Map".equals(detail.returnType().binaryName())) {
            throw new IllegalStateException("Expected Map return type, got " + detail.returnType());
        }
        if (!(detail.returnExpression() instanceof QinIrObjectLiteral objectLiteral)) {
            throw new IllegalStateException("Expected object literal return expression, got " + detail.returnExpression());
        }
        if (objectLiteral.properties().size() != 2) {
            throw new IllegalStateException("Expected two object properties, got " + objectLiteral.properties().size());
        }

        System.out.println("QinDeclarationIrObjectLiteralSmokeTestMain passed.");
    }
}
