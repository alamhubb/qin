package com.qin.lang.sema.esm;

import java.util.List;
import java.util.Objects;

/**
 * Aggregated ESM semantic analysis exception.
 */
public final class QinEsmSemanticException extends IllegalArgumentException {
    private final List<QinEsmDiagnostic> diagnostics;

    public QinEsmSemanticException(List<QinEsmDiagnostic> diagnostics) {
        super(formatMessage(diagnostics));
        Objects.requireNonNull(diagnostics, "diagnostics cannot be null");
        this.diagnostics = List.copyOf(diagnostics);
    }

    public List<QinEsmDiagnostic> diagnostics() {
        return diagnostics;
    }

    private static String formatMessage(List<QinEsmDiagnostic> diagnostics) {
        if (diagnostics == null || diagnostics.isEmpty()) {
            return "ESM semantic error";
        }
        StringBuilder sb = new StringBuilder("ESM semantic error(s):");
        for (QinEsmDiagnostic diagnostic : diagnostics) {
            sb.append(System.lineSeparator())
                    .append(" - ")
                    .append(diagnostic.code())
                    .append(" at ")
                    .append(diagnostic.sourceFile().toAbsolutePath())
                    .append(":")
                    .append(diagnostic.line())
                    .append(":")
                    .append(diagnostic.column())
                    .append(" -> ")
                    .append(diagnostic.message());
            QinEsmStaticAdmissionReport report = diagnostic.staticAdmissionReport();
            if (report != null) {
                sb.append(System.lineSeparator())
                        .append("   static admission package: ")
                        .append(report.packageName())
                        .append(System.lineSeparator())
                        .append("   package root: ")
                        .append(report.packageRoot().toAbsolutePath())
                        .append(System.lineSeparator())
                        .append("   unsupported shape: ")
                        .append(report.unsupportedShape())
                        .append(System.lineSeparator())
                        .append("   static lowering reason: ")
                        .append(report.staticLoweringReason())
                        .append(System.lineSeparator())
                        .append("   approved choices: ")
                        .append(String.join("; ", report.approvedChoices()));
            }
        }
        return sb.toString();
    }
}
