package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for declaration local const + return lowering.
 */
public final class QinDeclarationIrLocalConstReturnSmokeTestMain {
    private QinDeclarationIrLocalConstReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  greet(name: string) {
                    const normalized = name.trim()
                    return "hello " + normalized
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration"));
        QinIrMethodDeclaration greet = declaration.methods().stream()
                .filter(candidate -> "greet".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing greet method"));
        if (!(greet.returnExpression() instanceof QinIrBuiltinCallExpression builtin)
                || !"__qin_binary__".equals(builtin.methodName())) {
            throw new IllegalStateException("Expected binary builtin return, got " + greet.returnExpression());
        }

        System.out.println("QinDeclarationIrLocalConstReturnSmokeTestMain passed.");
    }
}
