package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.Objects;

/**
 * ESM semantic diagnostic item.
 */
public record QinEsmDiagnostic(
        String code,
        String message,
        Path sourceFile,
        int line,
        int column,
        QinEsmStaticAdmissionReport staticAdmissionReport) {
    public QinEsmDiagnostic(
            String code,
            String message,
            Path sourceFile,
            int line,
            int column) {
        this(code, message, sourceFile, line, column, null);
    }

    public QinEsmDiagnostic {
        Objects.requireNonNull(code, "code cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
    }
}
