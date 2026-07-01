package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;

public final class QinDeclarationIrJavaStaticMethodCallSmokeTestMain {
    private QinDeclarationIrJavaStaticMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                import { QinWeb } from "java:com.qin.web"

                export object UserController {
                    getAll(request) {
                        return QinWeb.jsonRaw("{\\"users\\":[]}")
                    }
                }
                """;
        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        QinIrClassDeclaration declaration = program.classDeclarations().get(0);
        QinIrMethodDeclaration method = declaration.methods().get(0);
        if (!(method.returnExpression() instanceof QinIrStaticMethodCallExpression call)) {
            throw new AssertionError("Expected Java static method call IR, got " + method.returnExpression());
        }
        if (!"com.qin.web.QinWeb".equals(call.ownerBinaryName())) {
            throw new AssertionError("Unexpected static owner: " + call.ownerBinaryName());
        }
        if (!"jsonRaw".equals(call.methodName())) {
            throw new AssertionError("Unexpected static method: " + call.methodName());
        }
        System.out.println("QinDeclarationIrJavaStaticMethodCallSmokeTestMain OK");
    }
}
