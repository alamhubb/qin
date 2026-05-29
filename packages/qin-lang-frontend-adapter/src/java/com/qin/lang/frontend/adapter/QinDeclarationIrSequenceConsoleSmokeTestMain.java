package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStringLiteral;

/**
 * Smoke test for declaration method body lowering with expression statement + return.
 */
public final class QinDeclarationIrSequenceConsoleSmokeTestMain {
    private QinDeclarationIrSequenceConsoleSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                class HelloService {
                  message() {
                    console.log("before")
                    return "hello"
                  }
                }
                """;

        QinIrClassDeclaration declaration = new QinFrontendLowerer()
                .lowerSource(source)
                .classDeclarations()
                .get(0);
        QinIrMethodDeclaration method = declaration.methods().get(0);
        if (!(method.returnExpression() instanceof QinIrSequenceExpression sequence)) {
            throw new IllegalStateException("Expected sequence expression, got " + method.returnExpression());
        }
        if (sequence.leadingExpressions().size() != 1
                || !(sequence.leadingExpressions().get(0) instanceof QinIrBuiltinCallExpression builtin)
                || !"console".equals(builtin.receiverName())
                || !"log".equals(builtin.methodName())) {
            throw new IllegalStateException("Expected console.log builtin leading expression, got " + sequence.leadingExpressions());
        }
        if (!(sequence.resultExpression() instanceof QinIrStringLiteral stringLiteral)
                || !"hello".equals(stringLiteral.value())) {
            throw new IllegalStateException("Expected string result expression, got " + sequence.resultExpression());
        }

        System.out.println("QinDeclarationIrSequenceConsoleSmokeTestMain passed.");
    }
}
