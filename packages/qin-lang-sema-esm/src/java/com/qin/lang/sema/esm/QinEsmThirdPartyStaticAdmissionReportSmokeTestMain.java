package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;
import java.util.List;

public final class QinEsmThirdPartyStaticAdmissionReportSmokeTestMain {
    private QinEsmThirdPartyStaticAdmissionReportSmokeTestMain() {
    }

    public static void main(String[] args) {
        Path file = Path.of("sample-project", "node_modules", "@vendor", "dynamic-kit", "dist", "index.js")
                .toAbsolutePath()
                .normalize();
        QinModuleGraph graph = new QinModuleGraph(file, List.of(new QinModuleSource(
                file,
                """
                export const wrapped = new Proxy({}, {
                    get(target, key) {
                        return target[key];
                    }
                });
                """,
                List.of())));
        try {
            new QinEsmRuntimeFeatureValidator().validate(graph);
        } catch (QinEsmSemanticException error) {
            QinEsmDiagnostic diagnostic = error.diagnostics().stream()
                    .filter(item -> "QIN_JS_UNSUPPORTED_PROXY".equals(item.code()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Expected Proxy diagnostic: "
                            + error.diagnostics(), error));
            QinEsmStaticAdmissionReport report = diagnostic.staticAdmissionReport();
            require(report != null, "expected static admission report");
            require("@vendor/dynamic-kit".equals(report.packageName()),
                    "unexpected package name: " + report.packageName());
            require(report.sourceFile().equals(file), "unexpected source file: " + report.sourceFile());
            require("new Proxy(...)".equals(report.unsupportedShape()),
                    "unexpected unsupported shape: " + report.unsupportedShape());
            require(report.staticLoweringReason().contains("runtime-selected behavior"),
                    "unexpected static reason: " + report.staticLoweringReason());
            require(report.approvedChoices().contains("reject package"), "missing reject-package choice");
            require(report.approvedChoices().contains("write a Qin-owned facade"), "missing facade choice");
            require(report.approvedChoices().contains("select a different static package entry"),
                    "missing entry choice");
            require(report.approvedChoices().contains("change project source with approval"),
                    "missing source-change choice");
            String message = error.getMessage();
            require(message.contains("@vendor/dynamic-kit"), "exception message missing package: " + message);
            require(message.contains("new Proxy(...)"), "exception message missing unsupported shape: " + message);
            System.out.println("QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK");
            return;
        }
        throw new IllegalStateException("Expected third-party package static admission failure");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
