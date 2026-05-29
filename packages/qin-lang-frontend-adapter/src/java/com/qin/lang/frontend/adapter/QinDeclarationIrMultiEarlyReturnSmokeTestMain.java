package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for multiple early-return if statements lowering.
 */
public final class QinDeclarationIrMultiEarlyReturnSmokeTestMain {
    private QinDeclarationIrMultiEarlyReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  choose(flag1: boolean, flag2: boolean) {
                    if (flag1) return "one"
                    if (flag2) return "two"
                    return "other"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration"));
        QinIrMethodDeclaration choose = declaration.methods().stream()
                .filter(candidate -> "choose".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing choose method"));
        if (!(choose.returnExpression() instanceof QinIrBuiltinCallExpression outer)
                || !"__qin_conditional__".equals(outer.methodName())) {
            throw new IllegalStateException("Expected outer conditional builtin, got " + choose.returnExpression());
        }
        if (!(outer.arguments().get(2) instanceof QinIrBuiltinCallExpression inner)
                || !"__qin_conditional__".equals(inner.methodName())) {
            throw new IllegalStateException("Expected nested conditional builtin in fallthrough, got " + outer.arguments().get(2));
        }

        System.out.println("QinDeclarationIrMultiEarlyReturnSmokeTestMain passed.");
    }
}
