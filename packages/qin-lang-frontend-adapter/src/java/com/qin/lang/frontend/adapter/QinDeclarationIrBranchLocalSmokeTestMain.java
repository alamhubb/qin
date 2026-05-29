package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for branch-local declarations inside declaration method bodies.
 */
public final class QinDeclarationIrBranchLocalSmokeTestMain {
    private QinDeclarationIrBranchLocalSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloController {
                  detail(flag: boolean) {
                    if (flag) {
                      const message = "hello"
                      return { message: message, ok: true }
                    }
                    const fallback = "bye"
                    return { message: fallback, ok: false }
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
        if (!(detail.returnExpression() instanceof QinIrBuiltinCallExpression builtin)
                || !"__qin_conditional__".equals(builtin.methodName())) {
            throw new IllegalStateException("Expected conditional builtin from branch-local method body, got " + detail.returnExpression());
        }
        if (!(builtin.arguments().get(1) instanceof QinIrObjectLiteral)
                || !(builtin.arguments().get(2) instanceof QinIrObjectLiteral)) {
            throw new IllegalStateException("Expected object literal branches, got " + builtin.arguments());
        }

        System.out.println("QinDeclarationIrBranchLocalSmokeTestMain passed.");
    }
}
