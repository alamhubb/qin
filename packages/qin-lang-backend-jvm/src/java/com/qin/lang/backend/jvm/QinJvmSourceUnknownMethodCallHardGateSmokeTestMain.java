package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStatement;

/**
 * Proves unknown receiver calls are rejected after the standard source parser
 * and frontend lowerer, using receiver/type facts instead of source text.
 */
public final class QinJvmSourceUnknownMethodCallHardGateSmokeTestMain {
    private QinJvmSourceUnknownMethodCallHardGateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource("""
                    class SourceUnknownMethodCallProbe {
                      payload: any

                      constructor(payload: any) {
                        this.payload = payload
                      }

                      read(): any {
                        return this.payload.missing("qin")
                      }
                    }
                    """);
            requireInstanceMethodCallIr(program);
            String message = captureFailure(() -> new QinJvmDeclarationClassEmitter().compileAllClasses(program));
            if (!message.contains("[QinDynamicSemanticError]")
                    || !message.contains("__qin_call_method_array__")
                    || !message.contains("receiverType=QinIrTypeRef[kind=CLASS, binaryName=java.lang.Object")) {
                throw new IllegalStateException("Expected source unknown method-call hard gate, got: " + message);
            }
            System.out.println("QinJvmSourceUnknownMethodCallHardGateSmokeTestMain OK");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void requireInstanceMethodCallIr(QinIrProgram program) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> "SourceUnknownMethodCallProbe".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered probe class"));
        QinIrMethodDeclaration method = declaration.methods().stream()
                .filter(candidate -> "read".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered read method"));
        QinIrExpression returned = returnedExpression(method);
        if (!(returned instanceof QinIrInstanceMethodCallExpression methodCall)) {
            throw new IllegalStateException("Expected QinIrInstanceMethodCallExpression, got: " + returned);
        }
        if (!"missing".equals(methodCall.methodName())
                || !(methodCall.receiver() instanceof QinIrPropertyAccessExpression receiver)
                || !"payload".equals(receiver.propertyName())) {
            throw new IllegalStateException("Expected this.payload.missing(...) method-call IR, got: " + methodCall);
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
