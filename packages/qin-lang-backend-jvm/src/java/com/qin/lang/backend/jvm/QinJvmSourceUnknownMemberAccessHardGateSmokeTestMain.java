package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;

/**
 * Proves unknown member access is rejected after the standard source parser
 * and frontend lowerer, using the lowered receiver type rather than source text.
 */
public final class QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain {
    private QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource("""
                    class SourceUnknownMemberAccessProbe {
                      payload: any

                      constructor(payload: any) {
                        this.payload = payload
                      }

                      read(): any {
                        return this.payload.missing
                      }
                    }
                    """);
            requirePropertyAccessIr(program);
            String message = captureFailure(() -> new QinJvmDeclarationClassEmitter().compileAllClasses(program));
            if (!message.contains("[QinDynamicSemanticError]")
                    || !message.contains("__qin_member_get__")
                    || !message.contains("Dynamic member lookup requires a statically admitted receiver")) {
                throw new IllegalStateException("Expected source unknown member hard gate, got: " + message);
            }
            QinIrProgram writeProgram = new QinFrontendLowerer().lowerSource("""
                    class SourceUnknownMemberSetProbe {
                      payload: any

                      constructor(payload: any) {
                        this.payload = payload
                      }

                      write(): any {
                        this.payload.missing = "qin"
                        return this.payload
                      }
                    }
                    """);
            requirePropertyAssignmentIr(writeProgram);
            String writeMessage = captureFailure(() -> new QinJvmDeclarationClassEmitter().compileAllClasses(writeProgram));
            if (!writeMessage.contains("[QinDynamicSemanticError]")
                    || !writeMessage.contains("__qin_member_set__")
                    || !writeMessage.contains("Member assignment requires a statically admitted field")) {
                throw new IllegalStateException("Expected source unknown member-set hard gate, got: " + writeMessage);
            }
            System.out.println("QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain OK");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
    }

    private static void requirePropertyAccessIr(QinIrProgram program) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> "SourceUnknownMemberAccessProbe".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered probe class"));
        QinIrMethodDeclaration method = declaration.methods().stream()
                .filter(candidate -> "read".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered read method"));
        QinIrExpression returned = returnedExpression(method);
        if (!(returned instanceof QinIrPropertyAccessExpression propertyAccess)) {
            throw new IllegalStateException("Expected QinIrPropertyAccessExpression, got: " + returned);
        }
        if (!"missing".equals(propertyAccess.propertyName())
                || !(propertyAccess.receiver() instanceof QinIrPropertyAccessExpression receiver)
                || !"payload".equals(receiver.propertyName())) {
            throw new IllegalStateException("Expected this.payload.missing property IR, got: " + propertyAccess);
        }
    }

    private static void requirePropertyAssignmentIr(QinIrProgram program) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> "SourceUnknownMemberSetProbe".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered write probe class"));
        QinIrMethodDeclaration method = declaration.methods().stream()
                .filter(candidate -> "write".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered write method"));
        QinIrAssignmentExpression assignment = method.bodyStatements().stream()
                .filter(QinIrStatementExpression.class::isInstance)
                .map(QinIrStatementExpression.class::cast)
                .map(QinIrStatementExpression::expression)
                .filter(QinIrAssignmentExpression.class::isInstance)
                .map(QinIrAssignmentExpression.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing lowered property assignment in write method"));
        if (!(assignment.target() instanceof QinIrPropertyAccessExpression propertyAccess)
                || !"missing".equals(propertyAccess.propertyName())
                || !(propertyAccess.receiver() instanceof QinIrPropertyAccessExpression receiver)
                || !"payload".equals(receiver.propertyName())) {
            throw new IllegalStateException("Expected this.payload.missing assignment target IR, got: " + assignment);
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
