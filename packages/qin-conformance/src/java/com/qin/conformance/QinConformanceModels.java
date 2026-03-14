package com.qin.conformance;

import java.util.List;
import java.util.Objects;

/**
 * Shared report/result models.
 */
public final class QinConformanceModels {
    private QinConformanceModels() {
    }

    public record CaseExecution(
            String status,
            String errorType,
            String message,
            String detail) {
        public CaseExecution {
            Objects.requireNonNull(status, "status cannot be null");
            errorType = errorType == null ? "" : errorType;
            message = message == null ? "" : message;
            detail = detail == null ? "" : detail;
        }
    }

    public record CaseReport(
            String caseId,
            String entry,
            CaseExecution qin,
            CaseExecution chrome,
            boolean match,
            boolean excluded,
            String exclusionReason) {
        public CaseReport {
            Objects.requireNonNull(caseId, "caseId cannot be null");
            Objects.requireNonNull(entry, "entry cannot be null");
            Objects.requireNonNull(qin, "qin cannot be null");
            Objects.requireNonNull(chrome, "chrome cannot be null");
            exclusionReason = exclusionReason == null ? "" : exclusionReason;
        }
    }

    public record Summary(
            int total,
            int matched,
            int mismatched,
            int excluded,
            int qinFailed,
            int chromeFailed) {
    }

    public record Report(
            String generatedAtUtc,
            String suite,
            String chromeChannel,
            String chromeFrozenDate,
            String chromeBinary,
            String projectRoot,
            Summary summary,
            List<CaseReport> cases) {
        public Report {
            Objects.requireNonNull(generatedAtUtc, "generatedAtUtc cannot be null");
            Objects.requireNonNull(suite, "suite cannot be null");
            Objects.requireNonNull(chromeChannel, "chromeChannel cannot be null");
            Objects.requireNonNull(chromeFrozenDate, "chromeFrozenDate cannot be null");
            chromeBinary = chromeBinary == null ? "" : chromeBinary;
            Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
            Objects.requireNonNull(summary, "summary cannot be null");
            Objects.requireNonNull(cases, "cases cannot be null");
            cases = List.copyOf(cases);
        }
    }
}
