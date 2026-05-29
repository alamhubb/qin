package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for declaration local const + if/else return lowering.
 */
public final class QinDeclarationIrLocalConstIfElseSmokeTestMain {
    private QinDeclarationIrLocalConstIfElseSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloController {
                  detail(flag: boolean) {
                    const message = "hello"
                    if (flag) {
                      return { message: message, ok: true }
                    } else {
                      return { message: message, ok: false }
                    }
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
            throw new IllegalStateException("Expected conditional builtin return, got " + detail.returnExpression());
        }

        System.out.println("QinDeclarationIrLocalConstIfElseSmokeTestMain passed.");
    }
}
