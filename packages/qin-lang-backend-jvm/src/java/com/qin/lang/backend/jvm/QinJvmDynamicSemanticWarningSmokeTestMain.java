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
 * Smoke test for hard failures when JVM .class emission would use JavaEsmGlobal.
 */
public final class QinJvmDynamicSemanticWarningSmokeTestMain {
    private QinJvmDynamicSemanticWarningSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        System.clearProperty("qin.dynamicSemanticMode");
        System.clearProperty("qin.dynamicSemanticHardFailures");
        System.clearProperty("qin.dynamicSemanticWarnings");
        String dynamicFailure = captureFailure(() -> {
            QinJvmDynamicSemanticWarnings.resetForTest();
            System.setProperty("qin.dynamicSemanticWarnings", "false");
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
        System.clearProperty("qin.dynamicSemanticWarnings");
        if (!dynamicFailure.contains("[QinDynamicSemanticError]")
                || !(dynamicFailure.contains("__qin_member_get__")
                || dynamicFailure.contains("__qin_member_set__"))) {
            throw new IllegalStateException("Expected dynamic member hard failure, got: " + dynamicFailure);
        }

        String staticFailure = captureErr(() -> {
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
        if (!staticFailure.isBlank()) {
            throw new IllegalStateException("Static member access should not fail, got: " + staticFailure);
        }

        String strictFailure = captureFailure(() -> {
            QinJvmDynamicSemanticWarnings.resetForTest();
            System.setProperty("qin.dynamicSemanticMode", "error");
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDynamicSemanticWarningSmokeTestMain", "__qin_binary__");
        });
        System.clearProperty("qin.dynamicSemanticMode");
        if (!strictFailure.contains("[QinDynamicSemanticError]")
                || !strictFailure.contains("__qin_binary__")) {
            throw new IllegalStateException("Strict mode should hard-fail dynamic helpers, got: " + strictFailure);
        }

        String hardPropertyFailure = captureFailure(() -> {
            QinJvmDynamicSemanticWarnings.resetForTest();
            System.setProperty("qin.dynamicSemanticHardFailures", "true");
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDynamicSemanticWarningSmokeTestMain", "__qin_truthy__");
        });
        System.clearProperty("qin.dynamicSemanticHardFailures");
        if (!hardPropertyFailure.contains("[QinDynamicSemanticError]")
                || !hardPropertyFailure.contains("__qin_truthy__")) {
            throw new IllegalStateException("Hard-failure property should hard-fail dynamic helpers, got: " + hardPropertyFailure);
        }

        System.out.println("QinJvmDynamicSemanticWarningSmokeTestMain passed.");
    }

    private static String captureFailure(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
            return "no failure";
        } catch (Exception error) {
            return error.getMessage() == null ? String.valueOf(error) : error.getMessage();
        }
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
