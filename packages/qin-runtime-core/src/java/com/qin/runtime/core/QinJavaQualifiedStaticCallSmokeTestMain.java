package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;

public final class QinJavaQualifiedStaticCallSmokeTestMain {
    private QinJavaQualifiedStaticCallSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                package demo;

                import java.util.List;

                class QualifiedStaticCall {
                    List<String> readonly(List<String> input) {
                        return java.util.Collections.unmodifiableList(input);
                    }
                }
                """;
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource(source);
        QinIrClassDeclaration classDeclaration = program.classDeclarations().get(0);
        QinIrExpression returnExpression = classDeclaration.methods().get(0).returnExpression();
        require(returnExpression instanceof QinIrStaticMethodCallExpression, "static method IR");
        QinIrStaticMethodCallExpression call = (QinIrStaticMethodCallExpression) returnExpression;
        require("java.util.Collections".equals(call.classLocalName()), "qualified class local name");
        require("java.util.Collections".equals(call.ownerBinaryName()), "qualified owner binary name");
        require("unmodifiableList".equals(call.methodName()), "method name");
        System.out.println("QinJavaQualifiedStaticCallSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
