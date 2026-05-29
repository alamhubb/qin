package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

/**
 * Smoke test for declaration binary expression lowering into shared Qin IR.
 */
public final class QinDeclarationIrBinaryExpressionSmokeTestMain {
    private QinDeclarationIrBinaryExpressionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class HelloService {
                  greet(name: string) {
                    return "hello " + name
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

        if (!"java.lang.String".equals(greet.returnType().binaryName())) {
            throw new IllegalStateException("Expected String return type, got " + greet.returnType());
        }
        if (!(greet.returnExpression() instanceof QinIrBuiltinCallExpression builtinCall)) {
            throw new IllegalStateException("Expected builtin call return expression, got " + greet.returnExpression());
        }
        if (!"Global".equals(builtinCall.receiverName()) || !"__qin_binary__".equals(builtinCall.methodName())) {
            throw new IllegalStateException("Unexpected builtin call target: " + builtinCall);
        }
        if (!(builtinCall.arguments().get(0) instanceof QinIrStringLiteral operator)
                || !"+".equals(operator.value())) {
            throw new IllegalStateException("Unexpected binary operator payload: " + builtinCall.arguments().get(0));
        }

        System.out.println("QinDeclarationIrBinaryExpressionSmokeTestMain passed.");
    }
}
