package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for multiple local declaration chaining in declaration method bodies.
 */
public final class QinDeclarationIrMultiLocalChainSmokeTestMain {
    private QinDeclarationIrMultiLocalChainSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  greet(name: string) {
                    const normalized = name.trim()
                    const prefix = "hello "
                    return prefix + normalized
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
            throw new IllegalStateException("Expected binary builtin from local chain, got " + greet.returnExpression());
        }

        System.out.println("QinDeclarationIrMultiLocalChainSmokeTestMain passed.");
    }
}
