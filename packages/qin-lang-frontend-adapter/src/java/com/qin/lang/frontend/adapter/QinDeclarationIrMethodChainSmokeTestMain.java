package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for fixed method-call receivers such as token.getClass().getField(...).
 */
public final class QinDeclarationIrMethodChainSmokeTestMain {
    private QinDeclarationIrMethodChainSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                class Probe {
                  tokenName(token: any) {
                    let field = token.getClass().getField("tokenName")
                    return field.get(token)
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration"));
        QinIrMethodDeclaration tokenName = declaration.methods().stream()
                .filter(candidate -> "tokenName".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing tokenName method"));
        if (!(tokenName.returnExpression() instanceof QinIrInstanceMethodCallExpression call)
                || !"get".equals(call.methodName())) {
            throw new IllegalStateException("Expected final method call expression, got "
                    + tokenName.returnExpression());
        }

        System.out.println("QinDeclarationIrMethodChainSmokeTestMain passed.");
    }
}
