package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStatement;

/**
 * Proves unknown indexed access is rejected after the standard source parser
 * and frontend lowerer, not only when a test hand-builds IR.
 */
public final class QinJvmSourceUnknownElementAccessHardGateSmokeTestMain {
    private QinJvmSourceUnknownElementAccessHardGateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource("""
                    class SourceUnknownElementAccessProbe {
                      static second(values: any): any {
                        return values[1]
                      }
                    }
                    """);
            requireElementAccessIr(program);
            String message = captureFailure(() -> new QinJvmDeclarationClassEmitter().compileAllClasses(program));
            if (!message.contains("[QinDynamicSemanticError]")
                    || !message.contains("unresolved element access")
                    || !message.contains("statically admitted array, string, collection, or explicit Map/Dict receiver")) {
                throw new IllegalStateException("Expected source unknown indexed access hard gate, got: " + message);
            }
            System.out.println("QinJvmSourceUnknownElementAccessHardGateSmokeTestMain OK");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void requireElementAccessIr(QinIrProgram program) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> "SourceUnknownElementAccessProbe".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered probe class"));
        QinIrMethodDeclaration method = declaration.methods().stream()
                .filter(candidate -> "second".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered second method"));
        QinIrExpression returned = returnedExpression(method);
        if (!(returned instanceof QinIrElementAccessExpression elementAccess)) {
            throw new IllegalStateException("Expected QinIrElementAccessExpression, got: " + returned);
        }
        if (!(elementAccess.receiver() instanceof QinIrIdentifierReference receiver)
                || !"values".equals(receiver.name())) {
            throw new IllegalStateException("Expected values receiver in element access IR, got: " + elementAccess);
        }
    }

    private static QinIrExpression returnedExpression(QinIrMethodDeclaration method) {
        if (method.returnExpression() != null) {
            return method.returnExpression();
        }
        for (QinIrStatement statement : method.bodyStatements()) {
            if (statement instanceof QinIrReturnStatement returnStatement) {
                return returnStatement.value();
            }
        }
        throw new IllegalStateException("Expected a returned expression in lowered method: " + method);
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
