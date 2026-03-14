package com.qin.lang.module.policy;

import java.util.List;
import java.util.Objects;

/**
 * Aggregate exception for module import policy violations.
 */
public final class QinImportPolicyException extends IllegalArgumentException {
    private final List<QinImportPolicyViolation> violations;

    public QinImportPolicyException(List<QinImportPolicyViolation> violations) {
        super(formatMessage(violations));
        Objects.requireNonNull(violations, "violations cannot be null");
        this.violations = List.copyOf(violations);
    }

    public List<QinImportPolicyViolation> violations() {
        return violations;
    }

    private static String formatMessage(List<QinImportPolicyViolation> violations) {
        if (violations == null || violations.isEmpty()) {
            return "Import policy violation";
        }
        StringBuilder sb = new StringBuilder("Import policy violation(s):");
        for (QinImportPolicyViolation violation : violations) {
            sb.append(System.lineSeparator())
                    .append(" - ")
                    .append(violation.ruleCode())
                    .append(" at ")
                    .append(violation.sourceFile().toAbsolutePath())
                    .append(":")
                    .append(violation.line())
                    .append(":")
                    .append(violation.column())
                    .append(" -> ")
                    .append(violation.message());
        }
        return sb.toString();
    }
}
