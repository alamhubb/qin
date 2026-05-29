package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for declaration if/else return lowering into conditional builtin IR.
 */
public final class QinDeclarationIrIfElseReturnSmokeTestMain {
    private QinDeclarationIrIfElseReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  choose(flag: boolean) {
                    if (flag) {
                      return "hello"
                    } else {
                      return "bye"
                    }
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
        if (!(choose.returnExpression() instanceof QinIrBuiltinCallExpression builtin)
                || !"__qin_conditional__".equals(builtin.methodName())) {
            throw new IllegalStateException("Expected conditional builtin from if/else return, got " + choose.returnExpression());
        }

        System.out.println("QinDeclarationIrIfElseReturnSmokeTestMain passed.");
    }
}
