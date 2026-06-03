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

        Path root = Files.createTempDirectory("qin-js-backend-time-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-time\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                "globalThis.__qinJavaFixedNow = \"2026-06-04T03:26:00\";\n"
                        + generated
                        + "\nformatted;\n",
                "js_backend_time");
        if (!"2026-06-04-03-26".equals(result)) {
            throw new IllegalStateException("Expected generated time result, got: " + result);
        }
        System.out.println("QinJsBackendJavaTimeSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
