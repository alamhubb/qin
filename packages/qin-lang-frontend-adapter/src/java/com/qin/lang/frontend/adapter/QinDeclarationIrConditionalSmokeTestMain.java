package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for declaration logical and conditional lowering into shared Qin IR.
 */
public final class QinDeclarationIrConditionalSmokeTestMain {
    private QinDeclarationIrConditionalSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  choose(flag: boolean) {
                    return flag ? "hello" : "bye"
                  }

                  maybe(flag: boolean, name: string) {
                    return flag && name
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
        if (!(choose.returnExpression() instanceof QinIrBuiltinCallExpression chooseBuiltin)
                || !"__qin_conditional__".equals(chooseBuiltin.methodName())) {
            throw new IllegalStateException("Expected conditional builtin, got " + choose.returnExpression());
        }

        QinIrMethodDeclaration maybe = declaration.methods().stream()
                .filter(candidate -> "maybe".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing maybe method"));
        if (!(maybe.returnExpression() instanceof QinIrBuiltinCallExpression maybeBuiltin)
                || !"__qin_logical__".equals(maybeBuiltin.methodName())) {
            throw new IllegalStateException("Expected logical builtin, got " + maybe.returnExpression());
        }

        System.out.println("QinDeclarationIrConditionalSmokeTestMain passed.");
    }
}
