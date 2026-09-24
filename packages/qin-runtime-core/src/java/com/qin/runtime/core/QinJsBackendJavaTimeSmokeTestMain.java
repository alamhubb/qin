package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public final class QinJsBackendJavaTimeSmokeTestMain {
    private QinJsBackendJavaTimeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "now",
                                new QinIrStaticMethodCallExpression(
                                        "LocalDateTime",
                                        "java.time.LocalDateTime",
                                        "now",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "formatter",
                                new QinIrStaticMethodCallExpression(
                                        "DateTimeFormatter",
                                        "java.time.format.DateTimeFormatter",
                                        "ofPattern",
                                        List.of(new QinIrStringLiteral("yyyy-MM-dd-HH-mm")))),
                        new QinIrConstDeclaration(
                                "formatted",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("now"),
                                        "format",
                                        List.of(new QinIrIdentifierReference("formatter"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport(
                                "java:java.time",
                                "LocalDateTime",
                                "LocalDateTime",
                                "java.time.LocalDateTime"),
                        new QinIrJavaImport(
                                "java:java.time.format",
                                "DateTimeFormatter",
                                "DateTimeFormatter",
                                "java.time.format.DateTimeFormatter")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaTimeLocalDateTime"), "LocalDateTime runtime shim");
        require(generated.contains("class __QinJavaTimeFormatDateTimeFormatter"), "DateTimeFormatter runtime shim");
        require(generated.contains("const LocalDateTime = __QinJavaTimeLocalDateTime;"), "LocalDateTime alias");
        require(generated.contains("const DateTimeFormatter = __QinJavaTimeFormatDateTimeFormatter;"),
                "DateTimeFormatter alias");
        require(generated.contains("const tokenYyyy = String(date.getFullYear());"), "static date token locals");
        require(!generated.contains("tokens.yyyy"), "no dynamic date token object member read");

        Path root = Files.createTempDirectory("qin-js-backend-time-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-time\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                "globalThis.__qinJavaFixedNow = \"2026-06-04T03:26:00\";\n"
                        + generated
                        + "\nformatted;\n",
                "js_backend_time");
        if (!isFormattedDateTime(result)) {
            throw new IllegalStateException("Expected generated time shape, got: " + result);
        }
        Object externalPackageResult = new QinJsPackageRunner().runModuleSource(
                root,
                """
                import {
                  __QinJavaTimeFormatDateTimeFormatter,
                  __QinJavaTimeLocalDateTime
                } from "@qin/java-sdk-js/time";

                globalThis.__qinJavaFixedNow = "2026-06-04T03:26:00";
                const now = __QinJavaTimeLocalDateTime.now();
                const formatter = __QinJavaTimeFormatDateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                now.format(formatter);
                """,
                "java_sdk_time_static");
        if (!isFormattedDateTime(externalPackageResult)) {
            throw new IllegalStateException("Expected external java-sdk time shape, got: " + externalPackageResult);
        }
        System.out.println("QinJsBackendJavaTimeSmokeTestMain OK");
    }

    private static boolean isFormattedDateTime(Object result) {
        return result instanceof String text
                && Pattern.matches("\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}", text);
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
