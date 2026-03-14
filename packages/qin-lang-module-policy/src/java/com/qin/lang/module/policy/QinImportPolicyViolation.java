package com.qin.lang.module.policy;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Policy violation for import + source-zone combination.
 */
public record QinImportPolicyViolation(
        Path sourceFile,
        QinSourceZone zone,
        String moduleSpecifier,
        QinImportKind kind,
        int line,
        int column,
        String ruleCode,
        String message) {
    public QinImportPolicyViolation {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        Objects.requireNonNull(moduleSpecifier, "moduleSpecifier cannot be null");
        Objects.requireNonNull(kind, "kind cannot be null");
        Objects.requireNonNull(ruleCode, "ruleCode cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
    }
}
