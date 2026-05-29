package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrTypeKind;

/**
 * Smoke test for declaration builtin semantic return type inference.
 */
public final class QinDeclarationIrBuiltinSemanticTypeSmokeTestMain {
    private QinDeclarationIrBuiltinSemanticTypeSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                class BuiltinDemo {
                  jsonText() {
                    return JSON.stringify({ ok: true })
                  }

                  randomValue() {
                    return Math.random()
                  }
                }
                """;

        QinIrClassDeclaration declaration = new QinFrontendLowerer()
                .lowerSource(source)
                .classDeclarations()
                .get(0);
        QinIrMethodDeclaration jsonText = declaration.methods().get(0);
        QinIrMethodDeclaration randomValue = declaration.methods().get(1);

        if (jsonText.returnType().kind() != QinIrTypeKind.STRING) {
            throw new IllegalStateException("Expected JSON.stringify return type STRING, got " + jsonText.returnType());
        }
        if (randomValue.returnType().kind() != QinIrTypeKind.DOUBLE) {
            throw new IllegalStateException("Expected Math.random return type DOUBLE, got " + randomValue.returnType());
        }

        System.out.println("QinDeclarationIrBuiltinSemanticTypeSmokeTestMain passed.");
    }
}
