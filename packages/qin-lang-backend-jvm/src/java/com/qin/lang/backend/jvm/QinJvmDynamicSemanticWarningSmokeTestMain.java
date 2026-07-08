package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Smoke test for compile-time warnings when JVM .class emission uses JavaEsmGlobal.
 */
public final class QinJvmDynamicSemanticWarningSmokeTestMain {
    private QinJvmDynamicSemanticWarningSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String dynamicWarning = captureErr(() -> {
            QinJvmDynamicSemanticWarnings.resetForTest();
            QinIrProgram program = new QinIrProgram(
                    List.of(new QinIrConstDeclaration(
                            "payload",
                            new QinIrObjectLiteral(List.of(
                                    new QinIrObjectProperty("name", new QinIrStringLiteral("alice")))))),
                    List.of(new QinIrExpressionStatement(new QinIrMemberAccessExpression("payload", "name"))),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of());
            new QinJvmClassFileBackend().compileProgram(program, "QinDynamicWarningProbe");
        });
        if (!dynamicWarning.contains("[QinDynamicSemanticWarning]")
                || !dynamicWarning.contains("__qin_member_get__")) {
            throw new IllegalStateException("Expected dynamic member warning, got: " + dynamicWarning);
        }

        String staticWarning = captureErr(() -> {
            QinJvmDynamicSemanticWarnings.resetForTest();
            QinIrClassDeclaration payload = new QinIrClassDeclaration(
                    null,
                    "StaticPayload",
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of(),
                    List.of(new QinIrFieldDeclaration(
                            "name",
                            QinIrTypeRef.stringType(),
                            List.of(),
                            null)),
                    List.of());
            QinIrMethodDeclaration echoName = new QinIrMethodDeclaration(
                    "echoName",
                    QinIrTypeRef.stringType(),
                    List.of(new QinIrParameter(
                            "payload",
                            QinIrTypeRef.classType("StaticPayload"),
                            List.of())),
                    List.of(),
                    new QinIrMemberAccessExpression("payload", "name"));
            QinIrClassDeclaration controller = new QinIrClassDeclaration(
                    null,
                    "StaticPayloadController",
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of(),
                    List.of(),
                    List.of(echoName));
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
                    List.of(payload, controller));
            new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        });
        if (staticWarning.contains("[QinDynamicSemanticWarning]")) {
            throw new IllegalStateException("Static member access should not warn, got: " + staticWarning);
        }

        System.out.println("QinJvmDynamicSemanticWarningSmokeTestMain passed.");
    }

    private static String captureErr(ThrowingRunnable runnable) throws Exception {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setErr(capture);
            runnable.run();
        } finally {
            System.setErr(originalErr);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
