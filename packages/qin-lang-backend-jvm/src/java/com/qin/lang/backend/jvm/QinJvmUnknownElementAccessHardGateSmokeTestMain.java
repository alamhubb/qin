package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;

/**
 * Proves unknown indexed access fails at the IR/JVM boundary instead of
 * falling back to JavaScript dynamic member lookup.
 */
public final class QinJvmUnknownElementAccessHardGateSmokeTestMain {
    private QinJvmUnknownElementAccessHardGateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            String message = captureFailure(QinJvmUnknownElementAccessHardGateSmokeTestMain::compileUnknownObjectElementAccess);
            if (!message.contains("[QinDynamicSemanticError]")
                    || !message.contains("unresolved element access")
                    || !message.contains("statically admitted array, string, collection, or explicit Map/Dict receiver")) {
                throw new IllegalStateException("Expected unknown indexed access hard gate, got: " + message);
            }
            System.out.println("QinJvmUnknownElementAccessHardGateSmokeTestMain OK");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void compileUnknownObjectElementAccess() {
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "UnknownElementAccessProbe",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "second",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(new QinIrParameter(
                                "values",
                                QinIrTypeRef.classType("java.lang.Object"),
                                List.of())),
                        List.of(),
                        new QinIrElementAccessExpression(
                                new QinIrIdentifierReference("values"),
                                new QinIrNumberLiteral(1)))));
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(declaration));
        new QinJvmDeclarationClassEmitter().compileSingleClass(program, declaration.binaryName());
    }

    private static String captureFailure(Runnable runnable) {
        try {
            runnable.run();
            return "no failure";
        } catch (RuntimeException error) {
            return error.getMessage() == null ? String.valueOf(error) : error.getMessage();
        }
    }
}
